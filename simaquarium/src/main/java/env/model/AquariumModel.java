package env.model;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The `AquariumModel` interface defines the behavior and properties of the aquarium simulation environment.
 * It provides methods to manage agents, food, obstacles, and interactions within the environment.
 */
public interface AquariumModel {
    
    /** Sets the dimensions of the aquarium environment. */
    void setAquariumDimensions(int width, int height);

    /** 
     * Checks whether an agent with the given name exists in the environment.
     * @param name The name of the agent.
     * @return True if the agent exists, false otherwise.
     */
    boolean containsAgent(String name);

    /** 
     * Retrieves the set of all agents currently in the environment.
     * @return A set of `Fish` objects representing the agents.
     */    
    Set<Fish> getAllAgents();
    
    /** 
     * Retrieves the set of all food positions currently in the environment.
     * @return A set of `Food` objects representing the food.
     */
    Set<Food> getAllFood();

    /** 
     * Retrieves the set of all obstacles currently in the environment.
     * @return A set of `Obstacle` objects representing the obstacles.
     */
    Set<Obstacle> getAllObstacles();

    /** 
     * Retrieves the width of the environment.
     * @return The width of the aquarium.
     */
    int getWidth();

    /** 
     * Retrieves the height of the environment.
     * @return The height of the aquarium.
     */
    int getHeight();

    /** 
     * Checks whether a given position is inside the environment, using x and y coordinates.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @return True if the position is inside, false otherwise.
     */
    boolean isPositionInside(double x, double y);

    /** 
     * Checks whether a given position is inside the environment, using a `Vector2D` object.
     * @param position The position as a `Vector2D` object.
     * @return True if the position is inside, false otherwise.
     */
    default boolean isPositionInside(Vector2D position) {
        return isPositionInside(position.getX(), position.getY());
    }

    /** 
     * Checks whether a given position is outside the environment, using x and y coordinates.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @return True if the position is outside, false otherwise.
     */
    default boolean isPositionOutside(double x, double y) {
        return !isPositionInside(x, y);
    }

    /** 
     * Checks whether a given position is outside the environment, using a `Vector2D` object.
     * @param position The position as a `Vector2D` object.
     * @return True if the position is outside, false otherwise.
     */
    default boolean isPositionOutside(Vector2D position) {
        return isPositionOutside(position.getX(), position.getY());
    }

    /** 
     * Retrieves an agent's absolute position in the environment, given its name.
     * @param agent The name of the agent.
     * @return The `Fish` object representing the agent.
     */
    Fish getAgent(String agent);

    /** 
     * Removes an agent from the environment by its name.
     * @param name The name of the agent to remove.
     */
    void removeAgent(String name);

    /** 
     * Checks whether an agent is close to a specific food item.
     * @param agent The name of the agent.
     * @param food The ID of the food item.
     * @return True if the agent is close to the food, false otherwise.
     */
    boolean isAgentCloseToFood(String agent, String food);

    /** 
     * Checks whether a food item is within an obstacle.
     * @param food The ID of the food item.
     * @return True if the food is within an obstacle, false otherwise.
     */
    boolean isFoodWithinObstacle(String food);
    
    /** 
     * Retrieves a collection of food items that are nearby a specific agent.
     * @param agent The name of the agent.
     * @return A collection of `Food` objects representing nearby food.
     */
    default Collection<Food> getNearbyFood(String agent) {
        return getAllFood().stream()
            .filter(food -> !isFoodWithinObstacle(food.getId()))
            .filter(food -> this.isAgentCloseToFood(agent, food.getId()))
            .collect(Collectors.toList());
    }

     /** 
     * Checks whether an agent is close to a specific obstacle.
     * @param agent The name of the agent.
     * @param obstacle The obstacle to check.
     * @return True if the agent is close to the obstacle, false otherwise.
     */
    boolean isAgentCloseToObstacle(String agent, Obstacle obstacle);

    /** 
     * Retrieves a collection of obstacles that are nearby a specific agent.
     * @param agent The name of the agent.
     * @return A collection of `Obstacle` objects representing nearby obstacles.
     */
    default Collection<Obstacle> getNearbyObstacles(String agent) {
        return this.getAllObstacles().stream()
        .filter(o -> this.isAgentCloseToObstacle(agent, o))
        .collect(Collectors.toList());
    }
    
