package hr.fer.zemris.java.custom.collections;

/**
 * A very simple and ineffective implementation of a dictionary
 * that stores pairs (key, value). Null references are not allowed as keys,
 * but are allowed as values.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class Dictionary {
	/**
	 * Collection used for storing pairs(key, value).
	 */
	private ArrayIndexedCollection array = new ArrayIndexedCollection();
	
	/**
	 * Represents a pair (key, value).
	 * 
	 * @author Hrvoje Ditrih
	 *
	 */
	private static class Pair {
		/**
		 * key in pair (key, value)
		 */
		private Object key;
		
		/**
		 * value in pair (key, value)
		 */
		private Object value;
		
		/**
		 * Initializes an pair with given parameters.
		 * 
		 * @param key key in pair (key, value)
		 * @param value value in pair (key, value)
		 */
		private Pair(Object key, Object value) {
			this.key = key;
			this.value = value;
		}
	}
	
	/**
	 * Returns number of pairs (key, value) in the dictionary.
	 * 
	 * @return number of pairs in dictionary
	 */
	public int size() {
		return array.size();
	}
	
	/**
	 * Removes all the pairs from the dictionary leaving it empty.
	 */
	public void clear() {
		array.clear();
	}
	
	/**
	 * Checks whether the dictionary is empty.
	 * 
	 * @return true if dictionary has no pairs in it, false otherwise
	 */
	public boolean isEmpty() {
		return array.isEmpty();
	}
	
	/**
	 * Returns the value associated with the given key.
	 * Returns null if the pair (key, value) of the given key does not exist in the dictionary.
	 * 
	 * @param key key of the pair (key, value)
	 * @return value of the associated key, null if the pair doesn't exist
	 */
	public Object get(Object key) {
		if(key == null) return null;
		
		int index = getIndex(key);
		
		return index == -1 ? null : ((Pair)array.get(index)).value;
	}
	
	/**
	 * Stores a new pair (key, value) in the dictionary. If the pair
	 * with the given key already exists in the dictionary, the value will only be updated.
	 * 
	 * Can't store null references as keys.
	 * 
	 * @param key key of the pair
	 * @param value value of the pair
	 */
	public void put(Object key, Object value) {
		if(key == null)
			throw new IllegalArgumentException("Key can't be null");
		
		int index = getIndex(key);
		
		if(index == -1) {
			array.add(new Pair(key, value));
		} else {
			((Pair)array.get(index)).value = value;
		}
	}
	
	/**
	 * Returns an index of the pair (key, value) for the given key in an array.
	 * If the pair of the given key doesn't exist in the array it returns -1.
	 * 
	 * @param key key of the pair (key, value)
	 * @return index in array of the pair, -1 if it doesn't exist
	 */
	private int getIndex(Object key) {
		if(key == null) return -1;
		
		for(int i = 0; i < array.size(); ++i) {
			Pair currentPair = (Pair)array.get(i);
			if(currentPair.key.equals(key)) return i;
		}
		
		return -1;
	}
}
