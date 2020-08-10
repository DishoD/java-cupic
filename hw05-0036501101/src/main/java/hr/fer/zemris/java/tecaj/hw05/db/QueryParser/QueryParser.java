package hr.fer.zemris.java.tecaj.hw05.db.QueryParser;

import java.util.ArrayList;
import hr.fer.zemris.java.tecaj.hw05.db.IComparisonOperator.IComparisonOperator;
import hr.fer.zemris.java.tecaj.hw05.db.ComparisonOperators.ComparisonOperators;
import hr.fer.zemris.java.tecaj.hw05.db.IFieldValueGetter.IFieldValueGetter;
import hr.fer.zemris.java.tecaj.hw05.db.FieldValueGetters.FieldValueGetters;
import hr.fer.zemris.java.tecaj.hw05.db.ConditionalExpression.ConditionalExpression;
import java.util.List;

/**
 * Represents a query statement. Query can be direct: meaning it only consists
 * of one conditional expression which only compares equality of one jmbag
 * field. And every other query statement connected with AND boolean operator.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class QueryParser {
	/**
	 * is query direct
	 */
	private boolean isQueryDirect;
	/**
	 * if query is directs, holds the queried jmbag
	 */
	private String queriedJmbag;
	/**
	 * Contains conditional expressions of a query
	 */
	List<ConditionalExpression> expressions = new ArrayList<>();
	
	/**
	 * Query lexer
	 */
	private QueryLexer lexer;
	
	/**
	 * Initializes the parser with the given query text.
	 * 
	 * @param query query statement
	 * @throws ParserException if illegal statement is given
	 */
	public QueryParser(String query) {
		lexer = new QueryLexer(query);
		
		Token token;
		while(true) {
			token = nextToken();
			
			if(token.getType() != TokenType.FIELD)
				throw new ParserException("Illegal query");
			
			IFieldValueGetter fieldGetter = (IFieldValueGetter)token.getValue();
			
			token = nextToken();
			
			if(token.getType() != TokenType.OPERATOR)
				throw new ParserException("Illegal query");
			
			IComparisonOperator operator = (IComparisonOperator)token.getValue();
			
			token = nextToken();
			
			if(token.getType() != TokenType.STRING_LITERAL)
				throw new ParserException("Illegal query");
			
			String stringLiteral = (String)token.getValue();
			
			if(operator == ComparisonOperators.LIKE) {
				if(!isValidLikeStringLiteral(stringLiteral))
					throw new ParserException("Illegal string literal for LIKE operator: " + stringLiteral);
			}
			
			expressions.add(new ConditionalExpression(fieldGetter, stringLiteral, operator));
			
			token = nextToken();
			
			if(token.getType() == TokenType.EOF) break;
			
			if(token.getType() != TokenType.AND) 
				throw new ParserException("Illegal query");
		}
		
		if(expressions.size() == 1) {
			ConditionalExpression exp = expressions.get(0);
			
			if(exp.getFieldValueGetter() == FieldValueGetters.JMBAG && exp.getComparisonOperator() == ComparisonOperators.EQUALS) {
				isQueryDirect = true;
				queriedJmbag = exp.getStringLiteral();
			}
		}
	}
	
	private boolean isValidLikeStringLiteral(String stringLiteral) {
		int astrixNumber = 0;
		
		for(char c : stringLiteral.toCharArray()) {
			if(c == '*')
				astrixNumber++;
			
			if(astrixNumber > 1) return false;
		}
		
		return true;
	}

	/**
	 * Returns next token of a lexer.
	 * 
	 * @return
	 */
	private Token nextToken() {
		try {
			return lexer.nextToken();
		} catch (LexerException ex) {
			throw new ParserException(ex.getMessage());
		}
	}
	
	/**
	 * Returns if the query is direct.
	 * @return true if query is direct, false otherwise
	 */
	public boolean isQueryDirect() {
		return isQueryDirect;
	}
	
	/**
	 * If query is direct returns queried jmbag.
	 * 
	 * @return directly queried jmbag
	 * @throws IllegalStateException if query isn't direct
	 */
	public String getQueriedJmbag() {
		if(queriedJmbag == null)
			throw new IllegalStateException();
		
		return queriedJmbag;
	}
	
	/**
	 * Returns a list of conditional expressions of a query.
	 * 
	 * @return list of conditional expressions
	 */
	public List<ConditionalExpression> getQuery() {
		return expressions;
	}
}
