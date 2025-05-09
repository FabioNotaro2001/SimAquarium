package utils;

import java.util.Random;

import env.model.Direction;
import env.model.Pair;
import env.model.Position;
import env.model.Speed;
import env.model.Vector2D;
import jason.NoValueException;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class Utils {
    public static final Random RAND = new Random();
    public static final double MOVEMENT_MULTIPLIER = 1.0 / 200;
    public static final double FOOD_ENERGY_INCREASE = 30.0;

    public static ListTerm termToList(Term term){
        if(!term.isList()){
            throw new IllegalArgumentException("Cannot parse as ListTerm: " + term);
        }
        return (ListTerm)term;
    }

    public static double termToNumber(Term term) throws NoValueException {
        if (!term.isNumeric()) {
            throw new IllegalArgumentException("Cannot parse as number: " + term);
        }
        return ((NumberTerm)term).solve();
    }

    public static Term numberToTerm(int value) {
        return new NumberTermImpl(value);
    }

    public static Term numberToTerm(double value) {
        return new NumberTermImpl(value);
    }

    public static double termToDouble(Term term) throws NoValueException {
        return (double) termToNumber(term);
    }

    public static String termToString(Term term) throws NoValueException {
        if(!(term.isString() || term.isAtom())) {
            throw new IllegalArgumentException("Cannot parse as String: " + term);
        }
        return term.toString();
    }


    public static Position literalToPosition(Literal literal) {
        if (!literal.getTerm(0).isNumeric() || !literal.getTerm(1).isNumeric()) {
            throw new IllegalArgumentException("Cannot parse as Position: " + literal);
        }
        try {
            return Position.of(
                    termToDouble(literal.getTerm(0)),
                    termToDouble(literal.getTerm(1))
            );
        } catch (NoValueException e) {
            throw new IllegalArgumentException("Cannot parse as Position: " + literal);
        }
    }
    
    public static Vector2D literalToVector2D(Literal literal) {
        if (!literal.getTerm(0).isNumeric() || !literal.getTerm(1).isNumeric()) {
            throw new IllegalArgumentException("Cannot parse as Vector2D: " + literal);
        }
        try {
            return Vector2D.of(
                    termToDouble(literal.getTerm(0)),
                    termToDouble(literal.getTerm(1))
            );
        } catch (NoValueException e) {
            throw new IllegalArgumentException("Cannot parse as Vector2D: " + literal);
        }
    }

    public static Pair<Position, String> literalToPositionAndId(Literal literal) {
        if (!literal.getTerm(0).isNumeric() || !literal.getTerm(1).isNumeric() || !literal.getTerm(2).isString()) {
            throw new IllegalArgumentException("Cannot parse as position and id: " + literal);
        }
        try {
            return new Pair<>(
                    Position.of(
                            termToDouble(literal.getTerm(0)),
                            termToDouble(literal.getTerm(1))
                    ),
                    termToString(literal.getTerm(2))
            );
        } catch (NoValueException e) {
            throw new IllegalArgumentException("Cannot parse as position and id: " + literal);
        }
    }

    public static Pair<Position, Double> literalToPositionAndRadius(Literal literal) {
        if (!literal.getTerm(0).isNumeric() || !literal.getTerm(1).isNumeric()) {
            throw new IllegalArgumentException("Cannot parse as position and radius: " + literal);
        }
        try {
            return new Pair<>(
                    Position.of(
                            termToDouble(literal.getTerm(0)),
                            termToDouble(literal.getTerm(1))
                    ),
                    termToDouble(literal.getTerm(2))
            );
        } catch (NoValueException e) {
            throw new IllegalArgumentException("Cannot parse as position and radius: " + literal);
        }
    }
    
    public static Pair<Direction, Double> literalToDirectionAndDistance(Literal literal) {
        if (!literal.getTerm(0).isAtom() || !literal.getTerm(1).isNumeric()) {
            throw new IllegalArgumentException("Cannot parse as direction and distance: " + literal);
        }
        try {
            return Pair.of(
                termToDirection(literal.getTerm(0)),
                termToDouble(literal.getTerm(1))
            );
        } catch (NoValueException e) {
            throw new IllegalArgumentException("Cannot parse as direction and distance: " + literal);
        }
    }

    public static Speed termToSpeed(Term term) {
        if (!term.isAtom()) {
            throw new IllegalArgumentException("Cannot parse as Speed: " + term);
        }
        return Speed.valueOf(((Atom) term).getFunctor().toUpperCase());
    }

    public static Direction termToDirection(Term term) {
        if (!term.isAtom()) {
            throw new IllegalArgumentException("Cannot parse as Direction: " + term);
        }
        return Direction.valueOf(((Atom) term).getFunctor().toUpperCase());
    }

    public static double map(double min1, double max1, double min2, double max2, double val){
        double ratio = (max2 - min2) / (max1 - min1); 
        return (val - min1) * ratio + min2;
    }
}
