package hr.fer.zemris.java.hw11.jnotepadpp.documentmodels;

/**
 * Listener for tracking changes in a single document model.
 * 
 * @author Disho
 *
 */
public interface SingleDocumentListener {
	/**
	 * Method to be called when document's modified flag has changed.
	 * 
	 * @param model subject: document
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * Method to be called when document's file path has been changed.
	 * 
	 * @param model subject: document
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
