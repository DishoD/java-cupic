package hr.fer.zemris.java.hw16.jvdraw.actions;

import javax.swing.KeyStroke;

/**
 * Abstract action with the constructor that accepts the action name and accelerator.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractAction extends javax.swing.AbstractAction {

	/**
	 * Initializes the abstract action with the given parameters.
	 * 
	 * @param name action name
	 * @param accelerator key stroke for the action accelerator
	 */
	public AbstractAction(String name, String accelerator) {
		this.putValue(NAME, name);
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
	}

}
