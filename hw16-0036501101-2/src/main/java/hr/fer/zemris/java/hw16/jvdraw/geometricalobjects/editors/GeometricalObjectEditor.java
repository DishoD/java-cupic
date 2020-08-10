package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors;

import javax.swing.JPanel;

/**
 * Panel providing gui for editing of properties of the geometrical objects.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public abstract class GeometricalObjectEditor extends JPanel {
	/**
	 * check if the properties have legal values, if not, throws an runtime
	 * exception
	 */
	public abstract void checkEditing();

	/**
	 * called after checkEditing(), applies new properties to the geometrical object
	 */
	public abstract void acceptEditing();
}