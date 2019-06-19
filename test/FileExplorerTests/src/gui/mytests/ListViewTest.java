package gui.mytests;

import gui.mytests.handlers.BookmarkHandler;
import gui.mytests.handlers.BookmarkedItem;
import gui.mytests.handlers.fs.EndInNavigationHistoryException;
import gui.mytests.handlers.fs.FileAttributes;
import gui.mytests.handlers.fs.FileSystemHandler;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.io.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;
import java.awt.event.MouseEvent;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import javax.swing.table.TableModel;
import java.awt.event.*;
import gui.mytests.*;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static gui.mytests.SystemResources.IconRegistry;
import static gui.mytests.SystemResources.IconRegistry.IconSize;


public class ListViewTest extends javax.swing.JFrame {
	private BookmarkHandler bookmarkHandler = null;
    private javax.swing.tree.DefaultMutableTreeNode treeNodeQuickAccess;
    private javax.swing.tree.DefaultMutableTreeNode treeNodeDrives;
    private javax.swing.tree.DefaultMutableTreeNode treeNodeLibrary;
    private javax.swing.tree.DefaultMutableTreeNode treeNodeBookmarks;
    private javax.swing.tree.DefaultMutableTreeNode treeNodeRemoteServers;
	private FileSystemHandler fileSystemHandler = null;
	private IconRegistry iconRegistry = null;
	private DefaultTableModel tableModel = null;
	private String lastVisitedPath = null;
	
	private final String userHomeDirectoryPath, systemTempDirPath;
	private final int FILE_OBJECT_COLUMN_INDEX = 1;
	private final int ICON_COLUMN_INDEX = 0;
	
	/* Other frame instances */
	private JFrame propertiesForm = null;

	private void setPopupFileSelectedOptions() {
		// reset all options
		menuOpen.setVisible(true);
		menuOpenWith.setVisible(true);
        menuPrint.setVisible(true);
		menuOpenUsingSystem.setVisible(true);
		menuOpenInNewTab.setVisible(true);
        jSeparator1.setVisible(true);
		menuBookmark.setVisible(true);
		menuCreateLink.setVisible(true);
        jSeparator2.setVisible(true);
		menuCut.setVisible(true);
		menuCopy.setVisible(true);
		menuPaste.setVisible(pasteOperation != PasteOperation.NONE);
		menuRename.setVisible(true);
		menuDelete.setVisible(true);
        jSeparator5.setVisible(true);
		menuProperties.setVisible(true);
		
        // set appropriate options
		if(selectedFiles.length == 1) {
			if(selectedFiles[0].isDirectory) {
				menuOpenWith.setVisible(false);
                menuPrint.setVisible(false);
			} else if(selectedFiles[0].isLink) {
				menuCreateLink.setVisible(false);
				menuOpenUsingSystem.setVisible(false);
                menuPrint.setVisible(false);
			} else { // for files
				menuOpenInNewTab.setVisible(false);
				menuOpenUsingSystem.setVisible(false);
			}
		} else {
            menuPrint.setVisible(false);
            menuOpen.setVisible(false);
			menuOpenWith.setVisible(false);
            menuOpenUsingSystem.setVisible(false);
			menuOpenInNewTab.setVisible(false);
			jSeparator1.setVisible(false);
			menuRename.setVisible(false);
		}
	}
	
