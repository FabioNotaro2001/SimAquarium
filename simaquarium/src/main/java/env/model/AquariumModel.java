package env.model;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface AquariumModel {
    /** Checks whether an agent with the given name exists in the environment */
    boolean containsAgent(String name);

    /** Retrieves the set of agent identifiers of all the agents currently existing into the environment */
    Set<String> getAllAgents();
    
    /** Retrieves the set of positions of the food currently existing into the environment */
    Set<Food> getAllFood();

    Set<Obstacle> getAllObstacles();

    /** Retrieves the width of the environment */
    int getWidth();

    /** Retrieves the height of the environment */
    int getHeight();

    /** Checks whether a given position is inside the environment, given its coordinates */
    boolean isPositionInside(double x, double y);

    /** Checks whether a given position is inside the environment */
    default boolean isPositionInside(Vector2D position) {
        return isPositionInside(position.getX(), position.getY());
    }

    /** Checks whether a given position is outside the environment, given its coordinates */
    default boolean isPositionOutside(double x, double y) {
        return !isPositionInside(x, y);
    }

    /** Checks whether a given position is outside the environment */
    default boolean isPositionOutside(Vector2D position) {
        return isPositionOutside(position.getX(), position.getY());
    }

    /** Retrieves an agent's absolute position in the environment, given its name */
    Fish getAgent(String agent);

    boolean isAgentCloseToFood(String agent, String food);

    boolean isAgentCloseToObstacle(String agent, Obstacle obstacle);

    default Collection<Food> getNearbyFood(String agent) {
        return getAllFood().stream()
            .filter(food -> this.isAgentCloseToFood(agent, food.getId()))
            .collect(Collectors.toList());
    }

    default Collection<Obstacle> getNearbyObstacles(String agent) {
        return this.getAllObstacles().stream()
            .filter(o -> this.isAgentCloseToObstacle(agent, o))
            .collect(Collectors.toList());
    }

    /** Get the frame-per-second value to be used by any view of the system */
    long getFPS();
    void setFPS(long fps);

    void moveTowards(String agent, double x, double y, Speed speed);

    void sink(String food);

    default void sinkStep(){
        this.getAllFood().forEach(Food::sink);
    }

    Optional<Food> getFoodByPosition(double x, double y);
}
