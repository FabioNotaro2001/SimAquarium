package utils;

import static utils.Utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import env.model.Pair;
import env.model.Position;
import env.model.Vector2D;
import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

/**
 * The `avoid_obstacles` class is a custom internal action for agents in the simulation.
 * It allows agents to detect and avoid obstacles in their path by adjusting their direction.
 * The class uses vector calculations to determine the position and orientation of obstacles
 * relative to the agent and modifies the agent's direction accordingly.
 */
public class avoid_obstacles extends DefaultInternalAction {

    /**
     * Calculates the angle between the fish's direction and the direction towards an obstacle.
     * 
     * @param fishDir The direction vector of the fish.
     * @param obstaclePos The position of the obstacle.
     * @return The angle in radians between the fish's direction and the obstacle.
     */
    private double getAngleBetweenDirectionAndObstacle(Vector2D fishDir, Position obstaclePos) {
        Vector2D dirToObstacle = Vector2D.fromPositions(Position.zero(), obstaclePos);
        return Vector2D.angleBetween(fishDir, dirToObstacle);
    }

    /**
     * Checks if a position is inside an obstacle based on its radius.
     * 
     * @param obstaclePos The position of the obstacle.
     * @param obstacleRadius The radius of the obstacle.
     * @return True if the position is inside the obstacle, false otherwise.
     */
    private boolean isInsideObstacle(Position obstaclePos, double obstacleRadius) {
        Vector2D dirToObstacle = Vector2D.fromPositions(Position.zero(), obstaclePos);
        return dirToObstacle.getLength() < obstacleRadius;
    }

    /**
     * Checks if an obstacle is in the path of the fish based on its direction.
     * 
     * @param fishDir The direction vector of the fish.
     * @param fishHalfSize The half size of the fish.
     * @param obstaclePos The position of the obstacle.
     * @param obstacleRadius The radius of the obstacle.
     * @return True if the obstacle is in the fish's path, false otherwise.
     */
    private boolean isObstacleOnPath(Vector2D fishDir, double fishHalfSize, Position obstaclePos, double obstacleRadius) {
        Vector2D dirToObstacle = Vector2D.fromPositions(Position.zero(), obstaclePos);
        if (isInsideObstacle(obstaclePos, obstacleRadius)) {
            return true;
        }
        if (Math.abs(getAngleBetweenDirectionAndObstacle(fishDir, obstaclePos)) >= Math.PI / 2) { // Obstacle behind fish
            return false;
        }

        /*
         * Projection of the obstacle position onto the fish direction vector.
         * Simplified version thanks to the fish being at (0,0).
         */
        Vector2D projection = fishDir.times(dirToObstacle.times(fishDir) / fishDir.times(fishDir));
        Vector2D projToObstacle = dirToObstacle.minus(projection);
        return Math.sqrt(projToObstacle.times(projToObstacle)) <= obstacleRadius + fishHalfSize;
    }

    /**
     * Checks if food is in front of an obstacle from the fish's perspective.
     * 
     * @param fishDir The direction vector of the fish.
     * @param foodPos The position of the food.
     * @param obstaclePos The position of the obstacle.
     * @param obstacleRadius The radius of the obstacle.
     * @return True if the food is in front of the obstacle, false otherwise.
     */
    private boolean isFoodInFrontOfObstacle(Vector2D fishDir, Position foodPos, Position obstaclePos, double obstacleRadius) {
        Vector2D dirToFood = Vector2D.fromPositions(Position.zero(), foodPos);
        Vector2D dirToObstacle = Vector2D.fromPositions(Position.zero(), obstaclePos);
        Vector2D dirFromFoodToObstacle = Vector2D.fromPositions(foodPos, obstaclePos);

        return dirToFood.getLength() < dirToObstacle.getLength() && dirFromFoodToObstacle.getLength() >= obstacleRadius;
    }

    /**
     * Checks if an obstacle is to the left of the fish from its point of view.
     * 
     * @param fishDir The direction vector of the fish.
     * @param obstaclePos The position of the obstacle.
     * @return True if the obstacle is to the left, false otherwise.
     */
    private boolean isObstacleToTheLeft(Vector2D fishDir, Position obstaclePos) {
        return getAngleBetweenDirectionAndObstacle(fishDir, obstaclePos) >= 0; // A positive angle means the vector is "to the left" of the fish's direction.
    }

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();
        Literal directionAsLiteral = currentAgent.findBel(Literal.parseLiteral("direction(_, _)"), un);
        Vector2D fishDir = literalToVector2D(directionAsLiteral);

        List<Pair<Position, Double>> coordinatesList = termToList(args[0]).stream().map(t -> literalToPositionAndRadius((Literal)t)).toList();
        
        Literal hasTargLiteral = currentAgent.findBel(Literal.parseLiteral("has_target(_, _)"), un);
        Optional<Position> targetPos = Optional.ofNullable(hasTargLiteral).map(Utils::literalToPosition);

        Literal sizeBelief = currentAgent.findBel(Literal.parseLiteral("half_size(_)"), un);
        double halfSize = termToDouble(sizeBelief.getTerm(0));

        int maxLoops = 50;
        Map<Position, Integer> obstacleEncounters = new HashMap<>();
        boolean rotated;
        do {
            rotated = false;
            var dir = fishDir;
            
            Optional<Pair<Position, Double>> closestObstacleOnPath = coordinatesList.stream()
                .filter(p -> isObstacleOnPath(dir, halfSize, p.getFirst(), p.getSecond()))
                .min((p1, p2) -> Double.compare(Position.zero().distanceFrom(p1.getFirst()), Position.zero().distanceFrom(p2.getFirst())));

            if (closestObstacleOnPath.isEmpty()) {
                maxLoops--;
                continue;
            }
            
            Position obstaclePos = closestObstacleOnPath.get().getFirst();
            double obstacleRadius = closestObstacleOnPath.get().getSecond();

            if (isInsideObstacle(obstaclePos, obstacleRadius)) {
                fishDir = Vector2D.fromPositions(Position.zero(), obstaclePos).rotateBy(Math.PI);
                break;
            }

            if(targetPos.isEmpty() || !isFoodInFrontOfObstacle(fishDir, targetPos.get(), obstaclePos, obstacleRadius)){                
                Integer occurrences = obstacleEncounters.get(obstaclePos);
                if (occurrences == null) {
                    occurrences = 1;
                    obstacleEncounters.put(obstaclePos, 1);
                } else {
                    obstacleEncounters.put(obstaclePos, occurrences + 1);
                }
                double angle = Math.toRadians(3) * occurrences;

                fishDir = isObstacleToTheLeft(fishDir, obstaclePos) ? fishDir.rotateBy(-angle) : fishDir.rotateBy(angle);
                rotated = true;
            }
            maxLoops--;
        } while (rotated && maxLoops > 0);

        currentAgent.delBel(directionAsLiteral);
        currentAgent.addBel(Literal.parseLiteral(String.format("direction(%f, %f)", fishDir.getX(), fishDir.getY())));
        
        return true;
    }
}
