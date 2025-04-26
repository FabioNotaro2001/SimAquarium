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
        
        double distance = Vector2D.fromPositions(Position.zero(), new Position(x, y)).getLength();

        // The fish tries to eat the food
        if (distance < 5) {
            currentAgent.addBel(Literal.parseLiteral(String.format("close_to_food(%f, %f)", x, y)));
        }
        
        Literal energyLiteral = currentAgent.findBel(Literal.parseLiteral("energy(E)"), un);
        double fishEnergy = termToDouble(energyLiteral);
        currentAgent.delBel(energyLiteral);
        currentAgent.addBel(Literal.parseLiteral(String.format("energy(%f)", Math.max(0, fishEnergy - speed.getSpeed()))));

        return true;
    }
}
