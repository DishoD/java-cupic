package hr.fer.zemris.java.fractals;

import static org.junit.Assert.*;
import org.junit.Test;

import hr.fer.zemris.math.Vector3;

@SuppressWarnings("javadoc")
public class Vector3Test {
	private static final double THRESHOLD = 1e-6;
	@Test
	public void normTest() {
		Vector3 v1 = new Vector3(5, 0, 0);
		Vector3 v2 = new Vector3(1, 1, 0);
		
		assertEquals(5, v1.norm(), THRESHOLD);
		assertEquals(Math.sqrt(2), v2.norm(), THRESHOLD);
	}
	
	@Test
	public void addTest() {
		Vector3 v1 = new Vector3(5, 0, 0);
		Vector3 v2 = new Vector3(1, 1, 0);
		
		Vector3 actual = v1.add(v2);
		
		assertEquals(new Vector3(6, 1, 0), actual);
	}
	
	@Test
	public void subTest() {
		Vector3 v1 = new Vector3(5, 0, 0);
		Vector3 v2 = new Vector3(1, 1, 0);
		
		Vector3 actual = v1.sub(v2);
		
		assertEquals(new Vector3(4, -1, 0), actual);
	}
	
	@Test
	public void scaleTest() {
		Vector3 v1 = new Vector3(5, 6, 7);
		
		Vector3 actual = v1.scale(2);
		
		assertEquals(new Vector3(10, 12, 14), actual);
	}
	
	@Test
	public void normalizedTest() {
		Vector3 v1 = new Vector3(5, 5, 6);
		
		Vector3 actual = v1.normalized();
		
		assertEquals(new Vector3(0.539164, 0.539164, 0.646997), actual);
	}
	
	@Test
	public void crossProductTest() {
		Vector3 v1 = new Vector3(1, 0, 0);
		Vector3 v2 = new Vector3(0, 1, 0);
		Vector3 v3 = new Vector3(0, 0, 1);
		
		Vector3 actual = v1.cross(v2);
		
		assertEquals(v3, actual);
	}
	
	@Test
	public void dotProductTest() {
		Vector3 v1 = new Vector3(1, 0, 0);
		Vector3 v2 = new Vector3(0, 1, 0);
		
		double actual = v1.dot(v2);
		
		assertEquals(0, actual, THRESHOLD);
	}
	
	@Test
	public void cosAngleTest() {
		Vector3 v1 = new Vector3(1, 0, 0);
		Vector3 v2 = new Vector3(0, 1, 0);
		
		double actual = v1.cosAngle(v2);
		
		assertEquals(0, actual, THRESHOLD);
	}
}
