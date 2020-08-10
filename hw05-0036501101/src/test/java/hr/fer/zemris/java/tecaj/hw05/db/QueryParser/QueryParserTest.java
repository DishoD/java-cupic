package hr.fer.zemris.java.tecaj.hw05.db.QueryParser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw05.db.ComparisonOperators.ComparisonOperators;
import hr.fer.zemris.java.tecaj.hw05.db.ConditionalExpression.ConditionalExpression;
import hr.fer.zemris.java.tecaj.hw05.db.FieldValueGetters.FieldValueGetters;

@SuppressWarnings("javadoc")
public class QueryParserTest {

	@Test(expected=ParserException.class)
	public void illegal1() {
		new QueryParser("");
	}
	
	@Test(expected=ParserException.class)
	public void illegal2() {
		new QueryParser("lastName");
	}
	
	@Test(expected=ParserException.class)
	public void illegal3() {
		new QueryParser("jmbag=\"");
	}
	
	@Test(expected=ParserException.class)
	public void illegal4() {
		new QueryParser("AND lastName!=\"Ankica\"");
	}
	
	@Test(expected=ParserException.class)
	public void illegal5() {
		new QueryParser("lastNamE=\"Šrefoca\"");
	}
	
	@Test
	public void drirect() {
		QueryParser p = new QueryParser("jmbag=\"123456789\"");
		
		assertTrue(p.isQueryDirect());
		assertEquals("123456789", p.getQueriedJmbag());
	}
	
	@Test
	public void indirect() {
		QueryParser p = new QueryParser("jmbag!=\"123456789\" And lastName>\"Marko\"  ");
		
		assertFalse(p.isQueryDirect());
		
		List<ConditionalExpression> actual = p.getQuery();
		
		assertEquals(2, actual.size());
		
		assertEquals(new ConditionalExpression(FieldValueGetters.JMBAG, "123456789", ComparisonOperators.NOT_EQUALS), actual.get(0));
		assertEquals(new ConditionalExpression(FieldValueGetters.LAST_NAME, "Marko", ComparisonOperators.GREATER), actual.get(1));
		
	}

}
