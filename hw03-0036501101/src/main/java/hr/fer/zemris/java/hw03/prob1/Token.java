package hr.fer.zemris.java.hw03.prob1;

/**
 * Represents one token for a lexer.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class Token {
	private TokenType type;
	private Object value;
	
	/**
	 * Initializes the token.
	 * 
	 * @param type token type
	 * @param value token value
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * @return token type
	 */
	public TokenType getType() {
		return type;
	}
	
	/**
	 * @return token value
	 */
	public Object getValue() {
		return value;
	}
}
