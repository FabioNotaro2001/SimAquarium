package utils;

import static utils.Utils.*;

import java.util.List;
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
     * Returns the angle between the fish's direction and firection towards the obstacle vectors.
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
            Projection of the obstacle position onto the fish direction vector. 
            Simplified version thanks to the fish being at (0,0).
        */
        Vector2D projection = fishDir.times(dirToObstacle.times(fishDir) / fishDir.times(fishDir)); 
        Vector2D projToObstacle = dirToObstacle.minus(projection);


        // System.out.println("OSTACOLO " + (Math.sqrt(projToObstacle.times(projToObstacle)) - obstacleRadius));
        return Math.sqrt(projToObstacle.times(projToObstacle)) <= obstacleRadius;
    }

    /**
     * Checks if the obstacle is to the left of fish from its point of view.
     * @param fishDir
     * @param obstaclePos
     * @return
     */
    private boolean isObstacleToTheLeft(Vector2D fishDir, Position obstaclePos) {
        Vector2D dirToObstacle = Vector2D.fromPositions(Position.zero(), obstaclePos);
        
        if (dirToObstacle.getLength() > 30) { // TODO: belief sulla distanza minima? in base alla velocità?
            return true;
        }

        return getAngleBetweenDirectionAndObstacle(fishDir, obstaclePos) >= 0; // A positive angle means the vector is "to the left" of the fish's direction.
    }
    
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();
        Literal directionAsLiteral = currentAgent.findBel(Literal.parseLiteral("direction(_, _)"), un);
        Vector2D fishDir = literalToVector2D(directionAsLiteral);

        List<Term> coordinatesList = termToList(args[0]);
        Optional<Position> closestObstacleOnPath = coordinatesList.stream()
            .map(t -> literalToPositionAndRadius((Literal)t))
            .filter(p -> isObstacleOnPath(fishDir, p.getX(), p.getY()))
            .map(Pair::getX)
            .min((p1, p2) -> Double.compare(Position.zero().distanceFrom(p1), Position.zero().distanceFrom(p2)));

        if(closestObstacleOnPath.isPresent()){
            currentAgent.delBel(directionAsLiteral);
            Vector2D newDir = isObstacleToTheLeft(fishDir, closestObstacleOnPath.get()) ? fishDir.rotateBy(-Math.PI / 2) : fishDir.rotateBy(Math.PI / 2);
            currentAgent.addBel(Literal.parseLiteral(String.format("direction(%f, %f)", newDir.getX(), newDir.getY())));
        } 
        
        return true;
    }
}
