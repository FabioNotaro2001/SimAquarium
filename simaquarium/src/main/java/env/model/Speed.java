package env.model;

/**
 * The {@code Speed} enum represents different speed levels for an entity,
 * each associated with a specific multiplier value. The speed levels are
 * determined based on the entity's state, such as hunger and the presence
 * of food.
 * 
 * <ul>
 *   <li>{@link #SLOW} - Represents a speed multiplier of 0.5, used when there is no food and the entity is hungry.</li>
 *   <li>{@link #NORMAL} - Represents a speed multiplier of 1, used when there is no food and the entity is not hungry.</li>
 *   <li>{@link #FAST} - Represents a speed multiplier of 1.5, used when there is food but the entity is not hungry.</li>
 *   <li>{@link #FASTER} - Represents a speed multiplier of 2, used when there is food and the entity is hungry.</li>
 * </ul>
 * 
 * Each speed level is associated with a specific multiplier value that can
 * be retrieved using the {@link #getSpeed()} method.
 */
public enum Speed {
    SLOW(0.5),  // No food but hungry.
    NORMAL(1),  // No food and not hungry.
    FAST(1.5),  // Food but not hungry.
    FASTER(2);  // Food and hungry.

    private final double speed;

    private Speed(double speed) {
        this.speed = speed;
    }

    /**
     * Retrieves the current speed.
     *
     * @return the speed as a double value.
     */
    public double getSpeed(){
        return this.speed;
    }
}
