package gui.mytests;

import gui.mytests.handlers.fs.FileAttributes;
import java.util.*;
import javax.swing.*;
import java.io.*;


public class SystemResources {
	public final static List<String> LOOKnFEEL_NAMES = Arrays.asList("Windows", "Windows Classic");
	
	public static class IconRegistry {
		private static IconRegistry instance = null;
			
		public static IconRegistry getIconRegistry() {
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
		
		public static final Icon propertiesIcon_small = 
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/about_small.png");
		
		public static final Icon multipleFilesIcon_big =
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/multiples_big.png");
		
		public static final Icon driveIcon_big =
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/drive_big.png");
		
		public static final Icon libraryIcon_big =
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/lib_big.png");
		
		public static final Icon remoteServerIcon_big =
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/remote_big.png");
		
		public static enum IconSize { SMALL, BIG };
		
		private Object[][] registry = new Object[][] {
			new Object[] { Arrays.asList("png", "gif", "png", "jpg", "jpeg"), imageIcon_small, imageIcon_big },
			new Object[] { Arrays.asList("c", "cpp", "py", "java", "cs", "xml"), sourceCodeIcon_small, sourceCodeIcon_big },
			new Object[] { Arrays.asList("gitconfig", "ini", "cfg", "config", "db"), configIcon_small, configIcon_big },
			new Object[] { Arrays.asList("txt", "log"), textIcon_small, textIcon_big }
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
				System.out.printf("Warning: No icon associated with type=%s, filename=%s\n", file.type, file.name); // TODO log warning
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
