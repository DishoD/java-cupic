package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;

/**
 * This layout is like a grid layout that consists of 31 components on
 * a 5x7 grid. Except that element on the coordinate (1,1) occupies the space
 * of 5 elements in one row.
 * 
 * @author Disho
 *
 */
public class CalcLayout implements LayoutManager2 {
	/**
	 * minimal number of rows
	 */
	private static final int MIN_ROW = 1;
	/**
	 * minimal number of columns
	 */
	private static final int MIN_COL = 1;
	
	/**
	 * maximum number of rows
	 */
	private static final int MAX_ROW = 5;
	/**
	 * maximum number of columns
	 */
	private static final int MAX_COL = 7;
	
	/**
	 * gap in pixels, distance between rows and columns
	 */
	private int gap;
	/**
	 * is current cached information invalid
	 */
	private boolean invalid = false;
	
	/**
	 * minimum width of the container
	 */
	private int minWidth = 0;
	/**
	 * minimum height of the container
	 */
	private int minHeight = 0;
	
	/**
	 * maximum width of the container
	 */
	private int maxWidth = 0;
	/**
	 * maximum height of the container
	 */
	private int maxHeight = 0;
	
    /**
     * preferred width of the container
     */
    private int preferredWidth = 0;
    /**
     * preferred height of the container
     */
    private int preferredHeight = 0;
    
    /**
     * default gap
     */
    private static final int DEFAULT_GAP = 0;
    
    Map<RCPosition, Component> components = new HashMap<>();
    
    /**
     * Initializes the layout with the default gap
     */
    public CalcLayout() {
		this(DEFAULT_GAP);
	}

	/**
	 * Initializes the layout with the given gap.
	 * 
	 * @param gap gap in pixels, distance between rows and columns
	 */
	public CalcLayout(int gap) {
		this.gap = gap;
	}

	@Override
	public void addLayoutComponent(String str, Component component) {
	}

	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		
		int panelWidth = parent.getWidth() - insets.right - insets.left;
		int panelHeight = parent.getHeight() - insets.top - insets.bottom;
		
		int componentWidth = (panelWidth - (MAX_COL-1)*gap)/MAX_COL;
		int componentHeight = (panelHeight - (MAX_ROW-1)*gap)/MAX_ROW;
		
		int cornerX = insets.left;
		int cornerY = insets.top;
		
		components.forEach((k,v) -> {
			if(k.column == 1 && k.row == 1) {
				SwingUtilities.invokeLater(() -> v.setBounds(cornerX, cornerY, componentWidth*5 + 4*gap, componentHeight));
				return;
			}
			
			int x = cornerX + (k.column-1)*(gap+componentWidth);
			int y = cornerY + (k.row-1)*(gap+componentHeight);
			
			SwingUtilities.invokeLater(() -> v.setBounds(x, y, componentWidth, componentHeight));
		});
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		if(!invalid)
			return new Dimension(minWidth, minHeight);
		
		calculatePreferredSizes(parent);
		invalid = false;
		
		return new Dimension(minWidth, minHeight);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		if(!invalid)
			return new Dimension(preferredWidth, preferredHeight);
		
		calculatePreferredSizes(parent);
		invalid = false;
		
