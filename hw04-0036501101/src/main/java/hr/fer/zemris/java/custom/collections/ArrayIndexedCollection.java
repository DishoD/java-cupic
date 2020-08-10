package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;

/**
 * Dynamic collection of objects implemented with an array. Duplicate elements
 * are allowed, storage of null references is not allowed. Objects are added to
 * the end of an array. Index starts from 0;
 * 
 * @author Hrvoje Ditrih
 * @version 1.0
 */
public class ArrayIndexedCollection extends Collection {
	private int size;
	private int capacity;
	private Object[] elements;

	/**
	 * Defines default capacity of the elements array.
	 */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * Initializes an empty collection with given initial capacity.
	 * 
	 * @param initialCapacity
	 *            initial capacity of the collection. Must be greater or equal to 1.
	 * @throws IllegalArgumentException
	 *             if given capacity is lower then 1
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1)
			throw new IllegalArgumentException(
					"initialCapacity should be greater or equal to 1. It was: " + initialCapacity);

		capacity = initialCapacity;
		elements = new Object[initialCapacity];
	}

	/**
	 * Initializes an empty collection with initial capacity of 16.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Initializes the collection with the elements of the given collection and
	 * given initial capacity. If given initial capacity is smaller then the size of
	 * the given collection, initial capacity will be set to the size of the given
	 * collection.
	 * 
	 * @param other
	 *            Collection from which all the elements will be copied
	 * @param initialCapacity
	 *            initial capacity of the collection
	 * @throws NullPointerException
	 *             if given collection is null
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		this(Math.max(other.size(), initialCapacity));
		addAll(other);
	}

	/**
	 * Initializes the collection with the elements from the given collection.
	 * 
	 * @param other
	 * @throws NullPointerException
	 *             if given collection is null
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, DEFAULT_CAPACITY);
	}

	/**
	 * Returns an object thet is stored at the given index.
	 * 
	 * @param index
	 *            Object index. Should be in bounds of collection size.
	 * @return Object at the given index
	 * @throws IndexOutOfBoundsException
	 *             if given index is out of collection size bounds
	 */
	public Object get(int index) {
		if (index < 0 || index > size - 1)
			throw new IndexOutOfBoundsException(
					"index should be in interval [0, " + (size - 1) + "]. It was: " + index);

		return elements[index];
	}

	/**
	 * Returns the size of collection.
	 * 
	 * @return Number of objects in the collection
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Inserts given object to the collection at the the given position. Other
	 * elements are shifted to the right of the inserted element. Can insert(add) at
	 * the end of the array.
	 * 
	 * @param value
	 *            Object to be inserted
	 * @param position
	 *            index of the insertion
	 * @throws NullPointerException
	 *             if value is null
	 * @throws IndexOutOfBoundsException
	 *             if given position is out of bounds of the collection size
	 */
	public void insert(Object value, int position) {
		if (value == null)
			throw new NullPointerException("value musn't be null.");
		if (position < 0 || position > size)
			throw new IndexOutOfBoundsException(
					"position should be in interval [0, " + size + "]. It was: " + position);

		ensureCapacity(size + 1);

		if (position != size) {
			shiftElementsToRight(position, size);
		}

		elements[position] = value;
		size++;
	}

	/**
	 * Adds a given object to the collection. Cannot add null.
	 * 
	 * @param value
	 *            Object to be added (musn't be null)
	 * @throws NullPointerException
	 *             if given object is null
	 */
	@Override
	public void add(Object value) {
		try {
			insert(value, size);
		} catch (IndexOutOfBoundsException ex) {
			throw ex;
		}
	}

	/**
	 * Ensures that elements array is big enough for the given size.
	 * 
	 * @param size
	 *            length of the elements array to be ensured
	 */
	private void ensureCapacity(int size) {
		if (size > capacity) {
			int factor = (int) Math.ceil((double) size / (capacity));
			extendElementsArray(factor);
		}
	}

	/**
	 * Creates a new elements array of current length multiplied by the given
	 * extension factor. Elements from the former array will be copied into a new,
	 * bigger, array.
	 * 
	 * @param extensionFactor
	 *            multiply factor, must be 2 or greater
	 * @throws IllegalArgumentException
	 *             if given extension factor is smaller then 2
	 */
	private void extendElementsArray(int extensionFactor) {
		if (extensionFactor < 2)
			throw new IllegalArgumentException(
					"extension factor must be greater or equal to 2. it was: " + extensionFactor);

		capacity *= extensionFactor;
		elements = Arrays.copyOf(elements, capacity);
	}

	/**
	 * Returns an index of the given object.
	 * 
	 * @param value
	 *            Object to be checked
	 * @return -1 if given object is not in the collection, valid index otherwise
	 */
	public int indexOf(Object value) {
		if (value == null)
			return -1;

		for (int i = 0; i < size; ++i) {
			if (elements[i].equals(value))
				return i;
		}

		return -1;
	}
	
	/**
	 * Checks whether the given object exists in the collection.
	 * 
	 * @param value
	 *            Object to be checked
	 * @return true if value exists in collection, false otherwise
	 */
	@Override
	public boolean contains(Object value) {
		if (value == null)
			return false;

		for (int i = 0; i < size; ++i) {
			if (elements[i].equals(value))
				return true;
		}

		return false;
	}

	/**
	 * Removes an element from the collection at the given index. The rest of the
	 * elements are shifted to the left by one.
	 * 
	 * @param index
	 *            position of the object to be removed
	 */
	public void remove(int index) {
		if (index < 0 || index > size - 1)
			throw new IndexOutOfBoundsException(
					"index should be in interval [0, " + (size - 1) + "]. It was: " + index);

		if (index < size - 1) {
			shiftElementsToLeft(index + 1, size);
		} else {
			elements[index] = null;
		}

		size--;
	}

	/**
	 * Shifts elements of the collection to the left by one place.
	 * 
	 * @param startingIndex
	 *            starting index of the array (inclusive)
	 * @param endingIndex
	 *            ending index of the array (exclusive)
	 * @throws IndexOutOfBoundsException
	 *             if starting and ending indexes are switched or out of bounds of
	 *             the collection size
	 */
	private void shiftElementsToLeft(int startingIndex, int endingIndex) {
		if (startingIndex < 1 || startingIndex > size - 1)
			throw new IndexOutOfBoundsException(
					"starting index should be in interval [1, " + (size - 1) + "]. It was: " + startingIndex);
		if (endingIndex <= startingIndex || endingIndex > size)
			throw new IndexOutOfBoundsException(
					"ending index should be in interval (startingIndex, " + (size) + "]. It was: " + endingIndex);

		for (int i = startingIndex - 1; i < endingIndex; ++i) {
			elements[i] = elements[i + 1];
		}
	}

	/**
	 * Shifts elements of the collection to the right by one place.
	 * 
	 * @param startingIndex
	 *            starting index of the array (inclusive)
	 * @param endingIndex
	 *            ending index of the array (exclusive)
	 * @throws IndexOutOfBoundsException
	 *             if starting and ending indexes are switched or out of bounds of
	 *             the collection size
	 */
	private void shiftElementsToRight(int startingIndex, int endingIndex) {
		if (startingIndex < 0 || startingIndex >= size)
			throw new IndexOutOfBoundsException(
					"starting index should be in interval [0, " + (size - 1) + "). It was: " + startingIndex);
		if (endingIndex <= startingIndex || endingIndex > size)
			throw new IndexOutOfBoundsException(
					"ending index should be in interval [startingIndex, " + (size) + "]. It was: " + endingIndex);

		for (int i = endingIndex; i > startingIndex; --i) {
			elements[i] = elements[i - 1];
		}
	}

	/**
	 * Removes first occurrence of the given object from the collection. Rest of the
	 * elements are shifted to the left by one.
	 * 
	 * @param value
	 *            objet to be removed
	 */
	@Override
	public boolean remove(Object value) {
		if (value == null)
			return false;

		int index = indexOf(value);
		if (index != -1) {
			remove(index);
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * Returns an array representaton of the collection.
	 * 
	 * @return array of objects (can be empty)
	 */
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}
	
	/**
	 * Processes each element of the collection as defined with parameter processor
	 * 
	 * @param processor
	 *            Determens how each element will be processed
	 * @throws NullPointerException
	 *             if given processor is null
	 */
	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; ++i) {
			processor.process(elements[i]);
		}
	}

	/**
	 * Removes all elements from the collection
	 */
	@Override
	public void clear() {
		Arrays.fill(elements, 0, size, null);
		size = 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof ArrayIndexedCollection))
			return false;

		ArrayIndexedCollection other = (ArrayIndexedCollection) obj;

		if (this.size != other.size)
			return false;

		for (int i = 0; i < size; ++i) {
			if (this.elements[i].equals(other.elements[i]) == false)
				return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder("[");

		for (int i = 0; i < size; ++i) {
			string.append(elements[i]).append(", ");
		}
		
		string.delete(string.length() - 2, string.length());
		return string.append("]").toString();
	}
	
}
