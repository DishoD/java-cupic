package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * A simple lexer for SmartScriptParser. This lexer has two states: TEXT and
 * TAG. In TEXT state it reads everything as token TEXT until it runs into
 * TAG_OPEN token. In TAG state it reads: VARIABLE_NAME, STRING, INTEGER, FLOAT,
 * FUNCTION_NAME, TAG_CLOSE, OPERATOR, TAG_NAME.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class SmartScriptLexer {
	private char[] data;
	private Token token;
	private int currentIndex;
	private LexerState state = LexerState.TEXT;

	private static Token EOF_TOKEN = new Token(TokenType.EOF, null);
	private static Token TAG_OPEN_TOKEN = new Token(TokenType.TAG_OPEN, "{$");
	private static Token TAG_CLOSE_TOKEN = new Token(TokenType.TAG_CLOSE, "$}");

	/**
	 * Initializes an lexer with given text.
	 * 
	 * @param text
	 *            text
	 */
	public SmartScriptLexer(String text) {
		if (text == null)
			throw new IllegalArgumentException("Can't parse null!");

		data = text.toCharArray();
	}

	/**
	 * Generates next token for the current state of the lexer.
	 * 
	 * @return next token
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF)
			throw new SmartLexerException("No more tokens!");

		if (state == LexerState.TEXT) {
			token = nextTokenText();
		} else {
			token = nextTokenTag();
		}

		return token;
	}

	/**
	 * Generates next token according to rules of TEXT state.
	 * 
	 * @return next token
	 */
	private Token nextTokenText() {
		if (state != LexerState.TEXT)
			throw new SmartLexerException("Called nextTokenText() but wasn't in TEXT state");

		StringBuilder value = new StringBuilder();

		while (currentIndex < data.length) {
			if (data[currentIndex] == '{' && currentIndex + 1 < data.length && data[currentIndex + 1] == '$') {
				if (value.length() == 0) {
					currentIndex += 2;
					return TAG_OPEN_TOKEN;
				} else {
					return new Token(TokenType.TEXT, value.toString());
				}

			}

			if (data[currentIndex] == '\\') {
				if (currentIndex + 1 >= data.length)
					throw new SmartLexerException("Escape character \\ can't be last character in text!");

				currentIndex++;
				if (data[currentIndex] == '\\' || data[currentIndex] == '{') {
					value.append(data[currentIndex]);
					currentIndex++;
					continue;
				} else {
					throw new SmartLexerException(
							"Can only escape \\\\ and \\{. Tried to escape: " + data[currentIndex]);
				}
			}

			value.append(data[currentIndex]);
			currentIndex++;
		}

		if (value.length() == 0)
			return EOF_TOKEN;

		return new Token(TokenType.TEXT, value.toString());
	}

	/**
	 * Generates next token according to rules of TAG state.
	 * 
	 * @return next token
	 */
	private Token nextTokenTag() {
		if (state != LexerState.TAG)
			throw new SmartLexerException("Called nextTokenTag() but wasn't in TAG state");

		while (currentIndex < data.length) {
			char currentChar = data[currentIndex];

			if (Character.isWhitespace(currentChar)) {
				currentIndex++;
				continue;
			}

			if (currentChar == '=') {
				currentIndex++;
				return new Token(TokenType.TAG_NAME, "=");
			}

			if (isOperator(currentChar) && currentChar != '-') {
				currentIndex++;
				return new Token(TokenType.OPERATOR, Character.toString(currentChar));
			}

			if (currentChar == '-') {
				if (currentIndex + 1 < data.length && Character.isDigit(data[currentIndex + 1])) {
					return getNextNumber();
				} else {
					currentIndex++;
					return new Token(TokenType.OPERATOR, Character.toString(currentChar));
				}
			}

			if (Character.isDigit(currentChar)) {
				return getNextNumber();
			}

			if (currentChar == '$') {
				if (currentIndex + 1 >= data.length)
					throw new SmartLexerException("Illegal token in TAG state. It was: $");

				currentIndex++;
				if (data[currentIndex] == '}') {
					currentIndex++;
					return TAG_CLOSE_TOKEN;
				} else {
					throw new SmartLexerException("Illegal token in TAG state.");
				}
			}

			if (Character.isLetter(currentChar)) {
				try {
					return getNextVariableName();
				} catch (SmartLexerException ex) {
					throw new SmartLexerException("Illegal variable name! It was: " + ex.getMessage());
				}
			}

			if (currentChar == '@') {
				try {
					return new Token(TokenType.FUNCTION_NAME, getNextVariableName().getValue());
				} catch (SmartLexerException ex) {
					throw new SmartLexerException("Illegal function name! It was: " + ex.getMessage());
				}
			}

			if (currentChar == '"') {
				return getNextString();
			}

			// illegal tag
			throw new SmartLexerException("Illegal tag in TAG state! It was: " + currentChar);

		}

		return EOF_TOKEN;
	}

	/**
	 * Assumes that current character in data is an opening quotation mark(beggining
	 * of the string). Generates token according to rules of constructing a string.
	 * 
	 * @return token of type STRING
	 */
	private Token getNextString() {
		StringBuilder value = new StringBuilder();
		currentIndex++;

		while (currentIndex < data.length) {
			char currentChar = data[currentIndex];

			if (currentChar == '"') {
				currentIndex++;
				return new Token(TokenType.STRING, value.toString());
			}

			if (currentChar == '\\') {
				if (currentIndex + 1 >= data.length)
					throw new SmartLexerException("Escape character \\ can't be last character!");

				currentChar = data[++currentIndex];
				if (currentChar == 'n') {
					value.append((char) 10);
				} else if (currentChar == 'r') {
					value.append((char) 13);
				} else if (currentChar == 't') {
					value.append((char) 9);
				} else if (currentChar == '\\' || currentChar == '"') {
					value.append(currentChar);
				} else {
					throw new SmartLexerException("Tried to escape illegal character: " + currentChar);
				}
				currentIndex++;
				continue;
			}

			value.append(currentChar);
			currentIndex++;
		}

		throw new SmartLexerException("Unclosed string!");
	}

	/**
	 * Generates variable name token from the current character in data.
	 * 
	 * @return token of type VARIABLE_NAME
	 */
	private Token getNextVariableName() {
		StringBuilder value = new StringBuilder();
		value.append(data[currentIndex++]);
		boolean isIllegal = false;

		while (currentIndex < data.length) {
			char currentChar = data[currentIndex];

			if (Character.isWhitespace(currentChar) || isOperator(currentChar) || currentChar == '$')
				break;

			if (!Character.isDigit(currentChar) && !Character.isLetter(currentChar) && currentChar != '_') {
				isIllegal = true;
			}

			value.append(currentChar);
			currentIndex++;
		}

		if (isIllegal)
			throw new SmartLexerException(value.toString());

		return new Token(TokenType.VARIABLE_NAME, value.toString());
	}

	/**
	 * Generates a number from the current character in data.
	 * 
	 * @return token of type INTEGER or FLOAT
	 */
	private Token getNextNumber() {
		StringBuilder value = new StringBuilder();
		boolean isDecimal = false;

		if (data[currentIndex] == '-') {
			currentIndex++;
			value.append("-");
		}

		while (currentIndex < data.length) {
			char currentChar = data[currentIndex];

			if (currentChar == '.') {
				isDecimal = true;
			}

			if (Character.isWhitespace(currentChar) || isOperator(currentChar) || currentChar == '$')
				break;

			value.append(currentChar);
			currentIndex++;
		}

		try {
			if (isDecimal) {
				return new Token(TokenType.FLOAT, Double.parseDouble(value.toString()));
			} else {
				return new Token(TokenType.INTEGER, Integer.parseInt(value.toString()));
			}
		} catch (NumberFormatException ex) {
			throw new SmartLexerException("Illegal token. It was: " + value.toString());
		}

	}

	/**
	 * Checks whether the given character is an operator: +, -, *, /, ^.
	 * 
	 * @param c
	 *            character to be checked
	 * @return
	 */
	private boolean isOperator(char c) {
		if (c == '+' || c == '*' || c == '-' || c == '/' || c == '^')
			return true;

		return false;
	}

	/**
	 * @return current token (not next)
	 */
	public Token getCurrentToken() {
		if (token == null) {
			nextToken();
		}

		return token;
	}

	/**
	 * Sets the state of the lexer.
	 * 
	 * @param state
	 *            state to be set
	 */
	public void setState(LexerState state) {
		if (state == null)
			throw new IllegalArgumentException("State can't be set to null!");

		this.state = state;
	}

	/**
	 * @return current state of the lexer
	 */
	public LexerState getState() {
		return state;
	}
}
