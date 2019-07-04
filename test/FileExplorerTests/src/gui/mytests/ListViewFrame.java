package gui.mytests;

import gui.mytests.handlers.*;
import gui.mytests.handlers.fs.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.nio.file.InvalidPathException;
import gui.mytests.*;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static gui.mytests.SystemResources.IconRegistry;
import static gui.mytests.SystemResources.IconRegistry.IconSize;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;


public class ListViewFrame extends javax.swing.JFrame {
	private BookmarkHandler bookmarkHandler = null;
    private DefaultMutableTreeNode treeNodeQuickAccess;
    private DefaultMutableTreeNode treeNodeDrives;
    private DefaultMutableTreeNode treeNodeLibrary;
    private DefaultMutableTreeNode treeNodeBookmarks;
    private DefaultMutableTreeNode treeNodeRemoteServers;
	private DefaultTreeModel treeModelQuickAccess = null;
	private FileSystemHandler fileSystemHandler = null;
	private IconRegistry iconRegistry = null;
	private DefaultTableModel tableModel = null;
	private String lastVisitedPath = null;	
	
	private final String userHomeDirectoryPath;
	private final String systemTempDirPath;
	private final int FILE_OBJECT_COLUMN_INDEX = 1;
	private final int ICON_COLUMN_INDEX = 0;
	
	/* frame instances */
	private FilePropertiesForm propertiesForm = null;
	private static ListViewFrame listViewFrame = null;

	
	private void setFileSelectionContextOptions() {
		// reset all options
		menuOpen.setVisible(true);
		menuOpenWith.setVisible(true);
        menuPrint.setVisible(true);
		menuOpenUsingSystem.setVisible(true);
		menuOpenInNewTab.setVisible(true);        
		menuBookmark.setVisible(true);
		jSeparator1.setVisible(true);
		menuNew.setVisible(true);
		menuNewFile.setVisible(true);
		menuNewFolder.setVisible(true);
		menuCreateShortcut.setVisible(true);
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
			if(selectedFiles[0].equals(fileSystemHandler.getCurrentWorkingDirectory())) {
				menuOpen.setVisible(false);
				menuOpenWith.setVisible(false);
				menuPrint.setVisible(false);
				jSeparator2.setVisible(false);
				menuCut.setVisible(false);
				menuCopy.setVisible(false);
				menuRename.setVisible(false);
				menuDelete.setVisible(false);
			} else {
				if(selectedFiles[0].isDirectory) {
					menuOpenUsingSystem.setEnabled(fileSystemHandler.isDesktopSupported);

					menuOpenWith.setVisible(false);
					menuPrint.setVisible(false);
				} else if(selectedFiles[0].isLink) {
					menuOpen.setEnabled(fileSystemHandler.isDesktopSupported);
					menuOpenWith.setEnabled(fileSystemHandler.isDesktopSupported);				
					menuOpenInNewTab.setVisible(false);
					menuOpenUsingSystem.setVisible(false);
					menuPrint.setVisible(false);
				} else { // for files
					menuOpen.setEnabled(fileSystemHandler.isDesktopSupported);
					menuOpenWith.setEnabled(fileSystemHandler.isDesktopSupported);
					menuPrint.setEnabled(fileSystemHandler.isDesktopSupported);

					menuOpenInNewTab.setVisible(false);
					menuOpenUsingSystem.setVisible(false);
				}

				if(!selectedFiles[0].isLocal) {
					menuOpenUsingSystem.setVisible(false);
				}
			}
		} else {
			menuNew.setVisible(false); // no need to modify the visibility of the children menu items
            menuOpen.setVisible(false);
			menuOpenWith.setVisible(false);
			menuPrint.setVisible(false);
            menuOpenUsingSystem.setVisible(false);
			menuOpenInNewTab.setVisible(false);
//			jSeparator1.setVisible(false);
			menuRename.setVisible(false);
		}
	}
	
	/*
	private final class ShowPropertiesAction extends AbstractAction {
		ShowPropertiesAction() {
            super("Properties", IconRegistry.propertiesIcon_small);
            putValue(SHORT_DESCRIPTION, "Show properties");
            putValue(MNEMONIC_KEY, KeyEvent.VK_ENTER);
        }
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			System.out.println("  // cmd=" + e.getActionCommand());
			FilePropertiesForm.init(	selectedFiles, 
										iconRegistry.getFileIcon(selectedFiles[0], IconSize.BIG), 
										fileSystemHandler);
		}
	}
	*/
	
