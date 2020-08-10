package hr.fer.zemris.java.hw06.observer1;

/**
 * <code>IntegerStorageObserver</code> that prints the squared value of the
 * changed integer of <code>IntegerStorage</code>.
 * 
 * @author Disho
 *
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorage istorage) {
		int x = istorage.getValue();
		System.out.printf("Provided new value: %d, square is %d%n", x, x * x);

	}

}
