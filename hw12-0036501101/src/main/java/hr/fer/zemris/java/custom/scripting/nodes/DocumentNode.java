package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents base node for buliding a node tree in
 * SmartScriptParser.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class DocumentNode extends Node {

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
	
}
