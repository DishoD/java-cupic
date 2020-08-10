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
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints a tree of a given directory.
 * 
 * @author Disho
 *
 */
public class TreeShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "tree";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Prints a tree of a given directory.");
		l.add("arg1 (optinal) - path to a directory. If not given, prints a tree of a currently positioned directory.");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}
	

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.size() <= 1) {
			Path path = null;
			
			if(args.size() == 1) {
				path = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
			} else {
				path = env.getCurrentDirectory();
			}
			
			if(!Files.exists(path)) {
				env.writeln("The path '" + path + "' doesn't exist.");
				return ShellStatus.CONTINUE;
			}
			
			if(!Files.isDirectory(path)) {
				env.writeln("Given path '" + path + "' is not a directory.");
				return ShellStatus.CONTINUE;
			}
			
			FileVisitor<Path> fileVisitor = new FileVisitor<>() {
				private int level = 1;

				@Override
				public FileVisitResult postVisitDirectory(Path arg0, IOException arg1) throws IOException {
					level--;
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes arg1) throws IOException {
					env.writeln(String.format("%" + 2*level +"s%s", "", path.getFileName()));
					level++;
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes arg1) throws IOException {
					env.writeln(String.format("%" + 2*level +"s%s", "", path.getFileName()));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path arg0, IOException arg1) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			};
			
			try {
				Files.walkFileTree(path, fileVisitor);
			} catch (IOException e) {
				env.writeln("Error occured durning execution of the command.");
			}
			
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Command expects one argument: a path to the directory.");
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