		return new Dimension(preferredWidth, preferredHeight);
	}

	@Override
	public void removeLayoutComponent(Component component) {
		for(Entry<RCPosition, Component> e : components.entrySet()) {
			if(e.getValue() == component) {
				components.remove(e.getKey());
			}
		}
	}

	@Override
	public void addLayoutComponent(Component component, Object constraint) {
		if(!(constraint instanceof String || constraint instanceof RCPosition))
			throw new IllegalArgumentException("Constraint must be of type String or RCPosition. It was: " + 
																		constraint.getClass().getSimpleName());
		
		RCPosition pos = null;
		
		if(constraint instanceof String) {
			String str = (String)constraint;
			str = str.replaceAll("\\s+", "");
			String[] numbers = str.split(",");
			
			if(numbers.length != 2) throw new IllegalArgumentException("Constraint must bi in format 'row, column'. It was: " + str);
			
			int row, column;
			
			try {
				row = Integer.parseInt(numbers[0]);
				column = Integer.parseInt(numbers[1]);
			} catch(NumberFormatException e) {
				throw new IllegalAccessError("Constraints must be integer numbers. They were: " + str);
			}
			
			pos = new RCPosition(row, column);
		} else {
			pos = (RCPosition)constraint;
		}
		
		if(pos.row < MIN_ROW || pos.row > MAX_ROW)
			throw new CalcLayoutException("Constraint row must be in [1, 5]. It was: " + pos.row);
		
		if(pos.column < MIN_COL || pos.column > MAX_COL)
			throw new CalcLayoutException("Constraint column must be in [1, 7]. It was: " + pos.column);
		
		if(pos.row == 1 && !(pos.column == 1 || pos.column == 6 || pos.column == 7))
			throw new CalcLayoutException("If constraint row is 1, only legal positions are: (1,1), (1,4) and (1,5). Constraint was: " + pos);
		
		if(components.get(pos) != null)
			throw new CalcLayoutException("Component at the position " + pos + " already exists.");
		
		components.put(pos, component);
		
		invalid = true;
	}

	@Override
	public float getLayoutAlignmentX(Container parent) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container parent) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container parent) {
		invalid = true;
	}

	@Override
	public Dimension maximumLayoutSize(Container parent) {
		if(!invalid)
			return new Dimension(maxWidth, maxHeight);
		
		calculatePreferredSizes(parent);
		invalid = false;
		
		return new Dimension(maxWidth, maxHeight);
	}
	
	/**
	 * Calculates minimum, maximum and preferred dimensions for the given container.
	 * 
	 * @param parent container
	 */
	private void calculatePreferredSizes(Container parent) {
		Insets insets = parent.getInsets();
		int vInsets = insets.top + insets.bottom;
		int hInsets = insets.left + insets.right;
		
		preferredWidth = preferredComponentWidth()*MAX_COL + gap*(MAX_COL-1) + hInsets;
		preferredHeight = preferredComponentHeight()*MAX_ROW + gap*(MAX_ROW-1) + vInsets;
		
		minWidth = minComponentWidth();
		minWidth = minWidth < 0 ? preferredWidth : minWidth*MAX_COL + gap*(MAX_COL-1) + hInsets;
		minHeight = minComponentHeight();
		minHeight = minHeight < 0 ? preferredHeight : minHeight*MAX_ROW + gap*(MAX_ROW-1) + vInsets;
		
		maxWidth = maxComponentWidth();
		maxWidth = maxWidth < 0 ? preferredWidth : maxWidth*MAX_COL + gap*(MAX_COL-1) + hInsets;
		maxHeight = maxComponentHeight();
		maxHeight = maxHeight < 0 ? preferredHeight : maxHeight*MAX_ROW + gap*(MAX_ROW-1) + vInsets;
	}
	
	/**
	 * Calculates preferred component width.
	 * 
	 * @return preferred component width
	 */
	private int preferredComponentWidth() {
		int max = -1;
		
		for(Entry<RCPosition, Component> e : components.entrySet()) {
			int width;
			
			if(e.getKey().row == 1 && e.getKey().column == 1) {
				width = (e.getValue().getPreferredSize().width - 4*gap)/5;
			} else {
				width = e.getValue().getPreferredSize().width;
			}
			
			max = Math.max(width, max);
		}
		
		return max;
	}
	
	/**
	 * Calculates minimum component width.
	 * 
	 * @return minimum component width
	 */
	private int minComponentWidth() {
		int max = -1;
		
		for(Entry<RCPosition, Component> e : components.entrySet()) {
			int width;
			
			if(e.getKey().row == 1 && e.getKey().column == 1) {
				width = (e.getValue().getMinimumSize().width - 4*gap)/5;
			} else {
				width = e.getValue().getMinimumSize().width;
			}
			
			max = Math.max(width, max);
		}
		
		return max;
	}
	
	/**
	 * Calculates maximum component width.
	 * 
	 * @return maximum component width
	 */
	private int maxComponentWidth() {
		int max = -1;
		
		for(Entry<RCPosition, Component> e : components.entrySet()) {
			int width;
			
			if(e.getKey().row == 1 && e.getKey().column == 1) {
				width = (e.getValue().getMaximumSize().width - 4*gap)/5;
			} else {
				width = e.getValue().getMaximumSize().width;
			}
			
			max = Math.max(width, max);
		}
		
		return max;
	}
	
	/**
	 * Calculates preferred component height.
	 * 
	 * @return preferred component height
	 */
	private int preferredComponentHeight() {
		int max = -1;
		
		for(Entry<RCPosition, Component> e : components.entrySet()) {
			int width = e.getValue().getPreferredSize().height;
			
			max = Math.max(width, max);
		}
		
		return max;
	}
	
	/**
	 * Calculates minimum component height.
	 * 
	 * @return minimum component height
	 */
	private int minComponentHeight() {
		int max = -1;
		
		for(Entry<RCPosition, Component> e : components.entrySet()) {
			int width = e.getValue().getMinimumSize().height;
			
			max = Math.max(width, max);
		}
		
		return max;
	}
	
	/**
	 * Calculates maximum component height.
	 * 
	 * @return maximum component height
	 */
	private int maxComponentHeight() {
		int max = -1;
		
		for(Entry<RCPosition, Component> e : components.entrySet()) {
			int width = e.getValue().getMaximumSize().height;
			
			max = Math.max(width, max);
		}
		
		return max;
	}

}
