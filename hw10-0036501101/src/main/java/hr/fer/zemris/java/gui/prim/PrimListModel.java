package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * List model that consists of prime numbers. With the method next() generates
 * new prime numbers in ascending order beginning from number one.
 * 
 * @author Disho
 *
 */
public class PrimListModel implements ListModel<Integer> {
	/**
	 * list of prime numbers in the model
	 */
	private List<Integer> primeNumbers = new ArrayList<>();
	/**
	 * last added prime number
	 */
	private int lastPrime = 1;

	/**
	 * listeners to the model for when the next prime number is added
	 */
	private List<ListDataListener> listeners = new ArrayList<>();

	/**
	 * Initializes the list model.
	 */
	public PrimListModel() {
		primeNumbers.add(1);

		notifyListeners();
	}

	/**
	 * Generates next prime number and adds it to the end of the list.
	 */
	public void next() {
		while (!isPrime(++lastPrime)) {}
		primeNumbers.add(lastPrime);
		notifyListeners();
	}

	@Override
	public void addListDataListener(ListDataListener listener) {
		listeners.add(listener);
	}

	@Override
	public Integer getElementAt(int index) {
		if (index < 0 || index >= primeNumbers.size())
			throw new IndexOutOfBoundsException(
					"Index should be in interval [0," + (primeNumbers.size() - 1) + "]. Was: " + index);

		return primeNumbers.get(index);
	}

	@Override
	public int getSize() {
		return primeNumbers.size();
	}

	@Override
	public void removeListDataListener(ListDataListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Checks whether the given number is prime
	 * 
	 * @param number
	 *            number to check
	 * @return true if number is prime, false otherwise
	 */
	private static boolean isPrime(int number) {
		for (int i = 2; i <= (int) Math.sqrt(number); ++i) {
			if (number % i == 0)
				return false;
		}

		return true;
	}

	/**
	 * Notifies the registered listeners that the prime number has been added to the
	 * list.
	 */
	private void notifyListeners() {
		int last = primeNumbers.size() - 1;
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, last, last);

		listeners.forEach(l -> l.intervalAdded(e));
	}

}
