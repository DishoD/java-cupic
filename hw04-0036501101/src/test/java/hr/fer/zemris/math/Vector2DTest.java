package hr.fer.zemris.math;

import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class Vector2DTest {
	
	private static final double EPS = 1E-6;
	
	@Test
	public void constructor() {
		Vector2D v = new Vector2D(-985, 589.8);
		
		assertEquals(-985, v.getX(), EPS);
		assertEquals(589.8, v.getY(), EPS);
	}
	
	@Test
	public void equals() {
		Vector2D v1 = new Vector2D(-985, 589.8);
		Vector2D v2 = new Vector2D(-985, 589.8);
		Vector2D v3 = new Vector2D(-985.1, 589.9);
		
		assertTrue(v1.equals(v2));
		assertTrue(v2.equals(v1));
		assertFalse(v1.equals(v3));
		assertFalse(v2.equals(v3));
		assertFalse(v3.equals(v1));
		assertFalse(v3.equals(v2));
	}
	
	@Test
	public void rotate1() {
		Vector2D actual = new Vector2D(1, 1);
		Vector2D expected = new Vector2D(Math.sqrt(2), 0);
		actual.rotate(-45);
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void rotate2() {
		Vector2D actual = new Vector2D(1, 0);
		Vector2D expected = new Vector2D(0, -1);
		actual.rotate(-90);
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void rotate3() {
		Vector2D actual = new Vector2D(4, 3.8);
		Vector2D expected = new Vector2D(-4.80344489155, 2.71420654591);
		actual.rotate(107);
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void rotated1() {
		Vector2D actual = new Vector2D(1, 1).rotated(45);
		Vector2D expected = new Vector2D(0, Math.sqrt(2));
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void rotated2() {
		Vector2D actual = new Vector2D(1, 0).rotated(90);
		Vector2D expected = new Vector2D(0, 1);
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void rotated3() {
		Vector2D actual = new Vector2D(4, 3.8).rotated(107);
		Vector2D expected = new Vector2D(-4.80344489155, 2.71420654591);
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void translate() {
		Vector2D actual = new Vector2D(4, 3.8);
		Vector2D expected = new Vector2D(5, 2.8);
		actual.translate(new Vector2D(1, -1));
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void translated() {
		Vector2D actual = new Vector2D(4, 3.8).translated(new Vector2D(1, -1));
		Vector2D expected = new Vector2D(5, 2.8);
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void scale() {
		Vector2D actual = new Vector2D(1, 1);
		Vector2D expected = new Vector2D(56, 56);
		actual.scale(56);
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void scaled() {
		Vector2D actual = new Vector2D(1, 1).scaled(56);
		Vector2D expected = new Vector2D(56, 56);
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void copy() {
		Vector2D actual = new Vector2D(59, -95);
		Vector2D expected = new Vector2D(59, -95);
		
		assertTrue(actual.copy().equals(expected));
	}
}
