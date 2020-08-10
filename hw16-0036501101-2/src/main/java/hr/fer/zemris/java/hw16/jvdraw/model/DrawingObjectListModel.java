package hr.fer.zemris.java.hw16.jvdraw.model;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObject;

/**
 * List model implementation adapter for the drawing model.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> {
	/**
	 * adapted drawing model
	 */
	private DrawingModel model;

	/**
	 * Initializes the list model for the given drawing mdoel.
	 * 
	 * @param model drawing model to adapt
	 */
	public DrawingObjectListModel(DrawingModel model) {
		super();
		this.model = model;
		
		model.addDrawingModelListener(new DrawingModelListener() {
			
			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				fireIntervalRemoved(this, index0, index1);
			}
			
			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				fireContentsChanged(this, index0, index1);
			}
			
			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				fireIntervalAdded(this, index0, index1);
			}
		});
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}

	@Override
	public int getSize() {
		return model.getSize();
	}
	
	

}
