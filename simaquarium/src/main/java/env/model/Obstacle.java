package env.model;

/**
 * The `Obstacle` class represents an obstacle in the simulation.
 * Each obstacle has a position (x, y) and a radius, which defines its size.
 */
public class Obstacle {
    private double x;       // The x-coordinate of the obstacle's position
    private double y;       // The y-coordinate of the obstacle's position
    private double radius;  // The radius of the obstacle

    /**
     * Constructs a new `Obstacle` instance with the specified position and radius.
     * 
     * @param x The x-coordinate of the obstacle's position.
     * @param y The y-coordinate of the obstacle's position.
     * @param r The radius of the obstacle.
     */
    public Obstacle(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.radius = r;
    }

    /**
     * Retrieves the x-coordinate of the obstacle's position.
     * 
     * @return The x-coordinate.
     */
    public double getX(){
        return this.x;
    }

    /**
     * Retrieves the y-coordinate of the obstacle's position.
     * 
     * @return The y-coordinate.
     */
    public double getY(){
        return this.y;
    }

    /**
     * Retrieves the radius of the obstacle.
     * 
     * @return The radius.
     */
    public double getRadius(){
        return this.radius;
    }

    /**
     * Retrieves the position of the obstacle as a `Position` object.
     * 
     * @return The position of the obstacle.
     */
    public Position getPosition() {
        return new Position(getX(), getY());
    }
}
