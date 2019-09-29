package fileexplorer;

import fileexplorer.handlers.shared.SystemHandler;


public class Main {
    public static void main(final String[] args) {
        try {
            System.out.println("INFO: Main initialized");
            SystemHandler.getInstance().init();
        } catch(Exception e) { // final exception handler
			System.err.println("Fatal err: Unhandled exception caught in Main:");
			e.printStackTrace(System.err);
        }
    }
}
