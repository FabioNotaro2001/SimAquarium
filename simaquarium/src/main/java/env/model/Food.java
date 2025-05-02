package env.model;

import utils.Utils;

// TODO Fare che il pesce pesante abbia bisogno di più cibo per raggiungere il 100% di energia
public class Food {
    public final double DEFAULT_ENERGY = 20.0;
    private String id;
    private Position position;
    private double foodSinkSpeed;

    public Food(String id, Position position) {
        this.id = id;
        this.position = position;
        this.foodSinkSpeed = Utils.RAND.nextDouble() * 2 - 1;
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

    public void sink() { // FIXME: implementare nel model
        this.position.setY(Math.max(0, this.getY() + 2 + this.foodSinkSpeed));
    }

}
