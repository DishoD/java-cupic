package hr.fer.zemris.java.tecaj.hw05.db.IFieldValueGetter;

import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;

/**
 * Provides an interface for extracting a field(attribute) value from a student
 * record.
 * 
 * @author Hrvoje Ditrih
 *
 */
public interface IFieldValueGetter {
	/**
	 * Gets a field value from student record.
	 * 
	 * @param record
	 *            student record from which the field is extracted
	 * @return field(attribute) value
	 */
	String get(StudentRecord record);
}
