package env.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.Set;

public class AquariumModelImplTest {

    @Test
    public void testAddFish() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        assertTrue(model.containsAgent("Fish1"));
    }

    @Test
    public void testAddFood() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.setAquariumDimensions(100, 100);
        model.addFood(new Position(10, 20));
        assertEquals(1, model.getAllFood().size());
    }

    @Test
    public void testAddObstacle() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addObstacle(new Position(10, 20), 5);
        assertEquals(1, model.getAllObstacles().size());
    }

    @Test
    public void testCanAgentEatFood() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.addFood(new Position(12, 22));
        String foodId = model.getAllFood().iterator().next().getId();
        assertTrue(model.canAgentEatFood("Fish1", foodId));
    }

    @Test
    public void testContainsAgent() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        assertTrue(model.containsAgent("Fish1"));
        assertFalse(model.containsAgent("Fish2"));
    }

    @Test
    public void testEat() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.addFood(new Position(12, 22));
        String foodId = model.getAllFood().iterator().next().getId();
        assertTrue(model.eat("Fish1", foodId));
        assertEquals(0, model.getAllFood().size());
    }

    @Test
    public void testGetAgent() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        Fish fish = model.getAgent("Fish1");
        assertEquals("Fish1", fish.getId());
    }

    @Test
    public void testGetAllAgents() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.addFish("Fish2", 60, 120, 240, new Position(30, 40));
        Set<Fish> agents = model.getAllAgents();
        assertEquals(2, agents.size());
    }

    @Test
    public void testGetAllFood() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFood(new Position(10, 20));
        model.addFood(new Position(30, 40));
        Set<Food> food = model.getAllFood();
        assertEquals(2, food.size());
    }

    @Test
    public void testGetAllObstacles() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addObstacle(new Position(10, 20), 5);
        model.addObstacle(new Position(30, 40), 10);
        Set<Obstacle> obstacles = model.getAllObstacles();
        assertEquals(2, obstacles.size());
    }

    @Test
    public void testGetFairnessIndex() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.addFish("Fish2", 60, 120, 240, new Position(30, 40));
        assertEquals(1.0, model.getFairnessIndex(), 0.01);
    }

    @Test
    public void testGetFoodByPosition() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFood(new Position(10, 20));
        Optional<Food> food = model.getFoodByPosition(10, 20);
        assertTrue(food.isPresent());
    }

    @Test
    public void testGetFoodQuantity() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.setFoodQuantity(5);
        assertEquals(5, model.getFoodQuantity());
    }

    @Test
    public void testGetHeight() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.setAquariumDimensions(100, 200);
        assertEquals(200, model.getHeight());
    }

    @Test
    public void testGetNumberOfFoodEaten() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.addFood(new Position(12, 22));
        String foodId = model.getAllFood().iterator().next().getId();
        model.eat("Fish1", foodId);
        assertEquals(1, model.getNumberOfFoodEaten());
    }

    @Test
    public void testGetWidth() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.setAquariumDimensions(100, 200);
        assertEquals(100, model.getWidth());
    }

    @Test
    public void testIsAgentCloseToBorder() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.setAquariumDimensions(100, 100);
        model.addFish("Fish1", 50, 100, 200, new Position(5, 50));
        assertTrue(model.isAgentCloseToBorder("Fish1", Direction.LEFT));
    }

    @Test
    public void testIsAgentCloseToFood() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.addFood(new Position(12, 22));
        String foodId = model.getAllFood().iterator().next().getId();
        assertTrue(model.isAgentCloseToFood("Fish1", foodId));
    }

    @Test
    public void testIsAgentCloseToObstacle() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.addObstacle(new Position(12, 22), 5);
        Obstacle obstacle = model.getAllObstacles().iterator().next();
        assertTrue(model.isAgentCloseToObstacle("Fish1", obstacle));
    }

    @Test
    public void testIsAgentCloseToOtherAgent() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.addFish("Fish2", 60, 120, 240, new Position(12, 22));
        assertTrue(model.isAgentCloseToOtherAgent("Fish1", "Fish2"));
    }

    @Test
    public void testIsAgentStopped() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.removeAgent("Fish1");
        assertTrue(model.isAgentStopped("Fish1"));
    }

    @Test
    public void testIsFoodWithinObstacle() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFood(new Position(10, 20));
        model.addObstacle(new Position(10, 20), 5);
        String foodId = model.getAllFood().iterator().next().getId();
        assertTrue(model.isFoodWithinObstacle(foodId));
    }

    @Test
    public void testIsPositionInside() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.setAquariumDimensions(100, 100);
        assertTrue(model.isPositionInside(50, 50));
        assertFalse(model.isPositionInside(150, 150));
    }

    @Test
    public void testMoveTowards() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        Fish fish0 = model.getAgent("Fish1");
        var x = fish0.getX();
        var y = fish0.getY();
        Vector2D dir = Vector2D.of(5,5);
        var dirX = dir.normalize().getX();
        var dirY = dir.normalize().getY();
        var speed = fish0.getBaseSpeed();
        model.moveTowards("Fish1", dirX, dirY, Speed.NORMAL);
        Fish fish = model.getAgent("Fish1");
        assertEquals(x + dirX * speed * 1, fish.getX(), 0.4);
        assertEquals(y + dirY * speed * 1, fish.getY(), 0.01);
    }

    @Test
    public void testRemoveAgent() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.addFish("Fish1", 50, 100, 200, new Position(10, 20));
        model.removeAgent("Fish1");
        assertFalse(model.containsAgent("Fish1"));
    }

    @Test
    public void testSetAquariumDimensions() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.setAquariumDimensions(100, 200);
        assertEquals(100, model.getWidth());
        assertEquals(200, model.getHeight());
    }

    @Test
    public void testSetFoodQuantity() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.setFoodQuantity(10);
        assertEquals(10, model.getFoodQuantity());
    }

    @Test
    public void testSink() {
        AquariumModelImpl model = new AquariumModelImpl();
        model.setAquariumDimensions(100, 100);
        model.addFood(new Position(10, 20));
        Food food = model.getAllFood().iterator().next();
        model.sink(food.getId());
        assertTrue(food.getY() > 20);
    }
}