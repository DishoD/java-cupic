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
 * Copies the whole content from source directory to the destination directory.
 * 
 * @author Disho
 *
 */
public class CptreeShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "cptree";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Copies the whole content from source directory to the destination directory.");
		l.add("arg1 - path to a source directory");
		l.add("arg2 - path to a destination directory");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.size() != 2) {
			env.writeln("Command expects two arguments: a source and destiantion directory.");
			return ShellStatus.CONTINUE;
		}
		
		Path src = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
		Path dest = env.getCurrentDirectory().resolve(Paths.get(args.get(1)));
		
		if(!Files.isDirectory(src)) {
			env.writeln("Source argument: " + src + " is not a directory.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			Path res;
			
			
			if(Files.isDirectory(dest)) {
				res = dest.resolve(src.getFileName());
			} else if(Files.isDirectory(dest.getParent())) {
				Files.createDirectories(dest);
				res = dest;
			} else {
				env.writeln("Destination argument: " + dest + " is not a directory or it doesn't exist.");
				return ShellStatus.CONTINUE;
			}
			
			FileVisitor<Path> fv = new FileVisitor<>() {

				@Override
				public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes arg1) throws IOException {
					Path tempDest = res.resolve(src.relativize(path));
					Files.createDirectories(tempDest.getParent());
					Files.copy(path, tempDest);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
					env.writeln("Couldn't copy: " + path);
					return FileVisitResult.CONTINUE;
				}
			};
			
			Files.walkFileTree(src, fv);
			
		} catch(IOException e) {
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
