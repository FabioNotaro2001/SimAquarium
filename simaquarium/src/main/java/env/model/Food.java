package env.model;

// TODO Fare che il pesce pesante abbia bisogno di più cibo per raggiungere il 100% di energia
public class Food {
    public final double DEFAULT_ENERGY = 20.0;
    private String id;
    private Position position;

    public Food(String id, Position position) {
        this.id = id;
        this.position = position;
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
        this.position.setY(Math.max(0, this.getY() - 1));
    }

}
