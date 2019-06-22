package gui.mytests;

import gui.mytests.handlers.fs.FileAttributes;
import java.util.*;
import javax.swing.*;
import java.io.*;


public class SystemResources {
	public final static List<String> LOOKnFEEL_NAMES = Arrays.asList("Windows", "Windows Classic");
	
	public static class IconRegistry {
		private static IconRegistry instance = null;
			
		public static IconRegistry getInstance() {
			if(instance == null) {
				instance = new IconRegistry();
				System.out.println("Icon: IconRegistry instantiated");  // TODO log info
			}
			return instance;
		}
		
		/* TODO Replace with the absolute paths with the following before moving to production:
			new ImageIcon(getClass().getResource("/images/file.png"));
		*/
		
		public static final Icon dirIcon_small = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/folder_small.png");		
		public static final Icon dirIcon_big = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/folder_big.png");		
		
		public static final Icon fileIcon_small = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/file_small.png");		
		public static final Icon fileIcon_big = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/file_big.png");
		
		public static final Icon imageIcon_small = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/img_small.png");
		public static final Icon imageIcon_big = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/img_big.png");
		
		public static final Icon configIcon_small = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/config_small.png");
		public static final Icon configIcon_big = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/config_big.png");
		
		public static final Icon textIcon_small = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/text_small.png");		
		public static final Icon textIcon_big = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/text_big.png");
		
		public static final Icon sourceCodeIcon_small = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/src_small.png");
		public static final Icon sourceCodeIcon_big = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/src_big.png");
		
//		public static final Icon propertiesIcon_small = 
//				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/about_small.png");
		
		public static final Icon multipleFilesIcon_big =
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/multiples_big.png");
		
		public static final Icon driveIcon_big =
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/drive_big.png");
		
		public static final Icon libraryIcon_big =
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/lib_big.png");
		
		public static final Icon remoteServerIcon_big =
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/remote_big.png");
		
		public static final Icon binaryIcon_small =
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/bin_small.png");
		public static final Icon binaryIcon_big =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/bin_big.png");

		public static final Icon executableIcon_small =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/exe_small.png");
		public static final Icon executableIcon_big =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/exe_big.png");

		public static final Icon documentIcon_small =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/doc_small.png");
		public static final Icon documentIcon_big =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/doc_big.png");

		public static final Icon videoIcon_small =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/video_small.png");
		public static final Icon videoIcon_big =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/video_big.png");

		public static final Icon musicIcon_small =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/music_small.png");
		public static final Icon musicIcon_big =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/music_big.png");

		
		public static final Icon compressedIcon_small =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/zip_small.png");
		public static final Icon compressedIcon_big =
						new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/zip_big.png");

		
		public static enum IconSize { SMALL, BIG };
		
		private Object[][] registry = new Object[][] {
			// image files
			new Object[] { Arrays.asList("png", "gif", "png", "jpg", "jpeg", "ico", "bmp", "tif", "tiff", "svgz"), imageIcon_small, imageIcon_big },
			
			// source code files
			new Object[] { Arrays.asList("c", "h", "cpp", "py", "java", "cs", "xml", "html", "css", "js", "asp", "aspx"), sourceCodeIcon_small, sourceCodeIcon_big },
			
			// configuration files
			new Object[] { Arrays.asList("gitconfig", "ini", "cfg", "config", "db", "bat"), configIcon_small, configIcon_big },
			
			// text files
			new Object[] { Arrays.asList("txt", "log"), textIcon_small, textIcon_big },
			
			// binary files
			new Object[] { Arrays.asList("dat", "lnk", "bin", "class"), binaryIcon_small, binaryIcon_big },

			// executable files
			new Object[] { Arrays.asList("sys", "dll", "com", "exe", "dmg", "app", "sh", "deb"), executableIcon_small, executableIcon_big },
			
			// document files
			new Object[] { Arrays.asList("pdf", "xls", "xlsx", "doc", "docx", "ppt", "pptx", "csv"), documentIcon_small, documentIcon_big },

			// compressed files
			new Object[] { Arrays.asList("zip", "zipx", "rar", "tar", "tgz", "cab", "iso", "gz", "7z", "bzip", "bzip2", "gzip", "wim", "jar", "xz"), compressedIcon_small, compressedIcon_big },

			// video files
			new Object[] { Arrays.asList("avi", "mp4", "mpg", "mkv", "mov", "3gp", "3gpp" , "flv", "oga", "wmv", "webm"), videoIcon_small, videoIcon_big },
			
			// audio files
			new Object[] { Arrays.asList("wav", "mp3", "mp2", "wma", "m3u", "m4a", "aac", "mid", "mxmf"), musicIcon_small, musicIcon_big }
		};
		
		public Icon getFileIcon(final FileAttributes file, final IconSize size) {
			if(file.isDirectory) 
				return size==IconSize.SMALL ? dirIcon_small : dirIcon_big;
			
			if(!file.type.equals("file")) {
				for(Object[] record : registry) {
					if(((List)record[0]).contains(file.type)) {
						return (Icon)record[ size==IconSize.SMALL ? 1 : 2 ];
					}
				}
				System.out.println("Warning: No icon associated with type=" + file.type); // TODO log warning
			}
		
			return size==IconSize.SMALL ? fileIcon_small : fileIcon_big;
		}
	}
	
	
	/** Tester */ // TODO Remove before moving to production
//	public static void main(String[] args) {
////		System.out.println(SystemResources.getIconRegistry());
////		IconRegistry iconRegistry = SystemResources.getIconRegistry();
//		IconRegistry iconRegistry = IconRegistry.getIconRegistry();
//		File file = new File("a/b/c.cpp");
//		System.out.println(file + "=" + iconRegistry.get(file));
//	}
}
