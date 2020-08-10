package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Represents a '=' tag with the given elements which
 * represents some dynamic output to the screen.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class EchoNode extends Node{
	private Element[] elements;

	/**
	 * Initializes an EchoNode with the given
	 * elements.
	 * 
	 * @param elements
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}
	
	/**
	 * @return elements of the echo node
	 */
	public Element[] getElements() {
		return elements;
	}
}
