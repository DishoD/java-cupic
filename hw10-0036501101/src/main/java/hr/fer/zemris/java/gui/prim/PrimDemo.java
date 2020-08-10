package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * Simple demonstration program of lists that consist of prime numbers.
 * If you wish to add next prime number to the list, click 'sljedeći' button.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class PrimDemo extends JFrame {
	private PrimListModel model = new PrimListModel();
	
	/**
	 * Creates the window with two lists and one button for generating new prime numbers in the lists.
	 */
	public PrimDemo() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("PrimDemo");
		setSize(300, 300);
		setLocation(200, 200);
		
		initGui();
	}

	/**
	 * Creates all the gui components of the window.
	 */
	private void initGui() {
		Container cp = getContentPane();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(list1), new JScrollPane(list2));
		split.setResizeWeight(0.5);
		
		cp.add(split, BorderLayout.CENTER);
		
		Container bottom = new Container();
		bottom.setLayout(new FlowLayout());
		
		JButton btn = new JButton("sljedeći");
		btn.addActionListener(e -> model.next());
		bottom.add(btn);
		
		cp.add(bottom, BorderLayout.SOUTH);
	}
	
	/**
	 * Main method. Starts the gui.
	 * 
	 * @param args igorable
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new PrimDemo().setVisible(true);
		});
	}
}
