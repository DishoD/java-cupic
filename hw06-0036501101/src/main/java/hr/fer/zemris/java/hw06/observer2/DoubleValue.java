package hr.fer.zemris.java.hw06.observer2;

/**
 * <code>IntegerStorageObserver</code> that prints the doubled value of the
 * changed integer of <code>IntegerStorage</code> for a limited number of times.
 * 
 * @author Disho
 *
 */
public class DoubleValue implements IntegerStorageObserver {
	private final int limit;
	private int changeCounter;

	/**
	 * Initializes the object wit the given parameter.
	 * 
	 * @param limit
	 *            number of times to print the doubled changed value before the
	 *            unregistration from the subject
	 */
	public DoubleValue(int limit) {
		this.limit = limit;
	}

	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		int x = istorageChange.getNewValue();
		System.out.println("Double value: " + 2 * x);

		changeCounter++;
		if (changeCounter == limit) {
			istorageChange.getIntegerStorage().removeObserver(this);
		}
	}

}
