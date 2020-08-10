package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * Implementation of a calculator model.
 * 
 * @author Disho
 *
 */
public class CalcModelImpl implements CalcModel {
	/**
	 * Current value in calculator
	 */
	private String value = null;
	/**
	 * represents a sign of a current value in calculator: 1 -> positive, -1 -> negative
	 */
	private short sign = 1;
	/**
	 * tells if the decimal point is present in the current value
	 */
	private boolean isDecimalePointPresent;
	/**
	 * active operand
	 */
	private double activeOperand;
	/**
	 * tells if active operand is set
	 */
	private boolean isActiveOperandSet;
	/**
	 * pending binary operator or null if it is not set
	 */
	private DoubleBinaryOperator operator;
	
	/**
	 * listeners to the model for when the current value has changed
	 */
	private List<CalcValueListener> listeners = new ArrayList<>();
	
	/**
	 * binary operator: addition
	 */
	public static DoubleBinaryOperator ADDITION = (v1, v2) -> v1 + v2;
	/**
	 * binary operator: subtraction
	 */
	public static DoubleBinaryOperator SUBTRACTION = (v1, v2) -> v1 - v2;
	/**
	 * binary operator: multiplication
	 */
	public static DoubleBinaryOperator MULTIPLICATION = (v1, v2) -> v1 * v2;
	/**
	 * binary operator: division
	 */
	public static DoubleBinaryOperator DIVISION = (v1, v2) -> v1 / v2;
	
	/**
	 * maximum number of digits in current value
	 */
	private static int MAX_NUMBER_OF_DIGITS = 308;
	

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}

	@Override
	public double getValue() {
		if(value == null) return 0;
		
		return sign*Double.parseDouble(value);
	}

	@Override
	public void setValue(double value) {
		if(value < 0) {
			sign = -1;
			value *= -1;
		} else {
			sign = 1;
		}
		
		this.value = Double.toString(value);
		isDecimalePointPresent = true;
		
		notifyListeners();
	}

	@Override
	public void clear() {
		sign = 1;
		value = null;
		isDecimalePointPresent = false;
		
		notifyListeners();
	}

	@Override
	public void clearAll() {
		sign = 1;
		value = null;
		isDecimalePointPresent = false;
		isActiveOperandSet = false;
		operator = null;
		
		notifyListeners();
	}

	@Override
	public void swapSign() {
		sign *= -1;
		
		notifyListeners();
		
	}

	@Override
	public void insertDecimalPoint() {
		if(value != null && value.length() >= MAX_NUMBER_OF_DIGITS) return;
		
		if(isDecimalePointPresent) return;
		
		if(value == null || value.isEmpty()) {
			value = "0.";
		} else {
			value += ".";
		}
		
		isDecimalePointPresent = true;
		notifyListeners();
	}

	@Override
	public void insertDigit(int digit) {
		if(value != null && value.length() >= MAX_NUMBER_OF_DIGITS) return;
		
		if(digit < 0 || digit > 9) throw new IllegalArgumentException("digit must be in interval [0, 9]. It was: " + digit);
		
		if(value == null) {
			value = Integer.toString(digit);
			notifyListeners();
			return;
		}
		
		if(!isDecimalePointPresent && value.startsWith("0")) {
			value = Integer.toString(digit);
			notifyListeners();
			return;
		}
		
		value += Integer.toString(digit);
		
		notifyListeners();
	}

	@Override
	public boolean isActiveOperandSet() {
		return isActiveOperandSet;
	}

	@Override
	public double getActiveOperand() {
		if(!isActiveOperandSet) throw new IllegalStateException("Active operand is not set.");
		
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		isActiveOperandSet = true;
		this.activeOperand = activeOperand;
	}

	@Override
	public void clearActiveOperand() {
		isActiveOperandSet = false;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return operator;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		operator = op;
	}
	
	@Override
	public String toString() {
		if(value == null) return "0";
		
		String prefix;
		prefix = sign == -1 ? "-" : "";
		
		return prefix + value;
	}
	
	/**
	 * Notifies the registered listeners of the change in current value.
	 */
	private void notifyListeners() {
		listeners.forEach(l -> l.valueChanged(this));
	}

}
