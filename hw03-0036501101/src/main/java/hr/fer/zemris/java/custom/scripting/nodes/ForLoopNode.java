package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Represents FOR tag with it's arguments for SmartScriptParser.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ForLoopNode extends Node {
	private ElementVariable variable;
	private Element startExpression;
	private Element endExpression;
	private Element stepExpression;

	/**
	 * Initializes a node.
	 * 
	 * @param variable
	 *            variable name
	 * @param startExpression
	 *            start expression
	 * @param endExpression
	 *            end expression
	 * @param stepExpression
	 *            step expression, can be null
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		if (variable == null || startExpression == null || endExpression == null)
			throw new IllegalArgumentException("Variable, start and end expression can't be null.");

		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * @return variable name
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * @return start expression
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * @return end expression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * @return step expression, can be null
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

}
