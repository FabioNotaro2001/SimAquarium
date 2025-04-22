package env.model;

import java.util.Objects;

public final class Vector2D {
    private final double x;
    private final double y;

    public static Vector2D of(double x, double y) {
        return new Vector2D(x, y);
    }

    private Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D that = (Vector2D) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    public Vector2D times(double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    public Vector2D plus(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D minus(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D plus(double x, double y) {
        return new Vector2D(this.x + x, this.y + y);
    }

    public Vector2D minus(double x, double y) {
        return new Vector2D(this.x - x, this.y - y);
    }

    public Vector2D normalize(){
        double length = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
        return length == 0 ? zero : new Vector2D(this.x / length, this.y / length);
    }

    public static Vector2D fromPositions(Position to, Position from){
        return new Vector2D(from.getX() - to.getX(), from.getY() + to.getY());
    }

    public static Vector2D zero = new Vector2D(0, 0);
}
