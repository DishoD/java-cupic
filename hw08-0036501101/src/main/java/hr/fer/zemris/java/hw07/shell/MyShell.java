package hr.fer.zemris.java.hw07.shell;

/**
 * A simple shell program.
 * 
 * @author Disho
 *
 */
public class MyShell {

	/**
	 * Main method. Controls the flow of the program.
	 * 
	 * @param args ignorable
	 */
	public static void main(String[] args) {
		StandardIOShell shell = StandardIOShell.getInstance();
		shell.printGreetingMessage();
		
		while(true) {
			String prompt = shell.prompt();
			
			String[] arguments = prompt.split("\\s+");
			String commandName = arguments[0];
			String commandArguments = prompt.substring(commandName.length()).trim();
			
			ShellCommand command = shell.commands().get(commandName);
			
			if(command == null) {
				shell.writeln("Unknown command: '" + commandName + "'. Type 'help' to list all commands.");
			} else {
				try {
					ShellStatus status = command.executeCommand(shell, commandArguments);
					
					if(status == ShellStatus.TERMINATE) {
						shell.terminate();
						System.exit(0);
					}
				} catch(ShellIOException ex) {
					System.out.println("Shell IO error occured. Terminating the program...");
					shell.terminate();
					System.exit(0);
				} catch(IllegalArgumentException ex) {
					shell.writeln("error: " + ex.getMessage());
				}
			}
		}
	}

}