//	private final ShowPropertiesAction showPropertiesAction = new ShowPropertiesAction();
	/*
	private final AbstractAction showPropertiesAction = new AbstractAction() {
		@Override 
		public void actionPerformed(ActionEvent e) {
			System.out.println("  // cmd=" + e.getActionCommand());
			FilePropertiesForm.init(	selectedFiles, 
										iconRegistry.getFileIcon(selectedFiles[0], IconSize.BIG), 
										fileSystemHandler);
		}
	};
	*/
	
	/**
	 * Creates new form ListViewTest
	 */
	public ListViewFrame() throws FileNotFoundException {
		System.out.println("Info: Initializing form UI components..."); // TODO log info
		initComponents();
		tableFileList.setShowHorizontalLines(false);
		tableFileList.setShowVerticalLines(false);
		toolbarOptions.setLayout(new BoxLayout(toolbarOptions, BoxLayout.X_AXIS));
		
		userHomeDirectoryPath = System.getProperty("user.home");
		systemTempDirPath = System.getProperty("java.io.tmpdir");
		
		System.out.println("Info: Initializing FileSystemHandler..."); // TODO log info
		fileSystemHandler = FileSystemHandler.getHandler(userHomeDirectoryPath);
		
		if(!fileSystemHandler.isDesktopSupported) {
			String platform = String.format("Name=%s, architecture=%s, version=%s", 
													System.getProperty("os.name"), 
													System.getProperty("os.arch"), 
													System.getProperty("os.version"));
			JOptionPane.showMessageDialog(	this,
											"Desktop feature is not supported in this platform.\n" +
												"Some options will be disabled!\n" +
												"Platform info: " + platform,
											"Feature not supported",
											JOptionPane.WARNING_MESSAGE);
			System.err.println("Err: Desktop not supported in this platform: Platform: " + platform);
		}
		
		iconRegistry = IconRegistry.getInstance();
		
		System.out.println("Info: Setting bookmarks tree..."); // TODO log info
		setBookmarks();
		
		System.out.println("Info: Setting file list table..."); // TODO log info
		setTable();
		
		/*
		// Setting Actions
		System.out.println("Info: Attaching Actions...");
		menuProperties.setAction(showPropertiesAction);
		menuBookmarkProperties.setAction(showPropertiesAction);
		*/
	}
	
	private void setBookmarks() {
		bookmarkHandler = BookmarkHandler.getInstance(treeQuickAccess, treeModelQuickAccess);
		try {
			for(FileAttributes file : fileSystemHandler.listRoots()) {
				bookmarkHandler.add(	treeNodeDrives, 
										new BookmarkedItem(	file.absolutePath, 
															BookmarkedItem.TYPE_SYSTEM_DRIVE, 
//															IconRegistry.driveIcon_big,
															file.absolutePath));
			}
		} catch(FileNotFoundException e) {
			System.err.println("Err: Cannot add system roots to bookmark tree node: " + e); // TODO Log in logger 
			e.printStackTrace(System.err);
		}
		
		bookmarkHandler	.add(	treeNodeLibrary,
								new BookmarkedItem("Home", BookmarkedItem.TYPE_LIBRARY_FOLDER, userHomeDirectoryPath))
						.add(	treeNodeLibrary,
								new BookmarkedItem("Temp", BookmarkedItem.TYPE_LIBRARY_FOLDER, systemTempDirPath));
		
		// TODO load all bookmarks and remote server hostnames from disk file
		
		bookmarkHandler.expandAllNodes();
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
		
		updateTableList(true);
		
		// initially no history
		btnGoBackInHistory.setEnabled(false); 
		btnGoForwardInHistory.setEnabled(false);
	}
	
	/* calls updateTableList() & navigateTo() */ 
	private void loadPath(final String dirPath, final boolean registerInHistory) {
		try {
			loadPath(fileSystemHandler.getFileAttributes(dirPath), registerInHistory);
		} catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(	this,
											"Folder path does not exist: " + dirPath,
											"Invalid folder path",
											JOptionPane.ERROR_MESSAGE);
			txtPathAddress.setText(fileSystemHandler.getCurrentWorkingDirectory().absolutePath);
			System.err.printf("Err: Cannot load directory: Path does not exist: " + dirPath + " ("+e+")"); // log error
		}
	}

	private void loadPath(final FileAttributes dir, final boolean registerInHistory) {
		System.out.println("Info: Loading path " + dir.absolutePath + "...");
		FileAttributes cwd = fileSystemHandler.getCurrentWorkingDirectory();
		if(!dir.equals(cwd)) { // eliminate redundant loading for same path
			navigateTo(dir, registerInHistory);
		}
		updateTableList(true);
	} 

	private FileAttributes[] fileList = null;
	
	/** Updates with currentWorkingDirectory */
	private void updateTableList(final boolean loadFresh) {
		try {
			FileAttributes cwd = fileSystemHandler.getCurrentWorkingDirectory();
			System.out.printf("Info: %s table list for path='%s'\n", 
					loadFresh ? "Updating" : "Restoring", cwd.absolutePath); // TODO log info & show in status
			
			if(loadFresh)
				fileList = setDirectoryFirstArrangement(fileSystemHandler.listFiles(cwd));
			
			tableFileList.getRowSorter().setSortKeys(null);
			setTableRows(fileList);
			txtPathAddress.setText(cwd.absolutePath);
			
			// update navigation button states
			btnGoBackInHistory.setEnabled(fileSystemHandler.historyHandler.canGoBackward());
			btnGoForwardInHistory.setEnabled(fileSystemHandler.historyHandler.canGoForward());
			btnGoToParentDir.setEnabled(fileSystemHandler.canGoToParent());
			btnBookmarkIndicator.setSelected(bookmarkHandler.containsFile(treeNodeBookmarks, cwd));

			System.out.println("Info: Folder listing " + (loadFresh ? "loaded" : "restored")); // TODO show in status bar			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(	this,
											String.format("Cannot update table list!\n  Path: %s\n  Reason: %s",
													fileSystemHandler.getCurrentWorkingDirectory().absolutePath, e),
											"Directory listing error",
											JOptionPane.ERROR_MESSAGE);
			System.err.println("Err: Cannot update table list: " +  e); // TODO log error
			
			System.out.println("  // reverting...");
			System.out.println("  // before: cwd=" + fileSystemHandler.getCurrentWorkingDirectory() + 
					", history=" + fileSystemHandler.historyHandler);			
			
			fileSystemHandler.revertLastNavigation();
			
			System.out.println("  // after: cwd=" + fileSystemHandler.getCurrentWorkingDirectory() + 
					", history=" + fileSystemHandler.historyHandler);
//			setTableRows(lastFileListing); // populating with last successful file listing

			updateTableList(false); // load from cache
			
			System.out.println("  // done");
//			System.out.println("Info: Reverted to last folder listing"); // TODO log info
		}
	}

	/* Only change the currentWorkingDirectory */
	private void navigateTo(final FileAttributes dir, final boolean registerInHistory) {
		System.out.println("Info: Navigating to " + dir.absolutePath);  // TODO log info
		try {
			fileSystemHandler.navigateTo(dir, registerInHistory);
			
//			lastVisitedPath = dir.absolutePath;
			System.out.println("Info: Navigated to folder: " + dir.absolutePath); // log Info
		} catch(FileNotFoundException|InvalidPathException e) {
			JOptionPane.showMessageDialog(	this,
											String.format("Cannot navigate to path!\n  Path: %s\n  Reason: %s", 
													dir.absolutePath, e),
											"Navigation error",
											JOptionPane.ERROR_MESSAGE);
			System.err.printf("Err: Cannot navigate to path %s (%s)\n", dir.absolutePath, e); // log error
		}
	}

	private FileAttributes[] setDirectoryFirstArrangement(final FileAttributes[] oldList) {
		FileAttributes[] newList = new FileAttributes[oldList.length];
		int j=0;
		for(int i=0; i<oldList.length; i++) { // put dirs first
			if(oldList[i].isDirectory)
				newList[j++] = oldList[i];
		}
		
		for(int i=0; i<oldList.length; i++) { // put files next
			if(!oldList[i].isDirectory)
				newList[j++] = oldList[i];
		}
		
		return newList;
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
        menuBookmark = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuNew = new javax.swing.JMenu();
        menuNewFile = new javax.swing.JMenuItem();
        menuNewFolder = new javax.swing.JMenuItem();
        menuCreateShortcut = new javax.swing.JMenuItem();
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
        menuBookmarkOpenLocation = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        menuBookmarkRename = new javax.swing.JMenuItem();
        menuBookmarkRemove = new javax.swing.JMenuItem();
        menuBookmarkRemoveAll = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        menuBookmarkProperties = new javax.swing.JMenuItem();
        toolbarOptions = new javax.swing.JToolBar();
        btnGoBackInHistory = new javax.swing.JButton();
        btnGoForwardInHistory = new javax.swing.JButton();
        btnGoToParentDir = new javax.swing.JButton();
        btnReloadPath = new javax.swing.JButton();
        btnGoToHometDir = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        lblAddressIcon = new javax.swing.JLabel();
        txtPathAddress = new javax.swing.JTextField();
        btnBookmarkIndicator = new javax.swing.JToggleButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        lblSearchIcon = new javax.swing.JLabel();
        txtSearchFiles = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        scrollpaneTreeQuickAccess = new javax.swing.JScrollPane();
        /* Custom code */
        treeQuickAccess = new javax.swing.JTree();
        scrollpaneTableFileList = new javax.swing.JScrollPane();
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

            @Override
            public boolean getScrollableTracksViewportHeight() {
                return getPreferredSize().height < getParent().getHeight();
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
        menuPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrintActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuPrint);

        menuOpenUsingSystem.setText("Open using system...");
        menuOpenUsingSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenUsingSystemActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuOpenUsingSystem);

        menuOpenInNewTab.setText("Open in new tab");
        popupFileSelected.add(menuOpenInNewTab);

        menuBookmark.setText("Bookmark");
        menuBookmark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuBookmark);
        popupFileSelected.add(jSeparator1);

        menuNew.setText("New");

        menuNewFile.setText("File");
        menuNewFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewFileActionPerformed(evt);
            }
        });
        menuNew.add(menuNewFile);

        menuNewFolder.setText("Folder");
        menuNewFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewFolderActionPerformed(evt);
            }
        });
        menuNew.add(menuNewFolder);

        popupFileSelected.add(menuNew);

        menuCreateShortcut.setText("Create shortcut");
        menuCreateShortcut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCreateShortcutActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuCreateShortcut);
        popupFileSelected.add(jSeparator2);

        menuCut.setText("Cut");
        menuCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCutActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuCut);

        menuCopy.setText("Copy");
        menuCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCopyActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuCopy);

        menuPaste.setText("Paste");
        popupFileSelected.add(menuPaste);

        menuRename.setText("Rename");
        menuRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRenameActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuRename);

        menuDelete.setText("Delete");
        menuDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDeleteActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuDelete);
        popupFileSelected.add(jSeparator5);

        menuProperties.setText("Properties");
        menuProperties.setActionCommand("FileListProperties");
        menuProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPropertiesActionPerformed(evt);
            }
        });
        popupFileSelected.add(menuProperties);

        menuBookmarkOpen.setText("Open");
        menuBookmarkOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkOpenActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkOpen);

        menuBookmarkOpenInNewTab.setText("Open in new tab");
        menuBookmarkOpenInNewTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkOpenInNewTabActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkOpenInNewTab);

        menuBookmarkOpenLocation.setText("Open location");
        menuBookmarkOpenLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkOpenLocationActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkOpenLocation);
        popupBookmarkedItems.add(jSeparator9);

        menuBookmarkRename.setText("Rename");
        menuBookmarkRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkRenameActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkRename);

        menuBookmarkRemove.setText("Remove");
        menuBookmarkRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkRemoveActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkRemove);

        menuBookmarkRemoveAll.setText("Remove all");
        menuBookmarkRemoveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkRemoveAllActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkRemoveAll);
        popupBookmarkedItems.add(jSeparator8);

        menuBookmarkProperties.setText("Properties");
        menuBookmarkProperties.setActionCommand("BookmarkedItemProperties");
        menuBookmarkProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkPropertiesActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkProperties);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        toolbarOptions.setFloatable(false);
        toolbarOptions.setRollover(true);

        btnGoBackInHistory.setIcon(new javax.swing.ImageIcon("D:\\Projects\\Java\\NetBeans\\Projects\\project\\Summer-Project\\finalized\\FileExplorer\\resources\\images\\back_small.png")); // NOI18N
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

        btnGoForwardInHistory.setIcon(new javax.swing.ImageIcon("D:\\Projects\\Java\\NetBeans\\Projects\\project\\Summer-Project\\finalized\\FileExplorer\\resources\\images\\fwd_small.png")); // NOI18N
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

        btnGoToParentDir.setIcon(new javax.swing.ImageIcon("D:\\Projects\\Java\\NetBeans\\Projects\\project\\Summer-Project\\finalized\\FileExplorer\\resources\\images\\up_small.png")); // NOI18N
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

        btnReloadPath.setIcon(new javax.swing.ImageIcon("D:\\Projects\\Java\\NetBeans\\Projects\\project\\Summer-Project\\finalized\\FileExplorer\\resources\\images\\reload_small.png")); // NOI18N
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

        btnGoToHometDir.setIcon(new javax.swing.ImageIcon("D:\\Projects\\Java\\NetBeans\\Projects\\project\\Summer-Project\\finalized\\FileExplorer\\resources\\images\\home_small.png")); // NOI18N
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

        lblAddressIcon.setIcon(new javax.swing.ImageIcon("D:\\Projects\\Java\\NetBeans\\Projects\\project\\Summer-Project\\finalized\\FileExplorer\\resources\\images\\address_small.png")); // NOI18N
        lblAddressIcon.setToolTipText("");
        toolbarOptions.add(lblAddressIcon);

        txtPathAddress.setToolTipText("Address bar");
        txtPathAddress.setMinimumSize(new java.awt.Dimension(6, 28));
        txtPathAddress.setPreferredSize(new java.awt.Dimension(100, 28));
        txtPathAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPathAddressActionPerformed(evt);
            }
        });
        toolbarOptions.add(txtPathAddress);

        btnBookmarkIndicator.setIcon(new javax.swing.ImageIcon("D:\\Projects\\Java\\NetBeans\\Projects\\project\\Summer-Project\\finalized\\FileExplorer\\resources\\images\\bookmark - off.png")); // NOI18N
        btnBookmarkIndicator.setToolTipText("Bookmark current folder");
        btnBookmarkIndicator.setFocusable(false);
        btnBookmarkIndicator.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBookmarkIndicator.setMaximumSize(new java.awt.Dimension(30, 30));
        btnBookmarkIndicator.setMinimumSize(new java.awt.Dimension(30, 30));
        btnBookmarkIndicator.setPreferredSize(new java.awt.Dimension(30, 30));
        btnBookmarkIndicator.setSelectedIcon(new javax.swing.ImageIcon("D:\\Projects\\Java\\NetBeans\\Projects\\project\\Summer-Project\\finalized\\FileExplorer\\resources\\images\\bookmark - on.png")); // NOI18N
        btnBookmarkIndicator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBookmarkIndicatorActionPerformed(evt);
            }
        });
        toolbarOptions.add(btnBookmarkIndicator);
        toolbarOptions.add(jSeparator4);

        lblSearchIcon.setIcon(new javax.swing.ImageIcon("D:\\Projects\\Java\\NetBeans\\Projects\\project\\Summer-Project\\finalized\\FileExplorer\\resources\\images\\search_small.png")); // NOI18N
        lblSearchIcon.setToolTipText("");
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

        treeModelQuickAccess = new DefaultTreeModel(treeNodeQuickAccess);
        treeQuickAccess.setModel(treeModelQuickAccess);
        treeQuickAccess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                treeQuickAccessMouseReleased(evt);
            }
        });
        scrollpaneTreeQuickAccess.setViewportView(treeQuickAccess);

        jSplitPane1.setLeftComponent(scrollpaneTreeQuickAccess);

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
        tableFileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableFileListMouseReleased(evt);
            }
        });
        scrollpaneTableFileList.setViewportView(tableFileList);
        if (tableFileList.getColumnModel().getColumnCount() > 0) {
            tableFileList.getColumnModel().getColumn(0).setMinWidth(22);
            tableFileList.getColumnModel().getColumn(0).setPreferredWidth(22);
            tableFileList.getColumnModel().getColumn(0).setMaxWidth(22);
            tableFileList.getColumnModel().getColumn(1).setMinWidth(250);
            tableFileList.getColumnModel().getColumn(1).setPreferredWidth(250);
            tableFileList.getColumnModel().getColumn(1).setMaxWidth(250);
            tableFileList.getColumnModel().getColumn(2).setPreferredWidth(80);
            tableFileList.getColumnModel().getColumn(2).setMaxWidth(80);
            tableFileList.getColumnModel().getColumn(3).setPreferredWidth(80);
            tableFileList.getColumnModel().getColumn(3).setMaxWidth(80);
            tableFileList.getColumnModel().getColumn(4).setPreferredWidth(180);
            tableFileList.getColumnModel().getColumn(4).setMaxWidth(180);
        }

        jSplitPane1.setRightComponent(scrollpaneTableFileList);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
		
    private void btnGoToParentDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoToParentDirActionPerformed
		FileAttributes parent = null;
		try {
			parent = fileSystemHandler.getCurrentParent();
//			System.out.println("  // up: " + (parent==null ? null : parent.absolutePath));
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
	
	private void openFile(final FileAttributes file) {
		try {
			if(file.isDirectory) {
				loadPath(file, true);
//				System.out.println("Info: Directory loaded: " + file.absolutePath);
			} else {
				fileSystemHandler.openFile(file); // opens file using system
				System.out.println("Info: Opened file: " + file.absolutePath); // log Info
			}
		} catch(Exception e) {
			String msg = "Unable to open "+ (file.isDirectory ? "folder" : "file") +": " + file.absolutePath + "\nReason: " + e;
			JOptionPane.showMessageDialog(	this,
											msg,
											"File operation failure",
											JOptionPane.ERROR_MESSAGE);
			System.err.printf("Err: Unable to open %s: %s, Reason: %s", 
					file.isDirectory ? "directory" : "file", file.absolutePath, e); // log error
			e.printStackTrace();
		}
	}
	
	private FileAttributes filetoPaste = null;
	private enum PasteOperation { CUT, COPY, NONE }
	private PasteOperation pasteOperation = PasteOperation.NONE;
	private DefaultMutableTreeNode selectedBookmarkNode = null;
	
	private void setBookmarkContextOptions() {
		// reset variable options first
		menuBookmarkOpenLocation.setVisible(true);
		menuBookmarkRemove.setVisible(true);
		menuBookmarkRemoveAll.setVisible(true);
		menuBookmarkRename.setVisible(true);
		jSeparator9.setVisible(true);
			
		TreeNode parent = selectedBookmarkNode.getParent();
		if(parent == treeNodeDrives) {
			menuBookmarkOpenLocation.setVisible(false);
			menuBookmarkRemove.setVisible(false);
			menuBookmarkRemoveAll.setVisible(false);
			jSeparator9.setVisible(false);
			menuBookmarkRename.setVisible(false);
		} else if(parent == treeNodeLibrary) {
			menuBookmarkRemove.setVisible(false);
			menuBookmarkRemoveAll.setVisible(false);
			jSeparator9.setVisible(false);
			menuBookmarkRename.setVisible(false);
		} else if(parent == treeNodeRemoteServers) {
			menuBookmarkOpenLocation.setVisible(false);
		}
	}
	
    private void menuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenActionPerformed
		openFile(selectedFiles[0]);
    }//GEN-LAST:event_menuOpenActionPerformed

    private void btnReloadPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadPathActionPerformed
        try {
			System.out.println("  // reload: " + fileSystemHandler.getCurrentWorkingDirectory().absolutePath);
			updateTableList(true);
		} catch(Exception e) {
			String msg = "Unable to reload current folder: " + fileSystemHandler.getCurrentWorkingDirectory().absolutePath + "\nReason: " + e;
			JOptionPane.showMessageDialog(	this,
											msg,
											"File operation failure",
											JOptionPane.ERROR_MESSAGE);
			System.err.println("Err: " + msg); // log error
		}
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

    private void menuBookmarkPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkPropertiesActionPerformed
