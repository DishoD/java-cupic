package hr.fer.zemris.java.hw11.jnotepadpp.documentmodels;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import hr.fer.zemris.java.hw11.jnotepadpp.icons.IconUtil;

/**
 * Implementation of a MultipleDocumentModel interface as a tabbed panel.
 * Documents are organized in the tabs that can be closed. New documents
 * are added in the new tabs.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	/**
	 * list of opened documents in a model
	 */
	private List<SingleDocumentModel> singleDocuments = new ArrayList<>();
	/**
	 * currently selected document in the model
	 */
	private SingleDocumentModel currentDocument;
	
	/**
	 * list of registered multiple document listeners
	 */
	private List<MultipleDocumentListener> listeners = new ArrayList<>();
	
	/**
	 * Initializes an tabbed multiple document model with zero opened documents.
	 */
	public DefaultMultipleDocumentModel() {
		addChangeListener(e -> {
			setCurrentDocument(getSelectedIndex());
		});
	}
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return singleDocuments.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel document = new DefaultSingleDocumentModel();
		singleDocuments.add(document);
		
		addTab("new document", IconUtil.GREEN_DISKETTE, new JScrollPane(document.getTextComponent()));
		
		int index = singleDocuments.size() - 1;
		
		registerDocumentListener(document);
		setCurrentDocument(index);
		fireDocumentAdded(document);
		
		return document;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		
		for(int i = 0; i < singleDocuments.size(); ++i) {
			SingleDocumentModel document = singleDocuments.get(i);
			
			try {
				if(document.getFilePath() != null && Files.isSameFile(document.getFilePath(), path)) {
					setCurrentDocument(i);
					return document;
				}
			} catch (IOException ignorable) {
			}
		}
		
		byte[] data;
		
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Couln't read file: " + path, "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		String text = new String(data, StandardCharsets.UTF_8);
		
		SingleDocumentModel document = new DefaultSingleDocumentModel(path, text);
		singleDocuments.add(document);
		
		addTab(
				path.getFileName().toString(), 
				IconUtil.GREEN_DISKETTE, 
				new JScrollPane(document.getTextComponent()), 
				document.getFilePath().toAbsolutePath().toString()
		);
		
		int index = singleDocuments.size() - 1;
		
		registerDocumentListener(document);
		setCurrentDocument(index);
		fireDocumentAdded(document);
		
		return document;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		int index = singleDocuments.indexOf(model);
		
		for(int i = 0; i < singleDocuments.size(); ++i) {
			SingleDocumentModel document = singleDocuments.get(i);
			
			try {
				if(i != index && document.getFilePath() != null && Files.isSameFile(document.getFilePath(), newPath)) {
					JOptionPane.showMessageDialog(this, "Can't overwrite opened file.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (IOException ignorable) {
			}
		}
		
		byte[] data = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);
		
		try {
			Files.write(newPath, data);
			model.setFilePath(newPath);
			model.setModified(false);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error ocurred while saving. File not saved.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = singleDocuments.indexOf(model);
		singleDocuments.remove(index);
		remove(index);
		fireDocumentRemoved(model);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return singleDocuments.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return singleDocuments.get(index);
	}
	
	/**
	 * Notifies all registered observers that current document has been changed.
	 * 
	 * @param previousModel previous document
	 * @param currentModel current document
	 */
	private void fireCurrentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		listeners.forEach(l -> l.currentDocumentChanged(previousModel, currentModel));
	}
	
	/**
	 * Notifies all registered observers that new document has been added to the model.
	 * 
	 * @param model added document
	 */
	private void fireDocumentAdded(SingleDocumentModel model) {
		listeners.forEach(l -> l.documentAdded(model));
	}
	
	/**
	 * Notifies all registered observers that document has been removed from the model.
	 * 
	 * @param model removed document
	 */
	private void fireDocumentRemoved(SingleDocumentModel model) {
		listeners.forEach(l -> l.documentRemoved(model));
	}
	
	/**
	 * Registers an single document listener to the given document that
	 * tracks changes in the document and updates the modification indication icon
	 * and changes tab name and tool tip according to the document file path and name.
	 * 
	 * @param document document to which to register to
	 */
	private void registerDocumentListener(SingleDocumentModel document) {
		int index = singleDocuments.indexOf(document);
		
		document.addSingleDocumentListener(new SingleDocumentListener() {
			
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				boolean modified = model.isModified();
				
				if(modified) {
					setIconAt(index, IconUtil.RED_DISKETTE);
				} else {
					setIconAt(index, IconUtil.GREEN_DISKETTE);
				}
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				setTitleAt(index, model.getFilePath().getFileName().toString());
				setToolTipTextAt(index, model.getFilePath().toAbsolutePath().toString());
			}
		});
	}
	
	/**
	 * Set document at the given index as current document.
	 * 
	 * @param index
	 */
	private void setCurrentDocument(int index) {
		if(index < 0) return;
		
		SingleDocumentModel old = currentDocument;
		currentDocument = singleDocuments.get(index);
		
		fireCurrentDocumentChanged(old, currentDocument);
		
		setSelectedIndex(index);
	}
	
	/**
	 * Set given document as a current document.
	 * 
	 * @param document
	 */
	public void setCurrentDocument(SingleDocumentModel document) {
		setCurrentDocument(singleDocuments.indexOf(document));
	}

}
