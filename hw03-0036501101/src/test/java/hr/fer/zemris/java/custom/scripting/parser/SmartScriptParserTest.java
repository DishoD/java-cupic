package hr.fer.zemris.java.custom.scripting.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.hw03.SmartScriptTester;

public class SmartScriptParserTest {

	@Test
	public void documentNodeTest1() {
		String docBody = loader("doc1.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode actual = parser.getDocumentNode();

		DocumentNode expected = new DocumentNode();
		expected.addChildNode(new TextNode(""));
		
		ElementVariable dummy = new ElementVariable("");

		{
			
			ForLoopNode forNode = new ForLoopNode(dummy, dummy, dummy, null);
			forNode.addChildNode(new TextNode(""));
			forNode.addChildNode(new EchoNode(null));
			forNode.addChildNode(new TextNode(""));
			expected.addChildNode(forNode);
		}

		expected.addChildNode(new TextNode(""));

		{
			ForLoopNode forNode = new ForLoopNode(dummy, dummy, dummy, null);
			forNode.addChildNode(new TextNode(""));
			forNode.addChildNode(new EchoNode(null));
			forNode.addChildNode(new TextNode(""));
			forNode.addChildNode(new EchoNode(null));
			forNode.addChildNode(new TextNode(""));
			expected.addChildNode(forNode);
		}

		assertTrue(SmartScriptTester.areNodesSame(expected, actual));
	}

	@Test
	public void reparse() {
		String docBody = loader("doc1.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode actual = parser.getDocumentNode();

		SmartScriptParser parser2 = new SmartScriptParser(SmartScriptParser.createOriginalDocumentBody(actual));
		DocumentNode expected = parser2.getDocumentNode();

		assertTrue(SmartScriptTester.areNodesSame(actual, expected));
	}

	@Test(expected = SmartScriptParserException.class)
	public void unclosedTag() {
		String docBody = loader("doc2.txt");
		@SuppressWarnings("unused")
		SmartScriptParser parser = new SmartScriptParser(docBody);
	}

	@Test
	public void forLoopInForLoop() {
		String docBody = loader("doc3.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode actual = parser.getDocumentNode();

		DocumentNode expected = new DocumentNode();
		expected.addChildNode(new TextNode(""));
		
		ElementVariable dummy = new ElementVariable("");

		ForLoopNode forNode1 = new ForLoopNode(dummy, dummy, dummy, null);
		forNode1.addChildNode(new TextNode(""));
		forNode1.addChildNode(new EchoNode(null));
		forNode1.addChildNode(new TextNode(""));

		ForLoopNode forNode2 = new ForLoopNode(dummy, dummy, dummy, null);
		forNode2.addChildNode(new TextNode(""));
		forNode2.addChildNode(new EchoNode(null));
		forNode2.addChildNode(new TextNode(""));

		forNode1.addChildNode(forNode2);
		forNode1.addChildNode(new TextNode(""));

		expected.addChildNode(forNode1);

		assertTrue(SmartScriptTester.areNodesSame(expected, actual));

	}

	@Test
	public void escapingInTextState() {
		String docBody = loader("doc4.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode actual = parser.getDocumentNode();

		DocumentNode expected = new DocumentNode();
		expected.addChildNode(new TextNode(""));

		assertTrue(SmartScriptTester.areNodesSame(actual, expected));
	}

	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			while (true) {
				int read = is.read(buffer);
				if (read < 1)
					break;
				bos.write(buffer, 0, read);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			return null;
		}
	}

}
