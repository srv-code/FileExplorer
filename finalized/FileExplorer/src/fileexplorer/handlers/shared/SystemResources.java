package fileexplorer.handlers.shared;

import fileexplorer.gui.forms.*;
import fileexplorer.handlers.fs.FileAttributes;

import java.io.File;
import java.io.IOException;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import jdk.nashorn.internal.runtime.linker.NashornGuards;

/**
 * Just a container of system wide 
 *   shareable resources.
 */
public class SystemResources {
	private SystemResources() {} // disable external instantiation
	
	/** Map of theme custom names and look and feel class names */
	public final static Map<String,String> mapThemeClasses = new HashMap<>();
	
	static { // initialization block
		/* construct mapThemeClasses */
		mapThemeClasses.put(AppPreferences.THEME_MACOSX, "javax.swing.plaf.nimbus.NimbusLookAndFeel");
		mapThemeClasses.put(AppPreferences.THEME_WINDOWS, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		mapThemeClasses.put(AppPreferences.THEME_WINDOWSCLASSIC, "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		mapThemeClasses.put(AppPreferences.THEME_UBUNTU, "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		mapThemeClasses.put(AppPreferences.THEME_METAL, "javax.swing.plaf.metal.MetalLookAndFeel");
		mapThemeClasses.put(AppPreferences.THEME_MOTIF, "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
	}
	
	// Final fields
    public final static String APP_DIR_NAME = "FileExplorer";
    public final static File APP_DIR = new File(System.getProperty("java.io.tmpdir"), APP_DIR_NAME);
    public final static String LOG_FILE_NAME = String.format("Session-%td.%<tb.%<tY_%<tH.%<tM.%<tS.%<tL.log", new Date());
    public final static String PLATFORM_LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String ACCOUNT_USER_NAME = System.getProperty("user.name");
    public final static String ACCOUNT_USER_HOME_PATH = System.getProperty("user.home");
//    public final static String LOOK_AND_FEEL_CLASS_NAME = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	
//	public final static int FTP_PORT = 21;
	public final static String ANONYMOUS_USERNAME = "anonymous";
	public final static String ANONYMOUS_PASSWORD = "";
	
    
    /* Non-final fields - will be set only by SystemHandler, 
     *   paired with public getters only */
    static ActivityLogger logger = null;    
    public static ActivityLogger getActivityLogger() {
        return logger;
    }
    
    public static FileExplorerForm formFileExplorer = null; // TODO rectify
//    public static FileExplorerForm getFileExplorerForm() {
//        return formFileExplorer;
//    }
    
    public static AppPreferences prefs;
    public static AppPreferences getAppPreferences() {
        return prefs;
    }
	
	public static enum PasteOperation { CUT, COPY, NONE; };
	public static PasteOperation pasteOperation = PasteOperation.NONE;
	public static FileAttributes[] filesToPaste = null;
	
//	public final static List<JTextField> addressBarList = new ArrayList<>();
//	public final static List<JToggleButton> bookmarkIndicatorList = new ArrayList<>();
	
	public static class IconRegistry {
		private static IconRegistry instance = null;
			
		public static IconRegistry getInstance() {
			if(instance == null) {
				instance = new IconRegistry();
				getActivityLogger().logInfo("SystemResources.IconRegistry instantiated");
			}
			return instance;
		}
		
		private ImageIcon getImage(final String path) {
			return new ImageIcon(getClass().getResource(path));
		}
		
		public final ImageIcon appIcon_small = getImage("/images/app_icon_small.png");
		public final ImageIcon appIcon_big = getImage("/images/app_icon_big.png");
		
		public final ImageIcon dirIcon_small = getImage("/images/folder_small.png");
		public final ImageIcon dirIcon_big = getImage("/images/folder_big.png");
		
		public final ImageIcon fileIcon_small = getImage("/images/file_small.png");		
		public final ImageIcon fileIcon_big = getImage("/images/file_big.png");
		
		public final ImageIcon imageIcon_small = getImage("/images/img_small.png");
		public final ImageIcon imageIcon_big = getImage("/images/img_big.png");
		
		public final ImageIcon configIcon_small = getImage("/images/config_small.png");
		public final ImageIcon configIcon_big = getImage("/images/config_big.png");
		
		public final ImageIcon textIcon_small = getImage("/images/text_small.png");		
		public final ImageIcon textIcon_big = getImage("/images/text_big.png");
		
		public final ImageIcon sourceCodeIcon_small = getImage("/images/src_small.png");
		public final ImageIcon sourceCodeIcon_big = getImage("/images/src_big.png");
		
		public final ImageIcon propertiesIcon_small = getImage("/images/about_small.png");
		
		public final ImageIcon prefsIcon_big = getImage("/images/prefs_big.png");
		
		public final ImageIcon localTabIcon_small = getImage("/images/localtab_small.png");
		public final ImageIcon closeTabButton_small = getImage("/images/close_tab_button_small.png");
		
		public final ImageIcon remoteTabIcon_small = getImage("/images/remotetab_small.png");
		
		public final ImageIcon multipleFilesIcon_big = getImage("/images/multiples_big.png");
		
		public final ImageIcon driveIcon_big = getImage("/images/drive_big.png");
		
		public final ImageIcon libraryIcon_big = getImage("/images/lib_big.png");
		
		public final ImageIcon serverIcon_big = getImage("/images/server_big.png");
		
		public final ImageIcon binaryIcon_small = getImage("/images/bin_small.png");
		public final ImageIcon binaryIcon_big = getImage("/images/bin_big.png");

		public final ImageIcon executableIcon_small = getImage("/images/exe_small.png");
		public final ImageIcon executableIcon_big = getImage("/images/exe_big.png");

		public final ImageIcon documentIcon_small = getImage("/images/doc_small.png");
		public final ImageIcon documentIcon_big = getImage("/images/doc_big.png");

		public final ImageIcon videoIcon_small = getImage("/images/video_small.png");
		public final ImageIcon videoIcon_big = getImage("/images/video_big.png");

		public final ImageIcon musicIcon_small = getImage("/images/music_small.png");
		public final ImageIcon musicIcon_big = getImage("/images/music_big.png");
		
		public final ImageIcon compressedIcon_small = getImage("/images/zip_small.png");
		public final ImageIcon compressedIcon_big = getImage("/images/zip_big.png");

		
		public static enum IconSize { SMALL, BIG };
		
		private Object[][] registry = new Object[][] {
			// image files
			new Object[] { Arrays.asList("png", "gif", "png", "jpg", "jpeg", "ico", "bmp", "tif", "tiff", "svgz"), imageIcon_small, imageIcon_big },
			
			// source code files
			new Object[] { Arrays.asList("c", "h", "cpp", "py", "java", "cs", "xml", "html", "css", "js", "asp", "aspx", "vb", "vbs"), sourceCodeIcon_small, sourceCodeIcon_big },
			
			// configuration files
			new Object[] { Arrays.asList("gitconfig", "ini", "cfg", "config", "db", "bat", "conf"), configIcon_small, configIcon_big },
			
			// text files
			new Object[] { Arrays.asList("txt", "log"), textIcon_small, textIcon_big },
			
			// binary files
			new Object[] { Arrays.asList("dat", "lnk", "bin", "class", "drv", "o", "asm"), binaryIcon_small, binaryIcon_big },

			// executable files
			new Object[] { Arrays.asList("sys", "dll", "com", "exe", "cpl", "msc", "dmg", "app", "sh", "deb"), executableIcon_small, executableIcon_big },
			
			// document files
			new Object[] { Arrays.asList("pdf", "xls", "xlsx", "doc", "docx", "rtf", "ppt", "pptx", "csv"), documentIcon_small, documentIcon_big },

			// compressed files
			new Object[] { Arrays.asList("zip", "zipx", "rar", "tar", "tgz", "cab", "iso", "gz", "7z", "bzip", "bzip2", "gzip", "wim", "jar", "xz"), compressedIcon_small, compressedIcon_big },

			// video files
			new Object[] { Arrays.asList("avi", "mp4", "mpg", "mkv", "mov", "3gp", "3gpp" , "flv", "oga", "wmv", "webm"), videoIcon_small, videoIcon_big },
			
			// audio files
			new Object[] { Arrays.asList("wav", "mp3", "mp2", "wma", "m3u", "m4a", "aac", "mid", "mxmf"), musicIcon_small, musicIcon_big }
		};
		
		public ImageIcon getFileIcon(final FileAttributes file, final IconSize size) {
			return getIcon(file.type, size);
		}
		
		public ImageIcon getTypeIcon(final String type) { // returns only big icon
			if(BookmarkedItem.TYPE_SYSTEM_DRIVE.equals(type))
				return driveIcon_big;
			if(BookmarkedItem.TYPE_LIBRARY_FOLDER.equals(type))
				return libraryIcon_big;
			if(BookmarkedItem.TYPE_REMOTE_SERVER.equals(type))
				return serverIcon_big;
			return getIcon(type, IconRegistry.IconSize.BIG);				
		}
		
		private ImageIcon getIcon(final String type, final IconSize size) {
			if(type.equals(FileAttributes.TYPE_FOLDER))
				return size==IconSize.SMALL ? dirIcon_small : dirIcon_big;
			if(!type.equals(FileAttributes.TYPE_FILE)) {
				for(Object[] record : registry) {
					if(((List)record[0]).contains(type)) {
						return (ImageIcon)record[ size==IconSize.SMALL ? 1 : 2 ];
					}
				}
				logger.logWarning("No icon associated with type=" + type); // TODO log warning
			}		
			return size==IconSize.SMALL ? fileIcon_small : fileIcon_big;
		}
	}
}
