package gui.mytests.handlers;

import gui.mytests.SystemResources.IconRegistry;
import gui.mytests.handlers.fs.FileAttributes;
import java.io.File;
import javax.swing.Icon;
import javax.swing.tree.TreePath;


public final class BookmarkedItem {
	public static enum ItemType {
		SYS_DRIVE("system drive"),
		LIBRARY_FOLDER("library folder"),
		PATH(null),
		REMOTE_SERVER("remote server");
		
		private String desc;
		
		ItemType(final String desc) {
			this.desc = desc;
		}
		
		@Override 
		public String toString() {
			return desc;
		}
	}
	
	public final String name; // display name
	public final ItemType type;
	public final String absolutePath;
	public final Icon icon;
//	public TreePath path; // path from JTree root
	
	public BookmarkedItem(final String name, final ItemType type, final String absolutePath) {
		this.name = name;
		this.type = type;
		this.absolutePath = absolutePath;
		icon = getIcon(type);
	}
	
	private Icon getIcon(final ItemType type) {
		switch(type) {
				case SYS_DRIVE:
					return IconRegistry.driveIcon_big;
					
				case LIBRARY_FOLDER:
					return IconRegistry.libraryIcon_big;
					
				case REMOTE_SERVER:
					return IconRegistry.remoteServerIcon_big;
					
				case PATH: // should be dynamically assigned based on real file type
					return null;
					
				default: 
					System.err.println("Err: (Fatal) Unassigned BookmarkedItem.ItemType\n"); // TODO log fatal error
					return null;
			}
	}
	
	@Override 
	public String toString() {
//		return String.format("[%s: name=%s, path=%s]",
//				getClass().getSimpleName(), name, absolutePath);
		return name;
	}
}