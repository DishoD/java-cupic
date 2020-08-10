package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * This class tests SmartScriptParser.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class SmartScriptTester {

	public static void main(String[] args) throws IOException {

		// can load all documents in examples directory, just go through every one - it should throw
		// an appropriate exception message or it should parse and reparse just fine
		String filepath = "examples\\doc3.txt";

		String docBody = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println(e.getMessage());
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = SmartScriptParser.createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody);

		// parse again recreated parsed text
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		
		//print again reparsed text
		String originalDocumentBody2 = SmartScriptParser.createOriginalDocumentBody(document2);
		System.out.println();
		System.out.println(originalDocumentBody2);

		// should get the same document nodes
		System.out.println("\nAre two document nodes structurally the same: " + areNodesSame(document, document2));
	}

	/**
	 * Checks whether two nodes are structuraly the same. If they are of the same
	 * type(class) and if they have same number of children. Recursivly checks the
	 * same for all children nodes.
	 * 
	 * @param node1
	 *            first node
	 * @param node2
	 *            second node
	 * @return true if two nodes are structuraly the same, false otherwise
	 */
	public static boolean areNodesSame(Node node1, Node node2) {
		// check if two nodes are of the same type
		if (!(node1.getClass().getName().equals(node2.getClass().getName())))
			return false;

		if (node1.numberOfChildren() != node2.numberOfChildren())
			return false;

		for (int i = 0; i < node1.numberOfChildren(); ++i) {
			if (!areNodesSame(node1.getChild(i), node2.getChild(i)))
				return false;
		}

		return true;
	}

}
