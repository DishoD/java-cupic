package hr.fer.zemris.java.hw16.jvdraw.model;

/**
 * Defines an listener of the drawing model.
 * 
 * @author Disho
 *
 */
public interface DrawingModelListener {
	/**
	 * This method is called when some object were added at the given interval.
	 * 
	 * @param source drawing model that called this method
	 * @param index0 interval begin inclusive
	 * @param index1 interval end inclusive
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);
	
	/**
	 * This method is called when some object were removed at the given interval.
	 * 
	 * @param source drawing model that called this method
	 * @param index0 interval begin inclusive
	 * @param index1 interval end inclusive
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);
	
	/**
	 * This method is called when some object were changed at the given interval.
	 * 
	 * @param source drawing model that called this method
	 * @param index0 interval begin inclusive
	 * @param index1 interval end inclusive
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
