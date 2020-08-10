package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Writes directory listing to the shell.
 * 
 * @author Disho
 *
 */
public class LsShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "ls";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Writes directory listing to the shell.");
		l.add("arg1 - path to a directory to list");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		
		if(args.size() == 1) {
			Path path = Paths.get(args.get(0));
			
			if(!Files.exists(path)) {
				env.writeln("The path '" + path + "' doesn't exist.");
				return ShellStatus.CONTINUE;
			}
			
			if(!Files.isDirectory(path)) {
				env.writeln("Given path '" + path + "' is not a directory.");
				return ShellStatus.CONTINUE;
			}
			
			try {
				Files.list(path).forEach(p -> printPathInformation(env, p));
			} catch (IOException e) {
				env.writeln("Error occured during execution of the command.");
			}
			
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Command expects one argument: a path to the directory to list.");
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Prints information about given file/directory to the shell.
	 * 
	 * @param env shell environment
	 * @param path path to a file/directory
	 */
	private static void printPathInformation(Environment env, Path path) {
		StringBuilder drwx = new StringBuilder();
		drwx.append(Files.isDirectory(path) ? 'd' : '-');
		drwx.append(Files.isReadable(path) ? 'r' : '-');
		drwx.append(Files.isWritable(path) ? 'w' : '-');
		drwx.append(Files.isExecutable(path) ? 'x' : '-');
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attributes;
		try {
			attributes = faView.readAttributes();
		} catch (IOException ignorable) {
			return;
		}
		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
		
		long l = attributes.size();
		
		String name = path.getFileName().toString();
		
		env.writeln(String.format("%s %10d %s %s", drwx.toString(), l, formattedDateTime, name));
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
