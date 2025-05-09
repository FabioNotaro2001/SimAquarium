package utils;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import static utils.Utils.literalToVector2D;
import static utils.Utils.map;
import static utils.Utils.termToDouble;

import env.model.Vector2D;

public class rotate_dir extends DefaultInternalAction {
    private static final int MIN_WEIGHT = 30;
    private static final int MAX_WEIGHT = 120;
    private static final int START_ANGLE = 30;
    private static final int END_ANGLE = 10;
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Agent currentAgent = ts.getAg();

        Literal directionLiteral = currentAgent.findBel(Literal.parseLiteral("direction(_, _)"), un);
        Literal hasTargLiteral = currentAgent.findBel(Literal.parseLiteral("has_target(_, _)"), un);
        if(hasTargLiteral == null){
            return true;
        }
        
        Literal weightLiteral = currentAgent.findBel(Literal.parseLiteral("weight(_)"), un);
        
        Vector2D fishDir = literalToVector2D(directionLiteral);
        Vector2D targetDir = literalToVector2D(hasTargLiteral);
        
        double weight = termToDouble(weightLiteral.getTerm(0));
        double angle = Math.toRadians(map(MIN_WEIGHT, MAX_WEIGHT, START_ANGLE, END_ANGLE, weight));

        double angleBetween = Vector2D.angleBetween(fishDir, targetDir);
        fishDir = Math.abs(angleBetween) <= angle ? targetDir : fishDir.rotateBy(angle * Math.signum(angleBetween));

        currentAgent.delBel(directionLiteral);
        currentAgent.addBel(Literal.parseLiteral(String.format("direction(%f, %f)", fishDir.getX(), fishDir.getY())));

        return true;
    }
}

