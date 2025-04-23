package env;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.function.BiFunction;

public class AquariumModelImpl implements AquariumModel {

    private final Map<String, Fish> agents = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Food> food = Collections.synchronizedMap(new HashMap<>());
    private final int width;
    private final int height;

    public AquariumModelImpl(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean containsAgent(String name) {
        return agents.containsKey(name);
    }

    @Override
    public Set<String> getAllAgents() {
        return agents.keySet();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isPositionInside(double x, double y) {
        return x >= 0 && x < width && y >= 0 && y < height ;
    }

    private void ensureAgentExists(String agent) {
        if (!containsAgent(agent)) {
            throw new IllegalArgumentException("No such an agent: " + agent);
        }
    }

    @Override
    public Position getAgentPosition(String agent) {
        synchronized (agent) {
            ensureAgentExists(agent);
            return agents.get(agent).getPosition();
        }
    }

    @Override
    public Orientation getAgentDirection(String agent) {
        synchronized (agentPoses) {
            ensureAgentExists(agent);
            return agentPoses.get(agent).getOrientation();
        }
    }

    private void setAgentPosition(String agent, Vector2D position) {
        synchronized (agentPoses) {
            Pose currentPose = agentPoses.get(agent);
            agentPoses.put(agent, new Pose(position, currentPose.getOrientation()));
        }
    }

    private void setAgentDirection(String agent, Orientation orientation) {
        synchronized (agentPoses) {
            Pose currentPose = agentPoses.get(agent);
            agentPoses.put(agent, new Pose(currentPose.getPosition(), orientation));
        }
    }

    @Override
    public Optional<String> getAgentByPosition(Vector2D position) {
        synchronized (agentPoses) {
            return agentPoses.entrySet().stream()
                    .filter(it -> it.getValue().getPosition() == position)
                    .map(Map.Entry::getKey)
                    .findFirst();
        }
    }

    @Override
    public boolean setAgentPose(String agent, int x, int y, Orientation orientation) {
        synchronized (agentPoses) {
            if (containsAgent(agent)) {
                setAgentDirection(agent, orientation);
                if (isPositionInside(x, y)) {
                    Vector2D position = Vector2D.of(x, y);
                    if (!getAgentByPosition(position).isPresent()) {
                        setAgentPosition(agent, position);
                        return true;
                    }
                }
                return false;
            }
            agentPoses.put(agent, new Pose(Vector2D.of(x, y), orientation));
            return true;
        }
    }

    @Override
    public boolean areAgentsNeighbours(String agent, String neighbour) {
        if (!containsAgent(agent) || !containsAgent(neighbour)) return  false;
        Vector2D agentPosition = getAgentPosition(agent);
        Vector2D neighbourPosition = getAgentPosition(neighbour);
        return neighbourhoodFunction.apply(agentPosition, neighbourPosition);
    }

    @Override
    public long getFPS() {
        return fsp;
    }

    @Override
    public void setFPS(long fps) {
        this.fsp = Math.max(Math.min(60, fps), 1);
    }

    @Override
    public double getSlideProbability() {
        return slideProbability;
    }

    @Override
    public void setSlideProbability(double slideProbability) {
        this.slideProbability = Math.max(Math.min(1d, slideProbability), 0d);;
    }
}
