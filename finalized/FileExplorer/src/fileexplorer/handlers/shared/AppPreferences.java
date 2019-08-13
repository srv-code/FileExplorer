package fileexplorer.handlers.shared;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;


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
    
    
    /* Fields can be get or set from outside. */
    // Path: /FileExplorer/gui
//    public Color bgColor, fgColor;
//    public String fontName, listType, language;
	private final String prefNodePath_Misc = "misc";
	public boolean confirmBeforeExit = true; // default value set
	final private String keyString_ConfirmBeforeExit = "confirm_before_exit";
    
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
			prefBeingLoaded = "confirm before exit";
			prefNode = userPrefs.node(prefNodePath_Misc);
			logger.logInfo("Loading preference: '%s' from node path: '%s' ...", 
					prefBeingLoaded, prefNode.absolutePath());
			confirmBeforeExit = prefNode.getBoolean(keyString_ConfirmBeforeExit, true);
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
			
			prefBeingStored = "comfirm before exit";
			prefNode = userPrefs.node(prefNodePath_Misc);
			logger.logInfo("Storing preference: '%s' to node path: '%s' ...", 
					prefBeingStored, prefNode.absolutePath());
			prefNode.putBoolean(keyString_ConfirmBeforeExit, confirmBeforeExit);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(	null,
											"Cannot store preference: " + prefBeingStored + ".\nReason: " + e.getMessage(),
											"Preference store failure",
											JOptionPane.ERROR_MESSAGE);
			logger.logSevere(e, "Cannot store preference: %s. Path: %s. Reason: %s", 
					prefBeingStored, prefNode.absolutePath(), e.getMessage());
		}
	}
	
	private final Preferences userPrefs = Preferences.userNodeForPackage(this.getClass());
	
	/***** Related to remote servers *****/
	private final String prefNodePath_RemoteServerProfiles = "remote_servers/profiles";	
	private final String usernameListDelimiter = ";";
	public final Map<String,List<String>> remoteServerProfilesMap = new HashMap<>();
	/*---- Related to remote servers ----*/
    
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
