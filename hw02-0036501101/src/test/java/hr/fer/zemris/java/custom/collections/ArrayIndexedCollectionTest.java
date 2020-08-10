package hr.fer.zemris.java.custom.collections;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

public class ArrayIndexedCollectionTest {
	
	@Test
	public void emptyCollectionConstructor1() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		Assert.assertEquals(0, col.size());
		Assert.assertTrue(col.isEmpty());
	}
	
	@Test
	public void emptyCollectionConstructor2() {
		ArrayIndexedCollection col = new ArrayIndexedCollection(32);
		
		Assert.assertEquals(0, col.size());
		Assert.assertTrue(col.isEmpty());
	}
	
	@Test
	public void emptyCollectionConstructor3() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection();
		ArrayIndexedCollection col2 = new ArrayIndexedCollection(col1);
		
		Assert.assertEquals(0, col2.size());
		Assert.assertTrue(col2.isEmpty());
	}
	
	@Test
	public void emptyCollectionConstructor4() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection();
		ArrayIndexedCollection col2 = new ArrayIndexedCollection(col1, 64);
		
		Assert.assertEquals(0, col2.size());
		Assert.assertTrue(col2.isEmpty());
	}
	
	@Test
	public void collectionSize() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(int i = 0; i < 500; ++i) {
			col.add(i);
		}
		
		Assert.assertEquals(500, col.size());
	}
	
	@Test(expected = NullPointerException.class)
	public void addNull() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		col.add(null);
	}
	
	@Test
	public void addAndGet() {
		Integer[] numbers = {0, -9, 22, 56, 39, -100, 989, -9, 0, 55, 66, 258};
		
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(Integer number : numbers) {
			col.add(number);
		}
		
		for(int i = 0; i < numbers.length; ++i) {
			Assert.assertEquals(numbers[i], (Integer)col.get(i));
		}
		
	}
	
	@Test
	public void indexOf() {
		Integer[] numbers = {0, -9, 22, 56, 39, -100, 989, 55, 66, 258};
		
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(Integer number : numbers) {
			col.add(number);
		}
		
		for(int i = 0; i < numbers.length; ++i) {
			Assert.assertEquals(i, col.indexOf(numbers[i]));
		}
		
	}
	
	@Test
	public void contains() {
		Integer[] numbers = {0, -9, 22, 56, 39, -100, 989, -9, 0, 55, 66, 258};
		
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(Integer number : numbers) {
			col.add(number);
		}
		
		for(Integer number : numbers) {
			Assert.assertTrue(col.contains(number));
		}
		
		Assert.assertFalse(col.contains(2));
		Assert.assertFalse(col.contains(3));
		Assert.assertFalse(col.contains(4));
		Assert.assertFalse(col.contains(5));
	}
	
	@Test
	public void toArray() {
		Integer[] numbers = {0, -9, 22, 56, 39, -100, 989, -9, 0, 55, 66, 258};
		
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(Integer number : numbers) {
			col.add(number);
		}
		
		Assert.assertArrayEquals(numbers, col.toArray());
		
	}
	
	@Test
	public void addAll() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection();
		
		for(int i = 0; i < 1000; ++i) {
			col1.add(i);
		}
		
		ArrayIndexedCollection col2 = new ArrayIndexedCollection(col1);
		
		Assert.assertEquals(col1.size(), col2.size());
		Assert.assertArrayEquals(col1.toArray(), col2.toArray());
		
		for(int i = 0; i < 1000; ++i) {
			Assert.assertEquals(col1.get(i), col2.get(i));
			Assert.assertTrue(col1.contains(col2.get(i)));
		}
	}
	
	@Test
	public void clear() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(int i = 0; i < 1000; ++i) {
			col.add(i);
		}
		
		col.clear();
		
		Assert.assertEquals(0, col.size());
		Assert.assertTrue(col.isEmpty());
	}
	
	@Test
	public void removeByPosition() {
		Integer[] numbers = {0, -9, 22, 56, 39, -100, 989, -9, 0, 55, 66, 258};
		
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(Integer number : numbers) {
			col.add(number);
		}
		
		col.remove(0);
		col.remove(10);
		col.remove(4);
		
		Integer[] expecteds = {-9, 22, 56, 39, 989, -9, 0, 55, 66};
		
		Assert.assertEquals(numbers.length - 3, col.size());
		Assert.assertArrayEquals(expecteds, col.toArray());
	}
	
	@Test
	public void removeByObject() {
		String[] strings = {"1", "2", "3", "4", "4", "4", "4", "5" ,"6", "7", "8"};
		
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(String str : strings) {
			col.add(str);
		}
		
		col.remove("1");
		col.remove("8");
		col.remove("4");
		col.remove("4");
		col.remove("4");
		
		String[] expecteds = {"2", "3", "4", "5" ,"6", "7"};
		
		Assert.assertEquals(strings.length - 5, col.size());
		Assert.assertArrayEquals(expecteds, col.toArray());
	}
	
	@Test
	public void insert() {
		Integer[] numbers = {0, -9, 22, 56, 39, -100, 989, -9, 0, 55, 66, 258};
		
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(Integer number : numbers) {
			col.add(number);
		}
		
		col.insert(99, 0);
		col.insert(100, col.size());
		col.insert(101, 5);
		
		Assert.assertEquals(99, col.get(0));
		Assert.assertEquals(100, col.get(col.size()-1));
		Assert.assertEquals(101, col.get(5));
	} 
	
	@Test
	public void forEach() {
		String[] strings = {"1", "2", "3", "4", "4", "4", "4", "5" ,"6", "7", "8", " štefica"};
		
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		for(String str : strings) {
			col.add(str);
		}
		
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		class printProcessor extends Processor {
			
			@Override
			public void process(Object value) {
				System.out.print(value);
			}
		}
		
		col.forEach(new printProcessor());
		
		Assert.assertEquals("12344445678 štefica", outContent.toString());
		
		System.setOut(System.out);
	}
	
	
	@Test
	public void equals() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection();
		
		for(int i = 0; i < 200; ++i) {
			col1.add(i);
		}
		
		ArrayIndexedCollection col2 = new ArrayIndexedCollection(col1);
		
		Assert.assertTrue(col1.equals(col2));
	}
	
}
