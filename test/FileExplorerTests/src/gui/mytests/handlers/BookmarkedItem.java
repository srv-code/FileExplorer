package gui.mytests.handlers;

import gui.mytests.handlers.fs.FileAttributes;
import java.io.File;
import javax.swing.Icon;
import javax.swing.tree.TreePath;


public final class BookmarkedItem {
	public static enum ItemType {
		SYS_DRIVE,
		LIBRARY_FOLDER,
		LOCAL_PATH,
		REMOTE_SERVER
	}
	
	public String name; // display name
	public ItemType type;
	public String absolutePath; // File object
//	public TreePath path; // path from JTree root
	
	public BookmarkedItem(final String name, final ItemType type, final String absolutePath) {
		this.name = name;
		this.type = type;
		this.absolutePath = absolutePath;
	}
	
	@Override 
	public String toString() {
//		return String.format("[%s: name=%s, path=%s]",
//				getClass().getSimpleName(), name, absolutePath);
		return name;
	}
}