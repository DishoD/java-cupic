package hr.fer.zemris.java.hw03.prob1;

/**
 * Represents a simple lexer.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class Lexer {
	/**
	 * Input text data
	 */
	private char[] data;
	
	/**
	 * current token
	 */
	private Token token; 
	
	/**
	 * Current index of unprocessed character
	 * in text data.
	 */
	private int currentIndex;
	
	
	/**
	 * Current symbol in text data.
	 */
	private Symbol currentSymbol;
	
	
	/**
	 * Current lexer state.
	 */
	private LexerState state = LexerState.BASIC;
	
	private static Token EOF_TOKEN = new Token(TokenType.EOF, null);
	
	/**
	 * Represents one symbol of input text data.
	 * Contains it's value as Character, according token type
	 * and if it has been processed.
	 * 
	 * @author Hrvoje Ditrih
	 *
	 */
	private static class Symbol {
		public Character character;
		public TokenType type;
		public boolean isProcessed;
		
		/**
		 * Initializes an unprocessed symbol.
		 * 
		 * @param character character of the symbol
		 * @param type token type of the symbol
		 */
		Symbol(Character character, TokenType type) {
			this.character = character;
			this.type = type;
		}
		
		/**
		 * Marks current symbol as processed.
		 */
		public void markProcessed() {
			isProcessed = true;
		}
	}
	
	/**
	 * Initializes an lexer with a given input text
	 * and BASIC lexer state.
	 * 
	 * @param text input text
	 */
	public Lexer(String text) {
		if(text == null)
			throw new IllegalArgumentException("Text must not be null!");
		
		data = text.replaceAll("\\s+", " ").toCharArray();
	}
	
	/**
	 * Generates and returns next token for current state.
	 * 
	 * @return next token
	 * @throws LexerException if something went wrong
	 */
	public Token nextToken() {
		if(token != null && token.getType() == TokenType.EOF)
			throw new LexerException("No more tokens.");
		
		if(nextSymbol().type == TokenType.EOF) {
			token = EOF_TOKEN;
			return token;
		}
		
		if(state == LexerState.BASIC)
			return nextTokenBasic();
		else
			return nextTokenExtended();
	}
	
	/**
	 * Generates and returns next token for extended state.
	 * 
	 * @return next token
	 * @throws LexerException if something went wrong
	 */
	private Token nextTokenExtended() {
		if(currentSymbol != null && currentSymbol.character.equals('#')) {
			currentSymbol.markProcessed();
			return new Token(TokenType.SYMBOL, '#');
		}
		
		StringBuilder value = new StringBuilder();
		
		do {
			value.append(nextSymbol().character);
			currentSymbol.markProcessed();
		} while( currentIndex < data.length                      &&
				 Character.compare(data[currentIndex], ' ') != 0 &&
				 nextSymbol().character.equals('#') == false
				);
		
		return new Token(TokenType.WORD, value.toString());
	}

	/**
	 * Generates and returns next token for basic state.
	 * 
	 * @return next token
	 * @throws LexerException if something went wrong
	 */
	private Token nextTokenBasic() { 
		TokenType type = nextSymbol().type;
		StringBuilder value = new StringBuilder();
		
		do {
			value.append(currentSymbol.character);
			currentSymbol.markProcessed();
		} while (type != TokenType.SYMBOL                        && 
				 currentIndex < data.length                      &&
				 Character.compare(data[currentIndex], ' ') != 0 &&
				 type == nextSymbol().type
				);
		
		switch(type) {
		case WORD:
			token = new Token(type, value.toString());
			break;
		case NUMBER:
			try {
				token = new Token(type, Long.parseLong(value.toString()));
			} catch (NumberFormatException ex) {
				throw new LexerException("Number to big! Number was: " + value.toString());
			}
			break;
		case SYMBOL:
			token = new Token(type, Character.valueOf(value.charAt(0)));
		default:
			break;
		}
		
		return token;
	}
	
	/**
	 * Determents the next unprocessed symbol of the
	 * input data text. 
	 * 
	 * @return symbol of the next unprocessed character
	 */
	private Symbol nextSymbol() {
		if(currentSymbol != null && currentSymbol.isProcessed == false)
			return currentSymbol;
		
		if(currentSymbol != null && currentSymbol.type == TokenType.EOF)
			return currentSymbol;
		
		while(currentIndex < data.length) {
			Character currentCharacter = data[currentIndex];
			
			if(Character.isWhitespace(currentCharacter)) {
				currentIndex++;
				continue;
			}
			
			if(Character.isLetter(currentCharacter)) {
				currentSymbol = new Symbol(currentCharacter, TokenType.WORD);
				currentIndex++;
				return currentSymbol;
			}
			
			if(Character.isDigit(currentCharacter)) {
				currentSymbol = new Symbol(currentCharacter, TokenType.NUMBER);
				currentIndex++;
				return currentSymbol;
			}
			
			if(state == LexerState.BASIC && currentCharacter.equals('\\')) {
				currentIndex++;
				
				if(currentIndex >= data.length)
					throw new LexerException("Illegal input text. Escape character \\ can't be the last character");
				
				if(Character.isLetter(data[currentIndex]))
					throw new LexerException("Tried to escape an letter. Letter was: " + data[currentIndex]);
				
				currentSymbol = new Symbol(data[currentIndex], TokenType.WORD);
				
				currentIndex++;
				return currentSymbol;
			}
			
			currentIndex++;
			currentSymbol = new Symbol(currentCharacter, TokenType.SYMBOL);
			return currentSymbol;
		}
		
		currentSymbol = new Symbol(null, TokenType.EOF);
		return currentSymbol;
	}
	
	
	/**
	 * Returns a current token. Can be called multiple times,
	 * doesn't call nextToken() method.
	 * 
	 * @return current token
	 */
	public Token getToken() {
		//generates current token if nextToken() has never been called.
		if(token == null) {
			nextToken();
		}
		
		return token;
	}
	
	/**
	 * Sets lexer state.
	 * 
	 * @param state lexer state
	 */
	public void setState(LexerState state) {
		if(state == null)
			throw new IllegalArgumentException("State must not be null!");
		this.state = state;
	}

}
