package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Represents a stack context for L-system fractals using turtle graphics.
 * Uses TurtleState class to record current turtle state.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class Context {
	/**
	 * A stack used for storing turtle states.
	 */
	private ObjectStack stack = new ObjectStack();
	
	/**
	 * Returns current turtle state.
	 * 
	 * @return current turtle state
	 */
	public TurtleState getCurrentState() {
		return (TurtleState) stack.peek();
	}
	
	/**
	 * Pushes the given turtle state to the stack.
	 * 
	 * @param state turtle state to be pushed on the stack
	 */
	public void pushState(TurtleState state) {
		stack.push(state);
	}
	
	/**
	 * Removes the last added turtle state from the stack.
	 */
	public void popState() {
		stack.pop();
	}
}
