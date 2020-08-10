package hr.fer.zemris.java.hw11.jnotepadpp.documentmodels;

import java.nio.file.Path;

/**
 * Interface that defines an model that tracks multiple text documents.
 * 
 * @author Disho
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	/**
	 * Create new empty document with unknown file file path.
	 * 
	 * @return newly created document
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Gets currently selected text document.
	 * 
	 * @return currently selected document
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Loads text from the from the given file and opens an new document with the
	 * loaded text and given file path.
	 * 
	 * @param path
	 *            path of a file to load text from
	 * 
	 * @return loaded document
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Save given document to a file.
	 * 
	 * @param model
	 *            document to save
	 * @param newPath
	 *            path to a file in which it will be saved (overwrites an existing
	 *            file or crates new file if it desn't exist)
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Close the given document from the model.
	 * 
	 * @param model model to close
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Registers an multiple document model listener.
	 * 
	 * @param l listener to register
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Unregisters an multiple document model listener.
	 * 
	 * @param l listener to unregister
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Gets a number of documents in the model.
	 * 
	 * @return number of documents
	 */
	int getNumberOfDocuments();

	/**
	 * Returns a document of the given index.
	 * Indexes are the same as if the documents where added to the array.
	 * 
	 * @param index index of the document to get
	 * @return a document
	 */
	SingleDocumentModel getDocument(int index);
}
