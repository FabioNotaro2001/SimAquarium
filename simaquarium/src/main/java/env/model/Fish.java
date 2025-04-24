package env.model;

public class Fish {
    private double weight; // in grams, between 10 and 100
    private double energy;
    private Position position;

    public Fish(double weight, double energy, Position position) {
        this.weight = weight;
        this.energy = energy;
        this.position = position;    
    }

    public Fish(double weight, Position position) {
        this(weight, 100, position);
    }

    public double getX() {
        return this.position.getX();
    }
    
    public double getY() {
        return this.position.getY();
    }
    

    public double getWeight() {
        return this.weight;
    }
    
    public double getRange() {
        return 10 + this.weight * 5;
    }
    
    public double getBaseSpeed() {
        return Fish.speedFromWeight(this.weight);
    }
    
    public double getEnergy() {
        return this.energy;
    }

    public Position getPosition() {
        return new Position(getX(), getY());
    }

    public void moveTowards(Position target, Speed speed) {
        Vector2D direction = Vector2D.fromPositions(this.position, target).normalize();
        this.position.setX(this.position.getX() + direction.getX() * this.getBaseSpeed() * speed.getSpeed());
        this.position.setY(this.position.getY() + direction.getY() * this.getBaseSpeed() * speed.getSpeed());

        this.energy = Math.max(0, this.energy - speed.getSpeed());
    }

    public static double speedFromWeight(double weight) {
        return 300 / weight;
    }
}
