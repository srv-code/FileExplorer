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
    
}
