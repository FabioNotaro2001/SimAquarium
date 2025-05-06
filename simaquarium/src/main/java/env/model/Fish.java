package env.model;

public class Fish {
    private double weight; // in grams, between 10 and 100
    private double energy;
    private Position position;
    private Vector2D direction = Vector2D.of(1, 0);

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
    
    public double getDirX() {
        return this.direction.getX();
    }
    
    public double getDirY() {
        return this.direction.getY();
    }
    
    public double getWeight() {
        return this.weight;
    }
    
    public double getRange() {
        return 10 + this.weight * 2;
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
    
    public Vector2D getDirection() {
        return Vector2D.of(this.direction.getX(), this.direction.getY());
    }

    public void moveTowards(Position target, Speed speed) {
        this.direction = Vector2D.fromPositions(this.position, target).normalize();
        this.position.setX(this.position.getX() + this.direction.getX() * this.getBaseSpeed() * speed.getSpeed());
        this.position.setY(this.position.getY() + this.direction.getY() * this.getBaseSpeed() * speed.getSpeed());

        this.energy = Math.max(0, this.energy - speed.getSpeed());
    }

    public static double speedFromWeight(double weight) {
        return 300 / weight;
    }

    public void addEnergy(double energy) {
        this.energy = Math.min(100, this.energy + energy);
    }

    public void decreaseEnergy(double energy) {
        this.energy = Math.max(0, this.energy - energy);
    }

    public double getEatingRange(){
        return Math.min(10, getRange() / 15);
    }

    public double getObstacleRange(){
        return Math.min(10, this.getRange() / 10);
    }

    public double getDrawingSize(){
        return Math.max(10, this.getWeight() / 4);
    }
}
