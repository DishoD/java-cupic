package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects;

/**
 * Defines an geometrical object listener.
 * 
 * @author Disho
 *
 */
public interface GeometricalObjectListener {
	/**
	 * Called when the given object has changes some of its properties.
	 * 
	 * @param o geometrical object that changed
	 */
	public void geometricalObjectChanged(GeometricalObject o);
}