	private final class ShowPropertiesAction extends AbstractAction {
		ShowPropertiesAction() {
            super("Properties", IconRegistry.propertiesIcon_small);
            putValue(SHORT_DESCRIPTION, "Show properties");
            putValue(MNEMONIC_KEY, KeyEvent.VK_ENTER);
        }
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			FilePropertiesForm.init(	selectedFiles, 
										iconRegistry.getFileIcon(selectedFiles[0], IconSize.BIG), 
										fileSystemHandler);
		}
	}
	
	private final ShowPropertiesAction showPropertiesAction = new ShowPropertiesAction();
	
	
	/**
	 * Creates new form ListViewTest
	 */
	public ListViewTest() throws FileNotFoundException {
		System.out.println("Info: Initializing form UI components..."); // TODO log info
		initComponents();
		
		userHomeDirectoryPath = System.getProperty("user.home");
		systemTempDirPath = System.getProperty("java.io.tmpdir");
		
		System.out.println("Info: Initializing FileSystemHandler..."); // TODO log info
		fileSystemHandler = FileSystemHandler.getHandler(userHomeDirectoryPath);
		
		iconRegistry = IconRegistry.getIconRegistry();
		
		System.out.println("Info: Setting bookmarks tree..."); // TODO log info
		setBookmarks();
		
		System.out.println("Info: Setting file list table..."); // TODO log info
		setTable();
		
//		// Setting Actions
		System.out.println("Info: Attaching Actions...");
		menuProperties.setAction(showPropertiesAction);
	}
	
	private void setBookmarks() {
		bookmarkHandler = BookmarkHandler.getInstance();
		try {
			for(FileAttributes file : fileSystemHandler.listRoots()) {
				bookmarkHandler.add(	treeNodeDrives, 
										new BookmarkedItem(file.absolutePath, BookmarkedItem.ItemType.SYS_DRIVE, file.absolutePath));
			}
		} catch(FileNotFoundException e) {
			System.err.println("Err: Cannot add system roots to bookmark tree node: " + e); // TODO Log in logger 
			e.printStackTrace(System.err);
		}
		
		bookmarkHandler	.add(	treeNodeLibrary,
								new BookmarkedItem("Home", BookmarkedItem.ItemType.LIBRARY_FOLDER, userHomeDirectoryPath))
						.add(	treeNodeLibrary,
								new BookmarkedItem("Temp", BookmarkedItem.ItemType.LIBRARY_FOLDER, systemTempDirPath));
		
		// TODO load all bookmarks and remote server hostnames from disk file
		
		// expanding all nodes
		for(int row=0; row<treeQuickAccess.getRowCount(); row++)
			treeQuickAccess.expandRow(row);
		
		treeQuickAccess.getSelectionModel()
			.setSelectionMode(javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	private void setTable() {
		tableModel = (DefaultTableModel) tableFileList.getModel();
		tableFileList.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
		
		DefaultTableCellRenderer rightJustifiedCellRenderer = new DefaultTableCellRenderer();
		rightJustifiedCellRenderer.setHorizontalAlignment(JLabel.RIGHT);
		
		DefaultTableCellRenderer centerJustifiedCellRenderer = new DefaultTableCellRenderer();
		centerJustifiedCellRenderer.setHorizontalAlignment(JLabel.CENTER);

		tableFileList.getColumnModel().getColumn(3).setCellRenderer(rightJustifiedCellRenderer);
		tableFileList.getColumnModel().getColumn(4).setCellRenderer(centerJustifiedCellRenderer);
		
		updateTableList();
		
		// initially no history
		btnGoBackInHistory.setEnabled(false); 
		btnGoForwardInHistory.setEnabled(false);
	}
	
	/* calls updateTableList() & navigateTo() */ 
	private void loadPath(final String dirPath, final boolean registerInHistory) {
		try {
			loadPath(fileSystemHandler.getFileAttributes(dirPath), registerInHistory);
		} catch(Exception e) {
			System.err.println("Err: Cannot load directory: " + dirPath); // log error
		}
	}

	private void loadPath(final FileAttributes dir, final boolean registerInHistory) {
		System.out.println("Info: Loading path " + dir.absolutePath + "...");
		if(!dir.equals(fileSystemHandler.getCurrentWorkingDirectory())) // eliminate redundant loading for same path
			navigateTo(dir, registerInHistory);
		updateTableList();
	} 

	/** Updates with currentWorkingDirectory */
	private void updateTableList() {		
		System.out.println("Info: Updating table list for " + fileSystemHandler.getCurrentWorkingDirectory().absolutePath); // TODO log info & show in status
		try {
			tableModel.setRowCount(0);
			setTableRows(fileSystemHandler.listFiles(fileSystemHandler.getCurrentWorkingDirectory()));

			System.out.println("Info: Folder listing updated"); // TODO show in status bar
		} catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(	this,
											String.format("Cannot update table list!\n  Path: %s\n  Reason: %s",
													fileSystemHandler.getCurrentWorkingDirectory().absolutePath, e),
											"Directory listing error",
											JOptionPane.ERROR_MESSAGE);
			System.err.println("Err: Cannot update table list: " +  e); // log error
		}
	}

	/* Only change the currentWorkingDirectory */
	private void navigateTo(final FileAttributes dir, final boolean registerInHistory) {
		System.out.println("Info: Navigating to " + dir.absolutePath);  // TODO log info
		try {
			fileSystemHandler.navigateTo(dir, registerInHistory);
			
			// set navigation buttons status 
			btnGoBackInHistory.setEnabled(fileSystemHandler.historyHandler.canGoBackward());
			btnGoForwardInHistory.setEnabled(fileSystemHandler.historyHandler.canGoForward());
			btnGoToParentDir.setEnabled(fileSystemHandler.canGoToParent());
			
			txtPathAddress.setText(dir.absolutePath);
			lastVisitedPath = dir.absolutePath;
		} catch(FileNotFoundException|InvalidPathException e) {
			JOptionPane.showMessageDialog(	this,
											String.format("Cannot navigate to path!\n  Path: %s\n  Reason: %s", 
													dir.absolutePath, e),
											"Navigation error",
											JOptionPane.ERROR_MESSAGE);
			System.err.printf("Err: Cannot navigate to path %s (%s)\n", dir.absolutePath, e); // log error
		}
	}
	
	/*
	private void navigateTo(final FileAttributes path) {
		navigateTo(path.absolutePath);
	}
	
	private void navigateTo(final String path) {
//		if(path.equals(lastVisitedPath)) // save from irrelevant processing
//			return;
		
		try {
			System.out.printf("Info: Loading folder listing of '%s'...\n", path); // TODO log info and show in status bar
			
//			System.out.println("  // path loaded: " + path);

			// update navigation button status 
			
		} catch(InvalidPathException|FileNotFoundException e) {
			JOptionPane.showMessageDialog(	this,
											"Invalid path: " + path,
											"Path error",
											JOptionPane.ERROR_MESSAGE);
			txtPathAddress.setText(lastVisitedPath);
		} catch(NullPointerException e) {
			System.out.println("Err: Cannot visit directory: " + path); // TODO log error
		}
	}
	*/
	
	class ImageRenderer extends DefaultTableCellRenderer {
	  JLabel lbl = new JLabel();

	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	      boolean hasFocus, int row, int column) {
			// lbl.setText((String) value);
			lbl.setIcon((Icon)value);
			return lbl;
	  }
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
//	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupFileSelected = new javax.swing.JPopupMenu();
        menuOpen = new javax.swing.JMenuItem();
        menuOpenWith = new javax.swing.JMenuItem();
        menuPrint = new javax.swing.JMenuItem();
        menuOpenUsingSystem = new javax.swing.JMenuItem();
        menuOpenInNewTab = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuBookmark = new javax.swing.JMenuItem();
        menuCreateLink = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuCut = new javax.swing.JMenuItem();
        menuCopy = new javax.swing.JMenuItem();
        menuPaste = new javax.swing.JMenuItem();
        menuRename = new javax.swing.JMenuItem();
        menuDelete = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        menuProperties = new javax.swing.JMenuItem();
        popupBookmarkedItems = new javax.swing.JPopupMenu();
        menuBookmarkOpen = new javax.swing.JMenuItem();
        menuBookmarkOpenInNewTab = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        menuBookmarkRename = new javax.swing.JMenuItem();
        menuBookmarkRemove = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        menuBookmarkProperties = new javax.swing.JMenuItem();
        toolbarOptions = new javax.swing.JToolBar();
        btnGoBackInHistory = new javax.swing.JButton();
        btnGoForwardInHistory = new javax.swing.JButton();
        btnGoToParentDir = new javax.swing.JButton();
        btnReloadPath = new javax.swing.JButton();
        btnGoToHometDir = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        txtPathAddress = new javax.swing.JTextField();
        toggleButtonPathIsBookmarked = new javax.swing.JToggleButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        lblSearchIcon = new javax.swing.JLabel();
        txtSearchFiles = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        scrollPane_treeQuickAccess = new javax.swing.JScrollPane();
        /* Custom code */
        treeQuickAccess = new javax.swing.JTree();
        scrollPane_tableFileList = new javax.swing.JScrollPane();
        /* Custom code */
        tableFileList = new JTable(){
            //Implement table cell tool tips.
            @Override
            public String getToolTipText(MouseEvent e) {
                String tipText = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                //                int colIndex = columnAtPoint(p);

                try {
                    //          if(rowIndex != 0) // exclude heading
                    tipText = getFileToolTipText((FileAttributes)getValueAt(rowIndex, FILE_OBJECT_COLUMN_INDEX));
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }
                //        System.out.println("  // tipText=" + tipText);
                return tipText;
            }
        };

        menuOpen.setText("Open");
        menuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuOpen);

        menuOpenWith.setText("Open with...");
        popupFileSelected.add(menuOpenWith);

        menuPrint.setText("Print");
        popupFileSelected.add(menuPrint);

        menuOpenUsingSystem.setText("Open using system...");
        popupFileSelected.add(menuOpenUsingSystem);

        menuOpenInNewTab.setText("Open in new tab");
        popupFileSelected.add(menuOpenInNewTab);
        popupFileSelected.add(jSeparator1);

        menuBookmark.setText("Bookmark");
        popupFileSelected.add(menuBookmark);

        menuCreateLink.setText("Create link");
        popupFileSelected.add(menuCreateLink);
        popupFileSelected.add(jSeparator2);

        menuCut.setText("Cut");
        popupFileSelected.add(menuCut);

        menuCopy.setText("Copy");
        popupFileSelected.add(menuCopy);

        menuPaste.setText("Paste");
        popupFileSelected.add(menuPaste);

        menuRename.setText("Rename");
        popupFileSelected.add(menuRename);

        menuDelete.setText("Delete");
        popupFileSelected.add(menuDelete);
        popupFileSelected.add(jSeparator5);

        menuProperties.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        popupFileSelected.add(menuProperties);

        menuBookmarkOpen.setText("Open");
        popupBookmarkedItems.add(menuBookmarkOpen);

        menuBookmarkOpenInNewTab.setText("Open in new tab");
        popupBookmarkedItems.add(menuBookmarkOpenInNewTab);
        popupBookmarkedItems.add(jSeparator9);

        menuBookmarkRename.setText("Rename");
        popupBookmarkedItems.add(menuBookmarkRename);

        menuBookmarkRemove.setText("Remove");
        popupBookmarkedItems.add(menuBookmarkRemove);
        popupBookmarkedItems.add(jSeparator8);

        menuBookmarkProperties.setText("Properties");
        popupBookmarkedItems.add(menuBookmarkProperties);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        toolbarOptions.setFloatable(false);
        toolbarOptions.setRollover(true);

        btnGoBackInHistory.setText("back");
        btnGoBackInHistory.setToolTipText("Go back in history");
        btnGoBackInHistory.setFocusable(false);
        btnGoBackInHistory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGoBackInHistory.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGoBackInHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackInHistoryActionPerformed(evt);
            }
        });
        toolbarOptions.add(btnGoBackInHistory);

        btnGoForwardInHistory.setText("fwd");
        btnGoForwardInHistory.setToolTipText("Go forward in history");
        btnGoForwardInHistory.setFocusable(false);
        btnGoForwardInHistory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGoForwardInHistory.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGoForwardInHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoForwardInHistoryActionPerformed(evt);
            }
        });
        toolbarOptions.add(btnGoForwardInHistory);

        btnGoToParentDir.setText("up");
        btnGoToParentDir.setToolTipText("Go to parent folder");
        btnGoToParentDir.setFocusable(false);
        btnGoToParentDir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGoToParentDir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGoToParentDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoToParentDirActionPerformed(evt);
            }
        });
        toolbarOptions.add(btnGoToParentDir);

        btnReloadPath.setText("reload");
        btnReloadPath.setToolTipText("Reload current path contents");
        btnReloadPath.setFocusable(false);
        btnReloadPath.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReloadPath.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReloadPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadPathActionPerformed(evt);
            }
        });
        toolbarOptions.add(btnReloadPath);

        btnGoToHometDir.setText("home");
        btnGoToHometDir.setToolTipText("Go to parent folder");
        btnGoToHometDir.setFocusable(false);
        btnGoToHometDir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGoToHometDir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGoToHometDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoToHometDirActionPerformed(evt);
            }
        });
        toolbarOptions.add(btnGoToHometDir);
        toolbarOptions.add(jSeparator3);

        txtPathAddress.setToolTipText("Address bar");
        txtPathAddress.setPreferredSize(new java.awt.Dimension(100, 28));
        txtPathAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPathAddressActionPerformed(evt);
            }
        });
        toolbarOptions.add(txtPathAddress);

        toggleButtonPathIsBookmarked.setText("*");
        toggleButtonPathIsBookmarked.setFocusable(false);
        toggleButtonPathIsBookmarked.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleButtonPathIsBookmarked.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbarOptions.add(toggleButtonPathIsBookmarked);
        toolbarOptions.add(jSeparator4);

        lblSearchIcon.setText("search.icn");
        toolbarOptions.add(lblSearchIcon);

        txtSearchFiles.setToolTipText("Search for files and folders...");
        txtSearchFiles.setMaximumSize(new java.awt.Dimension(80, 28));
        txtSearchFiles.setMinimumSize(new java.awt.Dimension(80, 28));
        txtSearchFiles.setPreferredSize(new java.awt.Dimension(80, 28));
        toolbarOptions.add(txtSearchFiles);

        getContentPane().add(toolbarOptions, java.awt.BorderLayout.NORTH);

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setAutoscrolls(true);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(150, 25));
        jSplitPane1.setOneTouchExpandable(true);

        /*
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Quick access");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Drives");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Library");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Bookmarks");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Remote servers");
        treeNode1.add(treeNode2);
        treeQuickAccess.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        */

        /* Custom code */
        // Adding nodes to JTree
        treeNodeQuickAccess = new javax.swing.tree.DefaultMutableTreeNode("Quick access");

        treeNodeDrives = new javax.swing.tree.DefaultMutableTreeNode("Drives");
        treeNodeQuickAccess.add(treeNodeDrives);

        treeNodeLibrary = new javax.swing.tree.DefaultMutableTreeNode("Library");
        treeNodeQuickAccess.add(treeNodeLibrary);

        treeNodeBookmarks = new javax.swing.tree.DefaultMutableTreeNode("Bookmarks");
        treeNodeQuickAccess.add(treeNodeBookmarks);

        treeNodeRemoteServers = new javax.swing.tree.DefaultMutableTreeNode("Remote servers");
        treeNodeQuickAccess.add(treeNodeRemoteServers);

        treeQuickAccess.setModel(new javax.swing.tree.DefaultTreeModel(treeNodeQuickAccess));
        treeQuickAccess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                treeQuickAccessMouseReleased(evt);
            }
        });
        scrollPane_treeQuickAccess.setViewportView(treeQuickAccess);

        jSplitPane1.setLeftComponent(scrollPane_treeQuickAccess);

        tableFileList.setAutoCreateRowSorter(true);
        tableFileList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Name", "Size", "Type", "Modified", "Path"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableFileList.setRowHeight(22);
        tableFileList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tableFileList.setShowHorizontalLines(false);
        tableFileList.setShowVerticalLines(false);
        tableFileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableFileListMouseReleased(evt);
            }
        });
        scrollPane_tableFileList.setViewportView(tableFileList);
        if (tableFileList.getColumnModel().getColumnCount() > 0) {
            tableFileList.getColumnModel().getColumn(0).setMinWidth(20);
            tableFileList.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableFileList.getColumnModel().getColumn(0).setMaxWidth(20);
            tableFileList.getColumnModel().getColumn(1).setMinWidth(250);
            tableFileList.getColumnModel().getColumn(1).setPreferredWidth(250);
            tableFileList.getColumnModel().getColumn(1).setMaxWidth(250);
            tableFileList.getColumnModel().getColumn(2).setPreferredWidth(60);
            tableFileList.getColumnModel().getColumn(2).setMaxWidth(60);
            tableFileList.getColumnModel().getColumn(3).setPreferredWidth(80);
            tableFileList.getColumnModel().getColumn(3).setMaxWidth(80);
            tableFileList.getColumnModel().getColumn(4).setPreferredWidth(180);
            tableFileList.getColumnModel().getColumn(4).setMaxWidth(180);
        }

        jSplitPane1.setRightComponent(scrollPane_tableFileList);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
		
    private void btnGoToParentDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoToParentDirActionPerformed
		FileAttributes parent = null;
		try {
			parent = fileSystemHandler.getParent();
			System.out.println("  // up: " + (parent==null ? null : parent.absolutePath));
		} catch(FileNotFoundException e) {
			System.err.println("Err: (Logic) Cannot move parent directory: " + e); // log error
			JOptionPane.showMessageDialog(	this,
											"Cannot move to parent folder: " + e.getMessage(),
											"Navigation error",
											JOptionPane.ERROR_MESSAGE);
			return;
		}
 		loadPath(parent, true);
    }//GEN-LAST:event_btnGoToParentDirActionPerformed
	
    private void txtPathAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPathAddressActionPerformed
        loadPath(txtPathAddress.getText().trim(), true);
    }//GEN-LAST:event_txtPathAddressActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        
    }//GEN-LAST:event_formComponentResized
	
	private FileAttributes[] selectedFiles = null;
	
    private void tableFileListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableFileListMouseReleased
