package fileexplorer.handlers.shared;

import fileexplorer.handlers.fs.FileAttributes;
import fileexplorer.handlers.fs.LocalFileSystemHandler;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.*;


public class BookmarkHandler {
	private static BookmarkHandler instance = null;
	private final JTree tree;
	private final DefaultTreeModel treeModel;
	private final DefaultMutableTreeNode treeNodeDrives;
	private final DefaultMutableTreeNode treeNodeLibrary;
	private final DefaultMutableTreeNode treeNodeBookmarks;
	private final DefaultMutableTreeNode treeNodeRemoteServers;
	private final ActivityLogger logger = ActivityLogger.getInstance();
	
	
	private BookmarkHandler(	final JTree tree, 
								final DefaultTreeModel treeModel,
								final DefaultMutableTreeNode treeNodeDrives,
								final DefaultMutableTreeNode treeNodeLibrary,
								final DefaultMutableTreeNode treeNodeBookmarks,
								final DefaultMutableTreeNode treeNodeRemoteServers) {
		this.tree = tree;
		this.treeModel = treeModel;
		this.treeNodeDrives = treeNodeDrives;
		this.treeNodeLibrary = treeNodeLibrary;
		this.treeNodeBookmarks = treeNodeBookmarks;
		this.treeNodeRemoteServers = treeNodeRemoteServers;
				
		tree.getSelectionModel()
			.setSelectionMode(javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	public static BookmarkHandler getInstance(	final JTree tree, 
												final DefaultTreeModel treeModel,
												final DefaultMutableTreeNode treeNodeDrives,
												final DefaultMutableTreeNode treeNodeLibrary,
												final DefaultMutableTreeNode treeNodeBookmarks,
												final DefaultMutableTreeNode treeNodeRemoteServers) {
		if(instance == null) {
			instance = new BookmarkHandler(	tree, treeModel, treeNodeDrives, 
											treeNodeLibrary, treeNodeBookmarks, 
											treeNodeRemoteServers);
		}
		return instance;
	}
	
	/**
	 * Adds local drives and library folders to bookmark tree
	 */
	public void init(final LocalFileSystemHandler localFileSystemHandler) {
		try {
			for(FileAttributes file : localFileSystemHandler.listRoots()) {
				add(treeNodeDrives, 
					new BookmarkedItem( file.absolutePath, 
										BookmarkedItem.TYPE_SYSTEM_DRIVE,
										file.absolutePath));
			}
		} catch(FileNotFoundException e) {
			logger.logSevere(e, "Cannot add system roots to bookmark tree node: %s", e);
		}

		add(treeNodeLibrary,
			new BookmarkedItem(	"Home", 
									BookmarkedItem.TYPE_LIBRARY_FOLDER, 
									localFileSystemHandler.getUserHomeDirectoryPath()));
		add(treeNodeLibrary,
			new BookmarkedItem(	"Temp", 
									BookmarkedItem.TYPE_LIBRARY_FOLDER, 
									localFileSystemHandler.getTempDirectoryPath()));

		// TODO load all bookmarks and remote server hostnames from disk file

		expandAllNodes();
	}
	
	/* TODO
	 * Write methods for:
	 *		load() // loading from disk file
	 *		add(BookmarkedItem item) // adds to JTree UI
	 *		remove(BookmarkedItem item) // remove from JTree UI
	 */
	private boolean itemAlreadyPresent(final DefaultMutableTreeNode node, final BookmarkedItem item) {
		Enumeration<DefaultMutableTreeNode> bookmarkedNodes = node.children();
		while(bookmarkedNodes.hasMoreElements()) {
			if(((BookmarkedItem)bookmarkedNodes.nextElement().getUserObject()).absolutePath.equals(item.absolutePath)) {
				return true;
			}
		}
		return false;
	}
	
	private void refreshNode(final DefaultMutableTreeNode node) {
		treeModel.reload(node);
		expandAllNodes();
	}

	public BookmarkHandler addBookmarks(final FileAttributes[] files) {
		for(FileAttributes file : files)
			add(treeNodeBookmarks, 
				new BookmarkedItem("".equals(file.name) ? file.absolutePath : file.name, 
						file.type, file.absolutePath));
		refreshNode(treeNodeBookmarks);
		return this;
	}
	
	private void add(final DefaultMutableTreeNode node, final BookmarkedItem item) {
		if(!itemAlreadyPresent(node, item))
			node.add(new DefaultMutableTreeNode(item));
	}
	
	private void expandAllNodes() {
		for(int row=0; row<tree.getRowCount(); row++)
			tree.expandRow(row);
	}
	
	public BookmarkHandler removeBookmark(final FileAttributes[] files) {
		Enumeration<DefaultMutableTreeNode> nodes = treeNodeBookmarks.children();
		DefaultMutableTreeNode targetNode;
		for(FileAttributes file : files) {
			while(nodes.hasMoreElements()) {
				targetNode = nodes.nextElement();
				if(file.absolutePath.equals(((BookmarkedItem)targetNode.getUserObject()).absolutePath)) {
					targetNode.removeFromParent();
					break;
				}
			}
		}
		refreshNode(treeNodeBookmarks);
		return this;
	}
	
	public BookmarkHandler remove(final DefaultMutableTreeNode node) {
//		treeModel.removeNodeFromParent(node);
		node.removeFromParent();
		refreshNode(node);
		return this;
	}
	
//	public void remove(final DefaultMutableTreeNode parentNode, final BookmarkedItem item) {
//		Enumeration<DefaultMutableTreeNode> childrenNodes = parentNode.children();
//		DefaultMutableTreeNode node;
//		while(childrenNodes.hasMoreElements()) {
//			node = childrenNodes.nextElement();
//			if(item.equals(node.getUserObject())) {
//				remove(node);
//				return;
//			}
//		}
//	}
	
	public BookmarkHandler removeAllSiblings(final DefaultMutableTreeNode childNode) {
		((DefaultMutableTreeNode)childNode.getParent()).removeAllChildren();
		refreshNode(childNode);
//		System.out.printf("Info: All %d sibling bookmark items removed from node '%s'\n", childCount, parentNode.getUserObject()); // TODO log info
		return this;
	}

	public boolean containsBookmarkFile(final FileAttributes file) {
		Enumeration<DefaultMutableTreeNode> children = treeNodeBookmarks.children();
		while(children.hasMoreElements()) {
			if(((BookmarkedItem)children.nextElement().getUserObject()).absolutePath.equals(file.absolutePath))
				return true;
		}
		return false;
	}

	public void rename(final DefaultMutableTreeNode node, final String newName) {
		BookmarkedItem oldItem = (BookmarkedItem)node.getUserObject();
		node.setUserObject(new BookmarkedItem(newName, oldItem.type, oldItem.absolutePath));
		refreshNode(node);
//		System.out.printf("Info: Bookmark renamed from '%s' to '%s'\n", oldItem.name, newName); // log Info
	}
}
