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

public class find_nearest extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();
        Literal positionAsLiteral = currentAgent.findBel(Literal.parseLiteral("position(X, Y)"), un);
        Position fishPosition = literalToPosition(positionAsLiteral);

        List<Term> coordinatesList = termToList(args[0]);
        Optional<Position> closestFood = coordinatesList.stream()
            .map(t -> literalToPosition((Literal)t))
            .min((p1, p2) -> Double.compare(fishPosition.distanceFrom(p1), fishPosition.distanceFrom(p2)));

        Literal hasTargetLiteral = currentAgent.findBel(Literal.parseLiteral("has_target"), un);
        if(hasTargetLiteral != null){
            currentAgent.delBel(hasTargetLiteral);
        }

        if(closestFood.isPresent()){
            currentAgent.addBel(hasTargetLiteral);
            currentAgent.addBel(Literal.parseLiteral(String.format("direction(%f, %f)", closestFood.get().getX(), closestFood.get().getY())));
        } 
        
        return closestFood.isPresent();
    }
}
