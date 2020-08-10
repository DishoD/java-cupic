package hr.fer.zemris.java.tecaj.hw05.db.ConditionalExpression;

import hr.fer.zemris.java.tecaj.hw05.db.IComparisonOperator.IComparisonOperator;
import hr.fer.zemris.java.tecaj.hw05.db.IFieldValueGetter.IFieldValueGetter;
import hr.fer.zemris.java.tecaj.hw05.db.StudentRecord.StudentRecord;

/**
 * Represents one conditional expression of a query. It consists of filed value
 * getter, string literal and comparison operator.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class ConditionalExpression {
	private IFieldValueGetter fieldValueGetter;
	private String stringLiteral;
	private IComparisonOperator comparisonOperator;

	/**
	 * Initializes the conditional expression wit the given parameters.
	 * 
	 * @param fieldValueGetter
	 *            what field should expression compare
	 * @param stringLiteral
	 *            to what it will that field be compared to
	 * @param comparisonOperator
	 *            comparison operator
	 */
	public ConditionalExpression(IFieldValueGetter fieldValueGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {

		this.fieldValueGetter = fieldValueGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Returns what field is an expression comparing
	 * 
	 * @return IFiledValueGetter
	 */
	public IFieldValueGetter getFieldValueGetter() {
		return fieldValueGetter;
	}

	/**
	 * Returns to what it will the expressions field be compared to
	 * 
	 * @return string literal
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * REturns the comparison operator of the expression
	 * 
	 * @return the comparison operator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
	
	/**
	 * Evaluates expression for the given student record.
	 * 
	 * @param record evaluates expression for this record
	 * @return true if record satisfies the conditional expression, false otherwise
	 */
	public boolean evaluateExpression(StudentRecord record) {
		return comparisonOperator.satisfied(fieldValueGetter.get(record), stringLiteral);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comparisonOperator == null) ? 0 : comparisonOperator.hashCode());
		result = prime * result + ((fieldValueGetter == null) ? 0 : fieldValueGetter.hashCode());
		result = prime * result + ((stringLiteral == null) ? 0 : stringLiteral.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConditionalExpression other = (ConditionalExpression) obj;
		if (comparisonOperator == null) {
			if (other.comparisonOperator != null)
				return false;
		} else if (!comparisonOperator.equals(other.comparisonOperator))
			return false;
		if (fieldValueGetter == null) {
			if (other.fieldValueGetter != null)
				return false;
		} else if (!fieldValueGetter.equals(other.fieldValueGetter))
			return false;
		if (stringLiteral == null) {
			if (other.stringLiteral != null)
				return false;
		} else if (!stringLiteral.equals(other.stringLiteral))
			return false;
		return true;
	}
	
	
}
