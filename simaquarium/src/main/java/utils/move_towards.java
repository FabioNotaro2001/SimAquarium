package utils;

import static utils.Utils.*;

import env.model.Speed;
import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class move_towards extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Speed speed = termToSpeed(args[0]);

        Agent currentAgent = ts.getAg();
        
        Literal energyLiteral = currentAgent.findBel(Literal.parseLiteral("energy(_, _)"), un);
        Literal weightLiteral = currentAgent.findBel(Literal.parseLiteral("weight(_)"), un);
        double fishEnergy = termToDouble(energyLiteral.getTerm(0));
        double fishMaxEnergy = termToDouble(energyLiteral.getTerm(1));
        double weight = termToDouble(weightLiteral.getTerm(0));
        fishEnergy -= speed.getSpeed() * Utils.MOVEMENT_MULTIPLIER * weight;
        currentAgent.delBel(energyLiteral);
        currentAgent.addBel(Literal.parseLiteral(String.format("energy(%f, %f)", Math.max(0, fishEnergy), fishMaxEnergy)));
        Thread.sleep(100);
        return true;
    }
}
