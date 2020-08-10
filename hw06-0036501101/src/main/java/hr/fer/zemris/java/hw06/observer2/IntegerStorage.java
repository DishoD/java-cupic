package hr.fer.zemris.java.hw06.observer2;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that stores an integer value. If you wish to be notified when the
 * value is changed you can register as an <code>IntegerStorageObserver</code>.
 * 
 * @author Disho
 *
 */
public class IntegerStorage {
	/**
	 * stored integer value
	 */
	private int value;
	/**
	 * list of all registered observers
	 */
	private List<IntegerStorageObserver> observers = new ArrayList<>();
	/**
	 * list of pending observers to remove
	 */
	private List<IntegerStorageObserver> observersToRemove = new ArrayList<>();
	/**
	 * flag that says if the object is currently iterating trough the observers list
	 */
	private boolean isIterating;

	/**
	 * Initializes the object with the given parameter.
	 * 
	 * @param initialValue
	 *            initial integer value to be stored
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Register given observer to this subject. When the value of stored integer is
	 * changed the given observer will be notified.
	 * 
	 * @param observer
	 *            observer to register
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (observers.contains(observer))
			return;

		observers.add(observer);
	}

	/**
	 * Unregister given observer from this subject. Given observer will no longer be
	 * notified when the value of current integer is changed.
	 * 
	 * If the subject is currently iterating trough the internal observers list
	 * (which means that it cant't unregister the given observer in the given
	 * moment), the method will add the given observer to the list of pending
	 * observers to be removed when the iteration has finished.
	 * 
	 * @param observer
	 *            observer to unregister
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if (isIterating) {
			observersToRemove.add(observer);
			return;
		}

		observers.remove(observer);
	}

	/**
	 * Unregisters all pending observers from the obsrversToRemove list.
	 * 
	 * @throws IllegalStateException if the object is currently iterating through the observers list
	 */
	private void removeObserves() {
		if (isIterating)
			throw new IllegalStateException("Can't remove observers while iterating.");

		for (IntegerStorageObserver observer : observersToRemove) {
			removeObserver(observer);
		}

		observersToRemove.clear();
	}

	/**
	 * Unregisters all registered observers of this subject.
	 * 
	 * @throws IllegalStateException if the object is currently iterating through the observers list
	 */
	public void clearObservers() {
		if (isIterating)
			throw new IllegalStateException("Can't remove observers while iterating.");
		
		observers.clear();
	}

	/**
	 * Returns currently stored integer value.
	 * 
	 * @return currently stored integer value
	 */ 
	public int getValue() {
		return value;
	}

	/**
	 * Sets the integer value and notifies all registered observers if the value has changed.
	 * 
	 * @param value
	 */
	public void setValue(int value) {
		// Only if new value is different than the current value:
		if (this.value != value) {
			int oldValue = this.value;
			// Update current value
			this.value = value;
			// Notify all registered observers
			
			IntegerStorageChange info = new IntegerStorageChange(this, oldValue, value);
			if (observers != null) {
				isIterating = true;

				for (IntegerStorageObserver observer : observers) {
					observer.valueChanged(info);
				}

				isIterating = false;
				removeObserves();
			}
		}
	}
}
