package hr.fer.zemris.java.tecaj.hw05.db.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.tecaj.hw05.db.QueryFilter.QueryFilter;
import hr.fer.zemris.java.tecaj.hw05.db.QueryParser.ParserException;
import hr.fer.zemris.java.tecaj.hw05.db.QueryParser.QueryParser;
import hr.fer.zemris.java.tecaj.hw05.db.StudentDatabase.StudentDatabase;
import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;

/**
 * This program is a demonstration of querying the student database with simple
 * query commands.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class QueryDemo {
	private static StudentDatabase database;
	
	/**
	 * Main method. Controls the flow of the program.
	 * 
	 * @param args ignorable
	 */
	public static void main(String[] args) {
		System.out.println("Hello! This is a simple student database querying program.\n"
				+ "If you wish to exit the program type 'exit'.\n"
				+ "If you wish to query the database enter 'query' following with the query statement.\n"
				+ "Statements can be connected with the logical AND operator with 'AND' keyword.\n"
				+ "Statements consists of three parts: [field][operator][string literal], exactly in that order.\n"
				+ "A [filed] can be: jmbag, lastName or firstName (case sensitive).\n"
				+ "A [operator] can be: <, <=, >, >=, =, != or LIKE (case senstive). Operators check relations between fields and string liteals.\n"
				+ "Operator LIKE can use one wildcard '*'. Eg.: query firstName LIKE \"B*\" returns all the students which names starts with letter 'B'.\n"
				+ "String literal is everything enclosed in double quotation marks \"[string literal]\".\n"
				+ "Have fun!\n\n");
		
		 try {
			database = new StudentDatabase(Files.readAllLines(Paths.get("src/main/resources/database.txt")));
		} catch (IOException e) {
			System.out.println("Couldn't load the database :(\nExiting the program.");
			return;
		}
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.print("> ");
			String line = sc.nextLine().trim();
			
			if(line.toLowerCase().equals("exit")) {
				System.out.println("Goodby!");
				sc.close();
				break;
			}
			
			if(line.toLowerCase().startsWith("query")) {
				processQuery(line.substring(5));
			} else {
				System.out.println("Illegal command. I can only do 'query' commands!\nIf you wish to exit type 'exit'.\n");
			}
		}
	}

	/**
	 * Processes the given query string.
	 * 
	 * @param queryString string representing the query
	 */
	private static void processQuery(String queryString) {
		QueryParser query;
		
		try {
			query = new QueryParser(queryString);
		} catch(ParserException ex) {
			System.out.println(ex.getMessage() + "\n");
			return;
		}
		
		List<StudentRecord> result = new ArrayList<>();
		
		if(query.isQueryDirect()) {
			result.add(database.forJMBAG(query.getQueriedJmbag()));
			System.out.println("Using index for record retrieval.");
		} else {
			result.addAll(database.filter(new QueryFilter(query.getQuery())));
		}
		
		printStudentRecords(result);
	}

	/**
	 * Prints a list of student records in a table from to the standard output.
	 * 
	 * @param records list of student records
	 */
	private static void printStudentRecords(List<StudentRecord> records) {
		if(records.isEmpty()) {
			System.out.println("Records selected: 0\n");
			return;
		}
		TableWidths table = getTableWidths(records);
		
		System.out.println(generateSeparatorLine(table));
		
		for(StudentRecord record : records) {
			System.out.println(getStudentRecordAsString(record, table));
		}
		
		System.out.println(generateSeparatorLine(table));
		
		System.out.println("Records selected: " + records.size() + "\n");
	}
	
	/**
	 * Returns a string representation of one row of a table containing students information.
	 * 
	 * @param record which student record information to take
	 * @param table column widths
	 * @return on row of a table in string form
	 */
	private static String getStudentRecordAsString(StudentRecord record, TableWidths table) {
		return "| " + fillStringWithSpaces(record.getJmbag(), table.jmbagWidth) +
				"| " + fillStringWithSpaces(record.getLastName(), table.lastNameWidth) +
				"| " + fillStringWithSpaces(record.getFirstName(), table.firstNameWidth) +
				"| " + fillStringWithSpaces(record.getFinalGrade(), table.finalGradeWidth) +
				"|";
	}
	
	/**
	 * It expands the given string with spaces if necessary to have
	 * exactly the given length.
	 * 
	 * @param string string to expand
	 * @param n final string length (won't shorten the string if n < string.length())
	 * @return expanded string
	 */
	private static String fillStringWithSpaces(String string, int n) {
		if(n <= string.length()) return string;
		
		n = n - string.length();
		StringBuilder sb = new StringBuilder(string);
		
		for(int i = 0; i < n; ++i) {
			sb.append(' ');
		}
		
		return sb.toString();
	}

	/**
	 * Calculates appropriate table column widths for the given list of student records.
	 * 
	 * @param records list of student records
	 * @return appropriate table column widths
	 */
	private static TableWidths getTableWidths(List<StudentRecord> records) {
		TableWidths t = new TableWidths();
		
		for(StudentRecord record : records) {
			t.jmbagWidth = Math.max(t.jmbagWidth, record.getJmbag().length());
			t.lastNameWidth = Math.max(t.lastNameWidth, record.getLastName().length());
			t.firstNameWidth = Math.max(t.firstNameWidth, record.getFirstName().length());
			t.finalGradeWidth = Math.max(t.finalGradeWidth, record.getFinalGrade().length());
		}
		
		t.jmbagWidth++;
		t.lastNameWidth++;
		t.firstNameWidth++;
		t.finalGradeWidth++;
		
		return t;
	}
	
	/**
	 * Generates table top and bottom line with the given
	 * column widths.
	 * 
	 * @param table information of table column widths
	 * @return string representation of a table top or bottom line
	 */
	private static String generateSeparatorLine(TableWidths table) {
		return "+=" + generateEqualsString(table.jmbagWidth) +
				"+=" + generateEqualsString(table.lastNameWidth) +
				"+=" + generateEqualsString(table.firstNameWidth) +
				"+=" + generateEqualsString(table.finalGradeWidth) +
				"+";
	}
	
	/**
	 * Generates a string consisting only of '=' signs
	 * of the given length.
	 * 
	 * @param n length of string
	 * @return string consisting only of '=' signs of the given length
	 */
	private static String generateEqualsString(int n) {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < n; ++i) {
			sb.append('=');
		}
		
		return sb.toString();
	}

	/**
	 * Holds the information about table column widths.
	 * 
	 * @author Hrvoje Ditrih
	 *
	 */
	private static class TableWidths{
		private int jmbagWidth;
		private int lastNameWidth;
		private int firstNameWidth;
		private int finalGradeWidth;
	}
	
}
