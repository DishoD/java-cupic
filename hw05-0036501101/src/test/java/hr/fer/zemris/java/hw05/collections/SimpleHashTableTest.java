package hr.fer.zemris.java.hw05.collections;

import static org.junit.Assert.*;
import org.junit.Test;

import hr.fer.zemris.java.hw05.collections.SimpleHashtable;

@SuppressWarnings("javadoc")
public class SimpleHashTableTest {
	
	@Test
	public void isEmpty() {
		SimpleHashtable<Object, Object> t = new SimpleHashtable<>();
		
		assertTrue(t.isEmpty());
	}
	
	@Test
	public void putAndGetAndSize() {
		SimpleHashtable<String, Integer> t = new SimpleHashtable<>();
		
		t.put("1", 1);
		t.put("2", 2);
		t.put("3", 3);
		t.put("4", 4);
		t.put("4", 44);
		
		
		assertFalse(t.isEmpty());
		assertEquals(4, t.size());
		
		assertEquals(1, (int)t.get("1"));
		assertEquals(2, (int)t.get("2"));
		assertEquals(3, (int)t.get("3"));
		assertEquals(44, (int)t.get("4"));
	}
	
	@Test
	public void remove() {
		SimpleHashtable<String, Integer> t = new SimpleHashtable<>();
		
		t.put("1", 1);
		t.put("2", 2);
		t.put("3", 3);
		t.put("4", 4);
		
		assertTrue(t.containsKey("1"));
		assertTrue(t.containsValue(2));
		assertTrue(t.containsKey("3"));
		assertTrue(t.containsValue(4));
		
		assertFalse(t.containsKey("5"));
		assertFalse(t.containsValue(6));
		
		
		t.remove("1");
		t.remove("3");
		
		assertEquals(2, t.size());
		assertTrue(t.containsKey("4"));
		assertTrue(t.containsValue(2));
		
		assertFalse(t.containsKey("1"));
		assertFalse(t.containsValue(3));
	}
	
	@Test(expected=NullPointerException.class)
	public void putNull() {
		SimpleHashtable<Object, Object> t = new SimpleHashtable<>();
		
		t.put(null, null);
	}
	
	@Test
	public void expandingHashtable() {
		SimpleHashtable<String, Integer> t = new SimpleHashtable<>(1);

		for (int i = 0; i < 200; ++i) {
			t.put(Integer.toString(i), i);
		}


		for (int i = 0; i < 200; ++i) {
			assertTrue(t.containsValue(i));
		}
	}
	
	@Test
	public void clear() {
		SimpleHashtable<String, Integer> t = new SimpleHashtable<>();

		for (int i = 0; i < 200; ++i) {
			t.put(Integer.toString(i), i);
		}
		
		t.clear();

		for (int i = 0; i < 200; ++i) {
			assertFalse(t.containsValue(i));
		}
		
		assertEquals(0, t.size());
		assertTrue(t.isEmpty());
	}
	
}
