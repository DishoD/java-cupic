package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

/**
 * Represents L-system fractals builder. Settings that weren't set through the
 * public interface of this class will have their default values: Unit length of
 * 1, origin at the (0, 0), angle of 0, empty axiom and default unit length
 * degree scaler will be 1.
 * 
 * @author Hrvoje Ditrih
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {
	/**
	 * Stores associated commands with associated actions.
	 */
	private Dictionary commands = new Dictionary();
	/**
	 * Stores associated symbols with associated productions.
	 */
	private Dictionary productions = new Dictionary();

	/**
	 * Unit length for a draw call.
	 */
	private double unitLength = DEFAULT_UNIT_LENGTH;
	/**
	 * Starting position of the turtle.
	 */
	private Vector2D origin = DEFAULT_ORIGIN;
	/**
	 * Starting direction angle of the turtle.
	 */
	private double angle = DEFAULT_ANGLE;
	/**
	 * Axiom of the system.
	 */
	private String axiom = DEFAULT_AXIOM;
	/**
	 * Coefficient that determines how will unit length change with different levels
	 */
	private double unitLengthDegreeScaler = DEFAULT_UNIT_LENGTH_DEGREE_SCALER;

	/**
	 * Default unit length.
	 */
	private static final double DEFAULT_UNIT_LENGTH = 0.1;
	/**
	 * Default unit length degree scaler.
	 */
	private static final double DEFAULT_UNIT_LENGTH_DEGREE_SCALER = 1;
	/**
	 * Default starting position of the turtle.
	 */
	private static final Vector2D DEFAULT_ORIGIN = new Vector2D(1, 0);
	/**
	 * Default direction angle of the turtle.
	 */
	private static final double DEFAULT_ANGLE = 0;
	/**
	 * Default axiom.
	 */
	private static final String DEFAULT_AXIOM = "";

	/**
	 * Represents L-system fractal that can be drawn on screen and can generate
	 * string representing some level of the fractal.
	 * 
	 * @author Hrvoje Ditrih
	 *
	 */
	private class LSystemImpl implements LSystem {

		/**
		 * Draws the fractal of the given level on the screen.
		 * 
		 * @param level
		 *            level of the fractal
		 * @param painter
		 *            screen to be drawn to
		 */
		@Override
		public void draw(int level, Painter painter) {
			String actions = generate(level);

			Context context = new Context();

			TurtleState startState = new TurtleState(origin, Vector2D.unitVector(angle), Color.BLACK,
					Math.pow(unitLengthDegreeScaler, level) * unitLength);

			context.pushState(startState);

			for (char symbol : actions.toCharArray()) {
				Command command = (Command) commands.get(symbol);

				if (command == null)
					continue;

				command.execute(context, painter);
			}
		}

		/**
		 * Generates the string representing the given level of the fractal.
		 * 
		 * @param level
		 *            level of the fractal
		 */
		@Override
		public String generate(int level) {
			if (level < 0)
				throw new IllegalArgumentException("Level must be >= 0!");

			String result = axiom;

			for (int i = 0; i < level; ++i) {
				StringBuilder sb = new StringBuilder();

				for (char symbol : result.toCharArray()) {
					String production = (String) productions.get(symbol);

					if (production == null) {
						sb.append(symbol);
					} else {
						sb.append(production);
					}
				}

				result = sb.toString();
			}

			return result;
		}

	}

	/**
	 * Builds L-system fractal with the builder settings that can be drawn on the
	 * screen.
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	/**
	 * Configures the builder according to the given directives. If some settings
	 * wern't set, the default values will be used. If some settings were set more
	 * than once, last configuration of the setting will be used.
	 * 
	 * @param lines
	 *            list of directives
	 */
	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		for (String line : lines) {
			if (line.isEmpty())
				continue;

			// to make parsing easier
			line = line.replace("/", " / ");

			try (Scanner sc = new Scanner(line)) {
				// so that it reads numbers with the decimal dot (.)
				sc.useLocale(Locale.ENGLISH);

				String directive = sc.next();

				switch (directive) {
				case "origin":
					setOrigin(sc.nextDouble(), sc.nextDouble());
					break;
				case "angle":
					setAngle(sc.nextDouble());
					break;
				case "unitLength":
					setUnitLength(sc.nextDouble());
					break;
				case "unitLengthDegreeScaler":
					double numerator = sc.nextDouble();
					double denominator = 1;
					if (sc.hasNext()) {
						String input = sc.next();
						if (input.equals("/")) {
							denominator = sc.nextDouble();
						} else {
							if (input.startsWith("/")) {
								input = input.substring(1);
								input.replaceAll("\\s", "");
								denominator = Double.parseDouble(input);
							} else {
								throw new IllegalArgumentException();
							}
						}
					}

					setUnitLengthDegreeScaler(numerator / denominator);
					break;

				case "command":
					String symbolC = sc.next();
					if(symbolC.length() > 1) throw new IllegalArgumentException();
					registerCommand(symbolC.charAt(0), sc.nextLine());
					break;
				case "axiom":
					setAxiom(sc.nextLine());
					break;
				case "production":
					String symbolP = sc.next();
					if(symbolP.length() > 1) throw new IllegalArgumentException();
					registerProduction(symbolP.charAt(0), sc.nextLine());
					break;
				default:
					throw new IllegalArgumentException();
				}

				if (sc.hasNext())
					throw new IllegalArgumentException();

			} catch (NoSuchElementException | IllegalArgumentException ex) {
				throw new IllegalArgumentException("Illegal directive. It was: " + line);
			}

		}
		return this;
	}

	/**
	 * Registers the given action for the given symbol.
	 * 
	 * @param symbol
	 *            command symbol
	 * @param action
	 *            action of the command
	 */
	@Override
	public LSystemBuilder registerCommand(char symbol, String action) {
		if (action == null)
			throw new IllegalArgumentException("Action can't be null!");

		Command command = null;

		try (Scanner sc = new Scanner(action)) {
			// so that it reads numbers with the decimal dot (.)
			sc.useLocale(Locale.ENGLISH);
			String commandName = sc.next();

			switch (commandName) {
			case "draw":
				command = new DrawCommand(sc.nextDouble());
				break;
			case "skip":
				command = new SkipCommand(sc.nextDouble());
				break;
			case "rotate":
				command = new RotateCommand(sc.nextDouble());
				break;
			case "scale":
				command = new ScaleCommand(sc.nextDouble());
				break;
			case "push":
				command = new PushCommand();
				break;
			case "pop":
				command = new PopCommand();
				break;
			case "color":
				command = new ColorCommand(Color.decode("0x" + sc.next()));
				break;
			default:
				throw new IllegalArgumentException("Unknown command. It was: " + commandName);
			}

			if (sc.hasNext())
				throw new IllegalArgumentException("Too many arguments in the action.");

		} catch (NoSuchElementException | NumberFormatException e) {
			throw new IllegalArgumentException("Illegal action.");
		}

		this.commands.put(symbol, command);
		return this;
	}

	/**
	 * Registers given symbol for the given production.
	 * 
	 * @param symbol
	 *            production left-hand symbol
	 * @param production
	 *            right-hand side of production
	 */
	@Override
	public LSystemBuilder registerProduction(char symbol, String production) {
		if (production == null)
			throw new IllegalArgumentException("production can't be null!");

		this.productions.put(symbol, production);
		return this;
	}

	/**
	 * Configures the beginning direction angle of the turtle. Angle is in degrees.
	 * 0 degrees is facing right, 90 degrees is facing upwards, etc.
	 * 
	 * @param angle
	 *            angle in degrees
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	/**
	 * Configures the axiom of the system.
	 * 
	 * @param axiom
	 *            axiom
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		if (axiom == null)
			throw new IllegalArgumentException("Axiom can't be null");
		this.axiom = axiom;
		return this;
	}

	/**
	 * Sets the starting position of the turtle. (0, 0) is the most left bottom
	 * point on the screen.
	 * 
	 * @param x
	 *            x coordinate from 0 to 1.0
	 * @param y
	 *            y coordinate from 0 to 1.0
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		if (x < 0 || y < 0 || x > 1 || y > 1)
			throw new IllegalArgumentException("x and y must be in [0, 1.0]. They were: (" + x + ", " + y + ")");

		this.origin = new Vector2D(x, y);
		return this;
	}

	/**
	 * Unit length of the one draw or skip call. Unit length of 1.0 is the width and
	 * the height of the screen.
	 * 
	 * @param value
	 *            unit length value
	 */
	@Override
	public LSystemBuilder setUnitLength(double value) {
		if (value < 0 || value > 1)
			throw new IllegalArgumentException("Value must be in range [0, 1.0]. It was: " + value);

		this.unitLength = value;
		return this;
	}

	/**
	 * Configures the unit length degree scaler.
	 * 
	 * @param value
	 *            unit length degree scaler, must be positive decimal number number
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double value) {
		if (value <= 0)
			throw new IllegalArgumentException("Value must be > 0. It was: " + value);

		this.unitLengthDegreeScaler = value;
		return this;
	}

}
