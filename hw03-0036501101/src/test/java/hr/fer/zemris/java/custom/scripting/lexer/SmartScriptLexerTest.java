package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.*;
import org.junit.Test;


public class SmartScriptLexerTest {
	
	//folowing tests assume TEXT state of lexer
	
	@Test
	public void emptyText() {
		SmartScriptLexer l = new SmartScriptLexer("");
		
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test(expected=SmartLexerException.class)
	public void noMoreTokens() {
		SmartScriptLexer l = new SmartScriptLexer("");
		
		l.nextToken();
		l.nextToken();
	}
	
	@Test
	public void someText() {
		SmartScriptLexer l = new SmartScriptLexer("Some sample text  \n trd.");
		
		Token actual = l.nextToken();
		assertEquals(TokenType.TEXT, actual.getType());
		assertEquals("Some sample text  \n trd.", actual.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test(expected=SmartLexerException.class)
	public void escapeCharOnLastPlace() {
		SmartScriptLexer l = new SmartScriptLexer("\\");
		
		l.nextToken();
	}
	
	@Test(expected=SmartLexerException.class)
	public void illegalEscapeChar() {
		SmartScriptLexer l = new SmartScriptLexer("\\$");
		
		l.nextToken();
	}
	
	@Test
	public void legalEscapeChar() {
		SmartScriptLexer l = new SmartScriptLexer("This is not a tag \\{$ FOR 1 1 1 $}\n\\\\");
		
		Token actual = l.nextToken();
		assertEquals(TokenType.TEXT, actual.getType());
		assertEquals("This is not a tag {$ FOR 1 1 1 $}\n\\", actual.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void openTag() {
		SmartScriptLexer l = new SmartScriptLexer("this is a tag {$");
		
		Token actual = l.nextToken();
		assertEquals(TokenType.TEXT, actual.getType());
		assertEquals("this is a tag ", actual.getValue());
		assertEquals(TokenType.TAG_OPEN, l.nextToken().getType());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	//folowing tests assume TAG state of lexer
	
	@Test
	public void emptyTextTag() {
		SmartScriptLexer l = new SmartScriptLexer("");
		l.setState(LexerState.TAG);
		
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void closeTag() {
		SmartScriptLexer l = new SmartScriptLexer("$}");
		l.setState(LexerState.TAG);
		
		assertEquals(TokenType.TAG_CLOSE, l.nextToken().getType());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void integerNumber() {
		SmartScriptLexer l = new SmartScriptLexer("986");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.INTEGER, t.getType());
		assertEquals(986, (int)t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void integerNumberNegative() {
		SmartScriptLexer l = new SmartScriptLexer("-159875");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.INTEGER, t.getType());
		assertEquals(-159875, (int)t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test(expected=SmartLexerException.class)
	public void integerNumberTooBig() {
		SmartScriptLexer l = new SmartScriptLexer("9854875412547854125985");
		l.setState(LexerState.TAG);
		
		l.nextToken();
	}
	
	@Test
	public void floatNumber() {
		SmartScriptLexer l = new SmartScriptLexer("1.589");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.FLOAT, t.getType());
		assertEquals(1.589, (double)t.getValue(), 1E-6);
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void floatNumberNegative() {
		SmartScriptLexer l = new SmartScriptLexer("-5897.336");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.FLOAT, t.getType());
		assertEquals(-5897.336, (double)t.getValue(), 1E-6);
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void operatorPlus() {
		SmartScriptLexer l = new SmartScriptLexer("+");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.OPERATOR, t.getType());
		assertEquals("+", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void operatorMinus() {
		SmartScriptLexer l = new SmartScriptLexer("-");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.OPERATOR, t.getType());
		assertEquals("-", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void operatorDivide() {
		SmartScriptLexer l = new SmartScriptLexer("/");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.OPERATOR, t.getType());
		assertEquals("/", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void operatorMultiply() {
		SmartScriptLexer l = new SmartScriptLexer("*");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.OPERATOR, t.getType());
		assertEquals("*", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void operatorPower() {
		SmartScriptLexer l = new SmartScriptLexer("^");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.OPERATOR, t.getType());
		assertEquals("^", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void tagName() {
		SmartScriptLexer l = new SmartScriptLexer("=");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.TAG_NAME, t.getType());
		assertEquals("=", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void variableName() {
		SmartScriptLexer l = new SmartScriptLexer("c_589KOMA");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.VARIABLE_NAME, t.getType());
		assertEquals("c_589KOMA", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void functionName() {
		SmartScriptLexer l = new SmartScriptLexer("@sin");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.FUNCTION_NAME, t.getType());
		assertEquals("@sin", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void string() {
		SmartScriptLexer l = new SmartScriptLexer("  \"ovo je  striiing\" ");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.STRING, t.getType());
		assertEquals("ovo je  striiing", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test
	public void stringEscaping() {
		SmartScriptLexer l = new SmartScriptLexer("\"\\\"ovo je u navodnicima\\\"\\novo je u novom redu\"");
		l.setState(LexerState.TAG);
		
		Token t = l.nextToken();
		assertEquals(TokenType.STRING, t.getType());
		assertEquals("\"ovo je u navodnicima\"\novo je u novom redu", t.getValue());
		assertEquals(TokenType.EOF, l.nextToken().getType());
	}
	
	@Test(expected=SmartLexerException.class)
	public void stringEscapingIllegal() {
		SmartScriptLexer l = new SmartScriptLexer(" \"\\a\"");
		l.setState(LexerState.TAG);
		
		l.nextToken();
	}
	
	@Test(expected=SmartLexerException.class)
	public void illegalVariableName() {
		SmartScriptLexer l = new SmartScriptLexer("589var");
		l.setState(LexerState.TAG);
		
		l.nextToken();
	}
	
	@Test
	public void combinedInput1() {
		SmartScriptLexer l = new SmartScriptLexer("FOR i 1 10 1$}");
		l.setState(LexerState.TAG);
		
		Token[] expected = {
				new Token(TokenType.VARIABLE_NAME, "FOR"),
				new Token(TokenType.VARIABLE_NAME, "i"),
				new Token(TokenType.INTEGER, Integer.valueOf(1)),
				new Token(TokenType.INTEGER, Integer.valueOf(10)),
				new Token(TokenType.INTEGER, Integer.valueOf(1)),
				new Token(TokenType.TAG_CLOSE, "$}"),
				new Token(TokenType.EOF, null)
		};
		
		checkTokenStream(l, expected);
	}
	
	@Test
	public void combinedInput3() {
		SmartScriptLexer l = new SmartScriptLexer("FOR i \"1\"10$}");
		l.setState(LexerState.TAG);
		
		Token[] expected = {
				new Token(TokenType.VARIABLE_NAME, "FOR"),
				new Token(TokenType.VARIABLE_NAME, "i"),
				new Token(TokenType.STRING, "1"),
				new Token(TokenType.INTEGER, Integer.valueOf(10)),
				new Token(TokenType.TAG_CLOSE, "$}"),
				new Token(TokenType.EOF, null)
		};
		
		checkTokenStream(l, expected);
	}
	
	@Test
	public void combinedInput2() {
		SmartScriptLexer l = new SmartScriptLexer("-9 i = FOR END @sin_cos_123 598 \"ovo je string\"22 c_kana789+var -987 \n  FOR-p89 l$}");
		l.setState(LexerState.TAG);
		
		Token[] expected = {
				new Token(TokenType.INTEGER, Integer.valueOf(-9)),
				new Token(TokenType.VARIABLE_NAME, "i"),
				new Token(TokenType.TAG_NAME, "="),
				new Token(TokenType.VARIABLE_NAME, "FOR"),
				new Token(TokenType.VARIABLE_NAME, "END"),
				new Token(TokenType.FUNCTION_NAME, "@sin_cos_123"),
				new Token(TokenType.INTEGER, Integer.valueOf(598)),
				new Token(TokenType.STRING, "ovo je string"),
				new Token(TokenType.INTEGER, Integer.valueOf(22)),
				new Token(TokenType.VARIABLE_NAME, "c_kana789"),
				new Token(TokenType.OPERATOR, "+"),
				new Token(TokenType.VARIABLE_NAME, "var"),
				new Token(TokenType.INTEGER, Integer.valueOf(-987)),
				new Token(TokenType.VARIABLE_NAME, "FOR"),
				new Token(TokenType.OPERATOR, "-"),
				new Token(TokenType.VARIABLE_NAME, "p89"),
				new Token(TokenType.VARIABLE_NAME, "l"),
				new Token(TokenType.TAG_CLOSE, "$}"),
				new Token(TokenType.EOF, null)
		};
		
		checkTokenStream(l, expected);
	}
	
	private void checkTokenStream(SmartScriptLexer lexer, Token[] correctData) {
		int counter = 0;
		for(Token expected : correctData) {
			Token actual = lexer.nextToken();
			String msg = "Checking token "+counter + ":";
			assertEquals(msg, expected.getType(), actual.getType());
			assertEquals(msg, expected.getValue(), actual.getValue());
			counter++;
		}
	}
}
