package env.model;

public class Food {
    private String id;
    private Position position;

    public Food(String id, Position position) {
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
