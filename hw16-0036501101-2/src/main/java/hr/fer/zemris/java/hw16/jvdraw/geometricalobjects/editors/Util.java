package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors;

import javax.swing.JSpinner;

/**
 * Utility class with helper methods for creation of the geometrical object editors.
 * 
 * @author Disho
 *
 */
public class Util {
	/**
	 * Creates a JSpinner with the given integer initial value.
	 * 
	 * @param initialValue integer initial value
	 * @return created JSpinner
	 */
	public static JSpinner createJSpinner(int initialValue) {
		JSpinner s = new JSpinner();
		s.setValue(initialValue);
		return s;
	}
	
	/**
	 * Checks if the spinner has integer value and if that value is positive.
	 * If that is valid, method does nothing. Otherwise throws an runtime exception.
	 * 
	 * @param s JSpinner to check
	 * @param msg message appended in the exeption
	 */
	public static void checkSpinner(JSpinner s, String msg) {
		int value;
		
		try {
			value = Integer.parseInt(s.getValue().toString());
		} catch(NumberFormatException e) {
			throw new RuntimeException(msg + " must be an integer value.");
		}
		
		if(value < 0) {
			throw new RuntimeException(msg + " must be 0 or positive number.");
		}
	}
}
