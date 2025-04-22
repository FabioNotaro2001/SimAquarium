package env.model;

import java.util.Objects;

/**
 * The Position class represents a point in a 2D space with x and y coordinates.
 * It provides methods to create, access, modify, and calculate the distance between positions.
 */
public class Position {
    private double x;
    private double y;

    /**
     * Constructs a Position object with the specified x and y coordinates.
     *
     * @param x the x coordinate of the position
     * @param y the y coordinate of the position
     */
    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }


    /**
     * Creates a new {@code Position} instance with the specified x and y coordinates.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     * @return a new {@code Position} object with the given coordinates
     */
    public static Position of(double x, double y){
        return new Position(x, y);
    }
    
    
    /**
     * Retrieves the x-coordinate of this position.
     *
     * @return the x-coordinate as a double.
     */
    public double getX(){
        return this.x;
    }
    
    /**
     * Retrieves the y-coordinate of this position.
     *
     * @return the y-coordinate as a double.
     */
    public double getY(){
        return this.y;
    }
    
    /**
     * Sets the x-coordinate of the position to the specified value.
     *
     * @param newX the new x-coordinate value to set
     */
    public void setX(double newX){
        this.x = newX;
    }
    
    /**
     * Sets the y-coordinate of the position to the specified value.
     *
     * @param newY the new y-coordinate value to set
     */
    public void setY(double newY){
        this.y = newY;
    }

    /**
     * Calculates the Euclidean distance between this position and another position.
     *
     * @param other The other position to calculate the distance from.
     * @return The Euclidean distance between this position and the specified position.
     */
    public double distanceFrom(Position other){
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}
