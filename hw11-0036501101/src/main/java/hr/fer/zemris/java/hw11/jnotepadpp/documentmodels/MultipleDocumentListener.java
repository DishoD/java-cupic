package hr.fer.zemris.java.hw11.jnotepadpp.documentmodels;

/**
 * Listener for the multiple document model changes. Tracks when the current
 * document has been changed and when document has been added or removed.
 * 
 * @author Disho
 *
 */
public interface MultipleDocumentListener {

	/**
	 * Method to be called by the listener when the model changes the current
	 * document.
	 * 
	 * @param previousModel
	 *            previous document
	 * @param currentModel
	 *            current document
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

	/**
	 * Method to be called by the listener when the model adds a new document.
	 * 
	 * @param model
	 *            added document
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Method to be called by the listener when the model removes an document.
	 * 
	 * @param model
	 *            removed document
	 */
	void documentRemoved(SingleDocumentModel model);

}
