package env.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class FoodTest {

    @Test
    public void testGetId() {
        Food food = new Food("F1", new Position(10, 20), 100);
        assertEquals("F1", food.getId());
    }

    @Test
    public void testGetPosition() {
        Food food = new Food("F1", new Position(10, 20), 100);
        Position position = food.getPosition();
        assertEquals(10, position.getX(), 0.01);
        assertEquals(20, position.getY(), 0.01);
    }

    @Test
    public void testGetX() {
        Food food = new Food("F1", new Position(10, 20), 100);
        assertEquals(10, food.getX(), 0.01);
    }

    @Test
    public void testGetY() {
        Food food = new Food("F1", new Position(10, 20), 100);
        assertEquals(20, food.getY(), 0.01);
    }

    @Test
    public void testSink() {
        Food food = new Food("F1", new Position(10, 20), 100);
        double initialY = food.getY();
        food.sink();
        assertTrue(food.getY() > initialY);
        assertTrue(food.getY() <= 100);
    }
}