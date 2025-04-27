package env.model;

import java.util.*;

public class AquariumModelImpl implements AquariumModel {
    private final Map<String, Fish> agents = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Food> food = Collections.synchronizedMap(new HashMap<>());
    private final Set<Fish> recentEaters = Collections.synchronizedSet(new HashSet<>());
    private List<Obstacle> obstacles;
    private int width;
    private int height;
    private int fps;
    private long foodId;

    public AquariumModelImpl() {
        this.fps = 20;
        this.obstacles = Collections.synchronizedList(new ArrayList<>());
        this.foodId = 0;
    }

    public void setAquariumDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean containsAgent(String name) {
        return agents.containsKey(name);
    }

    @Override
    public Set<Fish> getAllAgents() {
        return new HashSet<>(agents.values());
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

    @Override
    public boolean eat(String agent, String foodId) {
        synchronized(this){
            this.ensureAgentExists(agent);
            if (!this.food.containsKey(foodId)) {
                return false;
            }
            if (!this.isAgentCloseToFood(agent, foodId)) {
                return false;
            }
            Fish fish = this.agents.get(agent);
            Food food = this.food.get(foodId);
            fish.addEnergy(food.DEFAULT_ENERGY);
            this.food.remove(foodId);
            if(!this.recentEaters.add(fish)){
                this.recentEaters.remove(fish);
                this.recentEaters.add(fish);
            }
            return true;
        }
    }

    @Override
    public boolean canAgentEatFood(String agent, String foodId) {
        this.ensureAgentExists(agent);
        if (!this.food.containsKey(foodId)) {
            return false;
        }
        Fish fish = this.agents.get(agent);
        Food food = this.food.get(foodId);

        Vector2D dir = Vector2D.fromPositions(fish.getPosition(), food.getPosition());
        return dir.getLength() <= fish.getEatingRange();
    }

    @Override
    public void addFish(String agentName, Position position, double weight) {
        this.agents.put(agentName, new Fish(weight, position));
    }

    @Override
    public void addFood(Position position) {
        String id = "food-" + this.foodId++;
        this.food.put(id, new Food(id, position));
    }

    @Override
    public void addObstacle(Position position, double radius) {
        this.obstacles.add(new Obstacle(position.getX(), position.getY(), radius));
    }
}
