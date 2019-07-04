package fileexplorer.handlers.shared;

import java.awt.Color;


public class AppPreferences {
    private AppPreferences() {} // disable external instantiation
    private static AppPreferences instance = null;
    
    public static AppPreferences getInstance() {
        if(instance == null) {
            instance = new AppPreferences();
//            System.out.println("Info: AppPreferences initialized");
        }
        return instance;
    }
    
    
    /* Fields can be get or set from outside. */
    // Path: /FileExplorer/gui
    public static Color bgColor, fgColor;
    public static String fontName, listType, language;
    
    public void loadUserPreferences() {}
    
    public void storeUserPreferences() {}
    
    /*
    public void loadSystemPreferences() {}
    
    public void storeSystemPreferences() {}
    */
    
    /** For both system & user */
    public void exportPreferences(final String filePath) {}
    
    /** For both system & user */
    public void importPreferences(final String filePath) {}
	
	/** 
	 * Sets the look and feel of the application on priority basis
	 * of the provided look and feel class names
	 */
	public void setAppLookAndFeel() {
		for(int i=0; i<SystemResources.LOOKnFEEL_CLASSNAMES.size(); i++) {
			try {
				javax.swing.UIManager.setLookAndFeel(SystemResources.LOOKnFEEL_CLASSNAMES.get(i));
				return;
			} catch(Exception e) {}
		}
		System.err.println("Warning: Cannot set predefined L&F: " + SystemResources.LOOKnFEEL_CLASSNAMES); // TODO log warning
	}
}
