package hr.fer.zemris.java.hw16.jvdraw.model;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObject;

/**
 * Model that holds a list of geometrical objects for drawing.
 * Model allows observer design pattern to notify listener when some
 * changes were made on objects.
 * 
 * @author Disho
 *
 */
public interface DrawingModel {
	/**
	 * Get number of geometrical objects.
	 * 
	 * @return number of geometrical objects
	 */
	public int getSize();

	/**
	 * Get geometrical object at the given index.
	 * 
	 * @param index object index
	 * @return geometrical object
	 */
	public GeometricalObject getObject(int index);
	
	/**
	 * Remove given geometrical object from the model.
	 * 
	 * @param object geometrical object to remove
	 */
	void remove(GeometricalObject object);
	
	/**
	 * Shift objects position  in the model by the given offset.
	 * 
	 * @param object object to shift
	 * @param offset offset by which the object will be shifted
	 */
	void changeOrder(GeometricalObject object, int offset);

	/**
	 * Adds geometrical object at the end of the list of the model.
	 * 
	 * @param object geometrical object to add
	 */
	public void add(GeometricalObject object);

	/**
	 * Register listener
	 * 
	 * @param l listener to register
	 */
	public void addDrawingModelListener(DrawingModelListener l);
	
	/**
	 * Unregister listener
	 * 
	 * @param l listener to unregister
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
}
