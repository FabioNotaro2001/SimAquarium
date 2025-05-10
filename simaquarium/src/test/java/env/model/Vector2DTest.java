package env.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class Vector2DTest {

    @Test
    public void testAngle() {
        Vector2D vector = Vector2D.of(1, 0);
        assertEquals(0, vector.angle(), 0.01);

        vector = Vector2D.of(0, 1);
        assertEquals(Math.PI / 2, vector.angle(), 0.01);
    }

    @Test
    public void testAngleBetween() {
        Vector2D v1 = Vector2D.of(1, 0);
        Vector2D v2 = Vector2D.of(0, 1);
        assertEquals(Math.PI / 2, Vector2D.angleBetween(v1, v2), 0.01);
    }

    @Test
    public void testEquals() {
        Vector2D v1 = Vector2D.of(1, 1);
        Vector2D v2 = Vector2D.of(1, 1);
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testFlipHorizontally() {
        Vector2D vector = Vector2D.of(1, 1);
        Vector2D flipped = vector.flipHorizontally();
        assertEquals(1, flipped.getX(), 0.01);
        assertEquals(-1, flipped.getY(), 0.01);
    }

    @Test
    public void testFlipVertically() {
        Vector2D vector = Vector2D.of(1, 1);
        Vector2D flipped = vector.flipVertically();
        assertEquals(-1, flipped.getX(), 0.01);
        assertEquals(1, flipped.getY(), 0.01);
    }

    @Test
    public void testFromPositions() {
        Position p1 = new Position(1, 1);
        Position p2 = new Position(3, 4);
        Vector2D vector = Vector2D.fromPositions(p2, p1);
        assertEquals(-2, vector.getX(), 0.01);
        assertEquals(-3, vector.getY(), 0.01);
    }

    @Test
    public void testGetLength() {
        Vector2D vector = Vector2D.of(3, 4);
        assertEquals(5, vector.getLength(), 0.01);
    }

    @Test
    public void testGetX() {
        Vector2D vector = Vector2D.of(3, 4);
        assertEquals(3, vector.getX(), 0.01);
    }

    @Test
    public void testGetY() {
        Vector2D vector = Vector2D.of(3, 4);
        assertEquals(4, vector.getY(), 0.01);
    }

    @Test
    public void testHashCode() {
        Vector2D v1 = Vector2D.of(1, 1);
        Vector2D v2 = Vector2D.of(1, 1);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testMinus() {
        Vector2D v1 = Vector2D.of(3, 4);
        Vector2D v2 = Vector2D.of(1, 2);
        Vector2D result = v1.minus(v2);
        assertEquals(2, result.getX(), 0.01);
        assertEquals(2, result.getY(), 0.01);
    }

    @Test
    public void testMinus2() {
        Vector2D vector = Vector2D.of(3, 4);
        Vector2D result = vector.minus(1, 2);
        assertEquals(2, result.getX(), 0.01);
        assertEquals(2, result.getY(), 0.01);
    }

    @Test
    public void testNormalize() {
        Vector2D vector = Vector2D.of(3, 4);
        Vector2D normalized = vector.normalize();
        assertEquals(1, normalized.getLength(), 0.01);
    }

    @Test
    public void testOf() {
        Vector2D vector = Vector2D.of(3, 4);
        assertEquals(3, vector.getX(), 0.01);
        assertEquals(4, vector.getY(), 0.01);
    }

    @Test
    public void testOf2() {
        Vector2D vector = Vector2D.of(Math.PI / 2);
        assertEquals(0, vector.getX(), 0.01);
        assertEquals(1, vector.getY(), 0.01);
    }

    @Test
    public void testPlus() {
        Vector2D v1 = Vector2D.of(1, 2);
        Vector2D v2 = Vector2D.of(3, 4);
        Vector2D result = v1.plus(v2);
        assertEquals(4, result.getX(), 0.01);
        assertEquals(6, result.getY(), 0.01);
    }

    @Test
    public void testPlus2() {
        Vector2D vector = Vector2D.of(1, 2);
        Vector2D result = vector.plus(3, 4);
        assertEquals(4, result.getX(), 0.01);
        assertEquals(6, result.getY(), 0.01);
    }

    @Test
    public void testRotateBy() {
        Vector2D vector = Vector2D.of(1, 0);
        Vector2D rotated = vector.rotateBy(Math.PI / 2);
        assertEquals(0, rotated.getX(), 0.01);
        assertEquals(1, rotated.getY(), 0.01);
    }

    @Test
    public void testTimes() {
        Vector2D vector = Vector2D.of(2, 3);
        Vector2D result = vector.times(2);
        assertEquals(4, result.getX(), 0.01);
        assertEquals(6, result.getY(), 0.01);
    }

    @Test
    public void testTimes2() {
        Vector2D v1 = Vector2D.of(1, 2);
        Vector2D v2 = Vector2D.of(3, 4);
        double result = v1.times(v2);
        assertEquals(11, result, 0.01); // Dot product: 1*3 + 2*4
    }

    @Test
    public void testToString() {
        Vector2D vector = Vector2D.of(1, 2);
        assertEquals("(1,000000, 2,000000)", vector.toString());
    }
}