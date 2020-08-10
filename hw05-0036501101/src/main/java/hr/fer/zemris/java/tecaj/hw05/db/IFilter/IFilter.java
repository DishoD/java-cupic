package hr.fer.zemris.java.tecaj.hw05.db.IFilter;

import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;

/**
 * Interface for filtering student records.
 * 
 * @author Hrvoje Ditrih
 *
 */
@FunctionalInterface
public interface IFilter {

	/**
	 * Test whether the student record passes the condition.
	 * 
	 * @param record
	 *            student record to test
	 * @return true if record passes the condition, false otherwise
	 */
	boolean accepts(StudentRecord record);
}
