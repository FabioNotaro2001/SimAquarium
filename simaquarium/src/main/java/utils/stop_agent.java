package utils;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import jason.runtime.RuntimeServices;
import jason.runtime.RuntimeServicesFactory;

public class stop_agent extends DefaultInternalAction{
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        RuntimeServices rs = RuntimeServicesFactory.get();
        rs.killAgent(ts.getAgArch().getAgName(), null, 1);
        System.out.println("L'AGENTE E' STATO UCCISO CORRETTAMENTE!!!!");
        return true;
    }
}
