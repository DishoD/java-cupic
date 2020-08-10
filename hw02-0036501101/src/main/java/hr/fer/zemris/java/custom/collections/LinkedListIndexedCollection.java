package hr.fer.zemris.java.custom.collections;

/**
 * Linked list implementation for storing objects. Duplicate elements are
 * allowed, storage of null references is not allowed. Elements are added to the
 * end of the list. Index starts from 0;
 * 
 * @author Hrvoje Ditrih
 * @version 1.0
 *
 */
public class LinkedListIndexedCollection extends Collection {
	private int size;
	private ListNode first;
	private ListNode last;

	/**
	 * Represents double-linked-list node.
	 * 
	 * @author Hrvoje Ditrih
	 *
	 */
	private static class ListNode {
		public ListNode previous;
		public ListNode next;
		public Object data;

		/**
		 * Initilizes a node with given parameters.
		 * 
		 * @param previous
		 *            node
		 * @param next
		 *            node
		 * @param data
		 *            object to be sored in a node
		 */
		public ListNode(ListNode previous, ListNode next, Object data) {
			this.previous = previous;
			this.next = next;
			this.data = data;
		}

		/**
		 * Initilizes a node with a given object. Previous and next node are set to
		 * null.
		 * 
		 * @param data
		 *            object to be stored in a node
		 */
		public ListNode(Object data) {
			this.data = data;
		}

	}

	/**
	 * Initializes an empty linked list.
	 */
	public LinkedListIndexedCollection() {

	}

	/**
	 * Initilizes linked list with the elements from the given collection.
	 * 
	 * @param other
	 *            collection from which elements are copied
	 */
	public LinkedListIndexedCollection(Collection other) {
		addAll(other);
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
	 * Adds given object to the end of the list.
	 * 
	 * @param Object
	 *            to be added
	 * @throws NullPointerException
	 *             if value is null
	 */
	@Override
	public void add(Object value) {
		try {
			insert(value, size);
		} catch (NullPointerException ex) {
			throw ex;
		}
	}

	/**
	 * Returns a list node of given index.
	 * 
	 * @param index
	 *            of the node
	 * @return node at the given index
	 * @throws IndexOutOfBoundsException
	 *             if index is out of bounds of list size
	 */
	private ListNode getNode(int index) {
		if (index < 0 || index > size - 1)
			throw new IndexOutOfBoundsException("index should be in [0, " + (size - 1) + "]. It was: " + index);

		if (index == 0)
			return first;
		if (index == size - 1)
			return last;

		ListNode node;
		if (index <= size / 2) {
			node = first;

			for (int i = 0; i < index; ++i) {
				node = node.next;
			}
		} else {
			node = last;

			for (int i = size; i > index; --i) {
				node = node.previous;
			}
		}

		return node;
	}

	/**
	 * Returns an object ftom the list at given index.
	 * 
	 * @param index
	 *            of the object
	 * @return object at the given index
	 * @throws IndexOutOfBoundsException
	 *             if index is out of bounds of list size
	 */
	public Object get(int index) {
		try {
			return getNode(index).data;
		} catch (IndexOutOfBoundsException ex) {
			throw ex;
		}
	}

	/**
	 * Inserts an object into the list at the given position. Rest of the nodes are
	 * shifted by one. Can insert(add) at the end of the list.
	 * 
	 * @param value
	 *            object to be inserted
	 * @param position
	 *            index of insertion
	 * @throws NullPointerException
	 *             if value is null
	 * @throws IndexOutOfBoundsException
	 *             if position is out of bounds of list size
	 */
	public void insert(Object value, int position) {
		if (value == null)
			throw new NullPointerException("value must not be null");
		if (position < 0 || position > size)
			throw new IndexOutOfBoundsException("index should be in [0, " + (size) + "]. It was: " + position);

		if (first == null && last == null && position == 0) {
			first = last = new ListNode(value);
		} else if (position == size) {
			last.next = new ListNode(last, null, value);
			last = last.next;
		} else if (position == 0) {
			first = new ListNode(null, first, value);
		} else {
			ListNode node = getNode(position - 1);
			node.next = new ListNode(node, node.next, value);
		}

		size++;
	}

	/**
	 * Returns an index of the first occurrence of the given object if it is stored
	 * in the list, -1 otherwise.
	 * 
	 * @param value
	 *            object to be searched for
	 * @return -1 if object is not found, valid index otherwise
	 */
	public int indexOf(Object value) {
		if (value == null)
			return -1;

		ListNode node = first;
		for (int i = 0; i < size; ++i) {
			if (node.data.equals(value))
				return i;
		}

		return -1;
	}

	/**
	 * Removes an object from the list at the given index.
	 * 
	 * @param index
	 *            position of the object to be removed
	 * @throws IndexOutOfBoundsException
	 *             if index is out of bounds of the list size
	 */
	public void remove(int index) {
		if (index < 0 || index > size - 1)
			throw new IndexOutOfBoundsException("index should be in [0, " + (size - 1) + "]. It was: " + index);

		if (index == 0) {
			first = first.next;
			first.previous = null;
		} else if (index == size - 1) {
			last = last.previous;
			last.next = null;
		} else {
			ListNode node = getNode(index - 1);
			node.next = node.next.next;
			node.next.previous = node;
		}

		size--;
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
		if (value == null || size == 0)
			return false;

		ListNode node = first;
		for (int i = 0; i < size; ++i) {
			if (node.data.equals(value))
				return true;

			node = node.next;
		}

		return false;
	}

	/**
	 * Removes the first occurance of the given object in the list.
	 * 
	 * @param value
	 *            objet to be removed
	 * @return true if object is removed, false otherwise
	 */
	@Override
	public boolean remove(Object value) {
		if (value == null)
			return false;

		int index = indexOf(value);
		if (index != -1) {
			remove(index);
			return true;
		}

		return false;
	}

	/**
	 * Returns an array representaton of the collection.
	 * 
	 * @return array of objects (can be empty)
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];

		ListNode node = first;
		for (int i = 0; i < size; ++i) {
			array[i] = node.data;
			node = node.next;
		}

		return array;
	}

	@Override
	public void forEach(Processor processor) {
		if (processor == null)
			throw new NullPointerException("processor must not be null.");

		ListNode node = first;
		for (int i = 0; i < size; ++i) {
			processor.process(node.data);
			node = node.next;
		}
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
	public void clear() {
		first = last = null;
		size = 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof LinkedListIndexedCollection))
			return false;

		LinkedListIndexedCollection other = (LinkedListIndexedCollection) obj;

		if (this.size != other.size)
			return false;

		ListNode myNode = this.first;
		ListNode otherNode = other.first;

		for (int i = 0; i < size; ++i) {
			if (myNode.data.equals(otherNode.data) == false)
				return false;

			myNode = myNode.next;
			otherNode = otherNode.next;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");

		ListNode node = first;
		for (int i = 0; i < size; ++i) {
			sb.append(node.data).append(", ");
			node = node.next;
		}

		sb.delete(sb.length() - 2, sb.length());
		return sb.append("]").toString();
	}

}
