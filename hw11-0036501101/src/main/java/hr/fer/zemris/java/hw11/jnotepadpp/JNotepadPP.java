package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import hr.fer.zemris.java.hw11.jnotepadpp.actions.LCaseAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.LChangeLanguageAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.LReplaceSelectedLinesAction;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * A simple notepade like program.
 * It can open, modify, crate and save multiple text documents.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class JNotepadPP extends JFrame {
	/**
	 * multiple document model, takes care of all opened documents
	 */
	private DefaultMultipleDocumentModel model = new DefaultMultipleDocumentModel();
	/**
	 * localization provider for internationalization (i18n), adjusted for use with JFrames
	 */
	private LocalizationProviderBridge localizationProvider = new FormLocalizationProvider(
																	LocalizationProvider.getInstance(), this);
	/**
	 * window title
	 */
	private static final String TITLE = "JNotepad++";
	
	/**
	 * status bar that displays length of the file, current position in file, 
	 * number of selected characters and current date and time
	 */
	private JStatusBar statusBar = new JStatusBar(model);

	/**
	 * Initializes the window.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); 
		setTitle(TITLE);
		setSize(1000, 800);
		setLocation(100, 100);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				ExitAction.actionPerformed(null);
			}
		});

		initGUI();
	}

	/**
	 * Initializes windows GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();

		cp.add((JTabbedPane) model, BorderLayout.CENTER);
		cp.add(statusBar, BorderLayout.SOUTH);

		initializeActions();
		createMenuBar();
		createToolBar();
	}

	/**
	 * Creates windows tool bar.
	 */
	private void createToolBar() {
		JToolBar toolBar = new JToolBar();
		
		toolBar.add(NewDocumentAction);
		toolBar.add(OpenAction);
		toolBar.addSeparator();
		
		toolBar.add(SaveAction);
		toolBar.add(SaveAsAction);
		
		toolBar.addSeparator();
		toolBar.add(CutAction);
		toolBar.add(CopyAction);
		toolBar.add(PasteAction);
		
		toolBar.addSeparator();
		toolBar.add(CloseAction);
		
		toolBar.addSeparator();
		toolBar.add(StatisticsAction);
		
		toolBar.addSeparator();
		toolBar.add(ExitAction);
		
		toolBar.addSeparator();
		toolBar.add(LEONA);
		
		
		add(toolBar, BorderLayout.NORTH);
	}

	/**
	 * Initializes beginning states of notepad actions and registers listeners to 
	 * the multiple document model for tracking current state of documents.
	 */
	private void initializeActions() {

		SaveAction.setEnabled(false);
		SaveAsAction.setEnabled(false);
		StatisticsAction.setEnabled(false);
		CloseAction.setEnabled(false);
		
		CutAction.setEnabled(false);
		CopyAction.setEnabled(false);
		PasteAction.setEnabled(false);
		
		ToLowerCaseAction.setEnabled(false);
		ToUpperCaseAction.setEnabled(false);
		InvertCaseAction.setEnabled(false);
		
		AscendingLinesSortAction.setEnabled(false);
		DescendingLinesSortAction.setEnabled(false);
		UniqueLinesAction.setEnabled(false);
		
		LEONA.setEnabled(false);

		model.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				if (JNotepadPP.this.model.getNumberOfDocuments() == 0) {
					SaveAsAction.setEnabled(false);
					CloseAction.setEnabled(false);
					StatisticsAction.setEnabled(false);
					CutAction.setEnabled(false);
					CopyAction.setEnabled(false);
					PasteAction.setEnabled(false);
					setTitle(TITLE);
				}
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				if (JNotepadPP.this.model.getNumberOfDocuments() > 0) {
					SaveAsAction.setEnabled(true);
					CloseAction.setEnabled(true);
					StatisticsAction.setEnabled(true);
					PasteAction.setEnabled(true);
				}
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if (previousModel != null) {
					previousModel.removeSingleDocumentListener(currentDocumentListener);
					previousModel.getTextComponent().removeCaretListener(currentDocumentCaretListener);
				}
				
				currentModel.addSingleDocumentListener(currentDocumentListener);
				currentModel.getTextComponent().addCaretListener(currentDocumentCaretListener);

				Path path = currentModel.getFilePath();
				String str = path == null ? "new document" : path.toAbsolutePath().toString();
				setTitle(str + " - " + TITLE);
				
				SaveAction.setEnabled(currentModel.isModified());
				CutAction.setEnabled(currentModel.getTextComponent().getSelectedText() != null);
				CopyAction.setEnabled(currentModel.getTextComponent().getSelectedText() != null);
				
			}
		});
	}

	/**
	 * Creates menu bar.
	 */
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		LJMenu fileMenu = new LJMenu("file", localizationProvider);
		fileMenu.add(NewDocumentAction);
		fileMenu.add(OpenAction);
		
		fileMenu.addSeparator();
		fileMenu.add(SaveAction);
		fileMenu.add(SaveAsAction);
		
		fileMenu.addSeparator();
		fileMenu.add(CloseAction);
		
		fileMenu.addSeparator();
		fileMenu.add(StatisticsAction);
		
		fileMenu.addSeparator();
		fileMenu.add(ExitAction);
		
		LJMenu editMenu = new LJMenu("edit", localizationProvider);
		editMenu.add(CutAction);
		editMenu.add(CopyAction);
		editMenu.add(PasteAction);
		
		LJMenu toolsMenu = new LJMenu("tools", localizationProvider);
		LJMenu caseMenu = new LJMenu("case", localizationProvider);
		toolsMenu.add(caseMenu);
		caseMenu.add(ToUpperCaseAction);
		caseMenu.add(ToLowerCaseAction);
		caseMenu.add(InvertCaseAction);
		
		LJMenu sortMenu = new LJMenu("sort", localizationProvider);
		toolsMenu.add(sortMenu);
		sortMenu.add(AscendingLinesSortAction);
		sortMenu.add(DescendingLinesSortAction);
		
		toolsMenu.add(UniqueLinesAction);

		LJMenu langMenu = new LJMenu("languages", localizationProvider);
		langMenu.add(new LChangeLanguageAction("hr", localizationProvider, null));
		langMenu.add(new LChangeLanguageAction("en", localizationProvider, null));
		langMenu.add(new LChangeLanguageAction("de", localizationProvider, null));

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(toolsMenu);
		menuBar.add(langMenu);
		
		setJMenuBar(menuBar);
	}

	/**
	 * Creates new empty document.
	 */
	private final LocalizableAction NewDocumentAction = new LocalizableAction("newDocument", localizationProvider, "control N") {

		@Override
		public void actionPerformed(ActionEvent e) {
			model.createNewDocument();
		}
	};

	/**
	 * Opens file from the file system and loads it as a document for modifying.
	 */
	private final LocalizableAction OpenAction = new LocalizableAction("open", localizationProvider, "control O") {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser(".");
			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION)
				return;

			Path path = Paths.get(fc.getSelectedFile().getAbsolutePath());
			model.loadDocument(path);
		}
	};

	/**
	 * Saves the current document or if its a new document runs save-as action.
	 */
	private final LocalizableAction SaveAction = new LocalizableAction("save", localizationProvider, "control S") {

		@Override
		public void actionPerformed(ActionEvent e) {
			Path filePath = model.getCurrentDocument().getFilePath();
			if (filePath == null) {
				JFileChooser fc = new JFileChooser(".");
				if (fc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION)
					return;

				Path path = Paths.get(fc.getSelectedFile().getAbsolutePath());
				model.saveDocument(model.getCurrentDocument(), path);
				return;
			}

			model.saveDocument(model.getCurrentDocument(), filePath);
		}
	};

	/**
	 * Asks user to choose where to save the current document and saves it there.
	 */
	private final LocalizableAction SaveAsAction = new LocalizableAction("saveAs", localizationProvider,
			"control alt S") {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser(".");
			if (fc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION)
				return;

			Path path = Paths.get(fc.getSelectedFile().getAbsolutePath());

			if (Files.exists(path)) {
				if (JOptionPane.showConfirmDialog(JNotepadPP.this,
						"Do you wish to overwrite chosen file?") != JOptionPane.YES_OPTION)
					return;
			}

			model.saveDocument(model.getCurrentDocument(), path);

		}
	};

	/**
	 * Closes the current document (tab). If document was modified (denoted by the red diskette) 
	 * prompts user if he want to save the file before closing.
	 */
	private final LocalizableAction CloseAction = new LocalizableAction("close", localizationProvider, "control W") {

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel current = model.getCurrentDocument();

			if (current.isModified()) {
				int res = JOptionPane.showConfirmDialog(JNotepadPP.this,
						"Do you wish to save modified file berofe closing?");
				if (res == JOptionPane.CANCEL_OPTION)
					return;
				if (res == JOptionPane.YES_OPTION) {
					current.setModified(false);
					SaveAction.actionPerformed(null);
				}
			}

			model.closeDocument(current);
		}
	};
	
	/**
	 * Shows user some useful statistics about the document: number of characters, number of non-blank characters
	 * and number of lines in the document.
	 */
	private final LocalizableAction StatisticsAction = new LocalizableAction("statistics", localizationProvider, "control Q") {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String text = model.getCurrentDocument().getTextComponent().getText();
			int numberOfCharacters = text.length();
			int nonBlankCharacters = text.replaceAll("\\s+", "").length();
			int lines = model.getCurrentDocument().getTextComponent().getLineCount();
			
			String message = String.format("Your document has %d characters, "
					+ "%d non-blank characters and %d lines.", numberOfCharacters, nonBlankCharacters, lines);
			
			JOptionPane.showMessageDialog(JNotepadPP.this, message, "Document statistics", JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Tries to exit the notepad application. If the user has some modified documents opened,
	 * he will be prompted to save them before closing.
	 */
	private final LocalizableAction ExitAction = new LocalizableAction("exit", localizationProvider, "alt F4") {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			for (int i = model.getNumberOfDocuments() - 1; i >= 0; --i) {
				SingleDocumentModel document = model.getDocument(i);

				if (document.isModified()) {
					int before = model.getNumberOfDocuments();
					model.setCurrentDocument(document);
					CloseAction.actionPerformed(null);
					int after = model.getNumberOfDocuments();

					if (before == after)
						return;
				}
			}
			
			statusBar.disconnect();
			JNotepadPP.this.dispose();
		}

	};
	
	/**
	 * Cuts the selected text from the current document.
	 */
	private final LocalizableAction CutAction = new LocalizableAction("cut", localizationProvider, "control X") {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			model.getCurrentDocument().getTextComponent().cut();
		}

	};
	
	/**
	 * Copies the selected text from the current document.
	 */
	private final LocalizableAction CopyAction = new LocalizableAction("copy", localizationProvider, "control C") {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			model.getCurrentDocument().getTextComponent().copy();
		}

	};
	
	/**
	 * Pastes the text from the clipboard into the current document at the current caret position.
	 */
	private final LocalizableAction PasteAction = new LocalizableAction("paste", localizationProvider, "control V") {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			model.getCurrentDocument().getTextComponent().paste();
		}

	};
	
	/**
	 * Function that takes list of strings, inverts case in those strings, 
	 * and returns a list of inverted strings.
	 */
	private final Function<String, String> InvertCase = s -> {
		char[] chars = s.toCharArray();
		
		for(int i = 0; i < chars.length; ++i) {
			char current = chars[i];
			
			if(Character.isAlphabetic(current)) {
				if(Character.isLowerCase(current)) {
					chars[i] = Character.toUpperCase(current);
				} else {
					chars[i] = Character.toLowerCase(current);
				}
			}
		}
		return new String(chars);
	};
	
	/**
	 * Converts characters in selected lines in document to uppercase.
	 */
	private final LocalizableAction ToLowerCaseAction = new LCaseAction("toLower", localizationProvider, null, model, String::toLowerCase);
	/**
	 * Converts characters in selected lines in document to lowercase.
	 */
	private final LocalizableAction ToUpperCaseAction = new LCaseAction("toUpper", localizationProvider, null, model, String::toUpperCase);
	/**
	 * Inverts case of characters in selected lines in document.
	 */
	private final LocalizableAction InvertCaseAction = new LCaseAction("invertCase", localizationProvider, null, model, InvertCase);
	
	private final LocalizableAction LEONA = new LChangeLanguageAction("leona", localizationProvider, null);
	
	/**
	 * Function that takes a list of strings, sorts those strings in an
	 * ascending order with locale sensitivity and returns the sorted list.
	 */
	private final Function<List<String>, List<String>> AscendingLinesSort = l -> {
		l.sort(getComparatorForCurentLocale());
		return l;
	};
	
	/**
	 * Function that takes a list of strings, sorts those strings in an
	 * descending order with locale sensitivity and returns the sorted list.
	 */
	private final Function<List<String>, List<String>> DescendingLinesSort = l -> {
		l.sort(getComparatorForCurentLocale().reversed());
		return l;
	};
	
	/**
	 * Function that takes a list of strings and removes all duplicate strings
	 * and returns the list with unique strings.
	 */
	private final Function<List<String>, List<String>> UniqueLines = l -> {
		return l.stream().distinct().collect(Collectors.toList());
	};
	
	/**
	 * Sorts selected lines in document in an ascending order.
	 */
	private final LReplaceSelectedLinesAction AscendingLinesSortAction = new LReplaceSelectedLinesAction("asc", localizationProvider, null, model, AscendingLinesSort);
	/**
	 * Sorts selected lines in document in an descending order.
	 */
	private final LReplaceSelectedLinesAction DescendingLinesSortAction = new LReplaceSelectedLinesAction("desc", localizationProvider, null, model, DescendingLinesSort);
	
	/**
	 * Removes all duplicate lines in the selected lines of the document.
	 */
	private final LReplaceSelectedLinesAction UniqueLinesAction = new LReplaceSelectedLinesAction("unique", localizationProvider, null, model, UniqueLines);
	
	/**
	 * Tracks changes in the current document and changes
	 * window's title accordingly and disables the save button
	 * if the current document hasn't been modified.
	 */
	private final SingleDocumentListener currentDocumentListener = new SingleDocumentListener() {

		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			SaveAction.setEnabled(model.isModified());
		}

		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			setTitle(model.getFilePath().toAbsolutePath().toString() + " - " + TITLE);
		}
	};
	
	/**
	 * Tracks the current in the current document.
	 * Disables cut and copy buttons if nothing is selected in the document.
	 */
	private final CaretListener currentDocumentCaretListener = new CaretListener() {
		
		@Override
		public void caretUpdate(CaretEvent e) {
			int delta = e.getMark() - e.getDot();
			
			CutAction.setEnabled(delta != 0);
			CopyAction.setEnabled(delta != 0);
			
			JTextArea ta = model.getCurrentDocument().getTextComponent();
			String tekst = ta.getSelectedText();
			
			if(tekst == null) return;
			
			String kljuc = "ne bahatim se!";
			
			if(tekst.equals(kljuc)) {
				LEONA.setEnabled(true);
			} else {
				LEONA.setEnabled(false);
			}
		}
	};
	
	/**
	 * Gets a comparator sensitive for the current selected language of the application.
	 *  
	 * @return a comparator
	 */
	private Comparator<String> getComparatorForCurentLocale() {
		Locale locale = new Locale(LocalizationProvider.getInstance().getCurrentLanguage());
		Collator col = Collator.getInstance(locale);
		return (s1, s2) -> col.compare(s1, s2);
	}

	/**
	 * Main method. Starts the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}
}
