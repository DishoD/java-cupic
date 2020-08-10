package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.visitors.ObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelListener;


/**
 * Drawing canvas for the JVDraw class.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class JDrawingCanvas extends JComponent{
	/**
	 * parent jvdraw class
	 */
	private JVDraw context;
	
	/**
	 * Initializes the component for the given JVDraw object.
	 * 
	 * @param context JVDraw object
	 */
	public JDrawingCanvas(JVDraw context) {
		this.context = context;
		
		context.getDrawingModel().addDrawingModelListener(new DrawingModelListener() {
			
			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				repaint();
			}
			
			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				repaint();
			}
			
			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				repaint();
			}
		});
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				context.getToolState().mouseReleased(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				context.getToolState().mousePressed(e);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				context.getToolState().mouseClicked(e);
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				context.getToolState().mouseMoved(e);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				context.getToolState().mouseDragged(e);
			}
		});
	}



	@Override
	public void paint(Graphics g) {
		int horizontalInset =  getInsets().left + getInsets().right;
		int verticalInset =  getInsets().top + getInsets().bottom;
		Dimension dim = getSize();
		
		g.setColor(Color.white);
		g.fillRect(getInsets().left, getInsets().top, dim.width - horizontalInset, dim.height - verticalInset);
		
		GeometricalObjectVisitor visitor = new ObjectPainter((Graphics2D) g);
		DrawingModel model = context.getDrawingModel();
		for(int i = 0; i < model.getSize(); ++i) {
			model.getObject(i).accept(visitor);
		}
		
		context.getToolState().paint((Graphics2D) g);
	}
}
