package hr.fer.zemris.java.hw07.crypto;

import static org.junit.Assert.*;
import org.junit.Test;


public class UtilTest {
	@Test
	public void hextobyte() {
		byte[] actuals = Util.hextobyte("0001fF08Aa0A0f0510");
		byte[] expecteds = new byte[] {0, 1, -1, 8, -86, 10, 15, 5, 16};
		
		assertArrayEquals(expecteds, actuals);
	}
	
	@Test
	public void bytetohex() {
		String expected = "0001ff08aa0a0f0510";
		String actual = Util.bytetohex(new byte[] {0, 1, -1, 8, -86, 10, 15, 5, 16});
		
		assertEquals(expected, actual);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void hextobyteOddNumberOfChars() {
		Util.hextobyte("059");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void hextobyteIllegalChars() {
		Util.hextobyte("0590pg");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void hextobyteEmptyString() {
		Util.hextobyte("");
	}
	
	@Test(expected=NullPointerException.class)
	public void hextobyteNull() {
		Util.hextobyte(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void bytetohexNull() {
		Util.bytetohex(null);
	}
}
