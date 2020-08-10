package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * A demonstration program that takes a smart script file, parses it, recreate
 * its original document body and prints it to the standard output.
 * 
 * @author Disho
 *
 */
public class TreeWriter {

	/**
	 * Implementation of a INodeVisitor that recreates the original
	 * document text from the parsed smart script. This visitor should
	 * be called on the base document node of the smart script parser.
	 * 
	 * @author Disho
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {
		/**
		 * string builder for text appending
		 */
		private StringBuilder sb;
		/**
		 * recreated document body text
		 */
		private String recreatedText;

		@Override
		public void visitTextNode(TextNode node) {
			sb.append(node.getText().replace("\\", "\\\\").replace("{", "\\{"));
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			sb.append("{$ FOR ");

			sb.append(node.getVariable().asText());
			sb.append(" ");
			sb.append(node.getStartExpression().asText());
			sb.append(" ");
			sb.append(node.getEndExpression().asText());

			if (node.getStepExpression() != null) {
				sb.append(" ");
				sb.append(node.getStepExpression().asText());
			}

			sb.append(" $}");

			for (int i = 0; i < node.numberOfChildren(); ++i) {
				node.getChild(i).accept(this);
			}

			sb.append("{$END$}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			sb.append("{$ = ");
			for (Element arg : node.getElements()) {
				sb.append(arg.asText()).append(" ");
			}

			sb.append("$}");
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			sb = new StringBuilder();

			for (int i = 0; i < node.numberOfChildren(); ++i) {
				node.getChild(i).accept(this);
			}

			recreatedText = sb.toString();
		}

		/**
		 * Returns a recreated document body text
		 * @return document body text
		 */
		public String getRecreatedText() {
			return recreatedText;
		}

	}

	/**
	 * Main method. Controls the flow of the program.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected one argument: a path to a smart script file.");
			return;
		}

		Path path = Paths.get(args[0]);

		if (!Files.isRegularFile(path) || !Files.isReadable(path)) {
			System.out.println("Given file: " + path.toAbsolutePath() + "is not a regular file or is not readable.");
			return;
		}

		String docBody;

		try {
			docBody = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Error ocurred while reading the file. Exiting the program...");
			return;
		}

		SmartScriptParser parser = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
		System.out.println(visitor.getRecreatedText());
	}

}
