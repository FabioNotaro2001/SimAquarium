package utils;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import static utils.Utils.literalToVector2D;
import static utils.Utils.termToDouble;
import static utils.Utils.literalToDirectionAndDistance;
import static utils.Utils.termToList;

import java.util.List;
import java.util.Optional;

import env.model.Direction;
import env.model.Position;
import env.model.Vector2D;

/**
 * The `check_aquarium_borders` class is a custom internal action for agents in the simulation.
 * It allows agents to detect when they are near the borders of the aquarium and adjust their
 * direction to avoid crossing the borders. The class uses vector calculations to determine
 * the agent's position and direction relative to the borders.
 */
public class check_aquarium_borders extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();
        Literal directionAsLiteral = currentAgent.findBel(Literal.parseLiteral("direction(_, _)"), un);
        Vector2D fishDir = literalToVector2D(directionAsLiteral);
        Literal bordersAsLiteral = currentAgent.findBel(Literal.parseLiteral("borders(_)"), un);
        Literal hasTargLiteral = currentAgent.findBel(Literal.parseLiteral("has_target(_, _)"), un);
        List<Term> bordersList = termToList(bordersAsLiteral.getTerm(0));
        Optional<Position> targetPos = Optional.ofNullable(hasTargLiteral).map(Utils::literalToPosition);
        Literal sizeBelief = currentAgent.findBel(Literal.parseLiteral("half_size(_)"), un);
        double halfSize = termToDouble(sizeBelief.getTerm(0));

        for (Term border : bordersList) {
            var b = literalToDirectionAndDistance((Literal)border);

            Direction dir = b.getFirst();
            double borderDistance = Math.max(0, b.getSecond());

            if((dir == Direction.RIGHT && fishDir.getX() > 0 && (targetPos.isEmpty() || targetPos.get().getX() >= borderDistance + halfSize)) || 
                (dir == Direction.LEFT && fishDir.getX() < 0 && (targetPos.isEmpty() || -targetPos.get().getX() >= borderDistance + halfSize))) {
                fishDir = fishDir.flipVertically();
            } else if((dir == Direction.BOTTOM && fishDir.getY() > 0 && (targetPos.isEmpty() || targetPos.get().getY() >= borderDistance + halfSize)) ||
                (dir == Direction.TOP && fishDir.getY() < 0 && (targetPos.isEmpty() || -targetPos.get().getY() >= borderDistance + halfSize))) {
                fishDir = fishDir.flipHorizontally();
            }
        }
        currentAgent.delBel(directionAsLiteral);
        currentAgent.addBel(Literal.parseLiteral(String.format("direction(%f, %f)", fishDir.getX(), fishDir.getY())));
        
        return true;
    }
}
