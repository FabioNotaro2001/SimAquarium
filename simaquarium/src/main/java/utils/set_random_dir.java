package utils;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import static utils.Utils.literalToVector2D;

import env.model.Vector2D;

/**
 * The `set_random_dir` class is a custom internal action for agents in the simulation.
 * It sets a random direction for the agent by rotating its current direction by a random angle.
 * The random angle is calculated within a range of ±45 degrees (π/4 radians).
 * This action is used to simulate unpredictable movement behavior in the simulation.
 */
public class set_random_dir extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();

        var lit = currentAgent.findBel(Literal.parseLiteral("direction(_, _)"), un);
        Vector2D fishDir = literalToVector2D(lit).rotateBy((Utils.RAND.nextDouble() * 2 - 1) * Math.PI / 4);
        
        currentAgent.delBel(lit);
        currentAgent.addBel(Literal.parseLiteral(String.format("direction(%f, %f)", fishDir.getX(), fishDir.getY())));

        return true;
    }
}
