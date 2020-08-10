package hr.fer.zemris.java.hw06.demo4;

import java.util.Comparator;
import java.util.Objects;

/**
 * Represents a record of one student. Holds information: jmbag, fist name, last
 * name, MI points, ZI points, lab points and grade.
 * 
 * @author Disho
 *
 */
public class StudentRecord implements Comparable<StudentRecord> {
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
	 * students MI points
	 */
	private double MIPoints;
	/**
	 * students ZI points
	 */
	private double ZIPoints;
	/**
	 * students lab points
	 */
	private double labPoints;
	/**
	 * students grade
	 */
	private int grade;

	/**
	 * use for sorting by students last name
	 */
	public static final Comparator<StudentRecord> BY_LAST_NAME = (s1, s2) -> s1.lastName.compareTo(s2.lastName);
	/**
	 * use for sorting by students first name
	 */
	public static final Comparator<StudentRecord> BY_FIRST_NAME = (s1, s2) -> s1.firstName.compareTo(s2.firstName);
	/**
	 * use for sorting by students MI points
	 */
	public static final Comparator<StudentRecord> BY_MI_POINTS = (s1, s2) -> Double.compare(s1.MIPoints, s2.MIPoints);
	/**
	 * use for sorting by students ZI points
	 */
	public static final Comparator<StudentRecord> BY_ZI_POINTS = (s1, s2) -> Double.compare(s1.ZIPoints, s2.ZIPoints);
	/**
	 * use for sorting by students lab points
	 */
	public static final Comparator<StudentRecord> BY_LAB_POINTS = (s1, s2) -> Double.compare(s1.labPoints,
			s2.labPoints);
	/**
	 * use for sorting by students grade
	 */
	public static final Comparator<StudentRecord> BY_GRADE = (s1, s2) -> Integer.compare(s1.grade, s2.grade);
	/**
	 * use for sorting by students points
	 */
	public static final Comparator<StudentRecord> BY_POINTS = (s1, s2) -> Double.compare(s1.getPoints(), s2.getPoints());

	/**
	 * Initializes the student with the given parameters.
	 * 
	 * @param jmbag
	 *            students jmbag
	 * @param lastName
	 *            students last name
	 * @param firstName
	 *            students first name
	 * @param mIPoints
	 *            students MI points
	 * @param zIPoints
	 *            students ZI points
	 * @param labPoints
	 *            students lab points
	 * @param grade
	 *            students grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, double mIPoints, double zIPoints,
			double labPoints, int grade) {
		Objects.requireNonNull(jmbag, "Jmbag must not be null");
		Objects.requireNonNull(lastName, "lastName must not be null");
		Objects.requireNonNull(firstName, "firstName must not be null");

		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		MIPoints = mIPoints;
		ZIPoints = zIPoints;
		this.labPoints = labPoints;
		this.grade = grade;
	}

	/**
	 * Creates student record from string text. The string should contain students:
	 * jmbag, last name, first name, MI points, ZI points, lab points and grade.
	 * Exactly in that order separated by whitespace;
	 * 
	 * @param line
	 *            string from which to create students record
	 * @return a new student record
	 */
	public static StudentRecord fromLine(String line) {
		String[] attributes = line.split("\\s+");

		if (attributes.length != 7)
			throw new IllegalArgumentException(
					"Illegal line. It must contains 7 attributes. There was: " + attributes.length + ". Line: " + line);

		double MIPoints;
		double ZIPoints;
		double labPoints;
		int grade;

		try {
			MIPoints = Double.parseDouble(attributes[3]);
			ZIPoints = Double.parseDouble(attributes[4]);
			labPoints = Double.parseDouble(attributes[5]);
			grade = Integer.parseInt(attributes[6]);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Illegal numeric attributes.");
		}

		return new StudentRecord(attributes[0], attributes[1], attributes[2], MIPoints, ZIPoints, labPoints, grade);
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

	@Override
	public int compareTo(StudentRecord other) {
		return this.jmbag.compareTo(other.jmbag);
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
	 * @return students MI points
	 */
	public double getMIPoints() {
		return MIPoints;
	}

	/**
	 * @return students ZI points
	 */
	public double getZIPoints() {
		return ZIPoints;
	}

	/**
	 * @return students lab points
	 */
	public double getLabPoints() {
		return labPoints;
	}

	/**
	 * @return students grade
	 */
	public int getGrade() {
		return grade;
	}
	
	/**
	 * Returns a sum of all students points.
	 * 
	 * @return sum of all students points
	 */
	public double getPoints() {
		return MIPoints + ZIPoints + labPoints;
	}
	
	@Override
	public String toString() {
		return jmbag + " " + lastName + " " + firstName + ", ukupan broj bodova: " + getPoints() + ", ocjena: " + grade;
	}

}
