package fileexplorer.handlers.shared;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;


public class AppPreferences {
    private AppPreferences() {} // disable external instantiation
    private static AppPreferences instance = null;
    
    public static AppPreferences getInstance() {
        if(instance == null) {
            instance = new AppPreferences();
            ActivityLogger.getInstance().logInfo("Info: AppPreferences initialized");
        }
        return instance;
    }
	
	private ActivityLogger logger = ActivityLogger.getInstance();
    
	/* preference nodes and paths */
	private final Preferences userPrefs = Preferences.userNodeForPackage(this.getClass());
	private final String NODE_PATH_MISC = "misc";
	private final String prefNodePath_RemoteServerProfiles = "remote_servers/profiles";	
    
    /* preference key strings */	
	public static final String KEY_CONFIRM_BEFORE_EXIT	= "confirm_exit";	
	public static final String KEY_LANGUAGE				= "lang";	
	public static final String KEY_THEME_CLASS_NAME		= "theme";
	
	/* preference values */
	public static final String LANG_ENGLISH	= "en";
	public static final String LANG_FRENCH	= "fr";
	public static final String LANG_GERMAN	= "ge";
	
	public static final String THEME_MACOSX				= "macosx";
	public static final String THEME_METAL				= "metal";
	public static final String THEME_MOTIF				= "motif";
	public static final String THEME_WINDOWS			= "windows";
	public static final String THEME_WINDOWSCLASSIC		= "windows_classic";
	public static final String THEME_UBUNTU				= "ubuntu";
	
	/* current session values */
	public boolean confirmBeforeExit	= true; // default value set
	public String language				= LANG_ENGLISH; // default value set
	public String themeClassName		= THEME_MACOSX; // default value set
	
	/* Related to remote servers */	
	private final String usernameListDelimiter = ";";
	public final Map<String,List<String>> remoteServerProfilesMap = new HashMap<>();
	
	
    public void loadUserPreferences() {
		String prefBeingLoaded = null;
		Preferences prefNode = null;
		try {
			/* load remote server profiles */
			prefBeingLoaded = "remote server profiles";			
			prefNode = userPrefs.node(prefNodePath_RemoteServerProfiles);
			logger.logInfo("Loading preference: '%s' from node path: '%s' ...", 
					prefBeingLoaded, prefNode.absolutePath());
			String[] hosts = prefNode.keys();
//			System.out.printf("  // prefNode=%s, hosts=%s\n", 
//					prefNode.absolutePath(), Arrays.toString(hosts));
			if(hosts!=null || hosts.length>0) {
				/* load into map */
				String usernames;
				List<String> usernameList;
				for(String host: hosts) {
					usernames = prefNode.get(host, "");
					usernameList = new ArrayList<>();
					if(usernames.length()>0) {
						for(String name: usernames.split(usernameListDelimiter))
							if(name.trim().length()>0)
								usernameList.add(name);
					}
//					System.out.printf("  // loaded into map: [key=%s, value=%s]\n", host, usernameList);
					remoteServerProfilesMap.put(host, usernameList);
				}
			}
			
			/* load misc prefs */
			prefNode = userPrefs.node(NODE_PATH_MISC);
			
			prefBeingLoaded = "confirm before exit";
			logger.logInfo("Loading preference: '%s' from node path: '%s' ...", 
					prefBeingLoaded, prefNode.absolutePath());
			confirmBeforeExit = prefNode.getBoolean(KEY_CONFIRM_BEFORE_EXIT, confirmBeforeExit);
			
			prefBeingLoaded = "theme name";
			logger.logInfo("Loading preference: '%s' from node path: '%s' ...", 
					prefBeingLoaded, prefNode.absolutePath());
			themeClassName = prefNode.get(KEY_THEME_CLASS_NAME, themeClassName);
//			System.out.println("  // loaded: themeClassName=" + themeClassName);
						
			prefBeingLoaded = "language";
			logger.logInfo("Loading preference: '%s' from node path: '%s' ...", 
					prefBeingLoaded, prefNode.absolutePath());
			language = prefNode.get(KEY_LANGUAGE, language);
		} catch(BackingStoreException e) {
			JOptionPane.showMessageDialog(	null,
											"Cannot load preference: " + prefBeingLoaded + ".\nReason: " + e.getMessage(),
											"Preference load failure",
											JOptionPane.ERROR_MESSAGE);
			logger.logSevere(e, "Cannot load preference: %s. Path: %s. Reason: %s", 
					prefBeingLoaded, prefNode.absolutePath(), e.getMessage());
		}
	}
	
