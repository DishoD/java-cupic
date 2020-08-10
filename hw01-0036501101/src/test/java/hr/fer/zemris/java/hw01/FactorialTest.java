package hr.fer.zemris.java.hw01;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests validity of a calculateFactorial(int number) method.
 * @author Hrvoje Ditrih
 *
 */
public class FactorialTest {
	
	@Test
	public void numbersInRange() {
		long expectedFactorial = 1;
		
		for(int i = 1; i <= 20; ++i) {
			expectedFactorial *= i;
			long actualFactorial = Factorial.calculateFactorial(i);
			
			Assert.assertEquals(expectedFactorial, actualFactorial);
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void numberOutOfRange1() {
		Factorial.calculateFactorial(-99);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void numberOutOfRange2() {
		Factorial.calculateFactorial(-6);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void numberOutOfRange3() {
		Factorial.calculateFactorial(-1);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void numberOutOfRange5() {
		Factorial.calculateFactorial(21);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void numberOutOfRange6() {
		Factorial.calculateFactorial(55);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void numberOutOfRange7() {
		Factorial.calculateFactorial(102);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void numberOutOfRange8() {
		Factorial.calculateFactorial(666);
	}
}
