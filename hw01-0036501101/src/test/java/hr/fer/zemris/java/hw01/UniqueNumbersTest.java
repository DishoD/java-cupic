package hr.fer.zemris.java.hw01;


import org.junit.Assert;
import org.junit.Test;

//import hr.fer.zemris.java.hw01.UniqueNumbers.TreeNode;

import static hr.fer.zemris.java.hw01.UniqueNumbers.*;

public class UniqueNumbersTest {

	@Test
	public void addingToTree() {
		TreeNode head = null;
		head = addNode(head, 56);
		head = addNode(head, 32);
		head = addNode(head, -9);
		head = addNode(head, 0);
		head = addNode(head, 123);
		head = addNode(head, 56);
		head = addNode(head, 69);
		
		Assert.assertEquals(56, head.value);
		Assert.assertEquals(32, head.left.value);
		Assert.assertEquals(-9, head.left.left.value);
		Assert.assertEquals(0, head.left.left.right.value);
		Assert.assertEquals(123, head.right.value);
		Assert.assertEquals(69, head.right.left.value);
	}
	
	@Test
	public void testTreeSize() {
		TreeNode head = null;
		
		Assert.assertEquals(0, treeSize(head));
		
		head = addNode(head, 56);
		head = addNode(head, 32);
		
		Assert.assertEquals(2, treeSize(head));
		
		head = addNode(head, -9);
		head = addNode(head, 0);
		head = addNode(head, 123);
		head = addNode(head, 56);
		
		Assert.assertEquals(5, treeSize(head));
		
		head = addNode(head, 69);
		
		Assert.assertEquals(6, treeSize(head));
	}
	
	@Test
	public void testContains() {
		TreeNode head = null;
		head = addNode(head, 56);
		head = addNode(head, 32);
		head = addNode(head, -9);
		head = addNode(head, 0);
		head = addNode(head, 123);
		head = addNode(head, 56);
		head = addNode(head, 69);
		
		Assert.assertTrue(containsValue(head, 56));
		Assert.assertTrue(containsValue(head, 32));
		Assert.assertTrue(containsValue(head, -9));
		Assert.assertTrue(containsValue(head, 0));
		Assert.assertTrue(containsValue(head, 123));
		Assert.assertTrue(containsValue(head, 69));
	}
	
	@Test
	public void testAscending() {
		TreeNode head = null;
		head = addNode(head, 56);
		head = addNode(head, 32);
		head = addNode(head, -9);
		head = addNode(head, 0);
		head = addNode(head, 123);
		head = addNode(head, 56);
		head = addNode(head, 69);
		
		int[] expecteds = {-9, 0, 32, 56, 69, 123};
		int[] actuals = sortedTreeAscending(head);
		
		Assert.assertArrayEquals(expecteds, actuals);
	}
	
	@Test
	public void testDescending() {
		TreeNode head = null;
		head = addNode(head, 56);
		head = addNode(head, 32);
		head = addNode(head, -9);
		head = addNode(head, 0);
		head = addNode(head, 123);
		head = addNode(head, 56);
		head = addNode(head, 69);
		
		int[] expecteds = {123, 69, 56, 32, 0, -9};
		int[] actuals = sortedTreeDescending(head);
		
		Assert.assertArrayEquals(expecteds, actuals);
	}

}
