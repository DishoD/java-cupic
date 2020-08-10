package hr.fer.zemris.java.custom.collections.demo;

import java.rmi.UnexpectedException;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * This is a simple command-line program that takes one string argument. Program
 * caluculates an mathematical exspression in an postfix notation. String should
 * consist only of integers, spaces and these operators: +, -, /, *, %. For
 * example: "-1 8 2 / +"
 * 
 * @author Hrvoje Ditrih
 * @version 1.0
 */
public class StackDemo {
	
	/**
	 * The main method. Controls the flow of the program.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Pozdrav! Ovaj program računa neki matematički izraz. "
					+ "U nastavku slijede pravila korištenja programa: ");
			printRules();
			
		} else if (args.length > 1) {
			System.out.println("Predali ste previše argumenata. Ovo su pravila programa:");
			printRules();
			
		} else {
			try {
				int result = evaluateExpression(args[0]);

				System.out.println("Konačni rezultat vašeg izraza je: " + result);
			} catch (UnexpectedException ex) {
				System.out.println(ex.getMessage());
				System.out.println("Prekidam program.\n");
				System.out.println("Molimo pažljivo pročitajte sljedeća pravila:");
				printRules();
			} catch(IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
				System.out.println("Prekidam program.");
			}
		}
	}

	/**
	 * Prints the rules of the program to the standard output.
	 */
	private static void printRules() {
		System.out.println("Program prima jedan znakovni niz(string) preko argumenta naredbenog retka.\n"
				+ "String je matematički izraz u postfix notaciji. String se može sastojati samo od:\n"
				+ "cijelih brojeva, razmaka te matematičkih operatora: +, -, /, *, %(ostatak pri cjelobrnojnom djeljenu).\n"
				+ "Primjer: '-1 8 2 / +'.");
	}

	/**
	 * Evaluates given mathematical expression in postfix notation and calculates the result.
	 * 
	 * @param expression
	 *            mathematical expression in postfix notation
	 * @return result of the mathematical expression
	 * @throws UnexpectedException
	 *             if something goes wrong with the evaluation, proper message will
	 *             be provided for the user
	 * @throws IllegalArgumentException
	 * 				if expression cointains division by zero
	 */
	private static int evaluateExpression(String expression) throws UnexpectedException {
		ObjectStack stack = new ObjectStack();
		String[] symbols = expression.split("\\s+");

		for (String symbol : symbols) {
			if(symbol.isEmpty()) 
				continue;
			
			Operator operator = getOperator(symbol);

			if (operator == Operator.NOP) {
				try {
					int number = Integer.parseInt(symbol);
					stack.push(number);
				} catch (NumberFormatException ex) {
					throw new UnexpectedException(String.format("'%s' se ne može interpretirati niti kao "
							+ "cijeli broj niti kao matematički operator.", symbol));
				}
			} else {
				try {
					int secondOperand = (Integer) stack.pop();
					int firstOperand = (Integer) stack.pop();
					stack.push(calculate(firstOperand, secondOperand, operator));
				} catch (EmptyStackException ex) {
					throw new UnexpectedException(
							"Došlo je do pogreške pri evaluiranju izraza. Provjerite je li vaš izraz u ispravnoj postfix notaciji.");
				} catch(ArithmeticException ex) {
					throw new IllegalArgumentException("Ne mogu dijeliti s nulom!");
				}
			}
		}

		if (stack.size() == 1) {
			return (Integer) stack.pop();
		} else {
			throw new UnexpectedException(
					"Došlo je do pogreške pri evaluiranju izraza. Provjerite je li vaš izraz u ispravnoj postfix notaciji.");
		}
	}

	/**
	 * Calculates an binary mathematical operation with given operator.
	 * 
	 * @param firstOperand
	 *            first operand
	 * @param secondOperand
	 *            secon operand
	 * @param operator
	 *            mathematical operator
	 * @return integer result of the mathematical operation
	 * @throws IllegalArgumentException
	 *             if given unkwnown operator
	 */
	private static int calculate(int firstOperand, int secondOperand, Operator operator) {
		if (operator == Operator.PLUS) {
			return firstOperand + secondOperand;
		}
		if (operator == Operator.MINUS) {
			return firstOperand - secondOperand;
		}
		if (operator == Operator.DIVIDE) {
			return firstOperand / secondOperand;
		}
		if (operator == Operator.MULTIPLY) {
			return firstOperand * secondOperand;
		}
		if (operator == Operator.MODULO) {
			return firstOperand % secondOperand;
		}

		throw new IllegalArgumentException(
				"operator must be +, -, /, *, %. It was: " + operator.getStringRepresentation());
	}

	/**
	 * Returns an enum Operator of given operator in string format.
	 * If given string doesn't represent an operator:
	 * +, -, /, *, %; The methode will return NOP.
	 * 
	 * @param symbol
	 *            mathematical symbol
	 * @return NOP if symbol isn't an operator, valid operator otherwise
	 */
	private static Operator getOperator(String symbol) {
		switch (symbol) {
		case "+":
			return Operator.PLUS;
		case "-":
			return Operator.MINUS;
		case "/":
			return Operator.DIVIDE;
		case "*":
			return Operator.MULTIPLY;
		case "%":
			return Operator.MODULO;
		default:
			return Operator.NOP;
		}
	}
}
