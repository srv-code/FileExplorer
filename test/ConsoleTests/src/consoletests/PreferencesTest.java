package consoletests;

import java.util.prefs.*;
import java.io.*;


public class PreferencesTest {
    public void setPreferenceTest() throws IOException, BackingStoreException, InvalidPreferencesFormatException {
        Preferences  prefs;
        // This will define a node in which the preferences can be stored
//        prefs = Preferences.systemRoot().node(this.getClass().getName());
//        System.out.println("systemRoot=" + prefs);
        
        prefs = Preferences.userRoot().node(consoletests.Test1.class.getName());
//        prefs = Preferences.userRoot().node(this.getClass().getName());
        System.out.println("userRoot=" + prefs);
        
//        prefs = Preferences.systemNodeForPackage(this.getClass());
//        System.out.println("systemNodeForPackage=" + prefs);
        
//        prefs = Preferences.userNodeForPackage(consoletests.Test1.class);
//        System.out.println("userNodeForPackage=" + prefs);
        
/*
        String ID1 = "Test1";
        String ID2 = "Test2";
        String ID3 = "Test3";

        // First we will get the values
        // Define a boolean value
        System.out.println(prefs.getBoolean(ID1, false));
        // Define a string with default "Hello World
        System.out.println(prefs.node("level1").get(ID2, null));
        // Define a integer with default 50
        System.out.println(prefs.node("level1").node("level2").getInt(ID3, -1));

        // now set the values
        prefs.putBoolean(ID1, true);
        prefs.node("level1").put(ID2, "Hello Europa");
        prefs.node("level1").node("level2").putInt(ID3, 45);

        // Delete the preference settings for the first value
//        prefs.remove(ID1);

        // export prefs to disk file
        System.out.println("Exporting prefs...");
        FileOutputStream exportFile = new FileOutputStream("export.pref");
        prefs.exportSubtree(exportFile);
        exportFile.close();
        */
        // importing prefs
        System.out.println("Importing prefs...");
//        prefs = prefs.node("backups/imported");
        FileInputStream inFile = new FileInputStream("export.pref");
        prefs.importPreferences(inFile);
        inFile.close();
    }
    
    public static void testPreferences1(final String systemNode, final String userNode) {
//        Preferences prefs = Preferences.systemRoot();
//        System.out.println("Creating system node...");
//        System.out.println("systemRoot=" + Preferences.systemRoot().node(systemNode));
        
//        prefs = Preferences.userRoot();
        System.out.println("Creating user node...");
        System.out.println("userRoot=" + Preferences.userRoot().node(userNode));
    }
    
    public static void testPreferences2() {
        
    }
}
