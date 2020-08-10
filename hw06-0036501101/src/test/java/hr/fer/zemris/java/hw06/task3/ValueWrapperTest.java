package hr.fer.zemris.java.hw06.task3;


import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ValueWrapperTest {

	@Test
	public void addNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());
		
		assertEquals(Integer.valueOf(0), v1.getValue());
		assertNull(v2.getValue());
	}
	
	@Test
	public void subNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.subtract(v2.getValue());
		
		assertEquals(Integer.valueOf(0), v1.getValue());
		assertNull(v2.getValue());
	}
	
	@Test
	public void mulNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.multiply(v2.getValue());
		
		assertEquals(Integer.valueOf(0), v1.getValue());
		assertNull(v2.getValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void divNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.divide(v2.getValue());
	}
	
	@Test
	public void compareNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());
		
		assertEquals(0, v1.numCompare(v2.getValue()));
	}
	
	@Test
	public void addDouble() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue());
		
		assertEquals(Double.valueOf(13), v3.getValue());
		assertEquals(Integer.valueOf(1), v4.getValue());
	}
	
	@Test
	public void subDouble() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.subtract(v4.getValue());
		
		assertEquals(Double.valueOf(11), v3.getValue());
		assertEquals(Integer.valueOf(1), v4.getValue());
	}
	
	@Test
	public void mulDouble() {
		ValueWrapper v3 = new ValueWrapper(12.0);
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(2));
		v3.multiply(v4.getValue());
		
		assertEquals(Double.valueOf(24), v3.getValue());
		assertEquals(Integer.valueOf(2), v4.getValue());
	}
	
	@Test
	public void divDouble() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(6));
		v3.divide(v4.getValue());
		
		assertEquals(Double.valueOf(2), v3.getValue());
		assertEquals(Integer.valueOf(6), v4.getValue());
	}
	
	@Test
	public void addInteger() {
		ValueWrapper v3 = new ValueWrapper("12");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue());
		
		assertEquals(Integer.valueOf(13), v3.getValue());
		assertEquals(Integer.valueOf(1), v4.getValue());
	}
	
	@Test
	public void subInteger() {
		ValueWrapper v3 = new ValueWrapper("12");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.subtract(v4.getValue());
		
		assertEquals(Integer.valueOf(11), v3.getValue());
		assertEquals(Integer.valueOf(1), v4.getValue());
	}
	
	@Test
	public void mulInteger() {
		ValueWrapper v3 = new ValueWrapper("12");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(-5));
		v3.multiply(v4.getValue());
		
		assertEquals(Integer.valueOf(-60), v3.getValue());
		assertEquals(Integer.valueOf(-5), v4.getValue());
	}
	
	@Test
	public void divInteger() {
		ValueWrapper v3 = new ValueWrapper("12");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(5));
		v3.divide(v4.getValue());
		
		assertEquals(Integer.valueOf(2), v3.getValue());
		assertEquals(Integer.valueOf(5), v4.getValue());
	}
	
	@Test
	public void compare1() {
		ValueWrapper v1 = new ValueWrapper(1);
		ValueWrapper v2 = new ValueWrapper("1");
		
		assertEquals(0, v1.numCompare(v2.getValue()));
	}
	
	@Test
	public void compare2() {
		ValueWrapper v1 = new ValueWrapper(-1);
		ValueWrapper v2 = new ValueWrapper("1");
		
		assertEquals(-1, v1.numCompare(v2.getValue()));
	}
	
	@Test
	public void compare3() {
		ValueWrapper v1 = new ValueWrapper("55.6");
		ValueWrapper v2 = new ValueWrapper(null);
		
		assertEquals(1, v1.numCompare(v2.getValue()));
	}
	
	@Test
	public void compare4() {
		ValueWrapper v1 = new ValueWrapper(1);
		ValueWrapper v2 = new ValueWrapper("1");
		
		assertEquals(0, v1.numCompare(v2.getValue()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void compareFail() {
		ValueWrapper v1 = new ValueWrapper("štefica");
		ValueWrapper v2 = new ValueWrapper("1");
		
		assertEquals(0, v1.numCompare(v2.getValue()));
	}

}
