package fileexplorer;

import fileexplorer.handlers.shared.ActivityLogger;
import fileexplorer.handlers.shared.SystemHandler;
import fileexplorer.handlers.shared.SystemResources;


public class Main {
	private static boolean logWrite = true; /* default value set */
	private static boolean showDebug = false; /* default value set */
	
	public static void main(final String[] args) {
        ActivityLogger logger = null;
		try {			
			inspectOptions(args);
			logger = ActivityLogger.getInstance(logWrite, showDebug);
			logger.printToConsole(ActivityLogger.LogLevel.CONFIG, "Main initialized");
			logger.printToConsole(ActivityLogger.LogLevel.CONFIG, "ActivityLogger initialized");
            SystemHandler.getInstance().init();
        } catch(IllegalArgumentException e) {
			System.err.println("Error: Invalid command line option: " + e.getMessage());
			e.printStackTrace(System.err);
		} catch(Exception e) { /* final exception handler */			
			System.err.println("Fatal error: Unhandled exception caught in Main:");
			e.printStackTrace(System.err);
        }
    }
	
	private static void inspectOptions(final String[] args) throws IllegalArgumentException {
		for(String arg: args) {
			switch(arg) {
				case "--nolog":
					logWrite = false;
					break;
					
				case "--debug":
					showDebug = true;
					break;
					
				case "--version":
					System.out.printf("%s v%.2f\n", SystemResources.APP_NAME, SystemResources.APP_VERSION);
					System.exit(0);
					
				case "--help":
					printLinesToConsole(SystemResources.getHelpText());
					System.exit(0);
					
				default:
					throw new IllegalArgumentException(arg);
			}
		}
	}
	
	private static void printLinesToConsole(final String[] text) {
		for(String line: text)
			System.out.println(line);
	}
}
