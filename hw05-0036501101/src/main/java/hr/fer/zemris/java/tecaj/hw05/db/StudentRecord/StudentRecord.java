package hr.fer.zemris.java.tecaj.hw05.db.StudentRecord;

import java.util.Objects;

/**
 * Represents an student database record. It contains attributes: jmbag,
 * lastName, firstName, finalGrade.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class StudentRecord {
	/**
	 * students jmbag
	 */
	private String jmbag;
	/**
	 * students last name
	 */
	private String lastName;
	/**
	 * students first name
	 */
	private String firstName;
	/**
	 * students final grade
	 */
	private String finalGrade;

	/**
	 * Initializes the student record with the given parameters.
	 * 
	 * @param jmbag
	 *            students jmbag
	 * @param lastName
	 *            students last name
	 * @param firstName
	 *            students first name
	 * @param finalGrade
	 *            students final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, String finalGrade) {
		Objects.requireNonNull(jmbag, "jmbag must not be null!");
		Objects.requireNonNull(lastName, "lastName must not be null!");
		Objects.requireNonNull(firstName, "firstName must not be null!");
		Objects.requireNonNull(finalGrade, "finalGrade must not be null!");

		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	/**
	 * Constructs a student record from a string. String must contain jmbag, last
	 * name, fist name, final Grade in that order separated by at least one
	 * whitespace.
	 * 
	 * @param record
	 *            string representation of a student record
	 * @return new student record
	 */
	public static StudentRecord fromString(String record) {
		Objects.requireNonNull(record, "Record must not be null");

		String[] attributes = record.split("\\s+");

		if (attributes.length < 4)
			throw new IllegalArgumentException("Illegal record: " + record);

		String jmbag = attributes[0];
		String lastName = "";
		for(int i = 1; i < attributes.length - 2; ++i) {
			lastName += attributes[i] + " ";
		}
		
		lastName = lastName.trim();
		String firstName = attributes[attributes.length - 2];
		String finalGrade = attributes[attributes.length - 1];

		if (!jmbag.matches("[0-9]{10}"))
			throw new IllegalArgumentException("Illegal jmbag: " + jmbag);


		if (!firstName.matches("[a-žA-ž]+"))
			throw new IllegalArgumentException("Illegal first name: " + firstName);

		if (!finalGrade.matches("[0-9]"))
			throw new IllegalArgumentException("Illegal final grade: " + finalGrade);

		return new StudentRecord(jmbag, lastName, firstName, finalGrade);
	}

	/**
	 * @return students jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * @return students last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return students first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return students final grade
	 */
	public String getFinalGrade() {
		return finalGrade;
	}

	@Override
	public int hashCode() {
		return jmbag.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		StudentRecord other = (StudentRecord) obj;

		return this.jmbag.equals(other.jmbag);
	}

}
