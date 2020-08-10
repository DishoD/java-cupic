package hr.fer.zemris.java.tecaj.hw05.db.ConditionalExpression;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw05.db.ComparisonOperators.ComparisonOperators;
import hr.fer.zemris.java.tecaj.hw05.db.FieldValueGetters.FieldValueGetters;
import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;

@SuppressWarnings("javadoc")
public class ConditionalExpressionTest {
	private static StudentRecord r1 = StudentRecord.fromString("0000000007	Čima	Sanjin	4");
	private static StudentRecord r2 = StudentRecord.fromString("0000000051	Skočir	Andro	4");
	private static StudentRecord r3 = StudentRecord.fromString("0000000043	Perica	Krešimir	4");

	@Test
	public void evaluation1() {
		assertTrue(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000007", ComparisonOperators.EQUALS).evaluateExpression(r1));
	}
	
	@Test
	public void evaluation2() {
		assertTrue(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000008", ComparisonOperators.LESS).evaluateExpression(r1));
	}
	
	@Test
	public void evaluation3() {
		assertTrue(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000007", ComparisonOperators.GREATER_OR_EQUALS).evaluateExpression(r1));
	}
	
	@Test
	public void evaluation4() {
		assertTrue(new ConditionalExpression(FieldValueGetters.LAST_NAME, "Perica", ComparisonOperators.NOT_EQUALS).evaluateExpression(r2));
	}
	
	@Test
	public void evaluation5() {
		assertTrue(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "K*", ComparisonOperators.LIKE).evaluateExpression(r3));
	}
}
