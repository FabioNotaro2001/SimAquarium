package env.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class PositionTest {

    @Test
    public void testAddX() {
        Position position = new Position(10, 20);
        position.addX(5);
        assertEquals(15, position.getX(), 0.01);
    }

    @Test
    public void testAddY() {
        Position position = new Position(10, 20);
        position.addY(5);
        assertEquals(25, position.getY(), 0.01);
    }

    @Test
    public void testDistanceFrom() {
        Position position1 = new Position(0, 0);
        Position position2 = new Position(3, 4);
        assertEquals(5, position1.distanceFrom(position2), 0.01); // Distance should be 5 (3-4-5 triangle)
    }

    @Test
    public void testGetX() {
        Position position = new Position(10, 20);
        assertEquals(10, position.getX(), 0.01);
    }

    @Test
    public void testGetY() {
        Position position = new Position(10, 20);
        assertEquals(20, position.getY(), 0.01);
    }

    @Test
    public void testOf() {
        Position position = Position.of(10, 20);
        assertEquals(10, position.getX(), 0.01);
        assertEquals(20, position.getY(), 0.01);
    }

    @Test
    public void testSetX() {
        Position position = new Position(10, 20);
        position.setX(15);
        assertEquals(15, position.getX(), 0.01);
    }

    @Test
    public void testSetY() {
        Position position = new Position(10, 20);
        position.setY(25);
        assertEquals(25, position.getY(), 0.01);
    }

    @Test
    public void testZero() {
        Position position = Position.zero();
        assertEquals(0, position.getX(), 0.01);
        assertEquals(0, position.getY(), 0.01);
    }
}