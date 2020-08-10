package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Line;
import hr.fer.zemris.java.hw16.jvdraw.geometricalobjects.Polygon;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelListener;

/**
 * JVDraw helper class that manages opening and saving files.
 * 
 * @author Disho
 *
 */
public class FileManagment {
	/**
	 * path of the current file, null if not saved
	 */
	private Path path;
	/**
	 * is current file modified
	 */
	private boolean isModified;
	/**
	 * JVDraw parent object
	 */
	private JVDraw context;

	/**
	 * Initializes the file manager for the given JVDraw object.
	 * 
	 * @param context
	 */
	public FileManagment(JVDraw context) {
		this.context = context;

		context.getDrawingModel().addDrawingModelListener(new DrawingModelListener() {

			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				isModified = true;
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				isModified = true;
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				isModified = true;
			}
		});
	}

	/**
	 * Deletes all objects from the context's draw model.
	 */
	private void clearDrawingModel() {
		DrawingModel model = context.getDrawingModel();

		while (model.getSize() > 0) {
			int index = model.getSize() - 1;
			model.remove(model.getObject(index));
		}
	}

	/**
	 * Converts a geometrical object to string representation for the file saving.
	 * 
	 * @param obj geometrical object
	 * @return string representation of the object
	 */
	private String GeometricalObjectToLine(GeometricalObject obj) {
		if (obj instanceof Line) {
			Line l = (Line) obj;
			return String.format("LINE %d %d %d %d %d %d %d", l.getStart().x, l.getStart().y, l.getEnd().x,
					l.getEnd().y, l.getColor().getRed(), l.getColor().getGreen(), l.getColor().getBlue());
		}
		
		if (obj instanceof FilledCircle) {
			FilledCircle c = (FilledCircle) obj;
			return String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", c.getCenter().x, c.getCenter().y, c.getRadius(),
					c.getOutlineColor().getRed(), c.getOutlineColor().getGreen(), c.getOutlineColor().getBlue(),
					c.getFillColor().getRed(), c.getFillColor().getGreen(), c.getFillColor().getBlue());
		}
		
		if (obj instanceof Circle) {
			Circle c = (Circle) obj;
			return String.format("CIRCLE %d %d %d %d %d %d", c.getCenter().x, c.getCenter().y, c.getRadius(),
					c.getOutlineColor().getRed(), c.getOutlineColor().getGreen(), c.getOutlineColor().getBlue());
		}
		
		if (obj instanceof Polygon) {
			Polygon p = (Polygon) obj;
			StringBuilder sb = new StringBuilder();
			sb.append("FPOLY ");
			int l = p.getNumberOfPoints();
			sb.append(l).append(" ");
			
			for(int i = 0; i < l; ++i) {
				sb.append(p.getPoint(i).x).append(" ");
				sb.append(p.getPoint(i).y).append(" ");
			}
			
			sb.append(p.getOutlineColor().getRed()).append(" ");
			sb.append(p.getOutlineColor().getGreen()).append(" ");
			sb.append(p.getOutlineColor().getBlue()).append(" ");
			
			sb.append(p.getFillColor().getRed()).append(" ");
			sb.append(p.getFillColor().getGreen()).append(" ");
			sb.append(p.getFillColor().getBlue()).append(" ");
			
			return sb.toString();
		}

		return null;
	}

	/**
	 * Parses the line of the saved file and returns the corresponding geometrical object.
	 * Or throws an runtime exception if the line is invalid.
	 * 
	 * @param line line to parse
	 * @return geometrical object
	 */
	private GeometricalObject parseLine(String line) {
		String[] splitted = line.split(" ");

		if (splitted[0].equals("LINE")) {
			return new Line(new Point(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2])),
					new Point(Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4])),
					new Color(Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6]),
							Integer.parseInt(splitted[7])));
		}

		if (splitted[0].equals("CIRCLE")) {
			return new Circle(new Point(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2])),
					Integer.parseInt(splitted[3]), new Color(Integer.parseInt(splitted[4]),
							Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6])));
		}

		if (splitted[0].equals("FCIRCLE")) {
			return new FilledCircle(new Point(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2])),
					Integer.parseInt(splitted[3]),
					new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]),
							Integer.parseInt(splitted[6])),
					new Color(Integer.parseInt(splitted[7]), Integer.parseInt(splitted[8]),
							Integer.parseInt(splitted[9])));
		}
		
		if (splitted[0].equals("FPOLY")) {
			int n = Integer.parseInt(splitted[1]);
			Polygon p = new Polygon(null, null);
			for(int i = 0; i < n; ++i) {
				int x = Integer.parseInt(splitted[2+2*i]);
				int y = Integer.parseInt(splitted[3+2*i]);
				p.addPoint(new Point(x, y));
			}
			
			int last = 2+2*n;
			Color outline = new Color(Integer.parseInt(splitted[last]), Integer.parseInt(splitted[last+1]), Integer.parseInt(splitted[last+2]));
			Color fill = new Color(Integer.parseInt(splitted[last+3]), Integer.parseInt(splitted[last+4]), Integer.parseInt(splitted[last+5]));
			p.setFillColor(fill);
			p.setOutlineColor(outline);
			return p;
		}

		throw new RuntimeException();
	}

	/**
	 * Shows and open dialog and opens the chossend jvd file.
	 */
	public void open() {
		if (isModified) {
			int res = JOptionPane.showConfirmDialog(context,
					"Do you wish to save current file before opening a new file?");
			if (res == JOptionPane.CANCEL_OPTION)
				return;
			if (res == JOptionPane.YES_OPTION) {
				save();
			}
		}

		Path newPath = null;

		while (true) {
			JFileChooser fc = new JFileChooser(".");
			fc.addChoosableFileFilter(new FileNameExtensionFilter("JVDraw file", "jvd"));
			fc.setAcceptAllFileFilterUsed(false);
			if (fc.showOpenDialog(context) != JFileChooser.APPROVE_OPTION)
				return;

			newPath = Paths.get(fc.getSelectedFile().getAbsolutePath());
			if (!newPath.toString().endsWith(".jvd")) {
				JOptionPane.showMessageDialog(context, "Selected file is not .jvd file.");
			} else {
				break;
			}
		}

		try {
			List<String> lines = Files.readAllLines(newPath);

			clearDrawingModel();
			for (String line : lines) {
				if (line.isEmpty())
					continue;
				context.getDrawingModel().add(parseLine(line));
			}

			isModified = false;
			path = newPath;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(context,
					"Error occured while reading the file. Maybe the file is corrupted.");
			return;
		}
	}

	/**
	 * Saves the current file.
	 */
	public void save() {
		if (isModified == false)
			return;
		if (path == null) {
			saveAs();
			return;
		}

		try {
			save(path);
			isModified = false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(context, "Error occured while saving the file.");
		}
	}

	/**
	 * Runs a save as action for the current file.
	 */
	public void saveAs() {
		Path savePath;

		JFileChooser fc = new JFileChooser(".");
		if (fc.showSaveDialog(context) != JFileChooser.APPROVE_OPTION)
			return;

		savePath = Paths.get(fc.getSelectedFile().getAbsolutePath());
		if(Files.exists(savePath)) {
			int res = JOptionPane.showConfirmDialog(context,
					"Do you wish to overwrite the choosen file??");
			if (res != JOptionPane.YES_OPTION) {
				return;
			}
		}
		if (!savePath.toString().endsWith(".jvd")) {
			savePath = Paths.get(savePath.toString() + ".jvd");
		}

		try {
			save(savePath);
			path = savePath;
			isModified = false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(context, "Error occured while saving the file.");
		}
	}

	private void save(Path savePath) throws IOException {
		DrawingModel model = context.getDrawingModel();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < model.getSize(); ++i) {
			sb.append(GeometricalObjectToLine(model.getObject(i))).append('\n');
		}

		Files.write(savePath, sb.toString().getBytes());
	}
	
	/**
	 * Tells if the current file has been modified.
	 * 
	 * @return modified flag
	 */
	public boolean isModified() {
		return isModified;
	}
}
