package env.model;

import utils.Utils;

// TODO: Fare che il pesce pesante abbia bisogno di più cibo per raggiungere il 100% di energia
// TODO: Alla fine del progetto ovunque togliere i magic number e metterci costanti.
public class Food {
    public final double DEFAULT_ENERGY = 30.0;
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
