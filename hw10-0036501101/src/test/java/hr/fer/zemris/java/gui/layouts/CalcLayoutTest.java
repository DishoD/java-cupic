package hr.fer.zemris.java.gui.layouts;


import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class CalcLayoutTest {
	
	@Test(expected=CalcLayoutException.class)
	public void rowLessThanOne() {
		CalcLayout l = new CalcLayout();
		l.addLayoutComponent(new JLabel(), "0, 5");
	}
	
	@Test(expected=CalcLayoutException.class)
	public void columnLessThanOne() {
		CalcLayout l = new CalcLayout();
		l.addLayoutComponent(new JLabel(), "5, -9");
	}
	
	@Test(expected=CalcLayoutException.class)
	public void rowGreaterThanFive() {
		CalcLayout l = new CalcLayout();
		l.addLayoutComponent(new JLabel(), "6, 3");
	}
	
	@Test(expected=CalcLayoutException.class)
	public void columnGreaterThanSeven() {
		CalcLayout l = new CalcLayout();
		l.addLayoutComponent(new JLabel(), "3, 12");
	}
	
	@Test(expected=CalcLayoutException.class)
	public void rowOneColumnTwo() {
		CalcLayout l = new CalcLayout();
		l.addLayoutComponent(new JLabel(), "1, 2");
	}
	
	@Test(expected=CalcLayoutException.class)
	public void rowOneColumnThree() {
		CalcLayout l = new CalcLayout();
		l.addLayoutComponent(new JLabel(), "1, 3");
	}
	
	@Test(expected=CalcLayoutException.class)
	public void rowOneColumnFour() {
		CalcLayout l = new CalcLayout();
		l.addLayoutComponent(new JLabel(), "1, 4");
	}
	
	@Test(expected=CalcLayoutException.class)
	public void rowOneColumnFive() {
		CalcLayout l = new CalcLayout();
		l.addLayoutComponent(new JLabel(), "1, 5");
	}
	
	@Test(expected=CalcLayoutException.class)
	public void sameConstraint() {
		CalcLayout l = new CalcLayout();
		l.addLayoutComponent(new JLabel(), "2, 3");
		l.addLayoutComponent(new JLabel(), "2, 3");
	}
	
	@Test
	public void prefferedWidthWithoutOneOne() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}
	
	@Test
	public void prefferedWidthWIthOneOne() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}
}
