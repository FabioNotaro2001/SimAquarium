package env.model;

public enum Speed {
    SLOW(0.5),  // No food but hungry.
    NORMAL(1),  // No food and not hungry.
    FAST(1.5),  // Food but not hungry.
    FASTER(2);  // Food and hungry.

    private final double speed;

    private Speed(double speed) {
        this.speed = speed;
    }

    public double getSpeed(){
        return this.speed;
    }
}
