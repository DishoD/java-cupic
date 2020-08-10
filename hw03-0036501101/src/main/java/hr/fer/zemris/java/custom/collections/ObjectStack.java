package hr.fer.zemris.java.custom.collections;

/**
 * A stack(LIFO) of objects implementation. This stack does not allow null
 * references.
 * 
 * @author Hrvoje Ditrih
 * @version 1.0
 */
public class ObjectStack {
	private ArrayIndexedCollection stack = new ArrayIndexedCollection();

	/**
	 * Initializes an empty stack.
	 */
	public ObjectStack() {

	}
	
	/**
	 * Checks weather the stack is emtpy.
	 * @return true if stack is empty, false otherwise
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	/**
	 * Returns a size of a stack.
	 * @return number of objects on a stack
	 */
	public int size() {
		return stack.size();
	}
	
	/**
	 * Pushes an elemen on to a stack.
	 * @param value object to be pushed
	 * @throws NullPointerException if value is null
	 */
	public void push(Object value) {
		if(value == null)
			throw new NullPointerException("Value must not be null.");
		
		stack.add(value);
	}
	
	/**
	 * Pops an element from the stack.
	 * @return last element on the stack
	 * @throws EmptyStackException if stack is empty
	 */
	public Object pop() {
		if(stack.isEmpty())
			throw new EmptyStackException("Cannot pop an empty stack.");
		
		int lastIndex = stack.size() - 1;
		Object element = stack.get(lastIndex);
		stack.remove(lastIndex);
		
		return element;
	}
	
	/**
	 * Peeks an element from the stack.
	 * @return last element on the stack
	 * @throws EmptyStackException if stack is empty
	 */
	public Object peek() {
		if(stack.isEmpty())
			throw new EmptyStackException("Cannot peek an empty stack.");
		
		return stack.get(stack.size() - 1);
	}
	
	/**
	 * Removes all elements from the stack.
	 */
	public void clear() {
		stack.clear();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof ObjectStack)) return false;
		
		ObjectStack other = (ObjectStack)obj;
		return this.stack.equals(other.stack);
	}
	
	@Override
	public String toString() {
		return stack.toString();
	}
}
