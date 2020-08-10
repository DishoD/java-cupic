package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Deletes the given directory and its content.
 * 
 * @author Disho
 *
 */
public class RmtreeShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "rmtree";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;

	static {
		List<String> l = new ArrayList<>();
		l.add("Deletes the given directory and its content.");
		l.add("arg1 - path to a directory.");

		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.size() != 1) {
			env.writeln("Command expects one argument: a path to a directory.");
			return ShellStatus.CONTINUE;
		}
		
		Path path = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
		
		try {
			if(Files.isSameFile(path, env.getCurrentDirectory())) {
				env.writeln("Can't delete the current directory.");
				return ShellStatus.CONTINUE;
			}
			
			if(!Files.isDirectory(path)) {
				env.writeln("Given path is not a directory.");
				return ShellStatus.CONTINUE;
			}
			
			if(Files.list(path).count() != 0) {
				env.writeln("Given directory is not empty. Are you sure you want to delete it? (y/n)");
				String response = env.readLine().trim();
				if(!(response.equals("y") || response.equals("yes"))) {
					env.writeln("Command canceled.");
					return ShellStatus.CONTINUE;
				}
			}
			
			FileVisitor<Path> fv = new FileVisitor<>() {

				@Override
				public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
					Files.delete(path);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes e) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes e) throws IOException {
					Files.delete(path);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
					env.writeln("Deliting " + path + " falied. Command aborted.");
					return FileVisitResult.TERMINATE;
				}
			
			};
			
			Files.walkFileTree(path, fv);
			
		} catch (ShellIOException | IOException e) {
			throw new ShellIOException();
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
