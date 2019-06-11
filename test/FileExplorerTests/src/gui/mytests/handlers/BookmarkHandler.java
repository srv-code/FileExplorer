package gui.mytests.handlers;

import gui.mytests.handlers.fs.FileAttributes;
import gui.mytests.handlers.fs.FileSystemHandler;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class BookmarkHandler {
	private static BookmarkHandler instance = null;
	
	private BookmarkHandler() {
		
	}
	
	public static BookmarkHandler getInstance() {
		if(instance == null) {
			instance = new BookmarkHandler();
		}
		return instance;
	}
	
	/* TODO
	 * Write methods for:
	 *		load() // loading from disk file
	 *		add(BookmarkedItem item) // adds to JTree UI
	 *		remove(BookmarkedItem item) // remove from JTree UI
	 */

	public BookmarkHandler add(	final DefaultMutableTreeNode treeNode, 
								final BookmarkedItem item) {
		treeNode.add(new DefaultMutableTreeNode(item));
		return this;
	}
}
