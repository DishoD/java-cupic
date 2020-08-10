package hr.fer.zemris.java.tecaj.hw05.db.StudentDatabase;

import java.util.ArrayList;
import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;
import hr.fer.zemris.java.tecaj.hw05.db.IFilter.IFilter;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw05.collections.SimpleHashtable;

/**
 * Represents a simple database which holds student records. Can retrieve a
 * student record by jmbag or it can retrieve a list of student records by
 * filtering.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class StudentDatabase {
	/**
	 * student records
	 */
	private List<StudentRecord> studentRecords = new ArrayList<>();
	/**
	 * used for fast retrieval of student records by jmbag
	 */
	private SimpleHashtable<String, Integer> studentIndexFromJmbag = new SimpleHashtable<>();

	/**
	 * Initializes the database with the given student records.
	 * 
	 * @param records
	 *            list of student records in string form
	 */
	public StudentDatabase(List<String> records) {
		for (String record : records) {
			record = record.trim();
			if (record.isEmpty())
				continue;

			StudentRecord newRecord = StudentRecord.fromString(record);

			studentRecords.add(newRecord);
			studentIndexFromJmbag.put(newRecord.getJmbag(), studentRecords.size() - 1);
		}
	}

	/**
	 * Gets a student record of the student with the given jmbag.
	 * 
	 * @param jmbag
	 *            students jmbag
	 * @return student record or null if student record with the given jmbag does
	 *         not exist
	 */
	public StudentRecord forJMBAG(String jmbag) {
		Objects.requireNonNull(jmbag, "Jmbag must not be null");

		Integer index = (Integer) studentIndexFromJmbag.get(jmbag);

		return index == null ? null : studentRecords.get(index);
	}

	/**
	 * Returns a list of student records which satisfy the given filter condition.
	 * 
	 * @param filter
	 *            condition for filtering
	 * @return list of students (can be empty)
	 */
	public List<StudentRecord> filter(IFilter filter) {
		Objects.requireNonNull(filter, "Filter must not be null");

		List<StudentRecord> filteredRecords = new ArrayList<>();

		for (StudentRecord studentRecord : studentRecords) {
			if (filter.accepts(studentRecord)) {
				filteredRecords.add(studentRecord);
			}
		}

		return filteredRecords;
	}

	/**
	 * @return list of all student records
	 */
	public List<StudentRecord> getStudentRecords() {
		return studentRecords;
	}

}
