package env.model;

/**
 * A generic class representing a pair of two objects.
 * 
 * @param <X> the type of the first element in the pair
 * @param <Y> the type of the second element in the pair
 */
public class Pair<X, Y> {
    private X first;
    private Y second;

    /**
     * Constructs a Pair object with the specified values for x and y.
     *
     * @param first the first value of the pair
     * @param second the second value of the pair
     */
    public Pair(X first, Y second){
        this.first = first;
        this.second = second;
    }

    /**
     * Retrieves the value of the x-coordinate.
     *
     * @return the value of the x-coordinate of type X
     */
    public X getFirst(){
        return this.first;
    }

    /**
     * Retrieves the value of the y-coordinate or second element of the pair.
     *
     * @return the value of the y-coordinate or second element of type Y
     */
    public Y getSecond(){
        return this.second;
    }

    /**
     * Sets the value of the x coordinate.
     *
     * @param newX the new value to set for x
     */
    public void setFirst(X newX){
        this.first= newX;
    }

    /**
     * Sets the value of the y coordinate or second element of the pair.
     *
     * @param newY the new value to set for y
     */
    public void setSecond(Y newY){
        this.second= newY;
    }


    /**
     * Creates a new instance of a Pair with the specified values.
     *
     * @param <X> the type of the first element in the pair
     * @param <Y> the type of the second element in the pair
     * @param first   the first element of the pair
     * @param second   the second element of the pair
     * @return a new Pair containing the specified elements
     */
    public static <X, Y> Pair<X, Y> of(X first, Y second){
        return new Pair<>(first, second);
    }
}
