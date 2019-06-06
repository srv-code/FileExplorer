package fileexplorer.handlers.shared;

import fileexplorer.gui.forms.*;

import java.io.File;
import java.util.Date;

/**
 * Just a container of system wide 
 *   shareable resources.
 */
public class SystemResources {
    // Final fields
    public final static String APP_TEMP_DIR_NAME = "FileExplorer";
    public final static File APP_TEMP_DIR_PATH = new File(System.getProperty("java.io.tmpdir"), APP_TEMP_DIR_NAME);
    public final static String LOG_FILE_NAME = String.format("Session-%td.%<tb.%<tY_%<tH.%<tM.%<tS.%<tL.%<tN.log", new Date());
    public final static String PLATFORM_LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String ACCOUNT_USER_NAME = System.getProperty("user.name");
    public final static String ACCOUNT_USER_HOME_PATH = System.getProperty("user.home");
    
    
    /* Non-final fields - will be set only by SystemHandler, 
     *   paired with public getters only */
    static ActivityLogger logger = null;    
    public static ActivityLogger getActivityLogger() {
        return logger;
    }
    
    static FileExplorerForm fileExplorerForm = null;
    public static FileExplorerForm getFileExplorerForm() {
        return fileExplorerForm;
    }
    
    static AppPreferences prefs;
    public static AppPreferences getAppPreferences() {
        return prefs;
    }
    
        
    private SystemResources() {} // disable external instantiation
}
