package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IColorProvider;

/**
 * A simple button that remembers the selected color.
 * The selected color is displayed on the whole surface of the button.
 * When clicked on the button the color picker will show up.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class JColorArea extends JComponent implements IColorProvider {
	/**
	 * currently selected color
	 */
	private Color selectedColor;
	
	/**
	 * Preferred dimensions of the component
	 */
	private static final Dimension PREFFERED_DIMENSION = new Dimension(15, 15);
	
	/**
	 * list of registered listeners
	 */
	private List<ColorChangeListener> listeners = new ArrayList<>();
	
	/**
	 * Initializes the component with the given color.
	 * 
	 * @param selectedColor initial selected color
	 */
	public JColorArea(Color selectedColor) {
		Objects.requireNonNull(selectedColor);
		this.selectedColor = selectedColor;
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color newColor = JColorChooser.showDialog(JColorArea.this, "Choose color", selectedColor);
				if(newColor == null) return;
				
				Color oldColor = JColorArea.this.selectedColor;
				JColorArea.this.selectedColor = newColor;
				notifyListeners(oldColor, newColor);
				repaint();
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		int horizontalInset =  getInsets().left + getInsets().right;
		int verticalInset =  getInsets().top + getInsets().bottom;
		Dimension dim = getSize();
		
		g.setColor(selectedColor);
		g.fillRect(getInsets().left, getInsets().top, dim.width - horizontalInset, dim.height - verticalInset);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return PREFFERED_DIMENSION;
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies the registered listeners of the color change.
	 * 
	 * @param oldColor old color
	 * @param newColor currently selected color
	 */
	private void notifyListeners(Color oldColor, Color newColor) {
		for(ColorChangeListener ccl : listeners) {
			ccl.newColorSelected(this, oldColor, newColor);
		}
	}

}
