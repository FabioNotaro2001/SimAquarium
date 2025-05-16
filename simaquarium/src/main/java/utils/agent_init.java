package utils;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

/**
 * The `agent_init` class is a custom internal action for initializing an agent in the simulation.
 * It sets up the agent's initial beliefs, such as weight, size, energy, and food energy.
 */
public class agent_init extends DefaultInternalAction {
    private static final int MIN_WEIGHT = 30;
    private static final int MAX_WEIGHT = 120;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();
        double weight = Utils.map(0, 1, MIN_WEIGHT, MAX_WEIGHT, Utils.RAND.nextDouble());
        double energy = weight * 5;
        currentAgent.addBel(Literal.parseLiteral(String.format("weight(%f)", weight)));
        currentAgent.addBel(Literal.parseLiteral(String.format("half_size(%f)", Math.max(10, weight / 4))));
        currentAgent.addBel(Literal.parseLiteral(String.format("energy(%f, %f)", energy, energy)));
        currentAgent.addBel(Literal.parseLiteral(String.format("food_energy(%f)", Utils.FOOD_ENERGY_INCREASE)));
        return true;
    }
}
