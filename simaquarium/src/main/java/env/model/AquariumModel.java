package env.model;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface AquariumModel {

    /** Checks whether an agent with the given name exists in the environment */
    boolean containsAgent(String name);

    /** Retrieves the set of agent identifiers of all the agents currently existing into the environment */
    Set<String> getAllAgents();
    
    /** Retrieves the set of positions of the food currently existing into the environment */
    Set<Food> getAllFood();

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

    /** Retrieves an agent name's given its absolute position, or nothing, if no agent is present in the provided position */
    Optional<String> getAgentByPosition(Vector2D position);

    boolean isAgentCloseToFood(String agent, String food);

    default Collection<Food> getNearbyFood(String agent) {
        Fish fishAgent = getAgent(agent);
        Position fishPosition = fishAgent.getPosition();
        
        return getAllFood().stream()
            .filter(food -> {
                Position foodPos = food.getPosition();
                Vector2D dir = Vector2D.fromPositions(fishPosition, foodPos);
                return dir.getLength() <= fishAgent.getRange();
            })
            .collect(Collectors.toList());
    }

    /** Get the frame-per-second value to be used by any view of the system */
    long getFPS();
    void setFPS(long fps);

    /** Gets the sliding probability for the current environment */
    double getSlideProbability();
    void setSlideProbability(double value);
}
