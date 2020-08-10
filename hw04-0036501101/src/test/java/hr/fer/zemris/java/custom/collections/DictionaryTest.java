package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class DictionaryTest {
	
	@Test
	public void isEmpty() {
		Dictionary t = new Dictionary();
		
		assertTrue(t.isEmpty());
	}
	
	@Test
	public void putAndGetAndSize() {
		Dictionary t = new Dictionary();
		
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
	
	
	@Test(expected=IllegalArgumentException.class)
	public void putNull() {
		Dictionary t = new Dictionary();
		
		t.put(null, null);
	}
	
	@Test
	public void clear() {
		Dictionary t = new Dictionary();

		for (int i = 0; i < 200; ++i) {
			t.put(Integer.toString(i), i);
		}
		
		t.clear();
		
		assertEquals(0, t.size());
		assertTrue(t.isEmpty());
	}
}
