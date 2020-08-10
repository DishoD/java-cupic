package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.EmptyStackException;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * An engine that executes the smart scripts
 * and writes the result to the http request context.
 * 
 * @author Disho
 *
 */
public class SmartScriptEngine {
	/**
	 * root document node of the parsed smart script
	 */
	private DocumentNode documentNode;
	/**
	 * http request context on which the result will be written
	 */
	private RequestContext requestContext;
	/**
	 * multistack for keeping track of variables, constants and parameters
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	
	/**
	 * multistack key for for-loop stack context
	 */
	private static final String KEY_FOR = "FOR";
	/**
	 * multistack key for echo tag stack context
	 */
	private static final String KEY_ECHO = "ECHO";
	
	/**
	 * Executes the script on the parsed nodes.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				throw new SmartScriptEngineException("Error ocurred: coudln't write some text to the requestContext");
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String var = node.getVariable().asText();
			ValueWrapper initial = new ValueWrapper(node.getStartExpression().asText(), var);
			ValueWrapper end = new ValueWrapper(node.getEndExpression().asText(), null);
			ValueWrapper step = new ValueWrapper(node.getStepExpression() == null ? 1 : node.getStepExpression().asText(), null);
			
			multistack.push(KEY_FOR, initial);
			
			if(initial.numCompare(end.getValue()) > 0) return;
			
			while(true) {
				for(int i = 0; i < node.numberOfChildren(); ++i) {
					node.getChild(i).accept(this);
				}
				
				ValueWrapper current = multistack.pop(KEY_FOR);
				current.add(step.getValue());
				if(current.numCompare(end.getValue()) > 0) break;
				multistack.push(KEY_FOR, current);
			}
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			for(Element element : node.getElements()) {
				if(element instanceof ElementVariable) {
					String var = element.asText();
					
					Stack<ValueWrapper> stack = new Stack<>();
					boolean found = false;
					
					while(true) {
						if(multistack.isEmpty(KEY_FOR)) break;
						ValueWrapper current = multistack.pop(KEY_FOR);
						stack.push(current);
						if(current.getName() == null) continue;
						if(var.equals(current.getName())) {
							found = true;
							multistack.push(KEY_ECHO, new ValueWrapper(current.getValue(), null));
							break;
						}
					}
					
					if(!found) throw new SmartScriptEngineException("Variable " + var + "doesn't exist.");
					
					while(!stack.isEmpty()) {
						multistack.push(KEY_FOR, stack.pop());
					}
					
					continue;
				}
				
				if(element instanceof ElementOperator) {
					String operator = element.asText();
					
					if(multistack.isEmpty(KEY_ECHO)) throw new SmartScriptEngineException("Expected some argument in the ECHO tag for operator: " + operator);
					ValueWrapper secondOperand = multistack.pop(KEY_ECHO);
					if(multistack.isEmpty(KEY_ECHO)) throw new SmartScriptEngineException("Expected some argument in the ECHO tag for operator: " + operator);
					ValueWrapper firstOperand = multistack.pop(KEY_ECHO);
					
					switch(operator) {
					case "+":
						firstOperand.add(secondOperand.getValue());
						multistack.push(KEY_ECHO, firstOperand);
						break;
					case "-":
						firstOperand.subtract(secondOperand.getValue());
						multistack.push(KEY_ECHO, firstOperand);
						break;
					case "*":
						firstOperand.multiply(secondOperand.getValue());
						multistack.push(KEY_ECHO, firstOperand);
						break;
					case "/":
						firstOperand.divide(secondOperand.getValue());
						multistack.push(KEY_ECHO, firstOperand);
						break;
					}
					
					continue;
				}
				
				if(element instanceof ElementFunction) {
					String function = element.asText();
					
					try {
						switch(function) {
						case "@sin":
							ValueWrapper arg = multistack.pop(KEY_ECHO);
							Double value = Double.parseDouble(arg.toString());
							value = Math.sin(value);
							multistack.push(KEY_ECHO, new ValueWrapper(value, null));
							break;
							
						case "@decfmt":
							DecimalFormat f = new DecimalFormat(multistack.pop(KEY_ECHO).toString());
							double x = Double.parseDouble(multistack.pop(KEY_ECHO).toString());
							String formatted = f.format(x);
							multistack.push(KEY_ECHO, new ValueWrapper(formatted, null));
							break;
						
						case "@dup":
							Object val = multistack.pop(KEY_ECHO).getValue();
							multistack.push(KEY_ECHO, new ValueWrapper(val, null));
							multistack.push(KEY_ECHO, new ValueWrapper(val, null));
							break;
							
						case "@swap":
							ValueWrapper a = multistack.pop(KEY_ECHO);
							ValueWrapper b = multistack.pop(KEY_ECHO);
							multistack.push(KEY_ECHO, a);
							multistack.push(KEY_ECHO, b);
							break;
							
						case "@setMimeType":
							String mime = multistack.pop(KEY_ECHO).toString();
							requestContext.setMimeType(mime);
							break;
						
						case "@paramGet":
							ValueWrapper dv = new ValueWrapper(multistack.pop(KEY_ECHO).getValue(), null);
							String name = multistack.pop(KEY_ECHO).toString();
							String param = requestContext.getParameter(name);
							multistack.push(KEY_ECHO, param == null ? dv : new ValueWrapper(param, null));
							break;
							
						case "@pparamGet":
							dv = new ValueWrapper(multistack.pop(KEY_ECHO).getValue(), null);
							name = multistack.pop(KEY_ECHO).toString();
							param = requestContext.getPersistentParameter(name);
							multistack.push(KEY_ECHO, param == null ? dv : new ValueWrapper(param, null));
							break;
							
						case "@pparamSet":
							name = multistack.pop(KEY_ECHO).toString();
							String sValue = multistack.pop(KEY_ECHO).toString();
							requestContext.setPersistentParameter(name, sValue);
							break;
							
						case "@pparamDel":
							name = multistack.pop(KEY_ECHO).toString();
							requestContext.removePersistentParameter(name);
							break;
						
						case "@tparamGet":
							dv = new ValueWrapper(multistack.pop(KEY_ECHO).getValue(), null);
							name = multistack.pop(KEY_ECHO).toString();
							param = requestContext.getTemporaryParameter(name);
							multistack.push(KEY_ECHO, param == null ? dv : new ValueWrapper(param, null));
							break;
						
						case "@tparamSet":
							name = multistack.pop(KEY_ECHO).toString();
							sValue = multistack.pop(KEY_ECHO).toString();
							requestContext.setTemporaryParameter(name, sValue);
							break;
							
						case "@tparamDel":
							name = multistack.pop(KEY_ECHO).toString();
							requestContext.removeTemporaryParameter(name);
							break;
						}
					} catch(EmptyStackException ex) {
						throw new SmartScriptEngineException("Expected a function argument for: " + function);
					} catch(NumberFormatException ex) {
						throw new SmartScriptEngineException("Illegal argument for function: " + function);
					}
					
					continue;
				}
				
				//it is some constant
				String constant = element.asText();
				multistack.push(KEY_ECHO, new ValueWrapper(constant, null));
			}
			
			Stack<String> tempStack = new Stack<>();
			while(!multistack.isEmpty(KEY_ECHO)) {
				tempStack.push(multistack.pop(KEY_ECHO).toString());
			}
			while(!tempStack.isEmpty()) {
				try {
					requestContext.write(tempStack.pop());
				} catch (IOException e) {
					throw new SmartScriptEngineException("Error ocurred: coudln't write some text to the requestContext");
				}
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0; i < node.numberOfChildren(); ++i) {
				node.getChild(i).accept(this);
			}
		}

	};

	/**
	 * Initializes the engine with the given root document node of the parsed smart script
	 * and http request context.
	 * 
	 * @param documentNode root document node of the parsed smart script
	 * @param requestContext http request context
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		Objects.requireNonNull(documentNode);
		Objects.requireNonNull(requestContext);
		
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * executes the script
	 */
	public void execute() {
		documentNode.accept(visitor);
	}

}