	public void storeUserPreferences() {
		String prefBeingStored = null;
		Preferences prefNode = null;
		
		try {
			/* save remote server profiles */
			prefBeingStored = "remote server profiles";
			prefNode = userPrefs.node(prefNodePath_RemoteServerProfiles);
			logger.logInfo("Storing preference: '%s' to node path: '%s' ...", 
					prefBeingStored, prefNode.absolutePath());
			for(Map.Entry<String,List<String>> entry: remoteServerProfilesMap.entrySet()) {
				StringBuilder sbUserNames = new StringBuilder();
				for(String name: entry.getValue())
					sbUserNames.append(name).append(usernameListDelimiter);
				prefNode.put(entry.getKey(), sbUserNames.toString());
			}
			
			prefNode = userPrefs.node(NODE_PATH_MISC);
			if(needsResettingMiscPrefs) {
				try {
					logger.logConfig("Resetting all application preferences from path='%s'...", userPrefs.absolutePath());
					prefNode.removeNode();
				} catch(BackingStoreException e) {
					JOptionPane.showMessageDialog(	null,
													"Cannot reset application preferences. Reason: " + e,
													"Preference remova; failure",
													JOptionPane.ERROR_MESSAGE);
					logger.logSevere(e, "Cannot reset application preferences. Reason: %s", e);
				}
			} else {
				prefBeingStored = "confirm before exit";
				logger.logInfo("Storing preference: '%s' to node path: '%s' ...", 
						prefBeingStored, prefNode.absolutePath());
				prefNode.putBoolean(KEY_CONFIRM_BEFORE_EXIT, confirmBeforeExit);

				prefBeingStored = "theme name";
				logger.logInfo("Storing preference: '%s' to node path: '%s' ...", 
						prefBeingStored, prefNode.absolutePath());
				prefNode.put(KEY_THEME_CLASS_NAME, themeClassName);

				prefBeingStored = "language";
				logger.logInfo("Storing preference: '%s' to node path: '%s' ...", 
						prefBeingStored, prefNode.absolutePath());
				prefNode.put(KEY_LANGUAGE, language);
			}
			prefNode.flush();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(	null,
											"Cannot store preference: " + prefBeingStored + ".\nReason: " + e.getMessage(),
											"Preference store failure",
											JOptionPane.ERROR_MESSAGE);
			logger.logSevere(e, "Cannot store preference: %s. Path: %s. Reason: %s", 
					prefBeingStored, prefNode.absolutePath(), e.getMessage());
		}
	}
	
	private boolean needsResettingMiscPrefs = false;
	
	/** resets all the preferences */
	public void resetAll() {
		needsResettingMiscPrefs = true; // marks for resetting
	}
	
	/*
    public void loadSystemPreferences() {}
    
    public void storeSystemPreferences() {}
    */
    
    /** For both system & user */
    public void exportPreferences(final String filePath) {
		throw new UnsupportedOperationException("Not developed yet");
	}
    
    /** For both system & user */
    public void importPreferences(final String filePath) {
		throw new UnsupportedOperationException("Not developed yet");
	}
	
	public void setAppLookAndFeel() {
		try {
			javax.swing.UIManager.setLookAndFeel(SystemResources.mapThemeClasses.get(themeClassName));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(	null,
											"Cannot set application theme to '" + themeClassName + 
													"'.\nReason: " + e,
											"Theme load failure",
											JOptionPane.ERROR_MESSAGE);
			logger.logSevere(e, "Cannot set theme '%s'. Reason: %s", themeClassName, e);
		}
	}
}
