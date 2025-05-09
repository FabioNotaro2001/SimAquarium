package env.model;

import utils.Utils;

public class Fish {
    private static final int MIN_RANGE = 10;
    private String id;
    private double weight; // in grams, between 30 and 120.
    private double energy;
    private double maxEnergy;
    private Position position;
    private long numberOfFoodEaten;
    private Vector2D direction = Vector2D.of(1, 0);

    public Fish(String id, double weight, double energy, double maxEnergy, Position position) {
        this.id = id;
        this.weight = weight;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        this.position = position;
        this.numberOfFoodEaten = 0;
    }

    public String getId(){
        return this.id;
    }

    public double getX() {
        return this.position.getX();
    }
    
    public double getY() {
        return this.position.getY();
    }
    
    public double getDirX() {
        return this.direction.getX();
    }
    
    public double getDirY() {
        return this.direction.getY();
    }
    
    public double getWeight() {
        return this.weight;
    }
    
    public double getRange() {
        return MIN_RANGE + this.weight * 2;
    }
    
    public double getBaseSpeed() {
        return Fish.speedFromWeight(this.weight);
    }
    
    public double getEnergy() {
        return this.energy;
    }

    public Position getPosition() {
        return new Position(getX(), getY());
    }
    
    public Vector2D getDirection() {
        return Vector2D.of(this.direction.getX(), this.direction.getY());
    }

    public void moveTowards(Position target, Speed speed) {
        this.direction = Vector2D.fromPositions(this.position, target).normalize();
        this.position.setX(this.position.getX() + this.direction.getX() * this.getBaseSpeed() * speed.getSpeed());
        this.position.setY(this.position.getY() + this.direction.getY() * this.getBaseSpeed() * speed.getSpeed());

        this.decreaseEnergy(speed.getSpeed() * Utils.MOVEMENT_MULTIPLIER * this.weight);
    }

    public static double speedFromWeight(double weight) {
        return 300 / weight;
    }

    public void addEnergy(double energy) {
        this.energy = Math.min(this.maxEnergy, this.energy + energy);
    }

    public void decreaseEnergy(double energy) {
        this.energy = Math.max(0, this.energy - energy);
    }

    public double getEatingRange(){
        return Math.min(MIN_RANGE, getRange() / 15);
    }

    public double getObstacleRange(){
        return Math.min(MIN_RANGE, this.getRange() / 10);
    }

    public double getSize(){
        return Math.max(MIN_RANGE, this.getWeight() / 4);
    }

    public void incrementFoodEaten() {
        this.numberOfFoodEaten++;
    }

    public long getNumberOfFoodEaten() {
        return this.numberOfFoodEaten;
    }

    public double getMaxEnergy(){
        return this.maxEnergy;
    }
}
