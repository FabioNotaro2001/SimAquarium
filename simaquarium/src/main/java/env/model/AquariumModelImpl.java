package env.model;

import java.util.*;

public class AquariumModelImpl implements AquariumModel {
    private final Map<String, Fish> agents = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Food> food = Collections.synchronizedMap(new HashMap<>());
    private List<Obstacle> obstacles;
    private final int width;
    private final int height;
    private int fps;

    public AquariumModelImpl(int width, int height) {
        this.width = width;
        this.height = height;
        this.fps = 20;
        this.obstacles = Collections.synchronizedList(new ArrayList<>());
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
    public long getFPS() {
        return this.fps;
    }

    @Override
    public void setFPS(long fps) {
        this.fps = Math.max(Math.min(60, this.fps), 1);
    }

    @Override
    public Set<Food> getAllFood() {
        return new HashSet<>(this.food.values());
    }

    @Override
    public Fish getAgent(String agent) {
        this.ensureAgentExists(agent);
        return this.agents.get(agent);
    }

    @Override
    public boolean isAgentCloseToFood(String agent, String food) {
        this.ensureAgentExists(agent);
        Position foodPos = this.food.get(food).getPosition();
        Fish fish = this.agents.get(agent);
        Vector2D dir = Vector2D.fromPositions(fish.getPosition(), foodPos);
        return dir.getLength() <= fish.getRange();
    }

    @Override
    public void moveTowards(String agent, double x, double y, Speed speed) {
        synchronized(this){
            this.ensureAgentExists(agent);
            Fish fish = this.agents.get(agent);
            Position pos = fish.getPosition();
            pos.addX(x);
            pos.addY(y);
            fish.moveTowards(pos, speed);
        }
            
    }

    @Override
    public void sink(String food) {
        synchronized(this){
            if(!this.food.containsKey(food)){
                throw new IllegalArgumentException("No such food: " + food);
            }
            this.food.get(food).sink();
        }
    }

    @Override
    public Optional<Food> getFoodByPosition(double x, double y) {
        synchronized(this){
            return this.food.values().stream()
                                        .filter(f -> f.getPosition().getX() == x && f.getPosition().getY() == y)
                                        .findFirst();
        }
    }

    @Override
    public Set<Obstacle> getAllObstacles() {
        return new HashSet<>(this.obstacles);
    }

    @Override
    public boolean isAgentCloseToObstacle(String agent, Obstacle obstacle) {
        this.ensureAgentExists(agent);
        Position obstaclePos = new Position(obstacle.getX(), obstacle.getY());
        Fish fish = this.agents.get(agent);
        Vector2D dir = Vector2D.fromPositions(fish.getPosition(), obstaclePos);
        return dir.getLength() <= fish.getRange() + obstacle.getRadius();
    }
}
