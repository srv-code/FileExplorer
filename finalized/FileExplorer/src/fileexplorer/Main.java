package fileexplorer;

import fileexplorer.handlers.shared.ActivityLogger;
import fileexplorer.handlers.shared.SystemHandler;
import fileexplorer.handlers.shared.SystemResources;


public class Main {
    public static void main(final String[] args) {
        try {
            System.out.println("Info: Main initialized");
            SystemHandler.getInstance().init();
        } catch(Exception e) { // final exception handler
			System.err.println("Fatal err: Unhandled exception caught in Main:");
			e.printStackTrace(System.err);
        }
    }
}
