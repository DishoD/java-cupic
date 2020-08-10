package hr.fer.zemris.java.hw16.jvdraw.geometricalobjects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.GeometricalObjectEditor;

/**
 * Base class for all geometrical objects in the JVDraw application.
 * 
 * @author Disho
 *
 */
public abstract class GeometricalObject {
	/**
	 * list of registered listeners
	 */
	private List<GeometricalObjectListener> listeners = new ArrayList<>();

	/**
	 * Registers an listener
	 * 
	 * @param l listener
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.add(l);
	}
	
	/**
	 * Unregisters an listener
	 * 
	 * @param l listener
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}
	
	protected void notifyListeners() {
		for(GeometricalObjectListener l : listeners) {
			l.geometricalObjectChanged(this);
		}
	}
	
	/**
	 * Accepts the visitor.
	 * 
	 * @param v geometrical object visitor
	 */
	public abstract void accept(GeometricalObjectVisitor v);
	
	/**
	 * Pains the geometrical objects on the given graphics object.
	 * 
	 * @param g graphics object
	 */
	public abstract void paint(Graphics2D g);
	
	/**
	 * Creates this objects editor.
	 * 
	 * @return geometrical object editor
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();

}
