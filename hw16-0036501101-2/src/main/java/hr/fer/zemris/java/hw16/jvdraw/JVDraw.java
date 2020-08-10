package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw16.jvdraw.actions.AbstractAction;
import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.components.JColorTracker;
import hr.fer.zemris.java.hw16.jvdraw.components.JDrawingCanvas;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.visitors.BBCalc;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.visitors.ObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelImpl;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.states.CircleState1;
import hr.fer.zemris.java.hw16.states.FilledCircleState1;
import hr.fer.zemris.java.hw16.states.LineState1;
import hr.fer.zemris.java.hw16.states.PolygonState1;
import hr.fer.zemris.java.hw16.states.Tool;

/**
 * A simple paint application that draws lines and circles.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class JVDraw extends JFrame {
	/**
	 * windows title
	 */
	private static final String WINDOW_TITLE = "JVDraw";

	/**
	 * drawing model
	 */
	private DrawingModel drawingModel = new DrawingModelImpl();
	/**
	 * currently selected foreground color
	 */
	private JColorArea foregroundColor = new JColorArea(Color.red);
	/**
	 * currently selected background color
	 */
	private JColorArea backgroundColor = new JColorArea(Color.blue);
	/**
	 * current state of the selected tool
	 */
	private Tool toolState = new LineState1(this);
	/**
	 * drawing canvas
	 */
	private JDrawingCanvas drawingCanvas = new JDrawingCanvas(this);
	/**
	 * list model for geometrical objects
	 */
	private ListModel<GeometricalObject> objectsListModel = new DrawingObjectListModel(drawingModel);
	/**
	 * list of objects
	 */
	private JList<GeometricalObject> objectsList = new JList<>(objectsListModel);
	/**
	 * helper class for opening and saving files
	 */
	private FileManagment fileManager = new FileManagment(this);

	/**
	 * Initializes the JVDraw window
	 */
	public JVDraw() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle(WINDOW_TITLE);
		setSize(800, 500);

		initGUI();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ExitAction.actionPerformed(null);
			}
		});
	}

	/**
	 * Initializes the gui
	 */
	private void initGUI() {
		Container cp = getContentPane();

		JToolBar toolBar = new JToolBar();
		cp.add(toolBar, BorderLayout.NORTH);

		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		toolBar.add(foregroundColor);
		toolBar.add(backgroundColor);

		JToggleButton btnLine = new JToggleButton("Line");
		btnLine.addActionListener(l -> {
			setToolState(new LineState1(JVDraw.this));
		});
		btnLine.setSelected(true);

		JToggleButton btnCircle = new JToggleButton("Circle");
		btnCircle.addActionListener(l -> {
			setToolState(new CircleState1(JVDraw.this));
		});

		JToggleButton btnFilledCircle = new JToggleButton("Filled Circle");
		btnFilledCircle.addActionListener(l -> {
			setToolState(new FilledCircleState1(JVDraw.this));
		});
		
		JToggleButton btnPolygon = new JToggleButton("Polygon");
		btnPolygon.addActionListener(l -> {
			setToolState(new PolygonState1(JVDraw.this));
		});

		ButtonGroup bg = new ButtonGroup();
		bg.add(btnLine);
		bg.add(btnCircle);
		bg.add(btnFilledCircle);
		bg.add(btnPolygon);

		toolBar.add(btnLine);
		toolBar.add(btnCircle);
		toolBar.add(btnFilledCircle);
		toolBar.add(btnPolygon);

		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);

		JMenu file = new JMenu("File");
		file.add(OpenAction);
		file.add(SaveAction);
		file.add(SaveAsAction);
		file.addSeparator();
		file.add(ExportAction);
		file.addSeparator();
		file.add(ExitAction);
		mb.add(file);

		JColorTracker colorTracker = new JColorTracker(foregroundColor, backgroundColor);
		cp.add(colorTracker, BorderLayout.SOUTH);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, drawingCanvas, new JScrollPane(objectsList));
		split.setResizeWeight(0.99);
		cp.add(split, BorderLayout.CENTER);

		objectsList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				GeometricalObject obj = objectsList.getSelectedValue();
				if (obj == null)
					return;

				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					drawingModel.remove(obj);
				}

				int index = objectsList.getSelectedIndex();
				try {
					if (e.getKeyCode() == KeyEvent.VK_PLUS) {
						drawingModel.changeOrder(obj, 1);
						objectsList.setSelectedIndex(index + 1);
					}

					if (e.getKeyCode() == KeyEvent.VK_MINUS) {
						drawingModel.changeOrder(obj, -1);
						objectsList.setSelectedIndex(index - 1);
					}
				} catch (IllegalArgumentException ignorable) {
				}
			}
		});

		objectsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				GeometricalObject obj = objectsList.getSelectedValue();
				if (obj == null)
					return;

				if (e.getClickCount() == 2) {
					GeometricalObjectEditor editor = obj.createGeometricalObjectEditor();

					while (true) {
						if (JOptionPane.showConfirmDialog(JVDraw.this, editor, "Edit component",
								JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
							try {
								editor.checkEditing();
								editor.acceptEditing();
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(JVDraw.this, ex.getMessage());
								continue;
							}
							break;
						}
						break;
					}
				}
			}
		});

	}

	/**
	 * open action
	 */
	private final Action OpenAction = new AbstractAction("Open", "control O") {

		@Override
		public void actionPerformed(ActionEvent e) {
			fileManager.open();
		}
	};

	/**
	 * save action
	 */
	private final Action SaveAction = new AbstractAction("Save", "control S") {

		@Override
		public void actionPerformed(ActionEvent e) {
			fileManager.save();
		}
	};

	/**
	 * save as action
	 */
	private final Action SaveAsAction = new AbstractAction("Save As", "control alt S") {

		@Override
		public void actionPerformed(ActionEvent e) {
			fileManager.saveAs();
		}
	};

	/**
	 * exit action
	 */
	private final Action ExitAction = new AbstractAction("Exit", null) {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (fileManager.isModified()) {
				int res = JOptionPane.showConfirmDialog(JVDraw.this,
						"Do you wish to save modified file berofe closing?");
				if (res == JOptionPane.CANCEL_OPTION)
					return;
				if (res == JOptionPane.YES_OPTION) {
					fileManager.save();
				}
			}
			JVDraw.this.dispose();
		}
	};

	/**
	 * export action
	 */
	private final Action ExportAction = new AbstractAction("Export", null) {

		@Override
		public void actionPerformed(ActionEvent e) {
			BBCalc bbcalc = new BBCalc();
			for (int i = 0; i < drawingModel.getSize(); ++i) {
				drawingModel.getObject(i).accept(bbcalc);
			}

			Rectangle box = bbcalc.getBoundingBox();
			BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = image.createGraphics();
			AffineTransform transform = g.getTransform();
			transform.translate(-box.x, -box.y);
			g.setTransform(transform);
			g.setColor(Color.white);
			g.fillRect(box.x, box.y, box.width, box.height);
			GeometricalObjectVisitor painter = new ObjectPainter(g);
			for (int i = 0; i < drawingModel.getSize(); ++i) {
				drawingModel.getObject(i).accept(painter);
			}
			g.dispose();
			
			JFileChooser fc = new JFileChooser(".");
			String filename;
			while(true) {
				if (fc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION)
					return;
				
				filename = fc.getSelectedFile().getAbsolutePath();
				if(filename.endsWith(".png") || filename.endsWith(".gif") || filename.endsWith(".jpg")) break;
				JOptionPane.showMessageDialog(JVDraw.this, "The file must have an extension: .png, .jpg or .gif.");
			}

			Path savePath = Paths.get(filename);
			if(Files.exists(savePath)) {
				int res = JOptionPane.showConfirmDialog(JVDraw.this,
						"Do you wish to overwrite the choosen file??");
				if (res != JOptionPane.YES_OPTION) {
					return;
				}
			}
			
			String extension = filename.substring(filename.lastIndexOf('.') + 1);
			File file = savePath.toFile();
			try {
				ImageIO.write(image, extension, file);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(JVDraw.this, "Error ocurred while exporting!");
				return;
			}
			
			JOptionPane.showMessageDialog(JVDraw.this, "Image successfully exported!");
		}

	};

	/**
	 * Gets drawing model.
	 * 
	 * @return drawing model
	 */
	public DrawingModel getDrawingModel() {
		return drawingModel;
	}

	/**
	 * Gets currently selected foreground color
	 * @return currently selected foreground color
	 */
	public Color getForegroundColor() {
		return foregroundColor.getCurrentColor();
	}

	/**
	 * Gets currently selected background color
	 * @return currently selected background color
	 */
	public Color getBackgroundColor() {
		return backgroundColor.getCurrentColor();
	}

	/**
	 * Gets current tool state.
	 * 
	 * @return current tool state
	 */
	public Tool getToolState() {
		return toolState;
	}

	/**
	 * Sets current tool state.
	 * @param toolState current tool state
	 */
	public void setToolState(Tool toolState) {
		this.toolState = toolState;
	}

	/**
	 * Gets drawing canvas
	 * @return drawing canvas
	 */
	public JDrawingCanvas getDrawingCanvas() {
		return drawingCanvas;
	}

	/**
	 * Main method. Creates the window of the application.
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JVDraw().setVisible(true);
		});
	}
}
