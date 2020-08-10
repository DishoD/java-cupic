package hr.fer.zemris.java.hw06.observer2;

/**
 * <code>IntegerStorageObserver</code> that prints the number of times that
 * integer value of the <code>IntegerStorage</code> integer has changed.
 * 
 * @author Disho
 *
 */
public class ChangeCounter implements IntegerStorageObserver {
	private int changeCounter;

	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		changeCounter++;
		System.out.println("Number of value changes since tracking: " + changeCounter);
	}

}
