package env.model;

public class Obstacle {
    private double x;
    private double y;
    private double radius;

    public Obstacle(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.radius = r;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public double getRadius(){
        return this.radius;
    }

    public Position getPosition() {
        return new Position(getX(), getY());
    }
}
