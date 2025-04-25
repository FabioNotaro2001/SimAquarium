package utils;

import java.util.Random;

import env.model.Pair;
import env.model.Position;
import env.model.Speed;
import jason.NoValueException;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

public class Utils {
    public static final Random RAND = new Random();

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
        if(!term.isString()) {
            throw new IllegalArgumentException("Cannot parse as String: " + term);
        }
        return ((StringTerm)term).getString();
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

    public static Speed termToSpeed(Term term) {
        if (!term.isAtom()) {
            throw new IllegalArgumentException("Cannot parse as Speed: " + term);
        }
        return Speed.valueOf(((Atom) term).getFunctor().toUpperCase());
    }
}
