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

/**
 * The `Utils` class provides utility methods and constants for the simulation.
 * These methods are used to perform common operations such as parsing terms,
 * converting between data types, and performing mathematical calculations.
 * 
 * The class also includes constants for random number generation, movement scaling,
 * and energy increase values.
 */
public class Utils {
    public static final Random RAND = new Random();
    public static final double MOVEMENT_MULTIPLIER = 1.0 / 200;
    public static final double FOOD_ENERGY_INCREASE = 30.0;

    /**
     * Converts a `Term` to a `ListTerm`.
     * 
     * @param term The term to convert.
     * @return The converted `ListTerm`.
     * @throws IllegalArgumentException If the term is not a list.
     */
    public static ListTerm termToList(Term term){
        if(!term.isList()){
            throw new IllegalArgumentException("Cannot parse as ListTerm: " + term);
        }
        return (ListTerm)term;
    }

    /**
     * Converts a `Term` to a numeric value.
     * 
     * @param term The term to convert.
     * @return The numeric value as a double.
     * @throws NoValueException If the term has no value.
     * @throws IllegalArgumentException If the term is not numeric.
     */
    public static double termToNumber(Term term) throws NoValueException {
        if (!term.isNumeric()) {
            throw new IllegalArgumentException("Cannot parse as number: " + term);
        }
        return ((NumberTerm)term).solve();
    }

    /**
     * Converts an integer to a `Term`.
     * 
     * @param value The integer value to convert.
     * @return The converted `Term`.
     */
    public static Term numberToTerm(int value) {
        return new NumberTermImpl(value);
    }

    /**
     * Converts a double to a `Term`.
     * 
     * @param value The double value to convert.
     * @return The converted `Term`.
     */
    public static Term numberToTerm(double value) {
        return new NumberTermImpl(value);
    }

    /**
     * Converts a `Term` to a double value.
     * 
     * @param term The term to convert.
     * @return The double value.
     * @throws NoValueException If the term has no value.
     */
    public static double termToDouble(Term term) throws NoValueException {
        return (double) termToNumber(term);
    }

    /**
     * Converts a `Term` to a string.
     * 
     * @param term The term to convert.
     * @return The string representation of the term.
     * @throws IllegalArgumentException If the term is not a string or atom.
     */
    public static String termToString(Term term) throws NoValueException {
        if(!(term.isString() || term.isAtom())) {
            throw new IllegalArgumentException("Cannot parse as String: " + term);
        }
        return term.toString();
    }

    /**
     * Converts a `Literal` to a `Position`.
     * 
     * @param literal The literal to convert.
     * @return The converted `Position`.
     * @throws IllegalArgumentException If the literal cannot be parsed as a position.
     */
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
    
    /**
     * Converts a `Literal` to a `Vector2D`.
     * 
     * @param literal The literal to convert.
     * @return The converted `Vector2D`.
     * @throws IllegalArgumentException If the literal cannot be parsed as a vector.
     */
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

    /**
     * Converts a `Literal` to a `Pair` containing a `Position` and a string ID.
     * 
     * @param literal The literal to convert.
     * @return The converted `Pair`.
     * @throws IllegalArgumentException If the literal cannot be parsed as a position and ID.
     */
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

    /**
     * Converts a `Literal` to a `Pair` containing a `Position` and a radius.
     * 
     * @param literal The literal to convert.
     * @return The converted `Pair`.
     * @throws IllegalArgumentException If the literal cannot be parsed as a position and radius.
     */
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
    
    /**
     * Converts a `Literal` to a `Pair` containing a `Direction` and a distance.
     * 
     * @param literal The literal to convert.
     * @return The converted `Pair`.
     * @throws IllegalArgumentException If the literal cannot be parsed as a direction and distance.
     */
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

    /**
     * Converts a `Term` to a `Speed`.
     * 
     * @param term The term to convert.
     * @return The converted `Speed`.
     * @throws IllegalArgumentException If the term cannot be parsed as a speed.
     */
    public static Speed termToSpeed(Term term) {
        if (!term.isAtom()) {
            throw new IllegalArgumentException("Cannot parse as Speed: " + term);
        }
        return Speed.valueOf(((Atom) term).getFunctor().toUpperCase());
    }

    /**
     * Converts a `Term` to a `Direction`.
     * 
     * @param term The term to convert.
     * @return The converted `Direction`.
     * @throws IllegalArgumentException If the term cannot be parsed as a direction.
     */
    public static Direction termToDirection(Term term) {
        if (!term.isAtom()) {
            throw new IllegalArgumentException("Cannot parse as Direction: " + term);
        }
        return Direction.valueOf(((Atom) term).getFunctor().toUpperCase());
    }

    /**
     * Maps a value from one range to another.
     * 
     * @param min1 The minimum value of the input range.
     * @param max1 The maximum value of the input range.
     * @param min2 The minimum value of the output range.
     * @param max2 The maximum value of the output range.
     * @param val The value to map.
     * @return The mapped value.
     */
    public static double map(double min1, double max1, double min2, double max2, double val){
        double ratio = (max2 - min2) / (max1 - min1); 
        return (val - min1) * ratio + min2;
    }
}
