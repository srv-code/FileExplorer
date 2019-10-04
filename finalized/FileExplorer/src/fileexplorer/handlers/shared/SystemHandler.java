package fileexplorer.handlers.shared;

import fileexplorer.gui.forms.FileExplorerForm;
import java.io.*;

public class SystemHandler implements AutoCloseable {

    private static SystemHandler instance = null;
    private ActivityLogger logger;

    public static SystemHandler getInstance() {
        if (instance == null) {
            instance = new SystemHandler();
            ActivityLogger.getInstance().printToConsole(ActivityLogger.LogLevel.CONFIG, "SystemHandler initialized");
        }
        return instance;
    }

    private SystemHandler() {} // disable external instantiation 

    public void init() throws IOException {
        logger = ActivityLogger.getInstance();
        if (SystemResources.APP_DIR.exists()) {
            logger.printToConsole(ActivityLogger.LogLevel.INFO, "Temp app dir: " + SystemResources.APP_DIR);
        } else {
            if (SystemResources.APP_DIR.mkdirs())
                logger.printToConsole(ActivityLogger.LogLevel.INFO, "Temp app dir created: " + SystemResources.APP_DIR);
            else
                throw new IOException("Fatal error: Cannot create temp app dir " + SystemResources.APP_DIR + "!");
        }        
      
        try {
            logger.printToConsole(ActivityLogger.LogLevel.CONFIG, "Initializing ActivityLogger...");
            File logFile = logger.getFile();
            if(logFile != null)
                logger.logConfig("ActivityLogger initialized, log file: "
                        + logFile.getAbsolutePath());

            logger.logConfig("Initializing AppPreferences...");
            SystemResources.prefs = AppPreferences.getInstance();

            logger.logConfig("Loading user preferences...");
            SystemResources.prefs.loadUserPreferences();
            logger.logConfig("Setting theme...");
            SystemResources.prefs.setAppLookAndFeel();

            logger.logConfig("Initializing FileExplorerForm...");

            // Start main GUI form
            SystemResources.formFileExplorer = FileExplorerForm.init();
        } catch (Exception e) {
            // TODO do something more here
            try {
                logger.logFatal(e, "Unhandled exception: %s", e);
            } catch (Exception e1) {
                logger.printToConsole(ActivityLogger.LogLevel.SEVERE, "Unable to write to log", e1);
            }
            logger.printErrorToConsole(ActivityLogger.LogLevel.FATAL, "Uncaught error: ", e);
        }
    }

    @Override
    public void close() {
        try {
            logger.logConfig("Storing user preferences...");
            SystemResources.prefs.storeUserPreferences();
            logger.logConfig("Closing ActivityLogger...");
            logger.close();
        } catch (IOException e) {
            System.err.println("ERR: Cannot close logger. (Exc: " + e + ")");
        }
        logger.printToConsole(ActivityLogger.LogLevel.CONFIG, "SystemHandler closing...");
    }
}
