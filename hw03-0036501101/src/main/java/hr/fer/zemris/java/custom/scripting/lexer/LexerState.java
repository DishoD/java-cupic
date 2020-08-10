package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Defines an SmartScriptLexer state.
 * 
 * @author Hrvoje Ditrih
 *
 */
public enum LexerState {
	/**
	 * Reads everything as TEXT token until "{$" TAG_OPEN.
	 * Espacing with \ is only permitted in this two cases:
	 * \{ -> { (avoids entering a tag), and \\ -> \.
	 * Should throw exception otherwise.
	 */
	TEXT, 
	
	/**
	 * reads TAG_NAME, INTEGERs, FLOATs, STRINGs, FUNCTION_NAMEs, OPERATORs
	 * until TAG_CLOSE "$}".
	 * 
	 * Escaping withing straing is possible (strings star and end with quotation marks):
	 * \\ -> \, \" -> " (avoids closing a string),
	 * \n -> ASCII 10, \r -> ASCII 13, \t -> ASCII 9
	 */
	TAG
}