//		BookmarkedItem item = (BookmarkedItem)selectedBookmarkNode.getUserObject();
//		System.out.println("  // item=" + item + ", selectedFiles[0]=" + selectedFiles[0]);
		FilePropertiesForm.init(	selectedFiles, 
									((BookmarkedItem)selectedBookmarkNode.getUserObject()).type,
									fileSystemHandler);
    }//GEN-LAST:event_menuBookmarkPropertiesActionPerformed

    private void menuPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPropertiesActionPerformed
        FilePropertiesForm.init(	selectedFiles,
									selectedFiles[0].type,
									fileSystemHandler);
    }//GEN-LAST:event_menuPropertiesActionPerformed

    private void menuOpenUsingSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenUsingSystemActionPerformed
        try {
			fileSystemHandler.openFile(selectedFiles[0]);
			System.out.println("Info: Opened directory using system: "+ selectedFiles[0].absolutePath);
		} catch(IOException|UnsupportedOperationException e) {
			JOptionPane.showMessageDialog(	this,
											"Cannot open folder: " + selectedFiles[0].absolutePath + "\nReason: " + e,
											"Desktop operation failure",
											JOptionPane.ERROR_MESSAGE);

			System.err.printf("Err: Cannot open directory: %s. Reason: %s\n", 
					selectedFiles[0].absolutePath, e); // log error
		}
    }//GEN-LAST:event_menuOpenUsingSystemActionPerformed

    private void menuPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrintActionPerformed
        try {
			fileSystemHandler.printFile(selectedFiles[0]);
			System.out.println("Info: Printed file: " + selectedFiles[0].absolutePath); // log info
		} catch(IllegalArgumentException|IOException e) {
			JOptionPane.showMessageDialog(	this,
											"Cannot print file: " + selectedFiles[0].absolutePath + "\nReason: " + e,
											"File operation failure",
											JOptionPane.ERROR_MESSAGE);

			System.err.println("Err: Cannot print file: " + selectedFiles[0].absolutePath + "\nReason: " + e); // log error
		}
    }//GEN-LAST:event_menuPrintActionPerformed

    private void menuBookmarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkActionPerformed
		for(FileAttributes file : selectedFiles) {
			bookmarkHandler.add(treeNodeBookmarks,
								new BookmarkedItem(	file.name, 
													file.type, 
//													iconRegistry.getFileIcon(file, IconSize.BIG), 
													file.absolutePath));
		}
		if(selectedFiles[0].equals(fileSystemHandler.getCurrentWorkingDirectory()))
			btnBookmarkIndicator.setSelected(true);
    }//GEN-LAST:event_menuBookmarkActionPerformed

    private void menuBookmarkOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkOpenActionPerformed
        openFile(selectedFiles[0]);
    }//GEN-LAST:event_menuBookmarkOpenActionPerformed

    private void menuBookmarkRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkRenameActionPerformed
		String newName = (String)JOptionPane.showInputDialog(	this, 
																"Enter new name for bookmark:",
																"Rename bookmark",
																JOptionPane.QUESTION_MESSAGE,
																null, null, ((BookmarkedItem)selectedBookmarkNode.getUserObject()).name);
		if(newName != null)
			bookmarkHandler.rename(selectedBookmarkNode, newName);
    }//GEN-LAST:event_menuBookmarkRenameActionPerformed

    private void menuBookmarkRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkRemoveActionPerformed
        if(btnBookmarkIndicator.isSelected()) {
			if(((BookmarkedItem)selectedBookmarkNode.getUserObject()).absolutePath
					.equals(fileSystemHandler.getCurrentWorkingDirectory().absolutePath))
				btnBookmarkIndicator.setSelected(false);
		}
		bookmarkHandler.remove(selectedBookmarkNode);
    }//GEN-LAST:event_menuBookmarkRemoveActionPerformed

    private void menuBookmarkRemoveAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkRemoveAllActionPerformed
        if(JOptionPane.showConfirmDialog(	this, 
											"Sure to remove all these "+ selectedBookmarkNode.getParent().getChildCount() +" bookmarks?", 
											"Delete confirmation", 
											JOptionPane.YES_NO_OPTION,
											JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			bookmarkHandler.removeAllSiblings(selectedBookmarkNode);
			btnBookmarkIndicator.setSelected(false);
		}
    }//GEN-LAST:event_menuBookmarkRemoveAllActionPerformed

	private void findAndSelectInList(final FileAttributes file) {
		for(int row=0; row<tableModel.getRowCount(); row++) {
			if(file.equals(tableModel.getValueAt(row, FILE_OBJECT_COLUMN_INDEX))) {
//				System.out.println("  // found at row=" + row);
				tableFileList.changeSelection(row, 0, false, false);
				return;
			}	
		}
		JOptionPane.showMessageDialog(	this,
										"Cannot locate " + (file.isDirectory ? "folder" : "file") + " in its previous location!",
										"Cannot locate file",
										JOptionPane.ERROR_MESSAGE);
		System.err.println("Err: Cannot locate " + (file.isDirectory ? "folder" : "file") + " in its previous location!");
	}
	
    private void menuBookmarkOpenLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkOpenLocationActionPerformed
        try {
			openFile(fileSystemHandler.getParent(selectedFiles[0]));
			findAndSelectInList(selectedFiles[0]);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(	this,
											"Cannot retieve parent folder of " + selectedFiles[0].absolutePath + "\nReason: " + e,
											"File operation failure",
											JOptionPane.WARNING_MESSAGE);
			System.err.println("Err: Cannot retieve parent folder of " + selectedFiles[0].absolutePath + "\nReason: " + e);
		}
    }//GEN-LAST:event_menuBookmarkOpenLocationActionPerformed

	private void bookmarkCurrentWorkingDirectory() {
		FileAttributes cwd = fileSystemHandler.getCurrentWorkingDirectory();
		bookmarkHandler.add(treeNodeBookmarks,
							new BookmarkedItem(	cwd.name, 
												cwd.type,
//												BookmarkedItem.ItemType.PATH, 
//												iconRegistry.getFileIcon(cwd, IconSize.BIG), 
												cwd.absolutePath));
		if(!btnBookmarkIndicator.isSelected())
			btnBookmarkIndicator.setSelected(true);
	}
	
    private void btnBookmarkIndicatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookmarkIndicatorActionPerformed
		if(btnBookmarkIndicator.isSelected()) { // bookmark cwd the show in the bookmark pane			
			bookmarkCurrentWorkingDirectory();
		} else { // remove cwd from bookmark pane
			if(!bookmarkHandler.remove(fileSystemHandler.getCurrentWorkingDirectory(), treeNodeBookmarks)) {
				JOptionPane.showMessageDialog(	this,
												"Cannot find the current folder in bookmark pane!",
												"Bookmark not found",
												JOptionPane.ERROR_MESSAGE);
				System.err.printf("Err: Cannot remove the current working directory (%s) from bookmark pane: Bookmark not found!",
						fileSystemHandler.getCurrentWorkingDirectory().absolutePath); // log Error
			}
		}
    }//GEN-LAST:event_btnBookmarkIndicatorActionPerformed

    private void menuBookmarkOpenInNewTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkOpenInNewTabActionPerformed
        System.out.println("  // history=" + fileSystemHandler.historyHandler);
    }//GEN-LAST:event_menuBookmarkOpenInNewTabActionPerformed

    private void menuNewFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewFileActionPerformed
		createNew(false);
    }//GEN-LAST:event_menuNewFileActionPerformed

	private void createNew(final boolean isDirectory) {
		String name = (String)JOptionPane.showInputDialog(	this, 
															"New "+ (isDirectory?"folder":"file") +" name:",
															"Create new " + (isDirectory?"folder":"file"),
															JOptionPane.QUESTION_MESSAGE,
															null, null, null);
		if(name != null) {
			name = name.trim();
			try {
				fileSystemHandler.createNew(name, isDirectory);
				updateTableList(true);
			} catch(FileAlreadyExistsException e) {
				JOptionPane.showMessageDialog(	this,
												"Cannot create "+ (isDirectory?"folder":"file") + "!\nReason: Name already exists.",
												"File operation failure",
												JOptionPane.ERROR_MESSAGE);
				System.err.printf("Err: %s creation faied. Reason: Name '%s' already exists\n", 
						(isDirectory?"Directory":"File"), name); // log Error
			} catch(IOException e) {
				JOptionPane.showMessageDialog(	this,
												"Cannot create "+ (isDirectory?"folder":"file") + "!\nReason: " + e.getMessage(),
//														"!\nReason: Specified name not supported by current platform.",
												"File operation failure",
												JOptionPane.ERROR_MESSAGE);
				System.err.printf("Err: %s creation faied. Reason: %s\n", 
						(isDirectory?"Directory":"File"), e.getMessage()); // log Error
			}
		}
	}
	
    private void menuNewFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewFolderActionPerformed
        createNew(true);
    }//GEN-LAST:event_menuNewFolderActionPerformed

    private void menuCreateShortcutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCreateShortcutActionPerformed
        System.out.println("  // Create links for: ");
		for(FileAttributes f : selectedFiles) {
			
			System.out.println("  // " + f);
		}
    }//GEN-LAST:event_menuCreateShortcutActionPerformed

    private void menuCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCutActionPerformed
		treeNodeRemoteServers.add(new DefaultMutableTreeNode(
				new BookmarkedItem("abc", BookmarkedItem.TYPE_REMOTE_SERVER, "C:\\")));
    }//GEN-LAST:event_menuCutActionPerformed

    private void menuCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCopyActionPerformed
        
    }//GEN-LAST:event_menuCopyActionPerformed

    private void menuRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRenameActionPerformed
		String type = selectedFiles[0].isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE;
		String newName = (String)JOptionPane.showInputDialog(	this, 
																"Enter new "+ type +" name:",
																"Rename " + type,
																JOptionPane.QUESTION_MESSAGE,
																null, null, selectedFiles[0].name);
		if(newName != null) {
			try {
				selectedFiles[0] = fileSystemHandler.rename(selectedFiles[0], newName.trim());
				updateTableList(true);
				findAndSelectInList(selectedFiles[0]);
			} catch(IOException e) {
				JOptionPane.showMessageDialog(	this,
												"Failed renaming the " + type + ".\nReason: Cannot assign the specified name.",
												"File operation failure",
												JOptionPane.ERROR_MESSAGE);
				System.err.printf("Err: Failed renaming a %s '%s'. Reason: %s\n", type, selectedFiles[0].name, e.getMessage());
			}
		}
    }//GEN-LAST:event_menuRenameActionPerformed

    private void menuDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeleteActionPerformed
		StringBuilder tmpMsg = new StringBuilder("Delete ");
		if(selectedFiles.length == 1) 
			tmpMsg.append(selectedFiles[0].isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE);
		else 
			tmpMsg.append(selectedFiles.length).append(" items");
		tmpMsg.append('?');
		
		if(JOptionPane.showConfirmDialog(	this, 
											tmpMsg.toString(), 
											"Delete confirmation", 
											JOptionPane.YES_NO_OPTION,
											JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			java.util.List<FileAttributes> undeletedFiles = new ArrayList<>();
			boolean anyDeleted = false;
			for(FileAttributes file : selectedFiles) {
				try {
					System.out.println("  // del: " + file);
					fileSystemHandler.delete(file);
					anyDeleted = true;
				} catch(IOException e) {
					undeletedFiles.add(file);
				}
			}
			if(anyDeleted) 
				updateTableList(true);
			
			if(!undeletedFiles.isEmpty()) {
				tmpMsg.setLength(0);
				tmpMsg.append("Failed to delete the ");
				if(undeletedFiles.size()==1)
					tmpMsg	.append(selectedFiles[0].isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE)
							.append('!');
				else {
					tmpMsg.append("following ").append(undeletedFiles.size()).append(" items:\n");
					for(FileAttributes file : undeletedFiles) {
						tmpMsg	.append("  [")
								.append(file.isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE)
								.append("] ")
								.append(file.name)
								.append("\n");
					}
				}
				String msgString = tmpMsg.toString();
				
				JOptionPane.showMessageDialog(	this,
												msgString,
												"File operation failure",
												JOptionPane.ERROR_MESSAGE);
				System.err.printf("Err: Failed to delete at path='%s': %s\n", 
						fileSystemHandler.getCurrentWorkingDirectory().absolutePath, 
						undeletedFiles); // TODO log error
			}
		}
    }//GEN-LAST:event_menuDeleteActionPerformed

    private void tableFileListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableFileListMouseReleased
        //        JTable source = (JTable)evt.getSource();

        int row = tableFileList.rowAtPoint(evt.getPoint());
        if(row == -1) { // clicked on empty space
            tableFileList.clearSelection();
            if (evt.isPopupTrigger()) {
                selectedFiles = new FileAttributes[] { fileSystemHandler.getCurrentWorkingDirectory() };
                setFileSelectionContextOptions();
                popupFileSelected.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        } else  { // clicked on table row
            if (!tableFileList.isRowSelected(row))
            tableFileList.changeSelection(row, 0, false, false);

            //		TableModel tableModel = source.getModel();
            //		System.out.printf("  // getSelectedRowCount()=%d, selected row indices=[", tableFileList.getSelectedRowCount());
            int[] selectedRows = tableFileList.getSelectedRows();
            //		for(int viewRow : selectedRows)
            //			System.out.printf("{v=%d,m=%d} ", viewRow, tableFileList.convertRowIndexToModel(viewRow));

            selectedFiles = new FileAttributes[selectedRows.length];
            for(int i=0; i<selectedRows.length; i++) {
                selectedFiles[i] = (FileAttributes)tableModel.getValueAt(tableFileList.convertRowIndexToModel(selectedRows[i]), FILE_OBJECT_COLUMN_INDEX);
            }
            //		System.out.println(", selectedFiles updated");

            if (evt.isPopupTrigger()) {
                setFileSelectionContextOptions();
                popupFileSelected.show(evt.getComponent(), evt.getX(), evt.getY());
            } else if(evt.getClickCount() == 2) { // double clicked
                //			System.out.println("  // double clicked");
                openFile(selectedFiles[0]);
            }
        }
    }//GEN-LAST:event_tableFileListMouseReleased

    private void treeQuickAccessMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeQuickAccessMouseReleased
        int row = treeQuickAccess.getRowForLocation(evt.getX(), evt.getY());
        if(row == -1) // a parent node is expanded, so no row selected
        return;
        treeQuickAccess.setSelectionRow(row);

        TreePath selectedPath = treeQuickAccess.getPathForRow(row);

        if(selectedPath.getPathCount() > 2) { // only perform if a valid entry is selected
            //			Object[] nodes = selectedPath.getPath();
            //			selectedBookmarkNode = (DefaultMutableTreeNode)nodes[nodes.length-1];
            selectedBookmarkNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent(); // efficient approach

            //			if(nodes[1] == treeNodeDrives)
            //				bookmarkedItemSelected = treeNodeDrives;
            //			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)nodes[nodes.length-1];
            String filePath = ((BookmarkedItem)selectedBookmarkNode.getUserObject()).absolutePath;
            //			System.out.println("  // selected: " + filePath);
            try {
                selectedFiles = new FileAttributes[] { fileSystemHandler.getFileAttributes(filePath) };
            } catch(FileNotFoundException e) {
                JOptionPane.showMessageDialog(	this,
                    String.format("Cannot retieve bookmarked item!\n  Name: %s\n  Type: %s\n  Path: %s\n  Reason: %s",
                        selectedFiles[0].name, selectedFiles[0].type, selectedFiles[0].absolutePath, e),
                    "Invalid bookmarked item",
                    JOptionPane.ERROR_MESSAGE);
                System.err.printf("Err: Cannot retieve bookmarked item!\n  Name: %s\n  Type: %s\n  Path: %s\n  Reason: %s",
                    selectedFiles[0].name, selectedFiles[0].type, selectedFiles[0].absolutePath, e); // log error
                return;
            }

            //			System.out.printf("  // selected item: class=%s, value=%s\n",
                //				selectedNode.getUserObject().getClass().getName(), selectedNode.getUserObject());

            if(SwingUtilities.isRightMouseButton(evt)) { // do right-click action
                setBookmarkContextOptions();
                popupBookmarkedItems.show(evt.getComponent(), evt.getX(), evt.getY());
            } else {
                if(evt.getClickCount() == 2) { // do double-click action
                    // do popupBookmarkedItems's Open action
                    //					System.out.println("  // double clicked");
                    openFile(selectedFiles[0]);
                }
            }
        }
    }//GEN-LAST:event_treeQuickAccessMouseReleased
	
	/** 
	 * Sets the look and feel of the application on priority basis
	 * of the provided look and feel class names
	 */
	public static void setAppLookAndFeel() { // TODO move to app preferences module after migration
		for(int i=0; i<SystemResources.LOOKnFEEL_CLASSNAMES.size(); i++) {
			try {
				javax.swing.UIManager.setLookAndFeel(SystemResources.LOOKnFEEL_CLASSNAMES.get(i));
				return;
			} catch(Exception e) {}
		}
		System.err.println("Warning: Cannot set predefined L&F: " + SystemResources.LOOKnFEEL_CLASSNAMES); // TODO log warning
	}
	
	public static ListViewFrame init() {
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					listViewFrame = new ListViewFrame();
					listViewFrame.setVisible(true);
//	                javax.swing.SwingUtilities.updateComponentTreeUI(frame.getContentPane());
				} catch(FileNotFoundException e) {
					e.printStackTrace(System.err); // TODO log fatal error 
				}
			}
		});
		return listViewFrame;
	}
	
	/** Tester */ // TODO remove before moving to production
	public static void main(String args[]) throws Exception {
		/* Set the predefined look and feel */
		setAppLookAndFeel();
        init();
	}

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnBookmarkIndicator;
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
    private javax.swing.JLabel lblAddressIcon;
    private javax.swing.JLabel lblSearchIcon;
    private javax.swing.JMenuItem menuBookmark;
    private javax.swing.JMenuItem menuBookmarkOpen;
    private javax.swing.JMenuItem menuBookmarkOpenInNewTab;
    private javax.swing.JMenuItem menuBookmarkOpenLocation;
    private javax.swing.JMenuItem menuBookmarkProperties;
    private javax.swing.JMenuItem menuBookmarkRemove;
    private javax.swing.JMenuItem menuBookmarkRemoveAll;
    private javax.swing.JMenuItem menuBookmarkRename;
    private javax.swing.JMenuItem menuCopy;
    private javax.swing.JMenuItem menuCreateShortcut;
    private javax.swing.JMenuItem menuCut;
    private javax.swing.JMenuItem menuDelete;
    private javax.swing.JMenu menuNew;
    private javax.swing.JMenuItem menuNewFile;
    private javax.swing.JMenuItem menuNewFolder;
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
    private javax.swing.JScrollPane scrollpaneTableFileList;
    private javax.swing.JScrollPane scrollpaneTreeQuickAccess;
    private javax.swing.JTable tableFileList;
    private javax.swing.JToolBar toolbarOptions;
    private javax.swing.JTree treeQuickAccess;
    private javax.swing.JTextField txtPathAddress;
    private javax.swing.JTextField txtSearchFiles;
    // End of variables declaration//GEN-END:variables

	private void setTableRows(final FileAttributes[] files) {
		tableModel.setRowCount(0);
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
