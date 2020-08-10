package hr.fer.zemris.java.hw06.observer2;

/**
 * The interface that modes the observer of the <code>IntegerStorage</code>
 * 
 * @author Disho
 *
 */
public interface IntegerStorageObserver {

	/**
	 * When the integer value is changed of the <code>IntegerStorage</code> object
	 * that this observer is registered to, this method will be called.
	 * 
	 * @param istorage
	 *            reference to the registered subject <code>IntegerStorage</code>
	 */
	void valueChanged(IntegerStorageChange istorageChange);

}
