package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Base class that represents structure element used for constructing document
 * node trees in SmartScriptParser.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class Node {
	private ArrayIndexedCollection children;

	/**
	 * Adds a child to the node.
	 * 
	 * @param child
	 */
	public void addChildNode(Node child) {
		if (child == null)
			throw new IllegalArgumentException("Can't add null as child node.");

		if (children == null) {
			children = new ArrayIndexedCollection();
		}

		children.add(child);
	}

	/**
	 * @return number of children
	 */
	public int numberOfChildren() {
		if (children == null)
			return 0;

		return children.size();
	}

	/**
	 * Gets a child from the node.
	 * 
	 * @param index
	 *            of a child
	 * @return child
	 */
	public Node getChild(int index) {
		return (Node) children.get(index);
	}
}
