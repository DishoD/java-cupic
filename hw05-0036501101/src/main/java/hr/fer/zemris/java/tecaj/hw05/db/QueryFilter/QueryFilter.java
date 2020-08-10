package hr.fer.zemris.java.tecaj.hw05.db.QueryFilter;

import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;
import hr.fer.zemris.java.tecaj.hw05.db.ConditionalExpression.ConditionalExpression;
import hr.fer.zemris.java.tecaj.hw05.db.IFilter.IFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents non direct query condition filter.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class QueryFilter implements IFilter {

	/**
	 * Contains conditional expressions of a query
	 */
	List<ConditionalExpression> expressions = new ArrayList<>();

	/**
	 * Initializes the filter with the given list of conditional expressions.
	 * 
	 * @param expressions
	 *            list of conditional expressions (can't be empty)
	 */
	public QueryFilter(List<ConditionalExpression> expressions) {
		Objects.requireNonNull(expressions, "Expressions cannot be null");
		if (expressions.isEmpty())
			throw new IllegalArgumentException("Expressions must contain at least one conditional expression.");

		this.expressions = expressions;
	}

	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression exp : expressions) {
			if (!exp.evaluateExpression(record)) return false;
		}

		return true;
	}

}
