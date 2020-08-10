package hr.fer.zemris.java.hw06.task3;

import static org.junit.Assert.*;

import java.util.EmptyStackException;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ObjectMultistackTest {
	private ObjectMultistack m;
	
	@Before
	public void setUp() {
		m = new ObjectMultistack();
		
		m.push("number", new ValueWrapper(1));
		m.push("number", new ValueWrapper(2));
		m.push("number", new ValueWrapper(3));
		
		m.push("name", new ValueWrapper("Marko"));
		m.push("name", new ValueWrapper("Pero"));
		m.push("name", new ValueWrapper("Štefica"));
	}
	
	@Test
	public void peekTest() {
		assertEquals(3, m.peek("number").getValue());
		assertEquals("Štefica", m.peek("name").getValue());
	}
	
	@Test
	public void popTest() {
		assertEquals(3, m.pop("number").getValue());
		assertEquals("Štefica", m.pop("name").getValue());
		
		assertEquals(2, m.pop("number").getValue());
		assertEquals("Pero", m.pop("name").getValue());
		
		m.push("number", new ValueWrapper(5));
		
		assertEquals(5, m.pop("number").getValue());
		assertEquals("Marko", m.pop("name").getValue());
		
		assertEquals(1, m.pop("number").getValue());
	}
	
	@Test(expected=EmptyStackException.class)
	public void emptyStackException() {
		m.pop("name");
		m.pop("name");
		m.pop("name");
		m.pop("name");
	}
	
	@Test
	public void isEmptyTest() {
		assertTrue(m.isEmpty("bla"));
		assertFalse(m.isEmpty("name"));
		assertFalse(m.isEmpty("number"));
		
		m.pop("name");
		m.pop("name");
		m.pop("name");
		m.pop("number");
		m.pop("number");
		m.pop("number");
		
		assertTrue(m.isEmpty("name"));
		assertTrue(m.isEmpty("number"));
	}
	
}
