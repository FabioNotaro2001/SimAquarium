package env.model;

import java.util.Objects;

/**
 * Represents a 2D vector with immutable x and y components.
 */
public final class Vector2D {
    private final double x;
    private final double y;

    /**
     * Creates a new Vector2D from the given x and y values.
     *
     * @param x the x component
     * @param y the y component
     * @return a new Vector2D instance
     */
    public static Vector2D of(double x, double y) {
        return new Vector2D(x, y);
    }
    
    public static Vector2D of(double angle) {
        return new Vector2D(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Private constructor to enforce immutability.
     *
     * @param x the x component
     * @param y the y component
     */
    private Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x component
     */
    public double getX() {
        return x;
    }

    /**
     * @return the y component
     */
    public double getY() {
        return y;
    }

    public double getLength() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D that = (Vector2D) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    /**
     * Returns a new Vector2D scaled by the given factor.
     *
     * @param factor the scale factor
     * @return a new scaled Vector2D
     */
    public Vector2D times(double factor) {
        return new Vector2D(x * factor, y * factor);
    }
    
    /**
     * Returns a new Vector2D scaled by the given factor.
     *
     * @param factor the scale factor
     * @return a new scaled Vector2D
     */
    public double times(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Returns a new Vector2D by adding another Vector2D.
     *
     * @param other the vector to add
     * @return a new resulting Vector2D
     */
    public Vector2D plus(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    /**
     * Returns a new Vector2D by subtracting another Vector2D.
     *
     * @param other the vector to subtract
     * @return a new resulting Vector2D
     */
    public Vector2D minus(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    /**
     * Returns a new Vector2D by adding the given x and y values.
     *
     * @param x the x component to add
     * @param y the y component to add
     * @return a new resulting Vector2D
     */
    public Vector2D plus(double x, double y) {
        return new Vector2D(this.x + x, this.y + y);
    }

    /**
     * Returns a new Vector2D by subtracting the given x and y values.
     *
     * @param x the x component to subtract
     * @param y the y component to subtract
     * @return a new resulting Vector2D
     */
    public Vector2D minus(double x, double y) {
        return new Vector2D(this.x - x, this.y - y);
    }
    
    /**
     * Rotates the vector counterclockwise by an angle.
     *
     * @param angle the angle of rotation in radians
     * @return a new resulting Vector2D
     */
    public Vector2D rotateBy(double angle) {
        return new Vector2D(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
    }

    /**
     * Returns the angle between the vector and the x-axis.
     */
    public double angle() {
        return Math.atan2(y, x);
    }

    /**
     * Normalizes this vector to have length 1, if length is not zero.
     *
     * @return a normalized Vector2D or the zero vector if length is zero
     */
    public Vector2D normalize(){
        double length = this.getLength();
        return length == 0 ? zero : new Vector2D(this.x / length, this.y / length);
    }

    public Vector2D flipHorizontally(){
        return new Vector2D(x, -y);
    }
    
    public Vector2D flipVertically(){
        return new Vector2D(-x, y);
    }

    /**
     * Creates a new Vector2D from two Positions.
     *
     * @param to the "to" position
     * @param from the "from" position
     * @return a new Vector2D based on position differences
     */
    public static Vector2D fromPositions(Position to, Position from) {
        return new Vector2D(from.getX() - to.getX(), from.getY() - to.getY());
    }

    /**
     * A reusable zero vector constant.
     */
    public static Vector2D zero = new Vector2D(0, 0);
}
