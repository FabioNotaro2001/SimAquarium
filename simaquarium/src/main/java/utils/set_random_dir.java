package utils;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import java.util.Random;

import env.model.Vector2D;

public class set_random_dir extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();
        Random r = new Random();

        Vector2D dir = Vector2D.of(r.nextDouble() * Math.PI);
        
        currentAgent.addBel(Literal.parseLiteral(String.format("direction(%f, %f)", dir.getX(), dir.getY())));

        return true;
    }
}
