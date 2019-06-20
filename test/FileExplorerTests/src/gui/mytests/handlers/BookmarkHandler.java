package gui.mytests.handlers;

import gui.mytests.handlers.fs.*;
import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.*;


public class BookmarkHandler {
	private static BookmarkHandler instance = null;
	private final JTree tree;
	final DefaultTreeModel treeModel;
	
	
	private BookmarkHandler(final JTree tree, final DefaultTreeModel treeModel) {
		this.tree = tree;
		this.treeModel = treeModel;
		
		tree.getSelectionModel()
			.setSelectionMode(javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	public static BookmarkHandler getInstance(final JTree tree, final DefaultTreeModel treeModel) {
		if(instance == null) {
			instance = new BookmarkHandler(tree, treeModel);
		}
		return instance;
	}
	
	/* TODO
	 * Write methods for:
	 *		load() // loading from disk file
	 *		add(BookmarkedItem item) // adds to JTree UI
	 *		remove(BookmarkedItem item) // remove from JTree UI
	 */
	@SuppressWarnings("unchecked")
	private List<String> getExistingPathList(final DefaultMutableTreeNode node) {
		List<String> pathList = new ArrayList<String>(node.getChildCount());
		Enumeration<DefaultMutableTreeNode> bookmarkedNodes = node.children();
		while(bookmarkedNodes.hasMoreElements())
			pathList.add(((BookmarkedItem)bookmarkedNodes.nextElement().getUserObject()).absolutePath);
		return pathList;
	}
	
	private void refreshNode(final DefaultMutableTreeNode node) {
		treeModel.reload(node);
		expandAllNodes();
	}

	public BookmarkHandler add(	final DefaultMutableTreeNode node, 
								final BookmarkedItem item) {
		List<String> bookmarkedPathList = getExistingPathList(node);
		if(!bookmarkedPathList.contains(item.absolutePath)) // check if already existing
			node.add(new DefaultMutableTreeNode(item));
		refreshNode(node);
		return this;
	}
	
	public void expandAllNodes() {
		for(int row=0; row<tree.getRowCount(); row++)
			tree.expandRow(row);
	}
	
	public BookmarkHandler remove(final DefaultMutableTreeNode node) {
		treeModel.removeNodeFromParent(node);
		refreshNode(node);
		return this;
	}
	
	public BookmarkHandler removeAllSiblings(final DefaultMutableTreeNode childNode) {
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)childNode.getParent();
		int childCount = parentNode.getChildCount();
		parentNode.removeAllChildren();
		refreshNode(childNode);
		System.out.printf("Info: All %d sibling bookmark items removed from node '%s'\n", childCount, parentNode.getUserObject()); // TODO log info
		return this;
	}

	public boolean containsFile(final DefaultMutableTreeNode parentNode, final FileAttributes file) {
		Enumeration<DefaultMutableTreeNode> children = parentNode.children();
		while(children.hasMoreElements()) {
			if(((BookmarkedItem)children.nextElement().getUserObject()).absolutePath.equals(file.absolutePath))
				return true;
		}
		return false;
	}

	public void rename(final DefaultMutableTreeNode node, final String newName) {
		BookmarkedItem oldItem = (BookmarkedItem)node.getUserObject();
		node.setUserObject(new BookmarkedItem(newName, oldItem.type, oldItem.icon, oldItem.absolutePath));
		refreshNode(node);
		System.out.printf("Info: Bookmark renamed from '%s' to '%s'\n", oldItem.name, newName); // log Info
	}
}
