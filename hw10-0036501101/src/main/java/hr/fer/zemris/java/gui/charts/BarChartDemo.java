package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Simple command-line program that takes path to a file as an argument.
 * That file is a description of a bar chart. The program draws an bar chart to the screen.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class BarChartDemo extends JFrame {
	
	/**
	 * Initializes the window with the given model and draws an bar char to the screen.
	 * 
	 * @param model bar chart model
	 */
	public BarChartDemo(BarChart model) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Bar Chart Demo");
		setSize(600, 400);
		
		Container cp = getContentPane();
		BarChartComponent chart = new BarChartComponent(model);
		cp.add(chart, BorderLayout.CENTER);
		cp.setBackground(Color.WHITE);
		cp.setForeground(new Color(255,	158, 87));
	}
	
	/**
	 * Main method. Controls the flow of the program.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Program expects one argument: a path to a file that describes a bar chart. Got " + args.length + " arguments.");
			return;
		}
		
		Path path = Paths.get(args[0]);
		
		if(!Files.isRegularFile(path)) {
			System.out.println("Given path " + path + "is either not valid or is not a file.");
			return;
		}
		
		List<String> lines = null;
		
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			System.out.println("Could not read a given file.\nExiting...");
			return;
		}
		
		String xTitle = lines.get(0);
		String yTitle = lines.get(1);
		String[] values = lines.get(2).split("\\s+");
		List<XYValue> xyValues = new ArrayList<>();
		
		for(String value : values) {
			String[] xyValue = value.split(",");
			if(xyValue.length != 2) {
				System.out.println("Illegal format: " + value);
				return;
			}
			
			try {
				xyValues.add(new XYValue(Integer.parseInt(xyValue[0]), Integer.parseInt(xyValue[1])));
			} catch (NumberFormatException e) {
				System.out.println("Illegal format. Given values must be integer numbers: " + value);
				return;
			} catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
				return;
			}
		}
		
		int yMin;
		int yMax;
		int yGap;
		
		try {
			yMin = Integer.parseInt(lines.get(3));
			yMax = Integer.parseInt(lines.get(4));
			yGap = Integer.parseInt(lines.get(5));
		} catch(NumberFormatException e) {
			System.out.println("All values should be integers!");
			return;
		}
		
		BarChart model = new BarChart(xyValues, xTitle, yTitle, yMin, yMax, yGap);
		
		SwingUtilities.invokeLater(()->{
			new BarChartDemo(model).setVisible(true);
		});
		
	}
}
