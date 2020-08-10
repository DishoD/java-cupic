package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.gui.layouts.CalcLayout;

/**
 * Simple gui calculator.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class Calculator extends JFrame {
	/**
	 * window title
	 */
	private static final String TITLE = "Simple Calculator";
	/**
	 * calculator model
	 */
	private CalcModel model = new CalcModelImpl();
	
	/**
	 * stack for calculator stack functionality
	 */
	Stack<Double> stack = new Stack<>();
	
	/**
	 * Label for showing current value in calculator.
	 * 
	 * @author Disho
	 *
	 */
	private static class CalculatorLabel extends JLabel implements CalcValueListener {
		
		/**
		 * Initializes the label and registers its self to the calculator model.
		 * 
		 * @param model calculator model to register to
		 */
		public CalculatorLabel(CalcModel model) {
			model.addCalcValueListener(this);
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			setHorizontalAlignment(RIGHT);
			setOpaque(true);
			setBackground(Color.lightGray);
		}

		@Override
		public void valueChanged(CalcModel model) {
			setText(model.toString());
		}
		
	}
	
	/**
	 * Initializes the calculator.
	 */
	public Calculator() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(TITLE);
		
		initGui();
	}
	
	/**
	 * Initializes all the buttons in the calculator.
	 */
	private void initGui() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(5));
		
		cp.add(new CalculatorLabel(model), "1,1");
		
		//equals button
		cp.add(createButtonWithAction("=", e -> {
			if(model.isActiveOperandSet()) {
				if(model.getPendingBinaryOperation() == CalcModelImpl.DIVISION && model.getValue() == 0) {
					JOptionPane.showMessageDialog(this, "Can't divide by zero!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				model.setValue(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
				model.clearActiveOperand();
			}
		}), "1,6");
		
		//clr button
		cp.add(createButtonWithAction("clr", e -> {
			model.clear();
		}), "1,7");
		
		//res button
		cp.add(createButtonWithAction("res", e -> {
			model.clearAll();
		}), "2,7");
		
		//1/x button
		cp.add(createButtonWithAction("1/x", e -> {
			double val  = model.getValue();
			if(Math.abs(val) < 1e-10) {
				JOptionPane.showMessageDialog(this, "Can't divide by zero!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			model.setValue(1/model.getValue());
		}), "2,1");
		
		JCheckBox cb = new JCheckBox("Inv");
		cp.add(cb, "5,7");
		
		
		cp.add(createSwitchableButton("sin", "arcsin", model, cb, m -> m.setValue(Math.sin(m.getValue())), m -> m.setValue(Math.asin(m.getValue()))), "2,2");
		cp.add(createSwitchableButton("log", "10^x", model, cb, m -> m.setValue(Math.log10(m.getValue())), m -> m.setValue(Math.pow(10, m.getValue()))), "3,1");
		cp.add(createSwitchableButton("cos", "arccos", model, cb, m -> m.setValue(Math.cos(m.getValue())), m -> m.setValue(Math.acos(m.getValue()))), "3,2");
		cp.add(createSwitchableButton("ln", "e^x", model, cb, m -> m.setValue(Math.log(m.getValue())), m -> m.setValue(Math.exp(m.getValue()))), "4,1");
		cp.add(createSwitchableButton("tan", "arctan", model, cb, m -> m.setValue(Math.tan(m.getValue())), m -> m.setValue(Math.atan(m.getValue()))), "4,2");
		cp.add(createSwitchableButton("x^n", "logn(x)", model, cb, m -> {
			if(model.isActiveOperandSet()) {
				model.setActiveOperand(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
			} else {
				model.setActiveOperand(model.getValue());
			}
			
			model.setPendingBinaryOperation((v1,v2) -> Math.pow(v1, v2));
			model.clear();
		}, m -> {
			if(model.isActiveOperandSet()) {
				model.setActiveOperand(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
			} else {
				model.setActiveOperand(model.getValue());
			}
			
			model.setPendingBinaryOperation((v1,v2) -> Math.log(v1)/Math.log(v2));
			model.clear();
		}), "5,1");
		cp.add(createSwitchableButton("ctg", "arcctg", model, cb, m -> m.setValue(1/(Math.tan(m.getValue()))), m -> m.setValue(1/(Math.atan(m.getValue())))), "5,2");
		
		cp.add(createDigitButton(7, model), "2,3"); cp.add(createDigitButton(8, model), "2,4"); cp.add(createDigitButton(9, model), "2,5");
		cp.add(createDigitButton(4, model), "3,3"); cp.add(createDigitButton(5, model), "3,4"); cp.add(createDigitButton(6, model), "3,5");
		cp.add(createDigitButton(1, model), "4,3"); cp.add(createDigitButton(2, model), "4,4"); cp.add(createDigitButton(3, model), "4,5");
		cp.add(createDigitButton(0, model), "5,3"); 
		
		cp.add(createButtonWithAction("+/-", e -> model.swapSign()), "5,4");
		cp.add(createButtonWithAction(".", e -> model.insertDecimalPoint()), "5,5");
		
		cp.add(createOperatorButton("/", model, CalcModelImpl.DIVISION), "2,6");
		cp.add(createOperatorButton("*", model, CalcModelImpl.MULTIPLICATION), "3,6");
		cp.add(createOperatorButton("-", model, CalcModelImpl.SUBTRACTION), "4,6");
		cp.add(createOperatorButton("+", model, CalcModelImpl.ADDITION), "5,6");
		
		cp.add(createButtonWithAction("push", e -> {
			if(model.isActiveOperandSet()) {
				if(model.getPendingBinaryOperation() == CalcModelImpl.DIVISION && model.getValue() == 0) {
					JOptionPane.showMessageDialog(this, "Can't divide by zero!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				stack.push(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
			} else {
				stack.push(model.getValue());
			}
			
			model.clear();
		}), "3,7");
		
		cp.add(createButtonWithAction("pop", e -> {
			if(stack.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Stack is empty!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			model.setValue(stack.pop());
		}), "4,7");
		
		setSize(cp.getPreferredSize());
	}

	/**
	 * Crates an button with the given text and performs an given action when clicked.
	 * 
	 * @param str button text
	 * @param l on action perform this
	 * @return a button
	 */
	private static JButton createButtonWithAction(String str, ActionListener l) {
		JButton btn = new JButton(str);
		btn.setPreferredSize(new Dimension(80, 20));
		
		btn.addActionListener(l);
		return btn;
	}
	
	/**
	 * Creates an button that produces an new digit in current calculator number.
	 * 
	 * @param digit what digit will button be
	 * @param model for which calculator model
	 * @return a button
	 */
	private static JButton createDigitButton(int digit, CalcModel model) {
		if(digit < 0 || digit > 9) throw new IllegalArgumentException("Digit must be in [0, 9]. It was: " + digit);
		
		JButton btn = new JButton(Integer.toString(digit));
		btn.setPreferredSize(new Dimension(50, 50));
		
		btn.addActionListener(e -> model.insertDigit(digit));
		return btn;
	}
	
	/**
	 * Creates an button that performs binary operations on calculator numbers.
	 * 
	 * @param str button text
	 * @param model calculator model
	 * @param operator binary operator
	 * @return a button
	 */
	private static JButton createOperatorButton(String str, CalcModel model, DoubleBinaryOperator operator) {
		JButton btn = new JButton(str);
		
		btn.addActionListener(e -> {
			if(model.isActiveOperandSet()) {
				if(operator == CalcModelImpl.DIVISION && model.getValue() == 0) {
					JOptionPane.showMessageDialog(btn, "Can't divide by zero!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				model.setActiveOperand(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
			} else {
				model.setActiveOperand(model.getValue());
			}
			
			model.setPendingBinaryOperation(operator);
			model.clear();
		});
		
		return btn;
	}
	
	/**
	 * Creates an button that switches its text and action based on the fact if the given check box is selected or not.
	 * 
	 * @param str1 text for deselected check box
	 * @param str2 text for selected check box
	 * @param model calculator model
	 * @param cb binds its text and actions to this check box
	 * @param m1 action for deselected check box
	 * @param m2 action for selected check box
	 * @return a button
	 */
	private static JButton createSwitchableButton(String str1, String str2, CalcModel model, JCheckBox cb, Consumer<CalcModel> m1, Consumer<CalcModel> m2) {
		JButton btn = new JButton(str1);
		btn.setPreferredSize(new Dimension(75, 75));
		
		cb.addActionListener(e -> {
			if(cb.isSelected()) {
				btn.setText(str2);
			} else {
				btn.setText(str1);
			}
		});
		
		btn.addActionListener(e -> {
			if(cb.isSelected()) {
				m2.accept(model);
			} else {
				m1.accept(model);
			}
		});
		
		return btn;
	}
	
	/**
	 * Main method. Starts the program
	 * 
	 * @param args ignorable
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() ->{
			new Calculator().setVisible(true);
		});
	}
}
