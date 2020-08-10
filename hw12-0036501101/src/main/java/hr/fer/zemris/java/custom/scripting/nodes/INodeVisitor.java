package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Defines a node visitor as in Visitor design pattern.
 * 
 * @author Disho
 *
 */
public interface INodeVisitor {
	/**
	 * Defines a method that is performed when the TextNode is visited.
	 * 
	 * @param node visited TextNode
	 */
	public void visitTextNode(TextNode node);
	
	/**
	 * Defines a method that is performed when the ForLoopNode is visited.
	 * 
	 * @param node visited ForLoopNode
	 */
	public void visitForLoopNode(ForLoopNode node);
	
	/**
	 * Defines a method that is performed when the EchoNode is visited.
	 * 
	 * @param node visited EchoNode
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * Defines a method that is performed when the DocumentNode is visited.
	 * 
	 * @param node visited DocumentNode
	 */
	public void visitDocumentNode(DocumentNode node);
}
