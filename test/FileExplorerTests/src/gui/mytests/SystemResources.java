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
		
		// TODO Switch to commented lines before moving to production
		private final Icon dirIcon_SmallSize = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/folder1.png"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/folder_small.png");
		
		private final Icon dirIcon_BigSize = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/folder1.png"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/folder_big.png");
		
		private final Icon fileIcon_SmallSize = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/file.png"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/file.png");
		
		private final Icon imageIcon_SmallSize = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/img.png"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/img.png");
		
		private final Icon configIcon_SmallSize = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/src.webp"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/config.png");
		
		private final Icon textIcon_SmallSize = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/src.webp"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/text.png");
		
		private final Icon sourceCodeIcon_SmallSize = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/src.webp"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/src.webp");
		
		public static enum Size { SMALL, BIG };
		
		private Object[][] registry = new Object[][] {
			new Object[] { Arrays.asList("png", "gif", "png", "jpg", "jpeg"), imageIcon },
			new Object[] { Arrays.asList("c", "cpp", "py", "java", "cs"), sourceCodeIcon },
			new Object[] { Arrays.asList("gitconfig", "ini", "cfg", "config", "db"), configIcon },
			new Object[] { Arrays.asList("txt", "log"), textIcon }
		};
		
		public Icon get(final FileAttributes file, final Size size) {
			Icon icon; // default
			if(file.isDirectory) 
				icon = size==Size.BIG ? dirIcon_BigSize : dirIcon_SmallSize;
			else {
				icon = fileIcon;
				if(!file.type.equals("file")) {
					for(Object[] record : registry) {
						if(((List)record[0]).contains(file.type)) {
							icon = (Icon)record[1];
							break;
						}
					}
					if(icon == fileIcon)
						System.out.printf("Warning: No icon associated with type=%s, filename=%s\n", file.type, file.name); // TODO log warning
				}
			}
			return icon;
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
