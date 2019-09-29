package fileexplorer.handlers.shared;

import java.util.ArrayList;
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
            ActivityLogger.getInstance().logInfo("INFO: AppPreferences initialized");
        }
        return instance;
    }
	
	private final ActivityLogger logger = ActivityLogger.getInstance();
    
	/* preference nodes and paths */
	private final Preferences userPrefs = Preferences.userNodeForPackage(this.getClass());
	private final String NODE_PATH_MISC = "misc";
	private final String prefNodePath_RemoteServerCache = "cache/remote_servers";
	private final String prefNodePath_LocalPathBookmarks = "bookmarks/local_paths";
	private final String prefNodePath_RemoteServerBookmarks = "bookmarks/remote_servers";
    
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
	
	/* Related to local bookmarks */
	public final List<BookmarkedItem> localBookmarkedItemList = new ArrayList<>();
	public final List<BookmarkedItem> remoteServerBookmarkedItemList = new ArrayList<>();
	
	private final String BOOKMARKEDITEM_KEY_NAME = "name";
	private final String BOOKMARKEDITEM_KEY_TYPE = "type";
	private final String BOOKMARKEDITEM_KEY_PATH = "path";
	
	
	/* @future Apply try-catch for loading each pref categories. The current design
	 *	represents a single point of failure.
	 */
    public void loadUserPreferences() {
		String prefBeingLoaded = null;
		Preferences prefNode = null;
		try {
			/* load bookmarked local paths */
			prefBeingLoaded = "bookmarked local paths";
			prefNode = userPrefs.node(prefNodePath_LocalPathBookmarks);
			logger.logInfo("Loading preference: '%s' from node path: '%s' ...", 
					prefBeingLoaded, prefNode.absolutePath());
			for(String nodeName: prefNode.childrenNames()) {
//				System.out.println("  // node: " + nodeName); // DEBUG
				Preferences newNode = prefNode.node(nodeName);
				if(newNode.get(BOOKMARKEDITEM_KEY_PATH, null) == null)
					logger.logSevere(null, "Cannot load bookmarked local path: %s={%s=%s, %s=%s, %s=%s}",
							nodeName,
							BOOKMARKEDITEM_KEY_NAME, newNode.get(BOOKMARKEDITEM_KEY_NAME, null),
							BOOKMARKEDITEM_KEY_TYPE, newNode.get(BOOKMARKEDITEM_KEY_TYPE, null),
							BOOKMARKEDITEM_KEY_PATH, newNode.get(BOOKMARKEDITEM_KEY_PATH, null));
				else 
					localBookmarkedItemList.add(
							new BookmarkedItem(
								newNode.get(BOOKMARKEDITEM_KEY_NAME, null),
								newNode.get(BOOKMARKEDITEM_KEY_TYPE, null),
								newNode.get(BOOKMARKEDITEM_KEY_PATH, null)));
			}
			
			/* load bookmarked remote servers */
			prefBeingLoaded = "bookmarked remote servers";
			prefNode = userPrefs.node(prefNodePath_RemoteServerBookmarks);
			logger.logInfo("Loading preference: '%s' from node path: '%s' ...", 
					prefBeingLoaded, prefNode.absolutePath());
			for(String nodeName: prefNode.childrenNames()) {
				System.out.println("  // node: " + nodeName); // DEBUG
				Preferences newNode = prefNode.node(nodeName);
				if(newNode.get(BOOKMARKEDITEM_KEY_PATH, null) == null)
					logger.logSevere(null, "Cannot load bookmarked remote server: %s={%s=%s, %s=%s, %s=%s}",
							nodeName,
							BOOKMARKEDITEM_KEY_NAME, newNode.get(BOOKMARKEDITEM_KEY_NAME, null),
							BOOKMARKEDITEM_KEY_TYPE, newNode.get(BOOKMARKEDITEM_KEY_TYPE, null),
							BOOKMARKEDITEM_KEY_PATH, newNode.get(BOOKMARKEDITEM_KEY_PATH, null));
				else 
					remoteServerBookmarkedItemList.add(
							new BookmarkedItem(
								newNode.get(BOOKMARKEDITEM_KEY_NAME, null),
								newNode.get(BOOKMARKEDITEM_KEY_TYPE, null),
								newNode.get(BOOKMARKEDITEM_KEY_PATH, null)));
			}
			
			/* load cached remote server profiles */
			prefBeingLoaded = "cached remote server profiles";
			prefNode = userPrefs.node(prefNodePath_RemoteServerCache);
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
//					System.out.printf("  // loaded into map: [key=%s, value=%s]\n", host, usernameList); // DEBUG
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
			/* save bookmarked local paths */
			prefBeingStored = "bookmarked local paths";
			prefNode = userPrefs.node(prefNodePath_LocalPathBookmarks);
			logger.logInfo("Storing preference: '%s' to node path: '%s' ...", 
					prefBeingStored, prefNode.absolutePath());
			int itemID = 0;
			for(BookmarkedItem item: SystemResources.formFileExplorer.bookmarkHandler.getLocalPathBookmarks()) {				
//				System.out.println("  // item: " + item); // DEBUG
				Preferences newNode = prefNode.node(String.valueOf(++itemID));
				newNode.put("name", item.name);
				newNode.put("type", item.type);
				newNode.put("path", item.absolutePath);
			}
			prefNode.flush();
			
			/* save bookmarked remote servers */
			prefBeingStored = "bookmarked remote servers";
			prefNode = userPrefs.node(prefNodePath_RemoteServerBookmarks);
			logger.logInfo("Storing preference: '%s' to node path: '%s' ...", 
					prefBeingStored, prefNode.absolutePath());
			itemID = 0;
			for(BookmarkedItem item: SystemResources.formFileExplorer.bookmarkHandler.getRemoteServerBookmarks()) {
//				System.out.println("  // item: " + item); // DEBUG
				Preferences newNode = prefNode.node(String.valueOf(++itemID));
				newNode.put("name", item.name);
				newNode.put("type", item.type);
				newNode.put("path", item.absolutePath);
			}
			prefNode.flush();
			
			/* save cached remote server profiles */
			prefBeingStored = "cached remote server profiles";
			prefNode = userPrefs.node(prefNodePath_RemoteServerCache);
			logger.logInfo("Storing preference: '%s' to node path: '%s' ...", 
					prefBeingStored, prefNode.absolutePath());
			for(Map.Entry<String,List<String>> entry: remoteServerProfilesMap.entrySet()) {
				StringBuilder sbUserNames = new StringBuilder();
				for(String name: entry.getValue())
					sbUserNames.append(name).append(usernameListDelimiter);
				prefNode.put(entry.getKey(), sbUserNames.toString());
			}
			prefNode.flush();
			
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
