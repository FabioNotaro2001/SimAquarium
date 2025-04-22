package env.model;

/**
 * A generic class representing a pair of two objects.
 * 
 * @param <X> the type of the first element in the pair
 * @param <Y> the type of the second element in the pair
 */
public class Pair<X, Y> {
    private X x;
    private Y y;

    /**
     * Constructs a Pair object with the specified values for x and y.
     *
     * @param x the first value of the pair
     * @param y the second value of the pair
     */
    public Pair(X x, Y y){
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the value of the x-coordinate.
     *
     * @return the value of the x-coordinate of type X
     */
    public X getX(){
        return this.x;
    }

    /**
     * Retrieves the value of the y-coordinate or second element of the pair.
     *
     * @return the value of the y-coordinate or second element of type Y
     */
    public Y getY(){
        return this.y;
    }

    /**
     * Sets the value of the x coordinate.
     *
     * @param newX the new value to set for x
     */
    public void setX(X newX){
        this.x= newX;
    }

    /**
     * Sets the value of the y coordinate or second element of the pair.
     *
     * @param newY the new value to set for y
     */
    public void setY(Y newY){
        this.y= newY;
    }


    /**
     * Creates a new instance of a Pair with the specified values.
     *
     * @param <X> the type of the first element in the pair
     * @param <Y> the type of the second element in the pair
     * @param x   the first element of the pair
     * @param y   the second element of the pair
     * @return a new Pair containing the specified elements
     */
    public static <X, Y> Pair<X, Y> of(X x, Y y){
        return new Pair<>(x, y);
    }
}
