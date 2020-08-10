package hr.fer.zemris.java.tecaj.hw05.db.QueryFilter;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw05.db.QueryParser.QueryParser;
import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;

@SuppressWarnings("javadoc")
public class QueryFilterTest {
	
	private static StudentRecord r1 = StudentRecord.fromString("0000000007	Čima	Sanjin	4");
	private static StudentRecord r2 = StudentRecord.fromString("0000000051	Skočir	Andro	4");
	private static StudentRecord r3 = StudentRecord.fromString("0000000043	Perica	Krešimir	4");

	@Test
	public void test1() {
		QueryFilter f = new QueryFilter(new QueryParser("jmbag>=\"0000000007\" and lastName LIKE \"*a\"").getQuery());
		
		assertTrue(f.accepts(r1));
		assertFalse(f.accepts(r2));
		assertTrue(f.accepts(r3));
	}
	
	@Test
	public void test2() {
		QueryFilter f = new QueryFilter(new QueryParser("jmbag=\"0000000051\" ").getQuery());
		
		assertFalse(f.accepts(r1));
		assertTrue(f.accepts(r2));
		assertFalse(f.accepts(r3));
	}
	
	@Test
	public void test3() {
		QueryFilter f = new QueryFilter(new QueryParser("jmbag>\"0000000006\" and jmbag <= \"0000000051\"").getQuery());
		
		assertTrue(f.accepts(r1));
		assertTrue(f.accepts(r2));
		assertTrue(f.accepts(r3));
	}
	
	@Test
	public void test4() {
		QueryFilter f = new QueryFilter(new QueryParser(" firstNameLIKE\"M*o\" ").getQuery());
		
		assertFalse(f.accepts(r1));
		assertFalse(f.accepts(r2));
		assertFalse(f.accepts(r3));
	}
	
	@Test
	public void test5() {
		QueryFilter f = new QueryFilter(new QueryParser(" firstNameLIKE\"Sa*in\" ").getQuery());
		
		assertTrue(f.accepts(r1));
		assertFalse(f.accepts(r2));
		assertFalse(f.accepts(r3));
	}

}
