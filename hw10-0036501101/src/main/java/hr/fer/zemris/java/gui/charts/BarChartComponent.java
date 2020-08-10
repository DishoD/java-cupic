package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JComponent;

/**
 * Draws a bar chart based on the given bar chart model.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class BarChartComponent extends JComponent {
	/**
	 * bar chart model on which this bar chart is drawn
	 */
	private BarChart model;
	/**
	 * list of y values
	 */
	private List<String> yValues = new ArrayList<>();
	/**
	 * list of XY values sorted in ascending order by X value
	 */
	private List<XYValue> sortedValues;
	
	/**
	 * maximum number of characters in Y values
	 */
	private int maxYWidth = -1;
	
	/**
	 * distance from the edges of the screen
	 */
	private static final int BLEED = 7;
	/**
	 * distance from Y axis name to the Y values
	 */
	private static final int FIRST_X_GAP = 15;
	/**
	 * distance from Y values to the Y axis line
	 */
	private static final int SECOND_X_GAP = 15;
	/**
	 * distance from X axis name to the X values
	 */
	private static final int FIRST_Y_GAP = 15;
	/**
	 * distance from X values to the X axis line
	 */
	private static final int SECOND_Y_GAP = 15;
	/**
	 * extension of the grid lines on the bottom and left side
	 */
	private static final int LINE_OVERHEAD = 6;
	/**
	 * how much in width is the shadow extended from the original bar
	 */
	private static final int SHADOW_EXTEND = 5;
	/**
	 * how much in height is the shadow shortened from the original bar
	 */
	private static final int SHADOW_SHORTEN = 5;

	/**
	 * Initializes the component with the given model
	 * 
	 * @param model bar chart model
	 */
	public BarChartComponent(BarChart model) {
		Objects.requireNonNull(model, "model cannot be null");
		
		this.model = model;
		
		int limit = model.getyMax() + Math.abs(model.getyMax()%model.getyGap());
		
		for(int i = model.getyMin(); i <= limit; i += model.getyGap()) {
			String str = Integer.toString(i);
			yValues.add(str);
			int width = str.length();
			
			maxYWidth = width > maxYWidth ? width : maxYWidth;
		}
		
		sortedValues = new ArrayList<>(model.getXYValues());
		sortedValues.sort((v1,v2) -> Integer.compare(v1.x, v2.x));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Insets ins = getInsets();
		Dimension dim = getSize();
		
		int fullWidth = dim.width - ins.right - ins.left;
		int fullHeight = dim.height - ins.bottom - ins.top;
		
		Color c = getBackground();
		
		
		if(isOpaque()) {
			g.setColor(c);
			g.fillRect(ins.left, ins.top, fullWidth, fullHeight);
		}
		
		//draw text
		g.setColor(Color.BLACK);
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform defaultAt = g2d.getTransform();
		AffineTransform at = new AffineTransform();
        at.rotate(- Math.PI / 2);
        g2d.setTransform(at);
        
        FontMetrics fm = g.getFontMetrics();
        int l = fm.stringWidth(model.getyString());
        int h = fm.getAscent();
        g2d.drawString(model.getyString(), -(fullHeight+l)/2, ins.top + BLEED + h);
        
        g2d.setTransform(defaultAt);
        l = fm.stringWidth(model.getxString());
        g2d.drawString(model.getxString(), (fullWidth-l)/2, dim.height - ins.bottom - BLEED);
        
        int maxStrWidth = fm.stringWidth("1")*maxYWidth;
        
        //calculate sheet borders
        int bottom = dim.height - ins.bottom - BLEED - 2*h - FIRST_Y_GAP - SECOND_Y_GAP;
        int left = ins.left + BLEED + h + FIRST_X_GAP;
        int top = ins.top + BLEED;
        int right = dim.width - ins.right - BLEED;
        int sheetWidth = dim.width - ins.right - BLEED - left - maxStrWidth - SECOND_X_GAP;
        int sheetHeight = bottom - ins.top - BLEED;
        
        int yGap = sheetHeight/(yValues.size()-1);
        
        //draw y axis and y values
        for(int i = 0; i < yValues.size(); ++i) {
        	g.setColor(Color.BLACK);
        	
        	int y = bottom - i*yGap;
        	l = fm.stringWidth(yValues.get(i));
        	g.drawString(yValues.get(i), left+(maxStrWidth - l), y+h/2);
        	
        	if(i == 0) {
        		c = Color.BLACK;
        		g.fillPolygon(new int[] {right - 5, right - 5, right}, new int[] {y + 5, y - 5, y}, 3 );
        	} else {
        		c = Color.LIGHT_GRAY;
        	}
        	
        	g.setColor(c);
        	g.drawLine(left + SECOND_X_GAP + maxStrWidth - LINE_OVERHEAD, y, right, y);
        }
        
        int bottomXNumbers = dim.height - ins.bottom - BLEED - h - FIRST_Y_GAP;
        int xGap = (sheetWidth-10)/sortedValues.size();
        int leftXNumbers = left + maxStrWidth + SECOND_X_GAP;
        
        //draw x values and x axis
        for(int i = 0; i <= sortedValues.size(); ++i) {
        	if(i != sortedValues.size()) {
        		g.setColor(Color.BLACK);
        		String str = Integer.toString(sortedValues.get(i).x);
        		l = fm.stringWidth(str);
        		int x = leftXNumbers + i*xGap + (xGap-l)/2;
        		g.drawString(str, x, bottomXNumbers);
        		
        		int height = sortedValues.get(i).y*yGap/model.getyGap();
        		int y = bottom - height;
        		x = leftXNumbers + i*xGap;
        		
        		g.setColor(Color.LIGHT_GRAY);
        		g.fillRect(x, y + SHADOW_SHORTEN, xGap + SHADOW_EXTEND, height - SHADOW_SHORTEN);
        		
        		g.setColor(getForeground());
        		g.fillRect(x, y, xGap, height);
        	}
        	
        	int x = leftXNumbers + i*xGap;
        	
        	if(i == 0) {
        		c = Color.BLACK;
        		g.setColor(Color.BLACK);
        		g.fillPolygon(new int[] {x - 5, x + 5, x}, new int[] {top, top, top - 5}, 3 );
        	} else {
        		c = Color.LIGHT_GRAY;
        	}
        	
        	g.setColor(c);
        	g.drawLine(x, bottom + LINE_OVERHEAD, x, top);
        }
	}
}
