package env.model;

import utils.Utils;

/**
 * The `Food` class represents a food item in the simulation.
 * Each food item has a unique ID, a position, a sinking speed, and a bottom limit.
 */
public class Food {
    private String id;              // Unique identifier for the food
    private Position position;      // Current position of the food
    private double foodSinkSpeed;   // Speed at which the food sinks
    private double bottom;          // The bottom limit of the aquarium

    /**
     * Constructs a new `Food` instance with the specified attributes.
     * 
     * @param id The unique identifier for the food.
     * @param position The initial position of the food.
     * @param bottom The bottom limit of the aquarium.
     */
    public Food(String id, Position position, double bottom) {
        this.id = id;
        this.position = position;
        this.foodSinkSpeed = Utils.RAND.nextDouble() * 2 - 1;
        this.bottom = bottom;
    }

    /**
     * Retrieves the unique identifier of the food.
     * 
     * @return The food's ID.
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * Retrieves the x-coordinate of the food's position.
     * 
     * @return The x-coordinate.
     */
    public double getX() {
        return this.position.getX();
    }
    
    /**
     * Retrieves the y-coordinate of the food's position.
     * 
     * @return The y-coordinate.
     */
    public double getY() {
        return this.position.getY();
    }

    /**
     * Retrieves the current position of the food.
     * 
     * @return The position as a `Position` object.
     */
    public Position getPosition() {
        return new Position(getX(), getY());
    }

    /**
     * Simulates the sinking of the food.
     * The food's y-coordinate is updated based on its sinking speed and the bottom limit.
     */
    public void sink() { 
        this.position.setY(Math.max(0, Math.min(this.bottom, this.getY() + 2 + this.foodSinkSpeed)));
    }

}