    /** 
     * Checks whether two agents are close to each other.
     * @param agent1 The name of the first agent.
     * @param agent2 The name of the second agent.
     * @return True if the agents are close to each other, false otherwise.
     */
    boolean isAgentCloseToOtherAgent(String agent1, String agent2);

    /** 
     * Retrieves a collection of agents that are nearby a specific agent.
     * @param agent The name of the agent.
     * @return A collection of `Fish` objects representing nearby agents.
     */
    default Collection<Fish> getNearbyFish(String agent) {
        return this.getAllAgents().stream()
            .filter(f -> agent != f.getId() && this.isAgentCloseToOtherAgent(agent, f.getId()))
            .collect(Collectors.toList());
    }

    /** 
     * Retrieves the total number of food items eaten in the environment.
     * @return The total number of food items eaten.
     */
    int getNumberOfFoodEaten();

    /** 
     * Checks whether an agent can eat a specific food item.
     * @param agent The name of the agent.
     * @param foodId The ID of the food item.
     * @return True if the agent can eat the food, false otherwise.
     */
    boolean canAgentEatFood(String agent, String foodId);
    
    /** 
     * Retrieves the closest food item that can be eaten by a specific agent.
     * @param agent The name of the agent.
     * @return An `Optional` containing the closest food item, or empty if none is found.
     */
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

    /** 
     * Checks whether an agent is close to a specific border of the environment.
     * @param agent The name of the agent.
     * @param dir The direction of the border.
     * @return True if the agent is close to the border, false otherwise.
     */
    boolean isAgentCloseToBorder(String agent, Direction dir);
    
    /** 
     * Retrieves a collection of borders that are nearby a specific agent.
     * @param agent The name of the agent.
     * @return A collection of `Direction` objects representing nearby borders.
     */
    default Collection<Direction> getNearbyBorders(String agent) {
        return Stream.of(Direction.values())
            .filter(d -> this.isAgentCloseToBorder(agent, d))
            .collect(Collectors.toList());
    }

    /** 
     * Moves an agent towards a specific position at a given speed.
     * @param agent The name of the agent.
     * @param x The x-coordinate of the target position.
     * @param y The y-coordinate of the target position.
     * @param speed The speed at which the agent moves.
     */
    void moveTowards(String agent, double x, double y, Speed speed);

    /** 
     * Makes a food item sink in the environment.
     * @param food The ID of the food item.
     */
    void sink(String food);

    /** 
     * Makes all food items sink one step in the environment.
     */
    default void sinkStep(){
        this.getAllFood().forEach(Food::sink);
    }

    /** 
     * Retrieves a food item by its position in the environment.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @return An `Optional` containing the food item, or empty if none is found.
     */
    Optional<Food> getFoodByPosition(double x, double y);

    /** 
     * Makes an agent eat a specific food item.
     * @param agent The name of the agent.
     * @param foodId The ID of the food item.
     * @return True if the agent successfully eats the food, false otherwise.
     */
    boolean eat(String agent, String foodId);

    /** 
     * Adds a new fish agent to the environment.
     * @param agentName The name of the agent.
     * @param weight The weight of the fish.
     * @param energy The initial energy of the fish.
     * @param maxEnergy The maximum energy of the fish.
     * @param position The initial position of the fish.
     */
    void addFish(String agentName, double weight, double energy, double maxEnergy, Position position);

    /** 
     * Adds a new food item to the environment at a specific position.
     * @param position The position of the food item.
     */
    void addFood(Position position);

    /** 
     * Adds a new obstacle to the environment at a specific position with a given radius.
     * @param position The position of the obstacle.
     * @param radius The radius of the obstacle.
     */
    void addObstacle(Position position, double radius);

    /** 
     * Retrieves the current quantity of food in the environment.
     * @return The quantity of food.
     */
    int getFoodQuantity();

    /** 
     * Sets the quantity of food in the environment.
     * @param amount The new quantity of food.
     */
    void setFoodQuantity(int amount);

    /** 
     * Calculates the fairness index of the environment based on food distribution.
     * @return The fairness index.
     */
    double getFairnessIndex();

    /** 
     * Verifies the sequence of events in the environment.
     * @return A string indicating the result of the verification.
     */
    String verifyEvents();

    /** 
     * Adds an event to the list of events in the environment.
     * @param event The event to add, represented as a pair of agent ID and event type.
     */
    void addEventToList(Pair<String, String> event);
}
