package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.swing.JTextArea;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.documentmodels.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;

/**
 * Localizable action that replaces selected lines in current document of JNotepadpp
 * with some other lines as determined by the linesFunction in the constructor.
 * 
 * @author Disho
 *
 */
@SuppressWarnings("serial")
public class LReplaceSelectedLinesAction extends LocalizableAction {
	/**
	 * tracks this multiple document model for knowing when to disable the action
	 */
	private MultipleDocumentModel model;
	/**
	 * current document, used for enabling the action when some text is selected
	 */
	private SingleDocumentModel currentDocument;
	
	/**
	 * function that changes the selected lines in some way
	 */
	private Function<List<String>, List<String>> linesFunction;
	
	/**
	 * current document caret, used for enabling the action when some text is selected
	 */
	private final CaretListener CurrentDocumentCaretListener = e -> setEnabled((e.getDot() - e.getMark()) != 0);

	/**
	 * Initializes the action with the given parameters.
	 * 
	 * @param key localization key
	 * @param localizationProvider localization provider
	 * @param keyStroke accelerator key
	 * @param model multiple document model on which this action will operate
	 * @param linesFunction function that determines with what selected lines will be replaced with
	 */
	public LReplaceSelectedLinesAction(String key, ILocalizationProvider localizationProvider, String keyStroke,
			MultipleDocumentModel model, Function<List<String>, List<String>> linesFunction) {
		super(key, localizationProvider, keyStroke);

		this.model = model;
		this.linesFunction = linesFunction;
		currentDocument = model.getCurrentDocument();

		model.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				if (LReplaceSelectedLinesAction.this.model.getNumberOfDocuments() == 0) {
					setEnabled(false);
				}
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {

			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if (previousModel != null) {
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
		Caret caret = ta.getCaret();

		int first, last;
		int dot = caret.getDot();
		int mark = caret.getMark();

		if (dot < mark) {
			first = dot;
			last = mark;
		} else {
			first = mark;
			last = dot;
		}

		int selBegin;
		int selEnd;
		int firstLine;
		int lastLine;

		try {
			firstLine = ta.getLineOfOffset(first);
			lastLine = ta.getLineOfOffset(last);

			selBegin = ta.getLineStartOffset(firstLine);
			selEnd = ta.getLineEndOffset(lastLine);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
			return;
		}

		List<String> lines = new ArrayList<>();

		try {

			for (int i = firstLine; i <= lastLine; ++i) {

				int posBegin = ta.getLineStartOffset(i);
				int len = ta.getLineEndOffset(i) - posBegin;
				
				String line = ta.getDocument().getText(posBegin, len);
				lines.add(line.replaceFirst("\\s++$", ""));
			}
			
			StringBuilder sb = new StringBuilder();
			linesFunction.apply(lines).forEach(s -> {
				sb.append(s + "\n");
			});
			
			ta.select(selBegin, selEnd);
			ta.replaceSelection(sb.toString());

		} catch (BadLocationException e1) {
			e1.printStackTrace();
			return;
		}
	}

}
