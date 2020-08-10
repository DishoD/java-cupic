package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.SingleDocumentModel;

/**
 * Status bar of the JNotepadpp program. Tracks length of the file, current
 * position in file, number of selected characters and current date and time.
 * Displays that information in the three part panel. Important note: before
 * disposing the JFrame that this status bar will be attached to, you must
 * call disconnect() method or your application might not close properly.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class JStatusBar extends JPanel {
	/**
	 * for grid layout: number of rows
	 */
	private static final int LAYOUT_ROWS = 1;
	/**
	 * for grid layout: number of columns
	 */
	private static final int LAYOUT_COLS = 3;

	/**
	 * status bar tracks information for this multiple document model
	 */
	private MultipleDocumentModel model;
	/**
	 * current document in multiple document model,
	 * the information of the status bar is derived from this document
	 */
	private SingleDocumentModel currentDocument;

	/**
	 * length (number of characters) in the current document
	 */
	private int len = 0;
	/**
	 * line of the current caret position in the document
	 */
	private int ln = 0;
	/**
	 * column of the current caret position in the document
	 */
	private int col = 0;
	/**
	 * number of the selected characters in the current document
	 */
	private int sel = 0;

	/**
	 * tells if the current date and time is being tracked
	 * (used for killing the thread that periodically updates the date and time label)
	 */
	private boolean connected = true;

	/**
	 * label that display the length of the document
	 */
	private JLabel length = new JLabel();
	/**
	 * label that display the caret position and number of selected characters
	 */
	private JLabel posSel = new JLabel();
	/**
	 * label that displays current date and time
	 */
	private JLabel time = new JLabel();

	/**
	 * format of displaying date and time
	 */
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	/**
	 * tracks changes in the current document caret and updates the status bar info accordingly
	 */
	private final ChangeListener CurrentDocumentCaretLisener = l -> refresh();
	
	/**
	 * tracks changes in the current document and updates the status bar info accordingly
	 */
	private final SingleDocumentListener CurrentDocumentListener = new SingleDocumentListener() {

		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			refresh();
		}

		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
		}
	};

	/**
	 * Initializes the status bar for the given multiple document model.
	 * 
	 * @param model multiple document model for which this status bar will track info
	 */
	public JStatusBar(MultipleDocumentModel model) {
		this.model = model;
		currentDocument = model.getCurrentDocument();
		refresh();
		updateTime();

		setLayout(new GridLayout(LAYOUT_ROWS, LAYOUT_COLS));
		setBorder(BorderFactory.createLoweredBevelBorder());

		length.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		posSel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

		time.setHorizontalAlignment(JLabel.RIGHT);

		add(length);
		add(posSel);
		add(time);

		model.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				if (JStatusBar.this.model.getNumberOfDocuments() == 0) {
					currentDocument = null;
					refresh();
				}
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if (previousModel != null) {
					previousModel.removeSingleDocumentListener(CurrentDocumentListener);
					previousModel.getTextComponent().getCaret().removeChangeListener(CurrentDocumentCaretLisener);
				}

				currentDocument = currentModel;
				currentDocument.addSingleDocumentListener(CurrentDocumentListener);

				currentModel.getTextComponent().getCaret().addChangeListener(CurrentDocumentCaretLisener);
				refresh();
			}
		});

		Thread timeUpdater = new Thread(() -> {
			while (connected) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException ignorable) {

				}

				SwingUtilities.invokeLater(() -> updateTime());
			}
		});

		timeUpdater.setDaemon(true);
		timeUpdater.start();
	}

	/**
	 * Updates the status bar info for the current status in the current document.
	 */
	private void refresh() {
		if (currentDocument == null) {
			length.setText("");
			posSel.setText("");
			return;
		}

		JTextArea ta = currentDocument.getTextComponent();

		len = ta.getText().length();
		try {
			ln = ta.getLineOfOffset(ta.getCaretPosition());
			col = ta.getCaretPosition() - ta.getLineStartOffset(ln);
		} catch (BadLocationException e) {
			ln = 0;
			col = 0;
		}
		sel = Math.abs(ta.getCaret().getMark() - ta.getCaret().getDot());

		length.setText(String.format(" Length: %d", len));
		posSel.setText(String.format(" Ln: %d   Col: %d   Sel: %d", ln, col, sel));
	}

	/**
	 * Call before disposing the JFrame that this status bar is attached to.
	 * Kills the thread that periodically refreshes the current date and time.
	 */
	public void disconnect() {
		connected = false;
	}

	/**
	 * Updates the date and time label for the currend date and time.
	 */
	private void updateTime() {
		time.setText(formatter.format(LocalDateTime.now()));
	}

}
