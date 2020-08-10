package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.LexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * A simple parser that parses everything into text and tags. Everything outside
 * tags is a text. Tags are opened and closed wtih '{$' and '$}' respectivly.
 * There are 3 legal tags: FOR, = and END. For tag looks like this: {$FOR i 1 10
 * 1$}, and fourth argument is optional. Every FOR tag must be have its own END
 * tag: {$END$}. = tag looks like this: {$ i "sting" 589 @function $}, it
 * constists of variable number of arguments (zero or more).
 * 
 * This parser produces a document node for given text.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class SmartScriptParser {
	private DocumentNode documentNode = new DocumentNode();
	private ObjectStack stack = new ObjectStack();
	private SmartScriptLexer lexer;

	/**
	 * Initializes a parser with the given text and generates according document
	 * node.
	 * 
	 * @param text
	 */
	public SmartScriptParser(String text) {
		lexer = new SmartScriptLexer(text);
		documentNode = parse();
	}

	/**
	 * Parses the given text according to rules of SmartScript language and
	 * generates according document node.
	 * 
	 * @return document node for the given text
	 */
	private DocumentNode parse() {
		stack.push(documentNode);
		lexer.setState(LexerState.TEXT);

		Token token;
		while ((token = nextToken()).getType() != TokenType.EOF) {
			Node currentNode = (Node) stack.peek();

			if (lexer.getState() == LexerState.TEXT) {
				if (token.getType() == TokenType.TEXT) {
					currentNode.addChildNode(new TextNode((String) token.getValue()));
				} else if (token.getType() == TokenType.TAG_OPEN) {
					lexer.setState(LexerState.TAG);
					processTag();
					lexer.setState(LexerState.TEXT);
				}
			}
		}

		if (stack.size() != 1)
			throw new SmartScriptParserException("Some FOR tag wasn't ENDed!");

		return (DocumentNode) stack.pop();
	}

	/**
	 * Determens the tag name and processes it accordingly.
	 * 
	 * Only parse() method should call this method.
	 */
	private void processTag() {
		Token token = nextToken();
		EOFwhenTagOpened(token);

		if (token.getType() != TokenType.VARIABLE_NAME && token.getType() != TokenType.TAG_NAME)
			throw new SmartScriptParserException("Illegal tag name: " + token.getValue());

		String tagName = ((String) token.getValue()).toLowerCase();
		if (tagName.equals("for")) {
			processFor();
		} else if (tagName.equals("end")) {
			processEnd();
		} else if (tagName.equals("=")) {
			processEcho();
		} else {
			throw new SmartScriptParserException("Illegal tag name: " + tagName);
		}

	}

	/**
	 * processTag() method calls this method when it encounters '=' tag name.
	 * Constructs EchoNode and places it within a document node.
	 */
	private void processEcho() {
		ArrayIndexedCollection elements = new ArrayIndexedCollection();

		Token token = nextToken();
		EOFwhenTagOpened(token);
		while (token.getType() != TokenType.TAG_CLOSE) {
			Element arg;

			switch (token.getType()) {
			case VARIABLE_NAME:
				arg = new ElementVariable((String) token.getValue());
				break;
			case FUNCTION_NAME:
				arg = new ElementFunction((String) token.getValue());
				break;
			case STRING:
				arg = new ElementString((String) token.getValue());
				break;
			case INTEGER:
				arg = new ElementConstantInteger((int) token.getValue());
				break;
			case FLOAT:
				arg = new ElementConstantDouble((double) token.getValue());
				break;
			case OPERATOR:
				arg = new ElementOperator((String) token.getValue());
				break;
			default:
				throw new SmartScriptParserException("Illegal token in ECHO tag. It was: " + token.getValue());
			}

			elements.add(arg);

			token = nextToken();
			EOFwhenTagOpened(token);
		}

		Element[] args = new Element[elements.size()];

		for (int i = 0; i < elements.size(); ++i) {
			args[i] = (Element) elements.get(i);
		}

		Node currentNode = (Node) stack.peek();
		currentNode.addChildNode(new EchoNode(args));
	}

	/**
	 * processTag() method calls this method when it encounters END tag name. Closes
	 * currently opened ForLoopNode.
	 */
	private void processEnd() {
		Node currentNode = (Node) stack.peek();
		if (!(currentNode instanceof ForLoopNode))
			throw new SmartScriptParserException("END tag without FOR tag!");

		Token token = nextToken();
		EOFwhenTagOpened(token);

		if (token.getType() != TokenType.TAG_CLOSE)
			throw new SmartScriptParserException("Illegal token in END tag. It was: " + token.getValue());

		stack.pop();
	}

	/**
	 * processTag() method calls this method when it encounters FOR tag name.
	 * Produces and opens ForLoopNode with according parameters.
	 */
	private void processFor() {
		Token token = nextToken();
		EOFwhenTagOpened(token);

		if (token.getType() != TokenType.VARIABLE_NAME)
			throw new SmartScriptParserException("Expected variable name in for tag. Got: " + token.getValue());

		ElementVariable param1 = new ElementVariable((String) token.getValue());
		Element[] params = new Element[3];
		boolean closed = false;

		for (int i = 0; i < 3; ++i) {
			token = nextToken();
			EOFwhenTagOpened(token);

			if (i == 2 && token.getType() == TokenType.TAG_CLOSE) {
				closed = true;
				break;
			}

			if (token.getType() != TokenType.VARIABLE_NAME && token.getType() != TokenType.STRING
					&& token.getType() != TokenType.INTEGER)
				throw new SmartScriptParserException(
						"Expected variable name, number or string in for tag. Got: " + token.getValue());

			if (token.getType() == TokenType.VARIABLE_NAME) {
				params[i] = new ElementVariable((String) token.getValue());
			} else if (token.getType() == TokenType.STRING) {
				params[i] = new ElementString((String) token.getValue());
			} else {
				params[i] = new ElementConstantInteger((int) token.getValue());
			}
		}

		if (!closed) {
			token = nextToken();
			EOFwhenTagOpened(token);
			if (token.getType() != TokenType.TAG_CLOSE)
				throw new SmartScriptParserException("Expected close tag but got: " + token.getValue());
		}

		ForLoopNode newNode = new ForLoopNode(param1, params[0], params[1], params[2]);
		Node currentNode = (Node) stack.peek();
		currentNode.addChildNode(newNode);
		stack.push(newNode);
	}

	/**
	 * @return next token of a lexer
	 * @throws SmartScriptParserException
	 *             if lexer throws and exception
	 */
	private Token nextToken() {
		try {
			return lexer.nextToken();
		} catch (SmartLexerException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		}
	}

	/**
	 * This method should be called after every nextToken() call from lexer when EOF
	 * is not expected for the given token in TAG state. Throws according
	 * SmartScriptParserException.
	 * 
	 * @param token
	 *            token to be checked if it is EOF
	 */
	private void EOFwhenTagOpened(Token token) {
		if (token.getType() == TokenType.EOF)
			throw new SmartScriptParserException("Tag opened but not closed!");
	}

	/**
	 * @return Constructed document node for the given text
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}

	/**
	 * Recreates original text document from the goven documen node.
	 * 
	 * @param document
	 *            document node to be recreated
	 * @return string representing the original text
	 */
	public static String createOriginalDocumentBody(DocumentNode document) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < document.numberOfChildren(); ++i) {
			sb.append(printNode(document.getChild(i)));
		}

		return sb.toString();
	}

	/**
	 * Generates text of the given node.
	 * 
	 * @param node
	 *            node for which text should be generated
	 * @return text
	 */
	private static String printNode(Node node) {
		if (node instanceof TextNode) {
			TextNode textNode = (TextNode) node;
			return textNode.getText().replace("\\", "\\\\").replace("{", "\\{");
		} else if (node instanceof EchoNode) {
			EchoNode echoNode = (EchoNode) node;
			StringBuilder sb = new StringBuilder("{$ = ");
			for (Element arg : echoNode.getElements()) {
				sb.append(arg.asText()).append(" ");
			}

			return sb.append("$}").toString();
		} else if (node instanceof ForLoopNode) {
			ForLoopNode forLoopNode = (ForLoopNode) node;
			StringBuilder sb = new StringBuilder("{$ FOR ");

			sb.append(forLoopNode.getVariable().asText());
			sb.append(" ");
			sb.append(forLoopNode.getStartExpression().asText());
			sb.append(" ");
			sb.append(forLoopNode.getEndExpression().asText());

			if (forLoopNode.getStepExpression() != null) {
				sb.append(" ");
				sb.append(forLoopNode.getStepExpression().asText());
			}

			sb.append(" $}");

			for (int i = 0; i < forLoopNode.numberOfChildren(); ++i) {
				sb.append(printNode(forLoopNode.getChild(i)));
			}

			return sb.append("{$END$}").toString();
		}

		// should never come to this point
		return null;
	}

}
