package fileexplorer.handlers.shared;

import fileexplorer.gui.forms.FileExplorerForm;
import java.io.*;
import javax.swing.ImageIcon;


public class SystemHandler implements AutoCloseable {
    private static SystemHandler instance = null;
    
    public static SystemHandler getInstance() {
        if(instance == null) {
            instance = new SystemHandler();
            System.out.println("Info: SystemHandler initialized");
        }
        return instance;
    }
    
    private SystemHandler() {} // disable external instantiation 
    
    public void init() throws IOException {
        if(SystemResources.APP_TEMP_DIR_PATH.exists()) {
			System.out.println("Info: Temp app dir: " + SystemResources.APP_TEMP_DIR_PATH);
		} else {
            if(SystemResources.APP_TEMP_DIR_PATH.mkdirs())
                System.out.println("Info: Temp app dir created: " + SystemResources.APP_TEMP_DIR_PATH);
            else
                throw new IOException("Fatal err: Cannot create temp app dir " + SystemResources.APP_TEMP_DIR_PATH + "!");
        }
        
        try {
			System.out.println("Initializing ActivityLogger...");
            SystemResources.logger = ActivityLogger.getInstance();
			SystemResources.logger.logInfo("ActivityLogger initialized, log file: "
					+ SystemResources.logger.getFile().getAbsolutePath());
			
			SystemResources.logger.logInfo("Initializing AppPreferences...");
            SystemResources.prefs = AppPreferences.getInstance();
            
			SystemResources.logger.logInfo("Loading user preferences...");
            SystemResources.prefs.loadUserPreferences();
            
            // Start GUI
			SystemResources.logger.logInfo("Setting UI Look and Feel...");
            
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            // Setting LAF // TODO Push to AppPrefernces later
            try {
				javax.swing.UIManager.setLookAndFeel(SystemResources.LOOK_AND_FEEL_CLASS_NAME);

				/*
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
				*/
            } catch (ClassNotFoundException | InstantiationException | 
                    IllegalAccessException | javax.swing.UnsupportedLookAndFeelException exc) {
                SystemResources.logger.logSevere("Cannot set app LAF to "+ SystemResources.LOOK_AND_FEEL_CLASS_NAME +"!");
            }
            //</editor-fold>
            //</editor-fold>
            
            SystemResources.logger.logInfo("Initializing FileExplorerForm...");
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SystemResources.fileExplorerForm = new FileExplorerForm();
                }
            });
        } catch(Exception e) {
            // TODO do something more here
			SystemResources.logger.logFatal("Unhandled exception: %s", SystemResources.logger.getStackTrace(e));
        }
    }   
    
    @Override 
    public void close() {
		try {
			SystemResources.logger.logInfo("Closing ActivityLogger...");
            SystemResources.logger.close();
        } catch(IOException e) {
            System.err.println("Err: Cannot close logger. (Exc: " + e + ")");
        }
        
        System.out.println("Info: SystemHandler closing...");
    }
}
