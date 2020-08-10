package hr.fer.zemris.java.custom.collections;

/**
 * A class that represents an generic collection of objects. This class is meant
 * to be extended(inherited). Read javadoc of all methods to know which ones to
 * override and what they should do.
 * 
 * @author Hrvoje Ditrih
 * @version 1.0
 */
public class Collection {

	/**
	 * Constructror: does nothing.
	 */
	protected Collection() {

	}

	/**
	 * Checks whether the collection is empty.
	 * 
	 * Does not need to be implemented in the class that extends this class. This
	 * method just calls method size() and checks if it's 0.
	 * 
	 * @return true if collection is empty, false otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the size of collection.
	 * 
	 * Not implemented here. (always returns 0)
	 * 
	 * Class that extends this class should provide an implementation.
	 * 
	 * @return Number of objects in the collection
	 */
	public int size() {
		return 0;
	}

	/**
	 * Adds a given object to the collection.
	 * 
	 * Not implemented here. (does nothing)
	 * 
	 * Class that extends this class should provide an implementation.
	 * 
	 * @param value
	 *            Object to be added
	 */
	public void add(Object value) {

	}

	/**
	 * Checks whether the given object exists in the collection.
	 * 
	 * Not implemented here. (always returns false)
	 * 
	 * Class that extends this class should provide an implementation.
	 * 
	 * @param value
	 *            Object to be checked
	 * @return true if value exists in collection, false otherwise
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Removes an object from the collection.
	 * 
	 * Not implemented here. (always returns false)
	 * 
	 * Class that extends this class should provide an implementation.
	 * 
	 * @param value
	 *            Object to be removed
	 * @return true if object is removed, false otherwise
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Returns an array representaton of the collection.
	 * 
	 * Not implemented here. (always returns null)
	 * 
	 * Class that extends this class should provide an implementation.
	 * 
	 * @return array of objects (can be empty)
	 */
	public Object[] toArray() {
		return null;
	}

	/**
	 * Processes each element of the collection as defined with parameter processor
	 * 
	 * Not implemented here. (does nothing)
	 * 
	 * Class that extends this class should provide an implementation.
	 * 
	 * @param processor
	 *            Determens how each element will be processed
	 */
	public void forEach(Processor processor) {

	}

	/**
	 * Adds all elemnts from the given collection to the current collection.
	 * 
	 * @param other
	 *            Collection from which elements will be added
	 */
	public void addAll(Collection other) {

		class addProcessor extends Processor {

			@Override
			public void process(Object value) {
				add(value);
			}
		}

		other.forEach(new addProcessor());
	}

	/**
	 * Removes all elements from the collection
	 * 
	 * Not implemented here. (does nothing)
	 * 
	 * Class that extends this class should provide an implementation.
	 * 
	 */
	public void clear() {

	}

}
