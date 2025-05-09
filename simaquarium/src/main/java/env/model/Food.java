package env.model;

import utils.Utils;

public class Food {
    private String id;
    private Position position;
    private double foodSinkSpeed;
    private double bottom;

    public Food(String id, Position position, double bottom) {
        this.id = id;
        this.position = position;
        this.foodSinkSpeed = Utils.RAND.nextDouble() * 2 - 1;
        this.bottom = bottom;
    }

    public String getId() {
        return this.id;
    }
    
    public double getX() {
        return this.position.getX();
    }
    
    public double getY() {
        return this.position.getY();
    }

    public Position getPosition() {
        return new Position(getX(), getY());
    }

    public void sink() { 
        this.position.setY(Math.max(0, Math.min(this.bottom, this.getY() + 2 + this.foodSinkSpeed)));
    }

}