//        JTable source = (JTable)evt.getSource();
		
		int row = tableFileList.rowAtPoint(evt.getPoint());
		if (!tableFileList.isRowSelected(row))
			tableFileList.changeSelection(row, 0, false, false);
		
//		TableModel tableModel = source.getModel();
		System.out.printf("  // getSelectedRowCount()=%d, selected row indices=[", tableFileList.getSelectedRowCount());
		int[] selectedRows = tableFileList.getSelectedRows();
		for(int viewRow : selectedRows)
			System.out.printf("{v=%d,m=%d} ", viewRow, tableFileList.convertRowIndexToModel(viewRow));
		
		selectedFiles = new FileAttributes[selectedRows.length];
		for(int i=0; i<selectedRows.length; i++) {
			selectedFiles[i] = (FileAttributes)tableModel.getValueAt(tableFileList.convertRowIndexToModel(selectedRows[i]), FILE_OBJECT_COLUMN_INDEX);
		}
		System.out.println(", selectedFiles updated");

		if (evt.isPopupTrigger()) {
			setPopupFileSelectedOptions();
			popupFileSelected.show(evt.getComponent(), evt.getX(), evt.getY());
        } else if(evt.getClickCount() == 2) { // double clicked
			System.out.println("  // double clicked");
			openFile(selectedFiles[0]);
		}
    }//GEN-LAST:event_tableFileListMouseReleased

	private void openFile(final FileAttributes file) {
		if(file.isDirectory) 
			loadPath(file, true);
		// TODO else open file 
	}
	
	private FileAttributes filetoPaste = null;
	private enum PasteOperation { CUT, COPY, NONE }
	private PasteOperation pasteOperation = PasteOperation.NONE;
	
    private void treeQuickAccessMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeQuickAccessMouseReleased
		int row = treeQuickAccess.getRowForLocation(evt.getX(), evt.getY());
		if(row == -1) // a parent node is expanded, so no row selected
			return; 
		treeQuickAccess.setSelectionRow(row);
		
		TreePath selectedPath = treeQuickAccess.getPathForRow(row);
		
		if(selectedPath.getPathCount() > 2) { // only perform if folder entry is selected
			Object[] nodes = selectedPath.getPath();
//			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)nodes[nodes.length-1];
			String filePath = ((BookmarkedItem)((DefaultMutableTreeNode)nodes[nodes.length-1]).getUserObject()).absolutePath;
			System.out.println("  // selected: " + filePath);
			FileAttributes selectedFile = null;
			try {
				selectedFile = fileSystemHandler.getFileAttributes(filePath);
			} catch(FileNotFoundException e) {
				JOptionPane.showMessageDialog(	this,
												"Cannot open bookmarked item: " + filePath,
												"Bookmarked item error",
												JOptionPane.ERROR_MESSAGE);
				System.err.printf("Err: Cannot retrieve file: %s (%s)\n", filePath, e); // log error
				return;
			}
		
//			System.out.printf("  // selected item: class=%s, value=%s\n",
//				selectedNode.getUserObject().getClass().getName(), selectedNode.getUserObject());
			
			if(SwingUtilities.isRightMouseButton(evt)) { // do right-click action				
				popupBookmarkedItems.show(evt.getComponent(), evt.getX(), evt.getY());
				System.out.println("  // right clicked");
			} else {
				if(evt.getClickCount() == 2) { // do double-click action
					// do popupBookmarkedItems's Open action
					System.out.println("  // double clicked");
					openFile(selectedFile);				}
			}
		}
    }//GEN-LAST:event_treeQuickAccessMouseReleased

    private void menuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenActionPerformed
        
    }//GEN-LAST:event_menuOpenActionPerformed

    private void btnReloadPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadPathActionPerformed
        System.out.println("  // reload: " + fileSystemHandler.getCurrentWorkingDirectory().absolutePath);
		updateTableList();
    }//GEN-LAST:event_btnReloadPathActionPerformed

    private void btnGoBackInHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackInHistoryActionPerformed
        FileAttributes dir = fileSystemHandler.historyHandler.backward();
		System.out.println("  // back: " + dir.absolutePath);
		if(dir == null) {
			System.err.println("Err: (Logic) Cannot move back in history: Navigation limit reached"); // log error
			JOptionPane.showMessageDialog(	this,
											"Cannot move back in history: Navigation limit reached",
											"Navigation error",
											JOptionPane.ERROR_MESSAGE);
			return;
		}
		loadPath(dir, false);
    }//GEN-LAST:event_btnGoBackInHistoryActionPerformed

    private void btnGoToHometDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoToHometDirActionPerformed
        try {
			System.out.println("  // home: " + userHomeDirectoryPath);
			loadPath(fileSystemHandler.getUserHomeDirectory(), true);
		} catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(	this,
											"Cannot move to user home directory: " + e,
											"Navigation error",
											JOptionPane.ERROR_MESSAGE);
			System.err.println("Err: Cannot move to user home directory: " + e);
		}
    }//GEN-LAST:event_btnGoToHometDirActionPerformed

    private void btnGoForwardInHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoForwardInHistoryActionPerformed
        FileAttributes dir = fileSystemHandler.historyHandler.forward();
		System.out.println("  // fwd: " + dir.absolutePath);
		if(dir == null) {
			System.err.println("Err: (Logic) Cannot move forward in history: Navigation limit reached"); // log error
			JOptionPane.showMessageDialog(	this,
											"Cannot move forward in history: Navigation limit reached",
											"Navigation error",
											JOptionPane.ERROR_MESSAGE);
			return;
		}
		loadPath(dir, false);
    }//GEN-LAST:event_btnGoForwardInHistoryActionPerformed

	public static void setAppLookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		boolean lnfNameNotFound = true;
		for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
			if (SystemResources.LOOKnFEEL_NAMES.contains(info.getName())) {
				javax.swing.UIManager.setLookAndFeel(info.getClassName());
				lnfNameNotFound = false;
				break;
			}
		}
		if(lnfNameNotFound)
			System.err.println("Warning: Cannot set predefined L&F: " + SystemResources.LOOKnFEEL_NAMES); // log warning
	}
	
	public static void main(String args[]) throws Exception {
		/* Set the predefined look and feel */
		try {
			setAppLookAndFeel();
		} catch (Exception exc) {
			System.err.println("Error: Cannot set L&F: " + exc); // Log error
		}
        
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListViewTest frame = new ListViewTest();
					frame.setVisible(true);
//	                javax.swing.SwingUtilities.updateComponentTreeUI(frame.getContentPane());
				} catch(FileNotFoundException e) {
					e.printStackTrace(System.err);
				}
			}
		});
	}

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGoBackInHistory;
    private javax.swing.JButton btnGoForwardInHistory;
    private javax.swing.JButton btnGoToHometDir;
    private javax.swing.JButton btnGoToParentDir;
    private javax.swing.JButton btnReloadPath;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblSearchIcon;
    private javax.swing.JMenuItem menuBookmark;
    private javax.swing.JMenuItem menuBookmarkOpen;
    private javax.swing.JMenuItem menuBookmarkOpenInNewTab;
    private javax.swing.JMenuItem menuBookmarkProperties;
    private javax.swing.JMenuItem menuBookmarkRemove;
    private javax.swing.JMenuItem menuBookmarkRename;
    private javax.swing.JMenuItem menuCopy;
    private javax.swing.JMenuItem menuCreateLink;
    private javax.swing.JMenuItem menuCut;
    private javax.swing.JMenuItem menuDelete;
    private javax.swing.JMenuItem menuOpen;
    private javax.swing.JMenuItem menuOpenInNewTab;
    private javax.swing.JMenuItem menuOpenUsingSystem;
    private javax.swing.JMenuItem menuOpenWith;
    private javax.swing.JMenuItem menuPaste;
    private javax.swing.JMenuItem menuPrint;
    private javax.swing.JMenuItem menuProperties;
    private javax.swing.JMenuItem menuRename;
    private javax.swing.JPopupMenu popupBookmarkedItems;
    private javax.swing.JPopupMenu popupFileSelected;
    private javax.swing.JScrollPane scrollPane_tableFileList;
    private javax.swing.JScrollPane scrollPane_treeQuickAccess;
    private javax.swing.JTable tableFileList;
    private javax.swing.JToggleButton toggleButtonPathIsBookmarked;
    private javax.swing.JToolBar toolbarOptions;
    private javax.swing.JTree treeQuickAccess;
    private javax.swing.JTextField txtPathAddress;
    private javax.swing.JTextField txtSearchFiles;
    // End of variables declaration//GEN-END:variables

	private void setTableRows(final FileAttributes[] files) {
		for(FileAttributes file : files) {
			tableModel.addRow(new Object[] {
				iconRegistry.getFileIcon(file, IconSize.SMALL), // file icon
				file, // file name, stored as FileAttributes object for later retrieval
				file.size, // file size
				file.type, // file type
				file.lastModifiedDateString, // modified date & time
				file.absolutePath // file path
			});
		}
	}
	
	private String getFileToolTipText(final FileAttributes file) {
		return String.format(
			"<html><style>table{border-collapse: collapse;} td{padding: 0px;} .attr{color: green;} .value{color: blue;}</style> <table><tr><td class='attr'>Name:</td><td class='value'>%s</td></tr><tr><td class='attr'>Type:</td><td class='value'>%s</td></tr><tr><td class='attr'>Size:</td><td class='value'>%s</td></tr><tr><td class='attr'>Modified:</td><td class='value'>%s</td></tr></table></html>",
			file.name, file.type, file.sizeInWords, file.lastModifiedDateString);
	}

}
