package hr.fer.zemris.java.hw11.jnotepadpp.documentmodels;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Defines an interface of a text document opened
 * from the file system (or a new empty document that yet
 * doesn't exist on the file system).
 * 
 * @author Disho
 *
 */
public interface SingleDocumentModel {
	/**
	 * Returns a text component of a document.
	 * 
	 * @return text component of a document
	 */
	JTextArea getTextComponent();

	/**
	 * Returns a file path of this document.
	 * 
	 * @return file path of this document
	 */
	Path getFilePath();

	/**
	 * Sets a file path of the document.
	 * 
	 * @param path a file path
	 */
	void setFilePath(Path path);

	/**
	 * Checks whether the document has been modified.
	 * 
	 * @return true if modified, false otherwise
	 */
	boolean isModified();

	/**
	 * Set modified flag of this document.
	 * 
	 * @param modified true if modified, false otherwise
	 */
	void setModified(boolean modified);

	/**
	 * Register an single document listener for tracking changes of this document.
	 * 
	 * @param l listener to register
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	
	/**
	 * Unregister an single document listener from the document.
	 * 
	 * @param l listener to unregister
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
