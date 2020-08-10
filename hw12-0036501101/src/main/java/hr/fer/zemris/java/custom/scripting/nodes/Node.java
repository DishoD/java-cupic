package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class that represents structure element used for constructing document
 * node trees in SmartScriptParser.
 * 
 * @author Hrvoje Ditrih
 *
 */
public abstract class Node {
	private List<Node> children;

	/**
	 * Adds a child to the node.
	 * 
	 * @param child
	 */
	public void addChildNode(Node child) {
		if (child == null)
			throw new IllegalArgumentException("Can't add null as child node.");

		if (children == null) {
			children = new ArrayList<>();
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
		return children.get(index);
	}
	
	/**
	 * @param visitor
	 */
	public abstract void accept(INodeVisitor visitor);
}
