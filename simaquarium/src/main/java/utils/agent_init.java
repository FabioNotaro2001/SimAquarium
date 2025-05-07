package utils;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class agent_init extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();
        double weight = 30 + Utils.RAND.nextDouble() * 90;
        currentAgent.addBel(Literal.parseLiteral(String.format("weight(%f)", weight)));
        currentAgent.addBel(Literal.parseLiteral(String.format("half_size(%f)", Math.max(10, weight / 4))));

        return true;
    }
}
