package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;

/**
 * Defines a simple gui calculator model.
 * 
 * @author Disho
 *
 */
public interface CalcModel {
	/**
	 * Adds a listener to the model.
	 * 
	 * @param l
	 *            listener to ad
	 */
	void addCalcValueListener(CalcValueListener l);

	/**
	 * Removes a listener from the model.
	 * 
	 * @param l
	 *            listener to remove
	 */
	void removeCalcValueListener(CalcValueListener l);

	/**
	 * Returns a string representation of the current value in the calculator.
	 * 
	 * @return string representation of the current value
	 */
	String toString();

	/**
	 * Returns a current value in calculator.
	 * 
	 * @return current value in calculator
	 */
	double getValue();

	/**
	 * Sets the current value of the calculator to the given one.
	 * 
	 * @param value
	 *            value to set
	 */
	void setValue(double value);

	/**
	 * Clears the current value in the calculator (sets it to 0).
	 */
	void clear();

	/**
	 * Clears the current value (sets it to 0), clears the active operand and clears
	 * the pending binary operator.
	 */
	void clearAll();

	/**
	 * Swaps the sign of the current value in the calculator.
	 */
	void swapSign();

	/**
	 * Inserts a decimal point into the current number if one already doesn't exist.
	 */
	void insertDecimalPoint();

	/**
	 * Inserts a digit into the current number at the right most position.
	 * 
	 * @param digit
	 *            new digit to be inserted
	 */
	void insertDigit(int digit);

	/**
	 * Checks if the active operand is set.
	 * 
	 * @return true if active operand is set, false otherwise
	 */
	boolean isActiveOperandSet();

	/**
	 * Returns the active operand if it is set.
	 * 
	 * @return active operand
	 * @throws IllegalStateException
	 *             if active operand isn't set
	 */
	double getActiveOperand();

	/**
	 * Sets the active operand.
	 * 
	 * @param activeOperand
	 *            a value to set as an active operand
	 */
	void setActiveOperand(double activeOperand);

	/**
	 * Clears the active operand.
	 */
	void clearActiveOperand();

	/**
	 * Returns pending binary operation.
	 * 
	 * @return pending binary operation or null if it is not set
	 */
	DoubleBinaryOperator getPendingBinaryOperation();

	/**
	 * Sets pending binary operation.
	 * 
	 * @param op
	 *            binary operator to set
	 */
	void setPendingBinaryOperation(DoubleBinaryOperator op);
}
