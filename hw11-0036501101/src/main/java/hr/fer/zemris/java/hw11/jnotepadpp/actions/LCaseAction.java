package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.function.Function;

import javax.swing.JTextArea;
import javax.swing.event.CaretListener;

import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;

/**
 * Localizable action used for changing the case of the selected text in the JNotepadpp document.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class LCaseAction extends LocalizableAction {
	/**
	 * tracks this multiple document model for knowing when to disable the action
	 */
	private MultipleDocumentModel model; 
	/**
	 * current document, used for enabling the action when some text is selected
	 */
	private SingleDocumentModel currentDocument;
	/**
	 * function that changes the case of the selected text in document
	 */
	private Function<String, String> caseFunction;
	
	/**
	 * current document caret, used for enabling the action when some text is selected
	 */
	private final CaretListener CurrentDocumentCaretListener = e -> setEnabled((e.getDot() - e.getMark()) != 0);

	/**
	 * Initializes the action with the given parameters.
	 * 
	 * @param key localization key
	 * @param localizationProvider localization provider
	 * @param keyStroke accelerator key for this action (can be null)
	 * @param model multiple document model on which this action will operate
	 * @param caseFunction this function will be used for changing the case of the selected text
	 */
	public LCaseAction(String key, ILocalizationProvider localizationProvider, String keyStroke, 
						MultipleDocumentModel model, Function<String, String> caseFunction) {
		super(key, localizationProvider, keyStroke);
		
		this.model = model;
		this.caseFunction = caseFunction;
		currentDocument = model.getCurrentDocument();
		
		model.addMultipleDocumentListener(new MultipleDocumentListener() {
			
			@Override
			public void documentRemoved(SingleDocumentModel model) {
				if(LCaseAction.this.model.getNumberOfDocuments() == 0) {
					setEnabled(false);
				}
			}
			
			@Override
			public void documentAdded(SingleDocumentModel model) {

			}
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if(previousModel != null) {
					previousModel.getTextComponent().removeCaretListener(CurrentDocumentCaretListener);
				}
				
				currentDocument = currentModel;
				
				setEnabled(currentDocument.getTextComponent().getSelectedText() != null);
				currentDocument.getTextComponent().addCaretListener(CurrentDocumentCaretListener);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextArea ta = currentDocument.getTextComponent();
		String text = ta.getSelectedText();
		ta.replaceSelection(caseFunction.apply(text));
	}

}
