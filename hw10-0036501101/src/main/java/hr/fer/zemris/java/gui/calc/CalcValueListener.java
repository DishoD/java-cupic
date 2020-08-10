package hr.fer.zemris.java.gui.calc;

/**
 * Listeners to the CalcModel that will be notified every time that the current
 * value has changed.
 * 
 * @author Disho
 *
 */
public interface CalcValueListener {

	/**
	 * This method is called when the current value of the CalcModel has been
	 * changed.
	 * 
	 * @param model
	 *            model in which the value changed
	 */
	void valueChanged(CalcModel model);
}
