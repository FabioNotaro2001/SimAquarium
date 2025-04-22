package utils;

import static utils.Utils.*;

import env.model.Position;
import env.model.Speed;
import env.model.Vector2D;
import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class move_towards extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        double x = termToDouble(args[0]);
        double y = termToDouble(args[1]);
        Speed speed = termToSpeed(args[2]);
        Agent currentAgent = ts.getAg();
        Literal positionAsLiteral = currentAgent.findBel(Literal.parseLiteral("position(X, Y)"), un);
        Position fishPosition = literalToPosition(positionAsLiteral);
        
        Vector2D direction = Vector2D.fromPositions(fishPosition, new Position(x, y)).normalize();
        Position newFishPosition = new Position(fishPosition.getX() + direction.getX() * Utils.NORMAL_MOVEMENT * speed.getSpeed(), fishPosition.getY() + direction.getY() * Utils.NORMAL_MOVEMENT * speed.getSpeed());
        currentAgent.addBel(Literal.parseLiteral(String.format("position(%f, %f)", newFishPosition.getX(), newFishPosition.getY())));

        return true;
    }
}
