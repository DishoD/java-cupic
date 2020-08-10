package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Stores pairs of (key, value) -> (String, Stack of ValueWrappers).
 * Can't store keys and values as null.
 * 
 * @author Disho
 *
 */
public class ObjectMultistack {
	private Map<String, MultistackEntry> map = new HashMap<>();

	/**
	 * Represents one node of a Stack (implemented as a linked list) of
	 * ValueWrappers.
	 * 
	 * @author Disho
	 *
	 */
	private static class MultistackEntry {
		/**
		 * value of the node
		 */
		private ValueWrapper value;
		/**
		 * next node
		 */
		private MultistackEntry next;

		/**
		 * Initializes the node with the given parameters.
		 * 
		 * @param value
		 *            value of the node
		 * @param next
		 *            next node
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = value;
			this.next = next;
		}
		
	}
	
	/**
	 * Pushes the given valueWrapper to the stack of the given string.
	 * 
	 * @param name key
	 * @param valueWrapper value to push to the top of the stack of the given name
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		Objects.requireNonNull(name, "Name must not be null");
		Objects.requireNonNull(valueWrapper, "ValueWrapper must not be null");
		
		map.merge(name, new MultistackEntry(valueWrapper, null), (o,n)-> new MultistackEntry(valueWrapper, o));
	}
	
	/**
	 * Pops the value from the top of the stack of the given key.
	 * 
	 * @param name key
	 * @return value of the given key
	 * 
	 * @throws EmptyStackException if the stack of the given key is empty
	 */
	public ValueWrapper pop(String name) {
		MultistackEntry stackHead = map.get(name);
		if(stackHead == null) throw new EmptyStackException();
		
		ValueWrapper value = stackHead.value;
		map.compute(name, (k,v)-> v = v.next);
		
		return value;
	}
	
	/**
	 * Peeks the value from the top of the stack of the given key.
	 * 
	 * @param name key
	 * @return value of the given key
	 * 
	 * @throws EmptyStackException if the stack of the given key is empty
	 */
	public ValueWrapper peek(String name) {
		MultistackEntry stackHead = map.get(name);
		if(stackHead == null) throw new EmptyStackException();
		
		ValueWrapper value = stackHead.value;
		
		return value;
	}
	
	/**
	 * Checks whether the stack of the given key is empty.
	 * 
	 * @param name key
	 * @return true if the stack is empty, false otherwise
	 */
	public boolean isEmpty(String name) {
		return map.get(name) == null;
	}
}
