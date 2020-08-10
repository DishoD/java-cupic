package hr.fer.zemris.java.hw16.jvdraw.model;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObjectListener;

/**
 * Drawing model implementation.
 * 
 * @author Disho
 *
 */
public class DrawingModelImpl implements DrawingModel {
	/**
	 * list of geometrical objects
	 */
	private List<GeometricalObject> objects = new ArrayList<>();
	/**
	 * registered listeners
	 */
	private List<DrawingModelListener> listeners = new ArrayList<>();

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);

		object.addGeometricalObjectListener(new GeometricalObjectListener() {

			@Override
			public void geometricalObjectChanged(GeometricalObject o) {
				notifyListenersObjectChanged(objects.indexOf(o));
			}
		});

		notifyListenersObjectAdded(objects.size() - 1);
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);
		objects.remove(object);
		notifyListenersObjectRemoved(index);
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int index = objects.indexOf(object);
		int resultedIndex = index + (offset > 0 ? offset + 1 : offset);

		if (resultedIndex < 0 || resultedIndex > objects.size())
			throw new IllegalArgumentException("Offset puts the object out of bounds.");

		objects.add(resultedIndex, object);
		notifyListenersObjectAdded(resultedIndex);

		if (offset < 0) {
			objects.remove(index + 1);
			notifyListenersObjectRemoved(index + 1);
		} else {
			objects.remove(index);
			notifyListenersObjectRemoved(index);
		}
	}

	/**
	 * Notify listeners that object has been added at the given index.
	 * 
	 * @param index index of interest
	 */
	private void notifyListenersObjectAdded(int index) {
		for (DrawingModelListener l : listeners) {
			l.objectsAdded(this, index, index);
		}
	}
	
	/**
	 * Notify listeners that object has been removed from the given index.
	 * 
	 * @param index index of interest
	 */
	private void notifyListenersObjectRemoved(int index) {
		for (DrawingModelListener l : listeners) {
			l.objectsRemoved(this, index, index);
		}
	}
	
	/**
	 * Notify listeners that object has been changed at the given index.
	 * 
	 * @param index index of interest
	 */
	private void notifyListenersObjectChanged(int index) {
		for (DrawingModelListener l : listeners) {
			l.objectsChanged(this, index, index);
		}
	}
}
