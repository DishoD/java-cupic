package hr.fer.zemris.java.custom.scripting.exec.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Demonstration program that executes the osnovni.smscr smart script.
 * 
 * @author Disho
 *
 */
public class DemoOsnovni {
	/**
	 * Main method. Runs the demonstration.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String documentBody = new String(Files.readAllBytes(Paths.get("webroot/scripts/osnovni.smscr")), StandardCharsets.UTF_8);
		Map<String,String> parameters = new HashMap<>();
		Map<String,String> persistentParameters = new HashMap<>();
		List<RCCookie> cookies = new ArrayList<>();
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}
}
