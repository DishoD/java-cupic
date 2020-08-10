package hr.fer.zemris.java.tecaj.hw05.db.QueryParser;

import java.util.Objects;

/**
 * Represents one token of a query statement.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class Token {
	/**
	 * token type
	 */
	private TokenType type;
	/**
	 * token value
	 */
	private Object value;

	/**
	 * Initializes the token with the given parameters.
	 * 
	 * @param type
	 *            token type (can't be null)
	 * @param value
	 *            token value (can be null)
	 */
	public Token(TokenType type, Object value) {
		Objects.requireNonNull(type, "Type cannot be null");

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
