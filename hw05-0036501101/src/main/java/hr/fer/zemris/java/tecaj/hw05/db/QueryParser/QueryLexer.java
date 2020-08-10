package hr.fer.zemris.java.tecaj.hw05.db.QueryParser;

import hr.fer.zemris.java.tecaj.hw05.db.ComparisonOperators.ComparisonOperators;
import hr.fer.zemris.java.tecaj.hw05.db.FieldValueGetters.FieldValueGetters;

/**
 * Simple lexer for query statements.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class QueryLexer {
	/**
	 * text to be analyzed by the lexer
	 */
	private char[] text;
	/**
	 * current token
	 */
	private Token currentToken;
	/**
	 * index of the first uninterpreted character in text
	 */
	private int currentIndex;
	
	/**
	 * EOF token
	 */
	private static Token EOF_TOKEN = new Token(TokenType.EOF, null);
	
	/**
	 * Initializes the lexer with the given text.
	 * 
	 * @param text text to be analyzed
	 */
	public QueryLexer(String text) {
		this.text = text.toCharArray();
	}
	
	/**
	 * Returns next token if it exists.
	 * 
	 * @return next token
	 * @throws LexerException if no more tokens left
	 */
	public Token nextToken() {
		if(currentToken != null && currentToken.getType() == TokenType.EOF)
			throw new LexerException("No more tokens");
		
		StringBuilder sb = new StringBuilder();
		
		while(currentIndex < text.length) {
			char currentChar = text[currentIndex++];
			
			if(sb.length() == 0 && Character.isWhitespace(currentChar)) continue;
			
			if(sb.length() != 0 && Character.isWhitespace(currentChar))
				throw new LexerException("Illegal token: " + sb.toString());
			
			sb.append(currentChar);
			
			Token token = tokenize(sb.toString());
			
			if(token != null) {
				currentToken = token;
				return token;
			}
		}
		
		if(sb.length() == 0) {
			currentToken = EOF_TOKEN;
			return EOF_TOKEN;
		}
		
		throw new LexerException("Illegal token: " + sb.toString());
	}
	
	/**
	 * Tries to tokenize the string.
	 * 
	 * @param string string to tokenize
	 * @return token if it can tokenize the string, null otherwise
	 */
	private Token tokenize(String string) {
			switch(string) {
			case "<":
				if(text[currentIndex] == '=') {
					currentIndex++;
					return new Token(TokenType.OPERATOR, ComparisonOperators.LESS_OR_EQUALS);
				}
				return new Token(TokenType.OPERATOR, ComparisonOperators.LESS);
			case ">":
				if(text[currentIndex] == '=') {
					currentIndex++;
					return new Token(TokenType.OPERATOR, ComparisonOperators.GREATER_OR_EQUALS);
				}
				return new Token(TokenType.OPERATOR, ComparisonOperators.GREATER);
			case "=":
				return new Token(TokenType.OPERATOR, ComparisonOperators.EQUALS);
			case "!=":
				return new Token(TokenType.OPERATOR, ComparisonOperators.NOT_EQUALS);
			case "LIKE":
				return new Token(TokenType.OPERATOR, ComparisonOperators.LIKE);
			case "jmbag":
				return new Token(TokenType.FIELD, FieldValueGetters.JMBAG);
			case "lastName":
				return new Token(TokenType.FIELD, FieldValueGetters.LAST_NAME);
			case "firstName":
				return new Token(TokenType.FIELD, FieldValueGetters.FIRST_NAME);
			case "\"":
				StringBuilder sb = new StringBuilder();
				
				while(currentIndex < text.length) {
					char currentChar = text[currentIndex++];
					
					if(currentChar == '"') {
						return new Token(TokenType.STRING_LITERAL, sb.toString());
					}
					
					sb.append(currentChar);
				}
				
				throw new LexerException("String literal not closed! It was: " + sb.toString());
		}
			
		if(string.toLowerCase().equals("and"))
			return new Token(TokenType.AND, null);
		
		return null;
	}
	
	/**
	 * Returns current token.
	 * 
	 * @return current token or null if nextToken() method has never been called
	 */
	public Token getCurrentToken() {
		return currentToken;
	}
}
