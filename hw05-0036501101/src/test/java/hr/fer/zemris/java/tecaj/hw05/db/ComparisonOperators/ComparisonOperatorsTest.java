package hr.fer.zemris.java.tecaj.hw05.db.ComparisonOperators;

import static org.junit.Assert.*;
import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw05.db.IComparisonOperator.IComparisonOperator;

@SuppressWarnings("javadoc")
public class ComparisonOperatorsTest {
	
	@Test
	public void less() {
		IComparisonOperator o = ComparisonOperators.LESS;
		
		assertTrue(o.satisfied("Ana", "Banana"));
		assertFalse(o.satisfied("Ana", "Ana"));
		assertFalse(o.satisfied("Banana", "Ana"));
	}
	
	@Test
	public void lessOrEquals() {
		IComparisonOperator o = ComparisonOperators.LESS_OR_EQUALS;
		
		assertTrue(o.satisfied("Ana", "Banana"));
		assertTrue(o.satisfied("Ana", "Ana"));
		assertFalse(o.satisfied("Banana", "Ana"));
	}
	
	@Test
	public void greater() {
		IComparisonOperator o = ComparisonOperators.GREATER;
		
		assertFalse(o.satisfied("Ana", "Banana"));
		assertFalse(o.satisfied("Ana", "Ana"));
		assertTrue(o.satisfied("Banana", "Ana"));
	}
	
	@Test
	public void greaterOrEquals() {
		IComparisonOperator o = ComparisonOperators.GREATER_OR_EQUALS;
		
		assertFalse(o.satisfied("Ana", "Banana"));
		assertTrue(o.satisfied("Ana", "Ana"));
		assertTrue(o.satisfied("Banana", "Ana"));
	}
	
	@Test
	public void eqals() {
		IComparisonOperator o = ComparisonOperators.EQUALS;
		
		assertFalse(o.satisfied("Ana", "ANA"));
		assertTrue(o.satisfied("Ana", "Ana"));
		assertFalse(o.satisfied("anA", "Ana"));
	}
	
	@Test
	public void notEqals() {
		IComparisonOperator o = ComparisonOperators.NOT_EQUALS;
		
		assertTrue(o.satisfied("Ana", "ANA"));
		assertFalse(o.satisfied("Ana", "Ana"));
		assertTrue(o.satisfied("anA", "Ana"));
	}
	
	@Test
	public void like() {
		IComparisonOperator o = ComparisonOperators.LIKE;
		
		assertTrue(o.satisfied("Zagreb", "Zagreb"));
		assertTrue(o.satisfied("Zagreb", "Zag*reb"));
		assertTrue(o.satisfied("Zagreb", "*Zagreb"));
		assertTrue(o.satisfied("Zagreb", "Zagreb*"));
		assertTrue(o.satisfied("Zaggafsfsafsareb", "Zag*reb"));
		assertTrue(o.satisfied("AAAA", "AA*AA"));
		assertFalse(o.satisfied("AAA", "AA*AA"));
		assertFalse(o.satisfied("Zagreb", "Aba*"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void likeException1() {
		IComparisonOperator o = ComparisonOperators.LIKE;
		
		o.satisfied("Zagreb", "***");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void likeException2() {
		IComparisonOperator o = ComparisonOperators.LIKE;
		
		o.satisfied("Zagreb", "*Zagreb*");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void likeException3() {
		IComparisonOperator o = ComparisonOperators.LIKE;
		
		o.satisfied("Zagreb", "Zag*reb*");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void likeException4() {
		IComparisonOperator o = ComparisonOperators.LIKE;
		
		o.satisfied("Zagreb", "*Zag*reb");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void likeException5() {
		IComparisonOperator o = ComparisonOperators.LIKE;
		
		o.satisfied("Zagreb", "*Zag*reb*");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void likeException6() {
		IComparisonOperator o = ComparisonOperators.LIKE;
		
		o.satisfied("Zagreb", "Zag**reb");
	}
}
