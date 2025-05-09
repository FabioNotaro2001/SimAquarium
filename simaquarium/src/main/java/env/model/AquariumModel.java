package env.model;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface AquariumModel {
    void setAquariumDimensions(int width, int height);
    /** Checks whether an agent with the given name exists in the environment */
    boolean containsAgent(String name);

    /** Retrieves the set of agent identifiers of all the agents currently existing into the environment */
    Set<Fish> getAllAgents();
    
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

    void removeAgent(String name);

    boolean isAgentStopped(String name);

    boolean isAgentCloseToFood(String agent, String food);

    boolean isFoodWithinObstacle(String food);
    
    default Collection<Food> getNearbyFood(String agent) {
        return getAllFood().stream()
            .filter(food -> !isFoodWithinObstacle(food.getId()))
            .filter(food -> this.isAgentCloseToFood(agent, food.getId()))
            .collect(Collectors.toList());
    }

    boolean isAgentCloseToObstacle(String agent, Obstacle obstacle);

    default Collection<Obstacle> getNearbyObstacles(String agent) {
        return this.getAllObstacles().stream()
        .filter(o -> this.isAgentCloseToObstacle(agent, o))
        .collect(Collectors.toList());
    }
    
    boolean isAgentCloseToOtherAgent(String agent1, String agent2);

    default Collection<Fish> getNearbyFish(String agent) {
        return this.getAllAgents().stream()
            .filter(f -> agent != f.getId() && this.isAgentCloseToOtherAgent(agent, f.getId()))
            .collect(Collectors.toList());
    }

    int getNumberOfFoodEaten();

    boolean canAgentEatFood(String agent, String foodId);
    
    default Optional<Food> getClosestFoodThatCanBeEaten(String agent){
        Position fishPos = this.getAgent(agent).getPosition();
        return this.getNearbyFood(agent).stream()
            .filter(f -> this.canAgentEatFood(agent, f.getId()))
            .sorted((f1, f2) -> {
                double d1 = fishPos.distanceFrom(f1.getPosition());
                double d2 = fishPos.distanceFrom(f2.getPosition());
                return Double.compare(d1, d2);
            }).findFirst();
    }

    boolean isAgentCloseToBorder(String agent, Direction dir);
    
    default Collection<Direction> getNearbyBorders(String agent) {
        return Stream.of(Direction.values())
            .filter(d -> this.isAgentCloseToBorder(agent, d))
            .collect(Collectors.toList());
    }

    void moveTowards(String agent, double x, double y, Speed speed);

    void sink(String food);

    default void sinkStep(){
        this.getAllFood().forEach(Food::sink);
    }

    Optional<Food> getFoodByPosition(double x, double y);

    boolean eat(String agent, String foodId);

    void addFish(String agentName, double weight, double energy, double maxEnergy, Position position);

    void addFood(Position position);

    void addObstacle(Position position, double radius);

    int getFoodQuantity();

    void setFoodQuantity(int amount);

    double getFairnessIndex();
}
