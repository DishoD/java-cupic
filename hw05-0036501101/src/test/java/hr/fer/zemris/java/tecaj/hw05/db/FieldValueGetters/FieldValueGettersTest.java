package hr.fer.zemris.java.tecaj.hw05.db.FieldValueGetters;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw05.db.IFieldValueGetter.IFieldValueGetter;
import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;

@SuppressWarnings("javadoc")
public class FieldValueGettersTest {
	private static StudentRecord r1 = StudentRecord.fromString("0000000007	Čima	Sanjin	4");
	private static StudentRecord r2 = StudentRecord.fromString("0000000051	Skočir	Andro	4");
	private static StudentRecord r3 = StudentRecord.fromString("0000000043	Perica	Krešimir	4");

	@Test
	public void jmbag() {
		IFieldValueGetter f = FieldValueGetters.JMBAG;
		
		assertEquals("0000000007", f.get(r1));
		assertEquals("0000000051", f.get(r2));
		assertEquals("0000000043", f.get(r3));
	}
	
	@Test
	public void lastName() {
		IFieldValueGetter f = FieldValueGetters.LAST_NAME;
		
		assertEquals("Čima", f.get(r1));
		assertEquals("Skočir", f.get(r2));
		assertEquals("Perica", f.get(r3));
	}
	
	@Test
	public void firstName() {
		IFieldValueGetter f = FieldValueGetters.FIRST_NAME;
		
		assertEquals("Sanjin", f.get(r1));
		assertEquals("Andro", f.get(r2));
		assertEquals("Krešimir", f.get(r3));
	}

}
