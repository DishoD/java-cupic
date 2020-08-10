package hr.fer.zemris.java.tecaj.hw05.db.QueryParser;

/**
 * Contains query statements token types.
 * 
 * @author Hrvoje Ditrih
 *
 */
public enum TokenType {
	/**
	 * can only be these strings: jmbag, firstName, lastName
	 */
	FIELD,
	
	/**
	 * can only be: <, <=, >, >=, =, !=m LIKE
	 */
	OPERATOR,
	
	/**
	 * word AND (not case sensitive)
	 */
	AND,
	
	/**
	 * anything in quotation marks
	 */
	STRING_LITERAL,
	
	/**
	 * end of file
	 */
	EOF
}
