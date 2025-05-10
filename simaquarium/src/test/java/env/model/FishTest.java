package env.model;

import org.junit.Test;
import static org.junit.Assert.*;


public class FishTest {
    @Test
    public void testAddEnergy() {
        Fish fish = new Fish("F1", 50, 20, 100, new Position(0, 0));
        fish.addEnergy(30);
        assertEquals(50, fish.getEnergy(), 0.01);
        fish.addEnergy(60);
        assertEquals(100, fish.getEnergy(), 0.01); // Should not exceed maxEnergy
    }

    @Test
    public void testDecreaseEnergy() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        fish.decreaseEnergy(20);
        assertEquals(30, fish.getEnergy(), 0.01);
        fish.decreaseEnergy(40);
        assertEquals(0, fish.getEnergy(), 0.01); // Should not go below 0
    }

    @Test
    public void testGetBaseSpeed() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(6, fish.getBaseSpeed(), 0.01); // 300 / weight
    }

    @Test
    public void testGetDirX() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(1, fish.getDirX(), 0.01);
    }

    @Test
    public void testGetDirY() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(0, fish.getDirY(), 0.01);
    }

    @Test
    public void testGetDirection() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        Vector2D direction = fish.getDirection();
        assertEquals(1, direction.getX(), 0.01);
        assertEquals(0, direction.getY(), 0.01);
    }

    @Test
    public void testGetEatingRange() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(Math.min(10, fish.getRange()/15), fish.getEatingRange(), 0.01); // MIN_RANGE
    }

    @Test
    public void testGetEnergy() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(50, fish.getEnergy(), 0.01);
    }

    @Test
    public void testGetId() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals("F1", fish.getId());
    }

    @Test
    public void testGetMaxEnergy() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(100, fish.getMaxEnergy(), 0.01);
    }

    @Test
    public void testGetNumberOfFoodEaten() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(0, fish.getNumberOfFoodEaten());
    }

    @Test
    public void testGetObstacleRange() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(10, fish.getObstacleRange(), 0.01); // MIN_RANGE
    }

    @Test
    public void testGetPosition() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(5, 10));
        Position position = fish.getPosition();
        assertEquals(5, position.getX(), 0.01);
        assertEquals(10, position.getY(), 0.01);
    }

    @Test
    public void testGetRange() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(110, fish.getRange(), 0.01); // MIN_RANGE + weight * 2
    }

    @Test
    public void testGetSize() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(12.5, fish.getSize(), 0.01); // weight / 4
    }

    @Test
    public void testGetWeight() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        assertEquals(50, fish.getWeight(), 0.01);
    }

    @Test
    public void testGetX() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(5, 10));
        assertEquals(5, fish.getX(), 0.01);
    }

    @Test
    public void testGetY() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(5, 10));
        assertEquals(10, fish.getY(), 0.01);
    }

    @Test
    public void testIncrementFoodEaten() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        fish.incrementFoodEaten();
        assertEquals(1, fish.getNumberOfFoodEaten());
    }

    @Test
    public void testMoveTowards() {
        Fish fish = new Fish("F1", 50, 50, 100, new Position(0, 0));
        fish.moveTowards(new Position(10, 0),  Speed.NORMAL);
        assertEquals(6, fish.getX(), 0.01); // Base speed * direction
        assertEquals(0, fish.getY(), 0.01);
    }

    @Test
    public void testSpeedFromWeight() {
        assertEquals(6, Fish.speedFromWeight(50), 0.01); // 300 / weight
    }
}
