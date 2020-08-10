package hr.fer.zemris.java.tecaj.hw05.db.StudentDatabase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;



@SuppressWarnings("javadoc")
public class StudentDatabaseTest {
	private static StudentDatabase db;
	
	@Before
	public void setUpDatabase() throws IOException {
		db = new StudentDatabase(Files.readAllLines(Paths.get("src/main/resources/database.txt")));
	}
	
	@Test
	public void forJMBAG1() {
		StudentRecord actual = db.forJMBAG("0000000013");
		StudentRecord expected = StudentRecord.fromString("0000000013	Gagić	Mateja	2");
		assertTrue(areSameRecords(actual, expected));
	}
	
	@Test
	public void forJMBAG2() {
		StudentRecord actual = db.forJMBAG("0000000015");
		StudentRecord expected = StudentRecord.fromString("0000000015	Glavinić Pecotić	Kristijan	4");
		assertTrue(areSameRecords(actual, expected));
	}
	
	@Test
	public void forJMBAG3() {
		StudentRecord actual = db.forJMBAG("0000000031");
		StudentRecord expected = StudentRecord.fromString("0000000031	Krušelj Posavec	Bojan	4");
		assertTrue(areSameRecords(actual, expected));
	}
	
	@Test 
	public void filterAllTrue() {
		List<StudentRecord> expected = db.getStudentRecords();
		List<StudentRecord> actual = db.filter(r -> true);
		
		assertEquals(expected, actual);
	}
	
	@Test 
	public void filterAllFalse() {
		List<StudentRecord> expected = new ArrayList<>();
		List<StudentRecord> actual = db.filter(r -> false);
		
		assertEquals(expected, actual);
	}
	
	private static boolean areSameRecords(StudentRecord r1, StudentRecord r2) {
		if(!r1.getJmbag().equals(r2.getJmbag())) return false;
		if(!r1.getLastName().equals(r2.getLastName())) return false;
		if(!r1.getFirstName().equals(r2.getFirstName())) return false;
		if(!r1.getFinalGrade().equals(r2.getFinalGrade())) return false;
		
		return true;
	}
}
