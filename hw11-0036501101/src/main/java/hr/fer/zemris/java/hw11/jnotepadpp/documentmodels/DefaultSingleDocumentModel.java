package hr.fer.zemris.java.hw11.jnotepadpp.documentmodels;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Implementation of a single document model.
 * 
 * @author Disho
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	/**
	 * text area for displaying and modifying the text
	 */
	private JTextArea textArea;
	/**
	 * file path of the document (null if document hasn't been saved yet)
	 */
	private Path filePath;
	/**
	 * modified flag - tells if the document has been modified
	 */
	private boolean isModified;
	
	/**
	 * list of all registered listeners for the single document model
	 */
	private List<SingleDocumentListener> listeners = new ArrayList<>();
	
	/**
	 * Initializes an empty new document wouthout a file path
	 */
	public DefaultSingleDocumentModel() {
		this(null, "");
	}
	
	/**
	 * Initializes the document with the given text and file path.
	 * 
	 * @param filePath file path of the document
	 * @param text text of the document
	 */
	public DefaultSingleDocumentModel(Path filePath, String text) {
		this.filePath = filePath;
		this.textArea = new JTextArea(text);
		
		this.textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				setModified(true);
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				setModified(true);
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				setModified(true);
			}
		});
	}

	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		Objects.requireNonNull(path, "Path cannot be null.");
		
		if(path.equals(filePath)) return;
		
		this.filePath = path;
		fireFilePathUpdated();
	}

	@Override
	public boolean isModified() {
		return isModified;
	}

	@Override
	public void setModified(boolean modified) {
		if(isModified == modified) return;
		
		isModified = modified;
		fireModifyStatusUpdated();
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notify all registered listeners of the change in modified flag.
	 */
	private void fireModifyStatusUpdated() {
		listeners.forEach(l -> l.documentModifyStatusUpdated(this));
	}
	
	/**
	 * Notify all registered listeners of the change in file path.
	 */
	private void fireFilePathUpdated() {
		listeners.forEach(l -> l.documentFilePathUpdated(this));
	}

}
