package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration of token types for a smart script lexer.
 * 
 * @author Hrvoje Ditrih
 *
 */
public enum TokenType {
	/**
	 * end of file
	 */
	EOF,

	/**
	 * everything outside tags
	 */
	TEXT,

	/**
	 * {$ - represents openig of the tag
	 */
	TAG_OPEN,

	/**
	 * $} - represents end of a tag
	 */
	TAG_CLOSE,

	/**
	 * starts by letter and after follows zero or more letters, digits or
	 * underscores
	 */
	VARIABLE_NAME,

	/**
	 * inside tags and inside quotation marks ""
	 */
	STRING,

	/**
	 * starts with @ after which follows a letter and after than can follow zero or
	 * more letters, digits or underscores
	 */
	FUNCTION_NAME,

	/**
	 * integer number, can be posotive or negative
	 */
	INTEGER,

	/**
	 * decimal number, can be positive or negative
	 */
	FLOAT,

	/**
	 * + (plus), - (minus), * (multiplication), / (division), ^ (power)
	 */
	OPERATOR,

	/**
	 * can only be "="
	 */
	TAG_NAME
}
