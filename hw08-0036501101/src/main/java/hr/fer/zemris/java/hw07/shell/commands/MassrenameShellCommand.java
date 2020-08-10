package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Moves/renames the files from the source to the destination directory.
 * 
 * @author Disho
 *
 */
public class MassrenameShellCommand implements ShellCommand {
	/**
	 * command name
	 */
	private static final String COMMAND_NAME = "massrename";
	/**
	 * command description
	 */
	private static final List<String> COMMAND_DESCRIPTION;
	
	static {
		List<String> l = new ArrayList<>();
		l.add("Moves/renames the files from the source to the destination directory.");
		l.add("arg1 - path to a source directory");
		l.add("arg2 - path to a destination directory");
		l.add("arg3 - subcommand: filter, groups, show or execute");
		l.add("     - filter: prints only the files that will be selected from the source directory by the given mask");
		l.add("     - groups: prints the groups of the file names selected by the mask");
		l.add("     - show: prints what will be the result of the renaming the files as defined by the mask and the expession");
		l.add("     - execute: executes the move/rename");
		l.add("arg4 (optional) - mask, determines what files will be selected from the source directory. If not given, all files are selected.");
		l.add("                - mask is a regular expression as defined by the Pattern class");
		l.add("arg5 (optional) - expression, use if you wish to use grouping in the mask");
		l.add("                - compact text containg supstitution commands ${groupNumber} or ${groupNumber,additional}");
		l.add("                - groups from the mask will be supstituted accordingly by those commands");
		l.add("                - example: 'gradovi-${2}-${1,03}.jpg'");
		l.add("                - additinal: least number of spaces that will the string from the group occupy or be filled by zeros");
		
		COMMAND_DESCRIPTION = Collections.unmodifiableList(l);
	}
	
	private static class FileNameDescription {
		private Path file;
		private Matcher matcher;
		
		public FileNameDescription(Path file, Matcher matcher) {
			this.file = file;
			this.matcher = matcher;
			if(matcher != null) {
				matcher.matches();
			}
		}
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = Util.parseArguments(arguments);
		
		if(args.size() < 3) {
			env.writeln("Too few arguments.");
			return ShellStatus.CONTINUE;
		}
		
		if(args.size() > 5) {
			env.writeln("Too many arguments.");
			return ShellStatus.CONTINUE;
		}
		
		Path src = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
		Path dest = env.getCurrentDirectory().resolve(Paths.get(args.get(1)));
		
		
		if(!Files.isDirectory(src)) {
			env.writeln("Given source path is not a directory.");
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.isDirectory(dest)) {
			env.writeln("Given destination path is not a directory.");
			return ShellStatus.CONTINUE;
		}
		
		String cmd = args.get(2).toLowerCase();
		String mask = null;
		NameBuilderParser parser = null;
		
		if(args.size() >= 4) {
			mask = args.get(3);
		}
		
		if(args.size() == 5) {
			try {
				parser = new NameBuilderParser(args.get(4));
			} catch(IllegalArgumentException e) {
				env.writeln("Illegal argument expression. Error: " + e.getMessage());
				return ShellStatus.CONTINUE;
			}
		}
		
		Pattern pattern = null;
		
		try {
			if(mask != null) {
				pattern = Pattern.compile(mask, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			}
		} catch(PatternSyntaxException e) {
			env.writeln("Given mask is not a valid regular expression.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			List<FileNameDescription> files = filter(src, pattern);
		
			switch(cmd) {
			case "filter":
				for(FileNameDescription file : files) {
					env.writeln(file.file.getFileName().toString());
				}
				break;
				
			case "groups":
				if(args.size() < 4) {
					env.writeln("Mask argument not given.");
					return ShellStatus.CONTINUE;
				}
				
				for(FileNameDescription file : files) {
					StringBuilder sb = new StringBuilder(file.file.getFileName().toString());
					sb.append(": ");
					for(int i = 0; i <= file.matcher.groupCount(); ++i) {
						sb.append("" + i + ": ");
						sb.append(file.matcher.group(i) + " ");
					}
					env.writeln(sb.toString());
				}
				break;
				
			case "show":
			case "execute":
				
				if(parser == null) {
					env.writeln("Provide adittional expression argument if you wish to use grouping.");
					for(FileNameDescription file : files) {
						String originalFileName = file.file.getFileName().toString();
						if(cmd.equals("show")) {
							env.writeln(originalFileName + " => " + originalFileName);
						} else {
							Path p1 = src.resolve(originalFileName);
							Path p2 = dest.resolve(originalFileName);
							Files.move(p1, p2, StandardCopyOption.REPLACE_EXISTING);
						}
					}
				} else {
					for(FileNameDescription file : files) {
						String originalFileName = file.file.getFileName().toString();
						NameBuilderInfo info = new NameBuilderInfo() {
							StringBuilder sb = new StringBuilder();
							
							@Override
							public StringBuilder getStringBuilder() {
								return sb;
							}
							
							@Override
							public String getGroup(int index) {
								return file.matcher.group(index);
							}
						};
						try {
							parser.getNameBuilder().execute(info);
						} catch (Exception e) {
							env.writeln("error: " + e.getMessage());
							return ShellStatus.CONTINUE;
						}
						String resultFileName = info.getStringBuilder().toString();
						
						if(cmd.equals("show")) {
							env.writeln(originalFileName + " => " + resultFileName);
						} else {
							Path p1 = src.resolve(originalFileName);
							Path p2 = dest.resolve(resultFileName);
							Files.move(p1, p2, StandardCopyOption.REPLACE_EXISTING);
						}
						
					}
				}
				break;
			default:
				env.writeln("Illegal subcommand name. It can only be: filter, groups, show or execute.");
			}
		} catch (IOException e) {
			throw new ShellIOException();
		}
		
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Selects files from the given directory by their file name according to the given pattern.
	 * 
	 * @param src directory from which to select the files
	 * @param pattern pattern for selecting files by their file name
	 * @return list of selected files
	 * @throws IOException 
	 */
	private static List<FileNameDescription> filter(Path src, Pattern pattern) throws IOException {
		return Files.list(src).filter(Files::isRegularFile)
								.filter(p -> pattern == null ? true : pattern.matcher(p.getFileName().toString()).matches())
								.map(p -> new FileNameDescription(p, pattern == null ? null : pattern.matcher(p.getFileName().toString())))
								.collect(Collectors.toList());
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
