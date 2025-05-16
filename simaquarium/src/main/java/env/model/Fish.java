package env.model;

import utils.Utils;

/**
 * The `Fish` class represents a fish agent in the simulation.
 * It contains attributes and methods to manage the fish's state, movement, and interactions.
 */
public class Fish {
    private static final int MIN_RANGE = 10; // Minimum range for various calculations
    private String id; // Unique identifier for the fish
    private double weight; // Weight of the fish in grams, between 30 and 120
    private double energy; // Current energy level of the fish
    private double maxEnergy; // Maximum energy level of the fish
    private Position position; // Current position of the fish
    private long numberOfFoodEaten; // Number of food items eaten by the fish
    private Vector2D direction = Vector2D.of(1, 0); // Current direction of the fish

     /**
     * Constructs a new `Fish` instance with the specified attributes.
     * 
     * @param id The unique identifier for the fish.
     * @param weight The weight of the fish in grams.
     * @param energy The initial energy level of the fish.
     * @param maxEnergy The maximum energy level of the fish.
     * @param position The initial position of the fish.
     */
    public Fish(String id, double weight, double energy, double maxEnergy, Position position) {
        this.id = id;
        this.weight = weight;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        this.position = position;
        this.numberOfFoodEaten = 0;
    }

    /**
     * Retrieves the unique identifier of the fish.
     * 
     * @return The fish's ID.
     */
    public String getId(){
        return this.id;
    }

    /**
     * Retrieves the x-coordinate of the fish's position.
     * 
     * @return The x-coordinate.
     */
    public double getX() {
        return this.position.getX();
    }
    
    /**
     * Retrieves the y-coordinate of the fish's position.
     * 
     * @return The y-coordinate.
     */
    public double getY() {
        return this.position.getY();
    }
    
    /**
     * Retrieves the x-component of the fish's direction.
     * 
     * @return The x-component of the direction.
     */
    public double getDirX() {
        return this.direction.getX();
    }
    
    /**
     * Retrieves the y-component of the fish's direction.
     * 
     * @return The y-component of the direction.
     */
    public double getDirY() {
        return this.direction.getY();
    }
    
    /**
     * Retrieves the weight of the fish.
     * 
     * @return The weight in grams.
     */
    public double getWeight() {
        return this.weight;
    }
    
    /**
     * Calculates the range of the fish based on its weight.
     * 
     * @return The range of the fish.
     */
    public double getRange() {
        return MIN_RANGE + this.weight * 2;
    }
    
    /**
     * Calculates the base speed of the fish based on its weight.
     * 
     * @return The base speed of the fish.
     */
    public double getBaseSpeed() {
        return Fish.speedFromWeight(this.weight);
    }
    
    /**
     * Retrieves the current energy level of the fish.
     * 
     * @return The current energy level.
     */
    public double getEnergy() {
        return this.energy;
    }

    /**
     * Retrieves the current position of the fish.
     * 
     * @return The position as a `Position` object.
     */
    public Position getPosition() {
        return new Position(getX(), getY());
    }
    
    /**
     * Retrieves the current direction of the fish.
     * 
     * @return The direction as a `Vector2D` object.
     */
    public Vector2D getDirection() {
        return Vector2D.of(this.direction.getX(), this.direction.getY());
    }

    /**
     * Moves the fish towards a target position at a specified speed.
     * 
     * @param target The target position.
     * @param speed The speed at which the fish moves.
     */
    public void moveTowards(Position target, Speed speed) {
        this.direction = Vector2D.fromPositions(this.position, target).normalize();
        this.position.setX(this.position.getX() + this.direction.getX() * this.getBaseSpeed() * speed.getSpeed());
        this.position.setY(this.position.getY() + this.direction.getY() * this.getBaseSpeed() * speed.getSpeed());

        this.decreaseEnergy(speed.getSpeed() * Utils.MOVEMENT_MULTIPLIER * this.weight);
    }

    /**
     * Calculates the speed of the fish based on its weight.
     * 
     * @param weight The weight of the fish.
     * @return The calculated speed.
     */
    public static double speedFromWeight(double weight) {
        return 300 / weight;
    }

    /**
     * Increases the fish's energy by a specified amount, up to its maximum energy.
     * 
     * @param energy The amount of energy to add.
     */
    public void addEnergy(double energy) {
        this.energy = Math.min(this.maxEnergy, this.energy + energy);
    }

    /**
     * Decreases the fish's energy by a specified amount, down to a minimum of 0.
     * 
     * @param energy The amount of energy to subtract.
     */
    public void decreaseEnergy(double energy) {
        this.energy = Math.max(0, this.energy - energy);
    }

    /**
     * Calculates the eating range of the fish.
     * 
     * @return The eating range.
     */
    public double getEatingRange(){
        return Math.min(MIN_RANGE, getRange() / 15);
    }

    /**
     * Calculates the obstacle detection range of the fish.
     * 
     * @return The obstacle range.
     */
    public double getObstacleRange(){
        return Math.min(MIN_RANGE, this.getRange() / 10);
    }

    /**
     * Calculates the size of the fish based on its weight.
     * 
     * @return The size of the fish.
     */
    public double getSize(){
        return Math.max(MIN_RANGE, this.getWeight() / 4);
    }

    /**
     * Increments the number of food items eaten by the fish.
     */
    public void incrementFoodEaten() {
        this.numberOfFoodEaten++;
    }

    /**
     * Retrieves the total number of food items eaten by the fish.
     * 
     * @return The number of food items eaten.
     */
    public long getNumberOfFoodEaten() {
        return this.numberOfFoodEaten;
    }

    /**
     * Retrieves the maximum energy level of the fish.
     * 
     * @return The maximum energy level.
     */
    public double getMaxEnergy(){
        return this.maxEnergy;
    }
}
