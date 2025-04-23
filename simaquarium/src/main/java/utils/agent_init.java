package utils;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import java.util.Random;

public class agent_init extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();
        Random r = new Random();
        
        currentAgent.addBel(Literal.parseLiteral(String.format("weight(%f)", 10 + r.nextDouble() * 90)));

        return true;
    }
}
