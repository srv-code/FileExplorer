package gui.mytests;

import java.util.*;
import javax.swing.*;
import java.io.*;


public class SystemResources {
	public static class IconRegistry {
		private static IconRegistry instance = null;
			
		public static IconRegistry getIconRegistry() {
			if(instance == null) {
				instance = new IconRegistry();
				System.out.println("IconRegistry instantiated");  // TODO replace with: logger.logInfo("instantiated");
			}
			return instance;
		}
		
		// TODO Switch to commented lines before moving to production
		private Icon dirIcon = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/folder1.png"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/folder1.png");
		
		private Icon fileIcon = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/file.png"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/file.png");
		private Icon imageIcon = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/img.png"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/img.png");
		private Icon sourceCodeIcon = 
//				new ImageIcon(getClass().getResource("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/src.webp"));
				new ImageIcon("D:/Projects/Java/NetBeans/Projects/project/Summer-Project/finalized/FileExplorer/resources/images/src.webp");
		
		
		private Object[][] registry = new Object[][] {
			new Object[] { Arrays.asList(".png", ".gif", ".png", ".jpg", ".jpeg"), imageIcon },
			new Object[] { Arrays.asList(".c", ".cpp", ".py", ".java", ".cs"), sourceCodeIcon }
		};
		
		public Icon get(final File file) {
			Icon icon; // default
			if(file.isDirectory()) 
				icon = dirIcon;
			else {
				icon = fileIcon;
				String fname = file.getName();
				int dotIdx = fname.lastIndexOf('.');
				if(dotIdx != -1) {
					String ext = fname.substring(dotIdx).toLowerCase();
					for(Object[] record : registry) {
						if(((List)record[0]).contains(ext)) {
							icon = (Icon)record[1];
							break;
						}						
					}
				}
			}
			return icon;
		}
	}
	
	
	/** Tester */ // TODO Remove before moving to production
	public static void main(String[] args) {
//		System.out.println(SystemResources.getIconRegistry());
//		IconRegistry iconRegistry = SystemResources.getIconRegistry();
		IconRegistry iconRegistry = IconRegistry.getIconRegistry();
		File file = new File("a/b/c.cpp");
		System.out.println(file + "=" + iconRegistry.get(file));
	}
}
