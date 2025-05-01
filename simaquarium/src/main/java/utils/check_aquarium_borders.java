package utils;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import static utils.Utils.literalToVector2D;
import static utils.Utils.literalToDirectionAndDistance;
import static utils.Utils.termToDirection;
import static utils.Utils.termToList;

import java.util.List;

import env.model.Direction;
import env.model.Vector2D;

public class check_aquarium_borders extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();
        Literal directionAsLiteral = currentAgent.findBel(Literal.parseLiteral("direction(X, Y)"), un);
        Vector2D fishDir = literalToVector2D(directionAsLiteral);
        Literal bordersAsLiteral = currentAgent.findBel(Literal.parseLiteral("borders(L)"), un);

        List<Term> bordersList = termToList(bordersAsLiteral.getTerm(0));
        for (Term border : bordersList) {
            var b = literalToDirectionAndDistance((Literal)border);

            if(b.getY() > 30) { // TODO: belief sulla distanza minima? in base alla velocità?
                continue;
            }

            if((b.getX() == Direction.RIGHT && fishDir.getX() > 0)|| (b.getX() == Direction.LEFT && fishDir.getX() < 0)){
                fishDir = fishDir.flipVertically();
            } else if((b.getX() == Direction.BOTTOM && fishDir.getY() > 0)|| (b.getX() == Direction.TOP && fishDir.getY() < 0))  {
                fishDir = fishDir.flipHorizontally();
            }
        }
        currentAgent.delBel(directionAsLiteral);
        currentAgent.addBel(Literal.parseLiteral(String.format("direction(%f, %f)", fishDir.getX(), fishDir.getY())));
        
        return true;
    }
}
