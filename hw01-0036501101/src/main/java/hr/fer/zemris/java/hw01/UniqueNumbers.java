package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Simple command line program that takes an array of numbers and prints them in
 * an ascending and descending order.
 * 
 * @author Disho
 * @version 1.0
 */
public class UniqueNumbers {

	/**
	 * Node of a binary tree. With an integer value as data.
	 */
	public static class TreeNode {
		TreeNode left;
		TreeNode right;
		int value;

		/**
		 * Initializes a node.
		 * 
		 * @param value
		 *            A value to be stored in a node
		 */
		public TreeNode(int value) {
			left = right = null;
			this.value = value;
		}
	}

	/**
	 * The main method. Controls the flow of a program.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		TreeNode head = null;

		System.out.println("Pozdrav! Ovaj program prima cijele brojeve te ih ispisuje "
				+ "u ulaznom i silaznom poretku.\nUnesite 'kraj' kada ste završili s unosom brojeva.\n");
		System.out.print("Unesite cijeli broj > ");

		while (sc.hasNext()) {
			if (sc.hasNextInt()) {
				int value = sc.nextInt();

				if (containsValue(head, value)) {
					System.out.println("Broj već postoji. Preskačem.");
				} else {
					head = addNode(head, value);
					System.out.println("Dodano.");
				}
			} else {
				String input = sc.next();

				if (input.equals("kraj")) {
					sc.close();
					break;
				} else {
					System.out.println("'" + input + "' nije cijeli broj");
				}
			}

			System.out.print("Unesite cijeli broj > ");
		}

		if (treeSize(head) != 0) {
			System.out.print("Ispis od najmanjeg: ");
			printIntArray(sortedTreeAscending(head));

			System.out.print("\nIspis od najvećeg: ");
			printIntArray(sortedTreeDescending(head));
		}
	}

	/**
	 * Adds a new node to a binary tree(left smaller, right bigger). A node will not
	 * be added if the node with the same value already exists in the tree.
	 * 
	 * @param head
	 *            Head node of a tree to which the new node will be added to.
	 * @param value
	 *            A value to be stored in a new node.
	 * @return Head node of a new binary tree.
	 */
	public static TreeNode addNode(TreeNode head, int value) {
		if (head == null) {
			return new TreeNode(value);
		}

		if (value < head.value) {
			head.left = addNode(head.left, value);
		} else if (value > head.value) {
			head.right = addNode(head.right, value);
		}

		return head;
	}

	/**
	 * Returns the number of the nodes in a given binary tree(left smaller, right
	 * bigger).
	 * 
	 * @param head
	 *            Head node of the binary tree
	 * @return number of nodes in a tree
	 */
	public static int treeSize(TreeNode head) {
		if (head == null) {
			return 0;
		}

		return treeSize(head.left) + treeSize(head.right) + 1;
	}

	/**
	 * Checks whether the given value exists in the given binary tree(left smaller,
	 * right bigger).
	 * 
	 * @param head
	 *            Head node of a binary tree
	 * @param value
	 *            A value to be checked
	 * @return True if it exists, false otherwise
	 */
	public static boolean containsValue(TreeNode head, int value) {
		if (head == null) {
			return false;
		}

		if (value < head.value) {
			return containsValue(head.left, value);
		} else if (value > head.value) {
			return containsValue(head.right, value);
		}

		return true;
	}

	/**
	 * Returns an array of sorted numbers of a given binary tree(left smaller, right
	 * bigger) in an ascending order.
	 * 
	 * @param head
	 *            Head node of a binary tree
	 * @return an array of sorted numbers
	 */
	public static int[] sortedTreeAscending(TreeNode head) {
		int[] array = new int[UniqueNumbers.treeSize(head)];

		fillSortedArrayAscending(head, array, 0);

		return array;
	}

	/**
	 * Helper method of a sortedTreeAscending(TreeNode head) method. Recursively
	 * fills an array with sorted numbers from a binary tree(left smaller, right
	 * bigger) in an ascending order.
	 * 
	 * @param head
	 *            Head node of a binary tree
	 * @param array
	 *            Array to be filled
	 * @param counter
	 *            Starting index from which to fill an array.
	 * @return Size of a array
	 */
	private static int fillSortedArrayAscending(TreeNode head, int[] array, int counter) {
		if (head == null) {
			return counter;
		}

		counter = fillSortedArrayAscending(head.left, array, counter);
		array[counter++] = head.value;
		counter = fillSortedArrayAscending(head.right, array, counter);

		return counter;
	}

	/**
	 * Returns an array of sorted numbers of a given binary tree(left smaller, right
	 * bigger) in an descending order.
	 * 
	 * @param head
	 *            Head node of a binary tree
	 * @return an array of sorted numbers
	 */
	public static int[] sortedTreeDescending(TreeNode head) {
		int[] array = new int[UniqueNumbers.treeSize(head)];

		fillSortedArrayDescending(head, array, 0);

		return array;
	}

	/**
	 * Helper method of a sortedTreeAscending(TreeNode head) method. Recursively
	 * fills an array with sorted numbers from a binary tree(left smaller, right
	 * bigger) in an descending order.
	 * 
	 * @param head
	 *            Head node of a binary tree
	 * @param array
	 *            Array to be filled
	 * @param counter
	 *            Starting index from which to fill an array.
	 * @return Size of a array
	 */
	private static int fillSortedArrayDescending(TreeNode head, int[] array, int counter) {
		if (head == null) {
			return counter;
		}

		counter = fillSortedArrayDescending(head.right, array, counter);
		array[counter++] = head.value;
		counter = fillSortedArrayDescending(head.left, array, counter);

		return counter;
	}

	/**
	 * Prints an array of Ints to the standard output.
	 * 
	 * @param array
	 *            Array to be printed
	 */
	public static void printIntArray(int[] array) {
		for (int number : array) {
			System.out.print(number + " ");
		}
	}

}
