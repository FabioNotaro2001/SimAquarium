package utils;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.util.List;
import java.util.Optional;
import env.model.Position;
import static utils.Utils.*;

/**
 * The `find_nearest` class is a custom internal action for agents in the simulation.
 * It allows agents to find the nearest target (e.g., food) from a list of positions.
 * The class calculates the closest position to the agent's current location and updates
 * the agent's beliefs with the target's coordinates.
 */
public class find_nearest extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();

        List<Term> coordinatesList = termToList(args[0]);
        Optional<Position> closestFood = coordinatesList.stream()
            .map(t -> literalToPosition((Literal)t))
            .min((p1, p2) -> Double.compare(Position.zero().distanceFrom(p1), Position.zero().distanceFrom(p2)));

        Literal hasTargetLiteral = currentAgent.findBel(Literal.parseLiteral("has_target(_, _)"), un);
        if(hasTargetLiteral != null){
            currentAgent.delBel(hasTargetLiteral);
        }

        if(closestFood.isPresent()){
            currentAgent.addBel(Literal.parseLiteral(String.format("has_target(%f, %f)", closestFood.get().getX(), closestFood.get().getY())));
        } 
        
        return true;
    }
}

