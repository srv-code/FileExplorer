package fileexplorer.handlers.shared;

import fileexplorer.gui.forms.FileExplorerForm;
import java.io.*;
import javax.swing.ImageIcon;


public class SystemHandler implements AutoCloseable {
    private static SystemHandler instance = null;
    
    public static SystemHandler getInstance() {
        if(instance == null) {
            instance = new SystemHandler();
            System.out.println("INFO: SystemHandler initialized");
        }
        return instance;
	}
    
    
    private SystemHandler() {} // disable external instantiation 
	
	private ActivityLogger logger;
    
    public void init() throws IOException {
        if(SystemResources.APP_DIR.exists()) {
			System.out.println("INFO: Temp app dir: " + SystemResources.APP_DIR);
		} else {
            if(SystemResources.APP_DIR.mkdirs())
                System.out.println("INFO: Temp app dir created: " + SystemResources.APP_DIR);
            else
                throw new IOException("Fatal err: Cannot create temp app dir " + SystemResources.APP_DIR + "!");
        }
        
        try {
			System.out.println("INFO: Initializing ActivityLogger...");
            logger = SystemResources.logger = ActivityLogger.getInstance();
			logger.logConfig("ActivityLogger initialized, log file: "
					+ logger.getFile().getAbsolutePath());
			
			logger.logConfig("Initializing AppPreferences...");
            SystemResources.prefs = AppPreferences.getInstance();
            
			logger.logConfig("Loading user preferences...");
            SystemResources.prefs.loadUserPreferences();
			logger.logConfig("Setting theme...");
			SystemResources.prefs.setAppLookAndFeel();
            
            logger.logConfig("Initializing FileExplorerForm...");
            
			// Start main GUI form
            SystemResources.formFileExplorer = FileExplorerForm.init();
        } catch(Exception e) {
            // TODO do something more here
			try {
				logger.logFatal(e, "Unhandled exception: %s", e);
			} catch(Exception e1) {
				System.err.println("Err: Unable to write to log: " + e1);
			}
			System.err.println("Err: (Fatal) " + e);
        }
    }   
    
    @Override 
    public void close() {
		try {
			logger.logConfig("Storing user preferences...");
			SystemResources.prefs.storeUserPreferences();
			logger.logConfig("Closing ActivityLogger...");
            logger.close();
        } catch(IOException e) {
            System.err.println("ERR: Cannot close logger. (Exc: " + e + ")");
        }
        
        System.out.println("INFO: SystemHandler closing...");
    }
}
