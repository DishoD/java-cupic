package hr.fer.zemris.java.tecaj.hw05.db.FieldValueGetters;

import hr.fer.zemris.java.tecaj.hw05.db.IFieldValueGetter.IFieldValueGetter;

/**
 * Contains field value getters for student records. Objects of this class
 * cannot be instantiated. Meant only for usage of public static members.
 * 
 * @author Hrvoje Ditrih
 *
 */
public final class FieldValueGetters {
	
	/**
	 * Private constructor so that objects of this class can't be instantiated.
	 * Meant only for usage of public static members.
	 * 
	 */
	private FieldValueGetters() {
		
	}
	
	/**
	 * Extracts students fist name from the student record.
	 */
	public static IFieldValueGetter FIRST_NAME = record -> record.getFirstName();
	
	/**
	 * Extracts students last name from the student record.
	 */
	public static IFieldValueGetter LAST_NAME = record -> record.getLastName();
	
	/**
	 * Extracts a students jmbag from the student record.
	 */
	public static IFieldValueGetter JMBAG = record -> record.getJmbag();
	
	
}
