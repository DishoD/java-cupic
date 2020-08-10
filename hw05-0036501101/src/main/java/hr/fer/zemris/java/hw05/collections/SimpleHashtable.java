package hr.fer.zemris.java.hw05.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Simple hashtable implementation. For collision resolution it uses separate
 * chaining with linked lists. Can't store null references as keys, but as
 * values can. It is iterable.
 * 
 * @author Hrvoje Ditrih
 *
 * @param <K>
 *            key
 * @param <V>
 *            value
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	/**
	 * Hashtable slots
	 */
	private TableEntry<K, V>[] slots;

	/**
	 * Number of pairs in the hashtable
	 */
	private int size;

	/**
	 * Every time the hashtable is modified this attribute must be incremented.
	 */
	private long modificationCount;

	private static final int DEFAULT_TABLE_SIZE = 16;

	/**
	 * Initializes an hashtable with the given size. Given size will be rounded up
	 * to the nearest power of two.
	 * 
	 * @param size
	 *            of the hashtable
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int size) {
		if (size <= 0)
			throw new IllegalArgumentException("Size must be a pozitive number! It was: " + size);

		size = nearestBiggerPowerOfTwo(size);
		this.slots = (TableEntry<K, V>[]) new TableEntry[size];
	}

	/**
	 * Initializes the hashtable with the initial table size of 16.
	 */
	public SimpleHashtable() {
		this(DEFAULT_TABLE_SIZE);
	}

	/**
	 * Represents one hashtable entry with pair (key, value) implemented as a linked
	 * list.
	 * 
	 * @author Hrvoje Ditrih
	 *
	 * @param <K>
	 *            key
	 * @param <V>
	 *            value
	 */
	static class TableEntry<K, V> {
		private K key;
		private V value;
		private TableEntry<K, V> next;

		/**
		 * Initializes table entry (key, value) with the given parameters.
		 * 
		 * @param key
		 *            key of the pair
		 * @param value
		 *            value of the pair
		 * @param next
		 *            next node in the list
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		/**
		 * Returns key of the pair (key, value)
		 * 
		 * @return key of the pair
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Returns value of the pair (key, value)
		 * 
		 * @return value of the pair
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets the value of the pair (key, value)
		 * 
		 * @param value
		 *            value to be set
		 */
		public void setValue(V value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return key.toString() + "=" + value.toString();
		}
	}

	/**
	 * Implementation of an iterator for SimpleHashtable.
	 * 
	 * @author Hrvoje Ditrih
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		private TableEntry<K, V> current;
		private TableEntry<K, V> parent;

		private int currentSlot;
		private int lastSlot;

		private boolean removeFlag;

		private long modificationCount;

		/**
		 * Initializes an iterator.
		 */
		private IteratorImpl() {
			this.modificationCount = SimpleHashtable.this.modificationCount;

			for (int i = slots.length - 1; i >= 0; --i) {
				if (slots[i] != null) {
					lastSlot = i;
					return;
				}
			}
		}

		@Override
		public boolean hasNext() {
			checkModificationCount();

			if (SimpleHashtable.this.isEmpty())
				return false;

			if (currentSlot == lastSlot && current != null && current.next == null)
				return false;

			return true;
		}

		@Override
		public TableEntry<K, V> next() {
			checkModificationCount();

			if (SimpleHashtable.this.isEmpty())
				throw new NoSuchElementException();

			parent = current;
			removeFlag = false;

			// first pair
			if (current == null) {
				while (slots[currentSlot] == null) {
					currentSlot++;
				}

				current = slots[currentSlot];
				return current;
			}

			if (current.next == null) {
				currentSlot++;
				while (currentSlot < slots.length && slots[currentSlot] == null) {
					currentSlot++;
				}

				if (currentSlot >= slots.length)
					throw new NoSuchElementException();

				current = slots[currentSlot];
				return current;
			}

			current = current.next;

			return current;
		}

		/**
		 * Removes current element of an iterator from the hashtable. Can't be called
		 * more than once for one element.
		 * 
		 * @throws IllegalStateException
		 *             if one tries to remove same element more than once or method
		 *             next() has never been called
		 */
		@Override
		public void remove() {
			checkModificationCount();

			if (current == null)
				throw new IllegalStateException("Method naxt() hasn't been called!");

			if (removeFlag)
				throw new IllegalStateException("Can't remove same pair more than once.");

			removeFlag = true;

			SimpleHashtable.this.remove(current.key);

			current = parent;
			this.modificationCount = SimpleHashtable.this.modificationCount;
		}

		private void checkModificationCount() {
			if (this.modificationCount != SimpleHashtable.this.modificationCount)
				throw new ConcurrentModificationException();
		}
	}

	/**
	 * Calculates nearest bigger power of two of a given number. Eg.: If given
	 * number 9 it will return 16. If given number 256 it will return 256.
	 * 
	 * @param number
	 *            number to round to the power of two, must be positive number or
	 *            zero
	 * @return nearest bigger power of two
	 */
	private static int nearestBiggerPowerOfTwo(int number) {
		if (number < 0)
			throw new IllegalArgumentException("Number must be positive! It was: " + number);

		if (number == 0)
			return 0;

		int lastBit = 1 << 31;

		int lastOne = -1;
		boolean flag = false;

		for (int i = 0; i < 32; ++i) {
			if (lastOne == -1 && ((number << i) & lastBit) != 0) {
				lastOne = 31 - i;
			} else if (((number << i) & lastBit) != 0) {
				flag = true;
				break;
			}
		}

		return flag ? 1 << (lastOne + 1) : 1 << lastOne;
	}

	/**
	 * Inserts new pair of the (key, value) in the hashtable. If pair with the given
	 * key already exists, it will just update the value of that pair.
	 * 
	 * @param key
	 *            key of the pair, can't be null
	 * @param value
	 *            value of the pair
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Key must not be null.");

		TableEntry<K, V> current = getEntry(key);

		if (current != null) {
			current.value = value;
			return;
		}

		int slot = slotNumber(key);
		current = slots[slot];
		size++;
		modificationCount++;

		if (current == null) {
			slots[slot] = new TableEntry<>(key, value, null);
			ensureEfficiency();
			return;
		}

		while (current.next != null) {
			current = current.next;
		}

		current.next = new TableEntry<>(key, value, null);
		ensureEfficiency();
	}

	/**
	 * Returns value of the paired key. If key does not exist in the hashtable it
	 * returns null.
	 * 
	 * @param key
	 *            key of the value to get
	 * @return value of the paired key if it exists, null otherwise
	 */
	public V get(Object key) {
		TableEntry<K, V> entry = getEntry(key);

		return entry == null ? null : entry.value;
	}

	/**
	 * Returns number of pairs (key, value) in the hashtable.
	 * 
	 * @return number of pairs
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks whether the hashtable contains pair (key, value) with the given key.
	 * 
	 * @param key
	 *            key to check if it exists in the hashtable
	 * @return true if key exists in the hashtable, false otherwise
	 */
	public boolean containsKey(Object key) {
		TableEntry<K, V> entry = getEntry(key);

		return entry != null;
	}

	/**
	 * Checks whether the hashtable contains pair (key, value) with the given value.
	 * 
	 * @param value
	 *            value to check if it exists in the hashtable
	 * @return true if value exists in the hashtable, false otherwise
	 */
	public boolean containsValue(Object value) {
		for (int slot = 0; slot < slots.length; ++slot) {
			TableEntry<K, V> current = slots[slot];

			while (current != null) {
				if (current.value.equals(value))
					return true;
				current = current.next;
			}
		}

		return false;
	}

	/**
	 * Removes the pair (key, value) from the hashtable. If pair with the given key
	 * does not exist, method does nothing.
	 * 
	 * @param key
	 *            key of the pair to remove
	 */
	public void remove(Object key) {
		if (!containsKey(key))
			return;

		int slot = slotNumber(key);
		TableEntry<K, V> current = slots[slot];
		size--;
		modificationCount++;

		if (current.key.equals(key)) {
			slots[slot] = current.next;
			return;
		}

		while (current.next != null) {
			if (current.next.key.equals(key)) {
				current.next = current.next.next;
				return;
			}

			current = current.next;
		}
	}

	/**
	 * Checks whether the hashtable is empty.
	 * 
	 * @return true if hashtable is empty, false otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Removes all pairs (key, value) from the hashtable. Doesn't change the
	 * hashtable's capacity.
	 */
	public void clear() {
		size = 0;
		modificationCount++;

		Arrays.fill(slots, null);
	}

	/**
	 * Returns table entry of the given key if it exists.
	 * 
	 * @param key
	 *            key of which table entry to get
	 * @return table entry of the given key if it exists, null otherwise
	 */
	private TableEntry<K, V> getEntry(Object key) {
		if (key == null)
			return null;

		int slot = slotNumber(key);

		TableEntry<K, V> current = slots[slot];

		while (current != null) {
			if (current.key.equals(key))
				return current;
			current = current.next;
		}

		return null;
	}

	/**
	 * Calculates slot number of the given key.
	 * 
	 * @param key
	 *            key of which to calculate the slot number
	 * @return slot number of the given key
	 */
	private int slotNumber(Object key) {
		return Math.abs(key.hashCode() % slots.length);
	}

	/**
	 * Ensures that get() method can return values in average complexity O(1).
	 */
	private void ensureEfficiency() {
		if ((double) size / slots.length >= 0.75)
			expandTable();
	}

	/**
	 * Doubles the size of the hashtable and efficiently copies the pairs from the
	 * old hashtable.
	 */
	@SuppressWarnings("unchecked")
	private void expandTable() {
		TableEntry<K, V>[] oldTable = slots;
		slots = (TableEntry<K, V>[]) new TableEntry[slots.length * 2];

		for (int slot = 0; slot < oldTable.length; ++slot) {
			TableEntry<K, V> current = oldTable[slot];

			while (current != null) {
				TableEntry<K, V> next = current.next;
				copyEntry(current);
				current = next;
			}
		}

	}

	/**
	 * This method is used by expandTable() method. It copies the references of the
	 * given entry from the old table to the new table to the appropriate slot.
	 * 
	 * This method expects that new slots table is already allocated. This method
	 * changes the next attribute of the given entry(have that in mind when calling
	 * this method as you will lose the linked list chain).
	 * 
	 * @param entry
	 *            entry to be copied to the new table
	 */
	private void copyEntry(TableEntry<K, V> entry) {
		if (entry == null)
			throw new NullPointerException("Entry can't be null!");

		int slot = slotNumber(entry.key);
		entry.next = null;

		if (slots[slot] == null) {
			slots[slot] = entry;
			return;
		}

		TableEntry<K, V> current = slots[slot];

		while (current.next != null) {
			current = current.next;
		}

		current.next = entry;
	}

	@Override
	public String toString() {
		if (size == 0)
			return "[]";

		StringBuilder sb = new StringBuilder("[");

		for (int slot = 0; slot < slots.length; ++slot) {
			TableEntry<K, V> current = slots[slot];

			while (current != null) {
				sb.append(current).append(", ");
				current = current.next;
			}
		}

		return sb.substring(0, sb.length() - 2) + "]";
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
}
