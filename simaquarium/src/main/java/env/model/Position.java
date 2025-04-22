package env.model;

public class Position {
    private double x;
    private double y;

    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static Position of(double x, double y){
        return new Position(x, y);
    }
    
    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }
    
    public void setX(double newX){
        this.x = newX;
    }
    
    public void setY(double newY){
        this.y = newY;
    }

    public double distanceFrom(Position other){
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}
