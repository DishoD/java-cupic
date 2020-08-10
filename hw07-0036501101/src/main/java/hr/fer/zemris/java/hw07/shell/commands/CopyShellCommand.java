package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Copies the file. It can take two arguments. arg1 - file to copy; and arg1 -
 * file to copy
 * 
 * @author Disho
 *
 */
public class CopyShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "copy";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;

	/**
	 * buffer size in bytes
	 */
	private static final int BUFFER_SIZE = 4 * 1024;

	static {
		List<String> l = new ArrayList<>();
		l.add("Copies the file.");
		l.add("arg1 - file to copy");
		l.add("arg1 - file to copy");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);

		if (args.size() != 2) {
			env.writeln("Command expects two arguemnts: source and destination.");
			return ShellStatus.CONTINUE;
		}

		Path src = Paths.get(args.get(0));
		Path dest = Paths.get(args.get(1));

		if (!Files.exists(src)) {
			env.writeln("Source '" + src + "' doesn't exist.");
			return ShellStatus.CONTINUE;
		}

		if (!Files.isRegularFile(src)) {
			env.writeln("Source '" + src + "' is not a file.");
			return ShellStatus.CONTINUE;
		}

		if (Files.isDirectory(dest)) {
			dest = dest.resolve(src.getFileName());
		}

		if (Files.isRegularFile(dest)) {

			try {
				if (Files.isSameFile(src, dest)) {
					return ShellStatus.CONTINUE;
				}
			} catch (IOException ignorable) {
			}

			env.writeln("The file '" + dest + "' already exists. Do you want to overwrite it? (y/n)");
			String response = env.readLine().trim().toLowerCase();
			if (!(response.equals("y") || response.equals("yes"))) {
				env.writeln("Copy command not execuetd.");
				return ShellStatus.CONTINUE;
			}
		}

		try (InputStream is = new BufferedInputStream(Files.newInputStream(src, StandardOpenOption.READ), BUFFER_SIZE);
				OutputStream os = new BufferedOutputStream(Files.newOutputStream(dest, StandardOpenOption.CREATE,
						StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING), BUFFER_SIZE)) {

			byte[] buff = new byte[BUFFER_SIZE];
			int r;
			while (true) {
				r = is.read(buff);
				if (r < 0)
					break;
				os.write(buff, 0, r);
			}
		} catch (IOException e) {
			env.writeln("Error ocurred during execution of the command.");
		}

		return ShellStatus.CONTINUE;
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
