package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents text for SmartScriptParser.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class TextNode extends Node {
	private String text;

	/**
	 * Initializes an text node with a given text.
	 * 
	 * @param text
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * @return text
	 */
	public String getText() {
		return text;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
