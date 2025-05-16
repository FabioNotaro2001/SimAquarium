package utils;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import jason.runtime.RuntimeServices;
import jason.runtime.RuntimeServicesFactory;

/**
 * The `stop_agent` class is a custom internal action for agents in the simulation.
 * It stops the execution of the agent by terminating its runtime.
 * This action is typically used when an agent has completed its tasks or needs to be removed from the simulation.
 */
public class stop_agent extends DefaultInternalAction{

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        RuntimeServices rs = RuntimeServicesFactory.get();
        rs.killAgent(ts.getAgArch().getAgName(), null, 1);
        return true;
    }
}
