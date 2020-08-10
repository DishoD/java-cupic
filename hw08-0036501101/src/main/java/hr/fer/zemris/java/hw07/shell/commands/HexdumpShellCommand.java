package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.crypto.Util;
import static hr.fer.zemris.java.hw07.shell.commands.Util.*;

/**
 * Prints hexdump of a given file.
 * 
 * @author Disho
 *
 */
public class HexdumpShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "hexdump";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	/**
	 * buffer size in bytes
	 */
	private static final int BUFFER_SIZE = 16;
	
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Prints hexdump of a given file.");
		l.add("arg1 - path to a file");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = parseArguments(arguments);
		
		if(args.size() != 1) {
			env.writeln("Command expects one argument: path to a file.");
			return ShellStatus.CONTINUE;
		}
		
		Path path = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
		
		if(!Files.exists(path)) {
			env.writeln("Given file '" + path + "' doesn't exist.");
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.isRegularFile(path)) {
			env.writeln("Given path '" + path + "' is not a file.");
			return ShellStatus.CONTINUE;
		}
		
		try(InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ), BUFFER_SIZE)){
			byte[] buff = new byte[BUFFER_SIZE];
			int r;
			long currentByte = 0;
			while(true) {
				r = is.read(buff);
				if(r < 0) break;
				byte[] row;
				if(r < BUFFER_SIZE) {
					row = Arrays.copyOf(buff, r);
				} else {
					row = buff;
				}
				env.writeln(byteToHexRow(row, currentByte));
				currentByte += r;
			}
		} catch (IOException e) {
			env.writeln("Couldn't open a file.");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Converts one row of bytes of a hexdump to a string format.
	 * 
	 * @param row bytes of a hexdump row
	 * @param start starting byte in the file
	 * @return
	 */
	private static String byteToHexRow(byte[] row, long start) {
		String hex = Util.bytetohex(row);
		String c1 = hex, c2 = "";
		if(hex.length() > 2*8) {
			c1 = hex.substring(0, 16).replaceAll("..", "$0 ").trim();
			c2 = hex.substring(16, hex.length()).replaceAll("..", "$0 ").trim();
		} else {
			c1 = hex.replaceAll("..", "$0 ").trim();
		}
		
		StringBuilder text = new StringBuilder();
		
		for(byte c : row) {
			if(c >= 32 && c <= 127) {
				text.append((char)c);
			} else {
				text.append('.');
			}
		}
		
		return String.format("%08X: %-23s|%-23s | %s", start, c1, c2, text.toString());
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return COMMAND_DESCRIPTION;
	}

}
