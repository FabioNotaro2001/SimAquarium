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

public class avoid_obstacles extends DefaultInternalAction {

    /**
     * Returns the angle between the fish's direction and firection towards the
     * obstacle vectors.
     * 
     * @param fishDir
     * @param obstaclePos
     * @return
     */
    private double getAngleBetweenDirectionAndObstacle(Vector2D fishDir, Position obstaclePos) {
        Vector2D dirToObstacle = Vector2D.fromPositions(Position.zero(), obstaclePos);
        double fishDirAngle = fishDir.angle();
        return dirToObstacle.rotateBy(-fishDirAngle).angle();
    }

    /**
     * Checks if the obstacle is in front of fish based on its direction.
     * 
     * @param fishDir
     * @param obstaclePos
     * @param obstacleRadius
     * @return
     */
    private boolean isObstacleOnPath(Vector2D fishDir, Position obstaclePos, double obstacleRadius) {
        Vector2D dirToObstacle = Vector2D.fromPositions(Position.zero(), obstaclePos);
        if (Math.abs(getAngleBetweenDirectionAndObstacle(fishDir, obstaclePos)) >= Math.PI / 2) { // Obstacle behind fish
            return false;
        }

        /*
         * Projection of the obstacle position onto the fish direction vector.
         * Simplified version thanks to the fish being at (0,0).
         */
        Vector2D projection = fishDir.times(dirToObstacle.times(fishDir) / fishDir.times(fishDir));
        Vector2D projToObstacle = dirToObstacle.minus(projection);

        // System.out.println("OSTACOLO " +
        // (Math.sqrt(projToObstacle.times(projToObstacle)) - obstacleRadius));
        return Math.sqrt(projToObstacle.times(projToObstacle)) <= obstacleRadius;
    }

    private boolean isFoodInFrontOfObstacle(Vector2D fishDir, Position foodPos, Position obstaclePos, double obstacleRadius) {
        Vector2D dirToFood = Vector2D.fromPositions(Position.zero(), foodPos);
        Vector2D dirToObstacle = Vector2D.fromPositions(Position.zero(), obstaclePos);
        Vector2D dirFromFoodToObstacle = Vector2D.fromPositions(foodPos, obstaclePos);

        return dirToFood.getLength() < dirToObstacle.getLength() && dirFromFoodToObstacle.getLength() >= obstacleRadius;
    }

    /**
     * Checks if the obstacle is to the left of fish from its point of view.
     * 
     * @param fishDir
     * @param obstaclePos
     * @return
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

        int maxLoops = 50;
        Map<Position, Integer> obstacleEncounters = new HashMap<>();
        boolean rotated;
        do {
            rotated = false;
            var dir = fishDir;
            Optional<Pair<Position, Double>> closestObstacleOnPath = coordinatesList.stream()
                .filter(p -> isObstacleOnPath(dir, p.getFirst(), p.getSecond()))
                .min((p1, p2) -> Double.compare(Position.zero().distanceFrom(p1.getFirst()), Position.zero().distanceFrom(p2.getFirst())));

            if(closestObstacleOnPath.isPresent() && (targetPos.isEmpty() || !isFoodInFrontOfObstacle(fishDir, targetPos.get(), closestObstacleOnPath.get().getFirst(), closestObstacleOnPath.get().getSecond()))){
                Position pos = closestObstacleOnPath.get().getFirst();
                
                Integer occurrences = obstacleEncounters.get(pos);
                if (occurrences == null) {
                    occurrences = 1;
                    obstacleEncounters.put(pos, 1);
                } else {
                    obstacleEncounters.put(pos, occurrences + 1);
                }
                double angle = Math.PI / 10 * occurrences;

                fishDir = isObstacleToTheLeft(fishDir, pos) ? fishDir.rotateBy(-angle) : fishDir.rotateBy(angle);
                rotated = true;
                // System.out.println("ROTATED: " + fishDir.angle());
            }
            maxLoops--;
        } while (rotated && maxLoops > 0);
        currentAgent.delBel(directionAsLiteral);
        currentAgent.addBel(Literal.parseLiteral(String.format("direction(%f, %f)", fishDir.getX(), fishDir.getY())));
        return true;
    }
}
