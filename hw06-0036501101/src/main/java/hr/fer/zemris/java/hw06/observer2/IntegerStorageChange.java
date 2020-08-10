package hr.fer.zemris.java.hw06.observer2;

/**
 * Stores the information of the state change for the
 * <code>IntegerStorage</code> class.
 * 
 * @author Disho
 *
 */
public final class IntegerStorageChange {

	/**
	 * reference to the <code>IntegerStorage</code> object that created this
	 * instance of the <code>IntegerStorageChange</code> object.
	 */
	private final IntegerStorage integerStorage;
	/**
	 * old integer value before the <code>IntegerStorage</code> changed state
	 */
	private final int oldValue;
	/**
	 * new integer value after the <code>IntegerStorage</code> changed state
	 */
	private final int newValue;

	/**
	 * Initializes the object with the gicen parameters.
	 * 
	 * @param integerStorage
	 *            reference to the <code>IntegerStorage</code> object that created
	 *            this instance of the <code>IntegerStorageChange</code> object.
	 * @param oldValue
	 *            old integer value before the <code>IntegerStorage</code> changed
	 *            state
	 * @param newValue
	 *            new integer value after the <code>IntegerStorage</code> changed
	 *            state
	 */
	public IntegerStorageChange(IntegerStorage integerStorage, int oldValue, int newValue) {
		this.integerStorage = integerStorage;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Returns reference to the <code>IntegerStorage</code> object that created this
	 * instance of the <code>IntegerStorageChange</code> object.
	 * 
	 * @return the integerStorage object
	 */
	public IntegerStorage getIntegerStorage() {
		return integerStorage;
	}

	/**
	 * Returns old integer value before the <code>IntegerStorage</code> changed
	 * state
	 * 
	 * @return the old integer value
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * Returns new integer value after the <code>IntegerStorage</code> changed state
	 * 
	 * @return the new integer value
	 */
	public int getNewValue() {
		return newValue;
	}

}
