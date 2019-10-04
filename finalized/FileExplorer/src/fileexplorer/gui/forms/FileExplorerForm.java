package fileexplorer.gui.forms;

import fileexplorer.handlers.fs.FileAttributes;
import fileexplorer.handlers.fs.FileSystemHandler;
import fileexplorer.handlers.fs.LocalFileSystemHandler;
import fileexplorer.handlers.fs.RemoteFileSystemHandler;
import fileexplorer.handlers.shared.SystemHandler;
import javax.swing.*;
import fileexplorer.handlers.shared.*;
import fileexplorer.handlers.shared.SystemResources.IconRegistry;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class FileExplorerForm extends javax.swing.JFrame {
	// members of status bar
	
	
	private static ActivityLogger logger = ActivityLogger.getInstance();
	private DefaultMutableTreeNode treeNodeQuickAccess;
    private DefaultMutableTreeNode treeNodeDrives;
    private DefaultMutableTreeNode treeNodeLibrary;
    private DefaultMutableTreeNode treeNodeBookmarks;
    private DefaultMutableTreeNode treeNodeRemoteServers;
	private DefaultTreeModel treeModelQuickAccess;
//	private DefaultMutableTreeNode selectedBookmarkNode = null;
	private final String HOME_DIR = "home";	
	private static FileExplorerForm form = null;
	private LocalFileSystemHandler localFileSystemHandler;
	private IconRegistry iconRegistry = IconRegistry.getInstance();
	
	/* public resources */
	public final BookmarkHandler bookmarkHandler;
	public AboutForm formAbout;
	public PreferencesForm formPreferences;
	public RemoteLoginForm formRemoteLogin;
	
	
	public static FileExplorerForm init() throws InterruptedException, InvocationTargetException {
		form = null;
		/* Create and display the form */
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				form = new FileExplorerForm();
			}
		});
		
		form.setVisible(true);
		form.addNewTab(form.HOME_DIR);
//		System.out.printf("  // form==null: %b, form.lblItemsSelected==null: %b, form.getSelectedForm()==null: %b, form.getSelectedForm().fileList==null: %b\n", 
//				form==null, form.lblItemsSelected==null, 
//				form.getSelectedForm()==null, form.getSelectedForm().fileList==null);
//		form.lblItemsSelected.setText(String.valueOf(form.getSelectedForm().fileList.length));
		return form;
	}
	
	
    /**
     * Creates new form FileExplorer
     */
    private FileExplorerForm() {
		logger.logInfo("Initialising FileExplorerForm...");
        initComponents();
		panelFolderListLoad.setVisible(false); // initial state 
		try {   
			localFileSystemHandler = (LocalFileSystemHandler)FileSystemHandler.getLocalHandler(null);
		} catch(IOException e) {
			logger.logFatal(e, "Cannot set up bookmarks. Reason: %s", e);
		}
		if(!localFileSystemHandler.isDesktopSupported) {
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
			logger.logSevere(null, "Desktop feature not supported in this platform: Platform: %s", platform);
		}
		
		bookmarkHandler = BookmarkHandler.getInstance(treeQuickAccess, treeModelQuickAccess,
									treeNodeDrives, treeNodeLibrary, treeNodeBookmarks, treeNodeRemoteServers);
		bookmarkHandler.init(localFileSystemHandler);
		setIconImage(iconRegistry.appIcon_big.getImage());
		lblActivityStatus.setText("System started");
    }
	
	void addNewTab(final String dirPath) {
		FileSystemHandler fileSystemHandler;
		if(!HOME_DIR.equals(dirPath)) {
			try {
				fileSystemHandler = FileSystemHandler.getLocalHandler(dirPath);
				addNewTab(fileSystemHandler);
				return;
			} catch(IOException e) {
				JOptionPane.showMessageDialog(	this,
												"Unable to load path: " + dirPath + "\nReason: " + e + "\nLoading user home folder",
												"Path load failure",
												JOptionPane.ERROR_MESSAGE);
				logger.logSevere(e, "Err: Unable to load path: %s, reason: %s", dirPath, e);
			}
		}
		try {
			fileSystemHandler = FileSystemHandler.getLocalHandler(null);
			addNewTab(fileSystemHandler);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(	this,
											"Fatal error: Unable to load user home folder!\nReason: " + e,
											"Fatal: Path load failure",
											JOptionPane.ERROR_MESSAGE);
			logger.logFatal(e, "Unable to load user home folder. Reason: %s", e);
		}
		
	}
	
	public void addNewTab(final FileSystemHandler fileSystemHandler) {
		final boolean isRemote = fileSystemHandler instanceof RemoteFileSystemHandler;		
		JLabel lblTabTitle = new JLabel(null, 
			isRemote ? iconRegistry.remoteTabIcon_small : iconRegistry.localTabIcon_small,
			SwingConstants.RIGHT);
		lblTabTitle.setIconTextGap(5);
		JButton btnTabClose = new JButton(iconRegistry.closeTabButton_small);
		btnTabClose.setPreferredSize(new Dimension(14, 14));
		btnTabClose.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					closeSelectedTab();
				}
			});
		
		JPanel tabComponent = new JPanel(new BorderLayout());
		tabComponent.add(lblTabTitle, BorderLayout.WEST);
		tabComponent.add(Box.createHorizontalStrut(10));
		tabComponent.add(btnTabClose, BorderLayout.EAST);
		
		// Assign tab component		
		tabbedPane.add(ListViewPanel.init(lblTabTitle, fileSystemHandler, bookmarkHandler));		
		int currentTabIdx = tabbedPane.getTabCount()-1;
		tabbedPane.setTabComponentAt(currentTabIdx, tabComponent);
		tabbedPane.setSelectedIndex(currentTabIdx); // set focus
		logger.logInfo("Opened a new tab: index=%d, location=%s, path=%s",
				currentTabIdx,
				fileSystemHandler instanceof LocalFileSystemHandler ? "local" : "remote", 
				fileSystemHandler.getCurrentWorkingDirectory().absolutePath);
	}
	
	private void closeAllButCurrentTab() {	
		for(int	i=0, removeIdx=0,
				selectedTabIdx=tabbedPane.getSelectedIndex(),
				tabCount=tabbedPane.getTabCount(); i<tabCount; i++, selectedTabIdx--) {
			if(selectedTabIdx==0)
				removeIdx=1;
			else
				tabbedPane.remove(removeIdx);
		}
	}
	
	private void closeSelectedTab() {
		if(tabbedPane.getTabCount() > 1) {
			ListViewPanel selectedPanel = (ListViewPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
			selectedPanel.closeHandler();
			tabbedPane.remove(selectedPanel);
		}
	}
	
//	private void setBookmarks() {
//		bookmarkHandler = BookmarkHandler.getInstance(treeQuickAccess, treeModelQuickAccess);
//		try {
//			for(FileAttributes file : localFileSystemHandler.listRoots()) {
//				bookmarkHandler.add(	treeNodeDrives, 
//										new BookmarkedItem(	file.absolutePath, 
//															BookmarkedItem.TYPE_SYSTEM_DRIVE, 
////															IconRegistry.driveIcon_big,
//															file.absolutePath));
//			}
//		} catch(FileNotFoundException e) {
//			logger.logSevere(e, "Cannot add system roots to bookmark tree node: %s", e);
//			e.printStackTrace(System.err);
//		}
//
//		bookmarkHandler	.add(	treeNodeLibrary,
//								new BookmarkedItem("Home", BookmarkedItem.TYPE_LIBRARY_FOLDER, localFileSystemHandler.getUserHomeDirectoryPath()))
//						.add(	treeNodeLibrary,
//								new BookmarkedItem("Temp", BookmarkedItem.TYPE_LIBRARY_FOLDER, localFileSystemHandler.getTempDirectoryPath()));
//
//		// TODO load all bookmarks and remote server hostnames from disk file
//
//		bookmarkHandler.expandAllNodes();
//	}
	
//	private long errorCount = 0L;
//	
//	private void reportError(final Exception e) { // TODO implement is tabbed frame
//		lblErrorCount.setText(String.valueOf(++errorCount));		
//		logger.logSevere("[errorCount=%d] %s", errorCount, logger.getStackTrace(e));
//	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngpViewLayoutStyle = new javax.swing.ButtonGroup();
        btngpAppLanguage = new javax.swing.ButtonGroup();
        btngpAppTheme = new javax.swing.ButtonGroup();
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
        jSplitPane1 = new javax.swing.JSplitPane();
        scrollpaneTreeQuickAccess = new javax.swing.JScrollPane();
        /* Custom code */
        treeQuickAccess = new javax.swing.JTree();
        panelStatus = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        btnCancelTableUpdate = new javax.swing.JButton();
        menubarMain = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuCreate = new javax.swing.JMenu();
        menuitemCreateNewFolder = new javax.swing.JMenuItem();
        menuitemCreateNewFile = new javax.swing.JMenuItem();
        menuTab = new javax.swing.JMenu();
        menuitemNewTab = new javax.swing.JMenuItem();
        menuitemCloseCurrentTab = new javax.swing.JMenuItem();
        menuitemCloseAllButCurrentTab = new javax.swing.JMenuItem();
        menuitemConnectRemoteServer = new javax.swing.JMenuItem();
        menuitemExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuitemSearch = new javax.swing.JMenuItem();
        menuitemEditAddressBar = new javax.swing.JMenuItem();
        menuitemBookmarkCurrentFolder = new javax.swing.JMenuItem();
        menuitemGoToParentFolder = new javax.swing.JMenuItem();
        menuitemReloadCurrentFolder = new javax.swing.JMenuItem();
        menuView = new javax.swing.JMenu();
        menuitemViewDiagnostics = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuTheme = new javax.swing.JMenu();
        menuitemThemeMetalRadioButton = new javax.swing.JRadioButtonMenuItem();
        menuitemThemeNimbuslRadioButton = new javax.swing.JRadioButtonMenuItem();
        menuitemThemeMotifCDERadioButton = new javax.swing.JRadioButtonMenuItem();
        menuitemThemeWindowsRadioButton = new javax.swing.JRadioButtonMenuItem();
        menuitemThemeWindowsClassicRadioButton = new javax.swing.JRadioButtonMenuItem();
        menuViewLanguage = new javax.swing.JMenu();
        menuitemLangEnglishUSRadioButton = new javax.swing.JRadioButtonMenuItem();
        menuitemLangFrenchRadioButton = new javax.swing.JRadioButtonMenuItem();
        menuitemLangGermanRadioButton = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuitemViewPreferences = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuitemAbout = new javax.swing.JMenuItem();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("lang/strings"); // NOI18N
        menuBookmarkOpen.setText(bundle.getString("FileExplorerForm.menuBookmarkOpen.text")); // NOI18N
        menuBookmarkOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkOpenActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkOpen);

        menuBookmarkOpenInNewTab.setText(bundle.getString("FileExplorerForm.menuBookmarkOpenInNewTab.text")); // NOI18N
        menuBookmarkOpenInNewTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkOpenInNewTabActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkOpenInNewTab);

        menuBookmarkOpenLocation.setText(bundle.getString("FileExplorerForm.menuBookmarkOpenLocation.text")); // NOI18N
        menuBookmarkOpenLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkOpenLocationActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkOpenLocation);
        popupBookmarkedItems.add(jSeparator9);

        menuBookmarkRename.setText(bundle.getString("FileExplorerForm.menuBookmarkRename.text")); // NOI18N
        menuBookmarkRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkRenameActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkRename);

        menuBookmarkRemove.setText(bundle.getString("FileExplorerForm.menuBookmarkRemove.text")); // NOI18N
        menuBookmarkRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkRemoveActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkRemove);

        menuBookmarkRemoveAll.setText(bundle.getString("FileExplorerForm.menuBookmarkRemoveAll.text")); // NOI18N
        menuBookmarkRemoveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkRemoveAllActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkRemoveAll);
        popupBookmarkedItems.add(jSeparator8);

        menuBookmarkProperties.setText(bundle.getString("FileExplorerForm.menuBookmarkProperties.text")); // NOI18N
        menuBookmarkProperties.setActionCommand(bundle.getString("FileExplorerForm.menuBookmarkProperties.actionCommand")); // NOI18N
        menuBookmarkProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBookmarkPropertiesActionPerformed(evt);
            }
        });
        popupBookmarkedItems.add(menuBookmarkProperties);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(bundle.getString("FileExplorerForm.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(500, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

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
        jSplitPane1.setRightComponent(tabbedPane);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        panelStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelStatus.setLayout(new java.awt.BorderLayout());

        lblItemsSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/item_count.png"))); // NOI18N
        lblItemsSelected.setText("0"); // NOI18N
        lblItemsSelected.setToolTipText(bundle.getString("FileExplorerForm.lblItemsSelected.toolTipText")); // NOI18N
        lblItemsSelected.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblItemsSelected.setIconTextGap(5);
        panelStatus.add(lblItemsSelected, java.awt.BorderLayout.LINE_START);

        lblActivityStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/activity_status.png"))); // NOI18N
        lblActivityStatus.setText("System not started yet"); // NOI18N
        lblActivityStatus.setToolTipText(bundle.getString("FileExplorerForm.lblActivityStatus.toolTipText")); // NOI18N
        lblActivityStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblActivityStatus.setIconTextGap(5);
        panelStatus.add(lblActivityStatus, java.awt.BorderLayout.CENTER);

        lblErrorCount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/errro_count.png"))); // NOI18N
        lblErrorCount.setText("0"); // NOI18N
        lblErrorCount.setToolTipText(bundle.getString("FileExplorerForm.lblErrorCount.toolTipText")); // NOI18N
        lblErrorCount.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblErrorCount.setIconTextGap(5);
        panelStatus.add(lblErrorCount, java.awt.BorderLayout.LINE_END);

        panelFolderListLoad.setLayout(new java.awt.BorderLayout());

        progressBar.setFont(new java.awt.Font("sansserif", 1, 10)); // NOI18N
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new java.awt.Dimension(150, 17));
        progressBar.setString(bundle.getString("FileExplorerForm.progressBar.string")); // NOI18N
        progressBar.setStringPainted(true);
        panelFolderListLoad.add(progressBar, java.awt.BorderLayout.CENTER);

        btnCancelTableUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close_tab_button_small.png"))); // NOI18N
        btnCancelTableUpdate.setText(bundle.getString("FileExplorerForm.btnCancelTableUpdate.text")); // NOI18N
        btnCancelTableUpdate.setPreferredSize(new java.awt.Dimension(17, 17));
        btnCancelTableUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelTableUpdateActionPerformed(evt);
            }
        });
        panelFolderListLoad.add(btnCancelTableUpdate, java.awt.BorderLayout.LINE_END);

        panelStatus.add(panelFolderListLoad, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(panelStatus, java.awt.BorderLayout.PAGE_END);

        menuFile.setText(bundle.getString("FileExplorerForm.menuFile.text")); // NOI18N

        menuCreate.setText(bundle.getString("FileExplorerForm.menuCreate.text")); // NOI18N

        menuitemCreateNewFolder.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuitemCreateNewFolder.setText(bundle.getString("FileExplorerForm.menuitemCreateNewFolder.text")); // NOI18N
        menuCreate.add(menuitemCreateNewFolder);

        menuitemCreateNewFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuitemCreateNewFile.setText(bundle.getString("FileExplorerForm.menuitemCreateNewFile.text")); // NOI18N
        menuCreate.add(menuitemCreateNewFile);

        menuFile.add(menuCreate);

        menuTab.setText(bundle.getString("FileExplorerForm.menuTab.text")); // NOI18N

        menuitemNewTab.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        menuitemNewTab.setText(bundle.getString("FileExplorerForm.menuitemNewTab.text")); // NOI18N
        menuitemNewTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemNewTabActionPerformed(evt);
            }
        });
        menuTab.add(menuitemNewTab);

        menuitemCloseCurrentTab.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        menuitemCloseCurrentTab.setText(bundle.getString("FileExplorerForm.menuitemCloseCurrentTab.text")); // NOI18N
        menuitemCloseCurrentTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemCloseCurrentTabActionPerformed(evt);
            }
        });
        menuTab.add(menuitemCloseCurrentTab);

        menuitemCloseAllButCurrentTab.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuitemCloseAllButCurrentTab.setText(bundle.getString("FileExplorerForm.menuitemCloseAllButCurrentTab.text")); // NOI18N
        menuitemCloseAllButCurrentTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemCloseAllButCurrentTabActionPerformed(evt);
            }
        });
        menuTab.add(menuitemCloseAllButCurrentTab);

        menuFile.add(menuTab);

        menuitemConnectRemoteServer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        menuitemConnectRemoteServer.setText(bundle.getString("FileExplorerForm.menuitemConnectRemoteServer.text")); // NOI18N
        menuitemConnectRemoteServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemConnectRemoteServerActionPerformed(evt);
            }
        });
        menuFile.add(menuitemConnectRemoteServer);

        menuitemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        menuitemExit.setText(bundle.getString("FileExplorerForm.menuitemExit.text")); // NOI18N
        menuitemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemExitActionPerformed(evt);
            }
        });
        menuFile.add(menuitemExit);

        menubarMain.add(menuFile);

        menuEdit.setText(bundle.getString("FileExplorerForm.menuEdit.text")); // NOI18N

        menuitemSearch.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        menuitemSearch.setText(bundle.getString("FileExplorerForm.menuitemSearch.text")); // NOI18N
        menuEdit.add(menuitemSearch);

        menuitemEditAddressBar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menuitemEditAddressBar.setText(bundle.getString("FileExplorerForm.menuitemEditAddressBar.text")); // NOI18N
        menuitemEditAddressBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemEditAddressBarActionPerformed(evt);
            }
        });
        menuEdit.add(menuitemEditAddressBar);

        menuitemBookmarkCurrentFolder.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        menuitemBookmarkCurrentFolder.setText(bundle.getString("FileExplorerForm.menuitemBookmarkCurrentFolder.text")); // NOI18N
        menuEdit.add(menuitemBookmarkCurrentFolder);

        menuitemGoToParentFolder.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, java.awt.event.InputEvent.ALT_MASK));
        menuitemGoToParentFolder.setText(bundle.getString("FileExplorerForm.menuitemGoToParentFolder.text")); // NOI18N
        menuEdit.add(menuitemGoToParentFolder);

        menuitemReloadCurrentFolder.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        menuitemReloadCurrentFolder.setText(bundle.getString("FileExplorerForm.menuitemReloadCurrentFolder.text")); // NOI18N
        menuitemReloadCurrentFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemReloadCurrentFolderActionPerformed(evt);
            }
        });
        menuEdit.add(menuitemReloadCurrentFolder);

        menubarMain.add(menuEdit);

        menuView.setText(bundle.getString("FileExplorerForm.menuView.text")); // NOI18N

        menuitemViewDiagnostics.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuitemViewDiagnostics.setText(bundle.getString("FileExplorerForm.menuitemViewDiagnostics.text")); // NOI18N
        menuView.add(menuitemViewDiagnostics);
        menuView.add(jSeparator1);

        menuTheme.setText(bundle.getString("FileExplorerForm.menuTheme.text")); // NOI18N

        btngpAppTheme.add(menuitemThemeMetalRadioButton);
        menuitemThemeMetalRadioButton.setText("Metal"); // NOI18N
        menuTheme.add(menuitemThemeMetalRadioButton);

        btngpAppTheme.add(menuitemThemeNimbuslRadioButton);
        menuitemThemeNimbuslRadioButton.setText("Nimbus"); // NOI18N
        menuTheme.add(menuitemThemeNimbuslRadioButton);

        btngpAppTheme.add(menuitemThemeMotifCDERadioButton);
        menuitemThemeMotifCDERadioButton.setText("Motif/CDE"); // NOI18N
        menuTheme.add(menuitemThemeMotifCDERadioButton);

        btngpAppTheme.add(menuitemThemeWindowsRadioButton);
        menuitemThemeWindowsRadioButton.setText("Windows"); // NOI18N
        menuTheme.add(menuitemThemeWindowsRadioButton);

        btngpAppTheme.add(menuitemThemeWindowsClassicRadioButton);
        menuitemThemeWindowsClassicRadioButton.setText("Windows Classic"); // NOI18N
        menuTheme.add(menuitemThemeWindowsClassicRadioButton);

        menuView.add(menuTheme);

        menuViewLanguage.setText(bundle.getString("FileExplorerForm.menuViewLanguage.text")); // NOI18N

        btngpAppLanguage.add(menuitemLangEnglishUSRadioButton);
        menuitemLangEnglishUSRadioButton.setText("English (US)"); // NOI18N
        menuViewLanguage.add(menuitemLangEnglishUSRadioButton);

        btngpAppLanguage.add(menuitemLangFrenchRadioButton);
        menuitemLangFrenchRadioButton.setText("French"); // NOI18N
        menuViewLanguage.add(menuitemLangFrenchRadioButton);

        btngpAppLanguage.add(menuitemLangGermanRadioButton);
        menuitemLangGermanRadioButton.setText("German"); // NOI18N
        menuViewLanguage.add(menuitemLangGermanRadioButton);

        menuView.add(menuViewLanguage);
        menuView.add(jSeparator2);

        menuitemViewPreferences.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        menuitemViewPreferences.setText(bundle.getString("FileExplorerForm.menuitemViewPreferences.text")); // NOI18N
        menuitemViewPreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemViewPreferencesActionPerformed(evt);
            }
        });
        menuView.add(menuitemViewPreferences);

        menubarMain.add(menuView);

        menuHelp.setText(bundle.getString("FileExplorerForm.menuHelp.text")); // NOI18N

        menuitemAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/about_small.png"))); // NOI18N
        menuitemAbout.setText(bundle.getString("FileExplorerForm.menuitemAbout.text")); // NOI18N
        menuitemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemAboutActionPerformed(evt);
            }
        });
        menuHelp.add(menuitemAbout);

        menubarMain.add(menuHelp);

        setJMenuBar(menubarMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
//        System.out.println("Info: Maximizing FileExplorerForm window...");
        // sets window state to maximized state
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		logger.logInfo("Window opened");
    }//GEN-LAST:event_formWindowOpened

//	private AboutForm aboutForm = null;
    private void menuitemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemAboutActionPerformed
		if(formAbout == null)
			AboutForm.init();
		else 
			formAbout.requestFocus();
    }//GEN-LAST:event_menuitemAboutActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // get close confirmation
		if(SystemResources.prefs.confirmBeforeExit) {
			if(JOptionPane.showConfirmDialog(this,
					"Do you want to Exit ?", "Exit Confirmation",
					JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
				return;
		}
		logger.logInfo("Closing all opened tabs and connections...");
		// close all open handlers
		for(int i=0, count=tabbedPane.getTabCount(); i<count; i++) {
			ListViewPanel selectedPanel = (ListViewPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
			selectedPanel.closeHandler();
			tabbedPane.remove(selectedPanel);
		}
		logger.logInfo("Window closing...");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SystemHandler.getInstance().close();
//		else if (result == JOptionPane.NO_OPTION)
//          this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }//GEN-LAST:event_formWindowClosing

    private void menuitemConnectRemoteServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemConnectRemoteServerActionPerformed
//        this.dispatchEvent(new java.awt.event.WindowEvent(this, java.awt.event.WindowEvent.WINDOW_CLOSING));
		if(formRemoteLogin == null)
			RemoteLoginForm.init(null);
		else 
			formRemoteLogin.requestFocus();
    }//GEN-LAST:event_menuitemConnectRemoteServerActionPerformed

	private ListViewPanel getSelectedForm() {
		return (ListViewPanel)tabbedPane.getSelectedComponent();
	}
	
	private DefaultMutableTreeNode selectedBookmarkNode = null;
	
    private void treeQuickAccessMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeQuickAccessMouseReleased
        int row = treeQuickAccess.getRowForLocation(evt.getX(), evt.getY());
        if(row == -1) // a parent node is expanded, so no row selected
			return;
		treeQuickAccess.clearSelection();
        treeQuickAccess.setSelectionRow(row);		
        TreePath selectedPath = treeQuickAccess.getPathForRow(row);
		
        if(selectedPath.getPathCount() > 2) { // only perform if a valid entry is selected
            //			Object[] nodes = selectedPath.getPath();
            //			selectedBookmarkNode = (DefaultMutableTreeNode)nodes[nodes.length-1];
//            selectedBookmarkNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent(); // efficient approach

            //			if(nodes[1] == treeNodeDrives)
            //				bookmarkedItemSelected = treeNodeDrives;
            //			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)nodes[nodes.length-1];
			
            selectedBookmarkNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent();
            //			System.out.println("  // selected: " + filePath);
//            try {
//                selectedFile = localFileSystemHandler.getFileAttributes(filePath);
//            } catch(FileNotFoundException e) {
//                JOptionPane.showMessageDialog(	this,
//                    String.format("Cannot retieve bookmarked item!\n  Name: %s\n  Type: %s\n  Path: %s\n  Reason: %s",
//                        selectedFiles[0].name, selectedFiles[0].type, selectedFiles[0].absolutePath, e),
//                    "Invalid bookmarked item",
//                    JOptionPane.ERROR_MESSAGE);
//                System.err.printf("Err: Cannot retieve bookmarked item!\n  Name: %s\n  Type: %s\n  Path: %s\n  Reason: %s",
//                    selectedFiles[0].name, selectedFiles[0].type, selectedFiles[0].absolutePath, e); // log error
//                return;
//            }

            //			System.out.printf("  // selected item: class=%s, value=%s\n",
                //				selectedNode.getUserObject().getClass().getName(), selectedNode.getUserObject());

            if(SwingUtilities.isRightMouseButton(evt)) { // do right-click action
                setBookmarkContextOptions();
                popupBookmarkedItems.show(evt.getComponent(), evt.getX(), evt.getY());
            } else {
                if(evt.getClickCount() == 2) { // do double-click action
                    // do popupBookmarkedItems's Open action
                    //					System.out.println("  // double clicked");
					
					BookmarkedItem selectedItem = (BookmarkedItem)selectedBookmarkNode.getUserObject();
					if(BookmarkedItem.TYPE_REMOTE_SERVER.equals(selectedItem.type))
						RemoteLoginForm.init(selectedItem.absolutePath);
					else 
						getSelectedForm().openBookmarkNode(selectedBookmarkNode, false);
                }
            }
        }
    }//GEN-LAST:event_treeQuickAccessMouseReleased
	
	private void setBookmarkContextOptions() {
		// reset variable options first
		menuBookmarkOpenInNewTab.setVisible(true);
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
		} else if(parent == treeNodeBookmarks) {
			menuBookmarkOpenInNewTab.setVisible(
					((BookmarkedItem)selectedBookmarkNode.getUserObject()).type
						.equals(FileAttributes.TYPE_FOLDER));
		} else if(parent == treeNodeRemoteServers) {
			menuBookmarkOpenLocation.setVisible(false);
		}
	}
	
    private void menuitemNewTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemNewTabActionPerformed
        addNewTab(HOME_DIR);
    }//GEN-LAST:event_menuitemNewTabActionPerformed

    private void menuitemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemExitActionPerformed
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_menuitemExitActionPerformed

    private void menuBookmarkOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkOpenActionPerformed
        getSelectedForm().openBookmarkNode(selectedBookmarkNode, false);
    }//GEN-LAST:event_menuBookmarkOpenActionPerformed

    private void menuBookmarkOpenInNewTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkOpenInNewTabActionPerformed
		addNewTab(((BookmarkedItem)selectedBookmarkNode.getUserObject()).absolutePath);
    }//GEN-LAST:event_menuBookmarkOpenInNewTabActionPerformed

    private void menuBookmarkOpenLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkOpenLocationActionPerformed
        getSelectedForm().openBookmarkNode(selectedBookmarkNode, true);
    }//GEN-LAST:event_menuBookmarkOpenLocationActionPerformed

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
        bookmarkHandler.remove(selectedBookmarkNode);
		getSelectedForm().removeCurrentDirectoryIfBookmarked(selectedBookmarkNode);
    }//GEN-LAST:event_menuBookmarkRemoveActionPerformed

    private void menuBookmarkRemoveAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkRemoveAllActionPerformed
        if(JOptionPane.showConfirmDialog(	this,
							"Sure to remove all these "+ selectedBookmarkNode.getParent().getChildCount() +" bookmarks?",
							"Delete confirmation",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			bookmarkHandler.removeAllSiblings(selectedBookmarkNode);
			getSelectedForm().removeCurrentDirectoryBookmark();
        }
    }//GEN-LAST:event_menuBookmarkRemoveAllActionPerformed

    private void menuBookmarkPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkPropertiesActionPerformed
        BookmarkedItem item = (BookmarkedItem)selectedBookmarkNode.getUserObject();
		try {
			if(BookmarkedItem.TYPE_REMOTE_SERVER.equals(item.type))
				PropertiesForm.init(getSelectedForm(), selectedBookmarkNode, item.name, item.absolutePath);
			else
				PropertiesForm.init(getSelectedForm(), selectedBookmarkNode,
									new FileAttributes[] { localFileSystemHandler.getFileAttributes(item.absolutePath) });
		} catch(IOException e) {
			JOptionPane.showMessageDialog(	this,
							String.format("Cannot fetch bookmarked %s: %s\nReason: %s", 
									item.type, item.absolutePath, e),
							"Corrupted bookmarked item",
							JOptionPane.ERROR_MESSAGE);
			logger.logSevere(e, "Cannot fetch bookmarked %s: %s. Reason: %s", item.type, item.absolutePath, e);
		}
    }//GEN-LAST:event_menuBookmarkPropertiesActionPerformed

    private void menuitemViewPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemViewPreferencesActionPerformed
        if(formPreferences == null)
			PreferencesForm.init();
		else 
			formPreferences.requestFocus();
    }//GEN-LAST:event_menuitemViewPreferencesActionPerformed

    private void menuitemCloseCurrentTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemCloseCurrentTabActionPerformed
        closeSelectedTab();
    }//GEN-LAST:event_menuitemCloseCurrentTabActionPerformed

    private void menuitemCloseAllButCurrentTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemCloseAllButCurrentTabActionPerformed
        closeAllButCurrentTab();
    }//GEN-LAST:event_menuitemCloseAllButCurrentTabActionPerformed

    private void menuitemEditAddressBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemEditAddressBarActionPerformed
		getSelectedForm().focusAddressBar();
//		
//		JTextField txtPathAddress = SystemResources.addressBarList.get(tabbedPane.getSelectedIndex());		
//		txtPathAddress.selectAll();
//		txtPathAddress.requestFocus();
    }//GEN-LAST:event_menuitemEditAddressBarActionPerformed

    private void menuitemReloadCurrentFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemReloadCurrentFolderActionPerformed
         System.out.println("  // tabbedPane.getComponentAt(0)=" + tabbedPane.getComponentAt(0).getClass().getName());
    }//GEN-LAST:event_menuitemReloadCurrentFolderActionPerformed

    private void btnCancelTableUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelTableUpdateActionPerformed
        SwingWorker worker = getSelectedForm().currentUpdateTableListWorker;
		if(worker != null)
			worker.cancel(true);			
    }//GEN-LAST:event_btnCancelTableUpdateActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelTableUpdate;
    private javax.swing.ButtonGroup btngpAppLanguage;
    private javax.swing.ButtonGroup btngpAppTheme;
    private javax.swing.ButtonGroup btngpViewLayoutStyle;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JSplitPane jSplitPane1;
    public final javax.swing.JLabel lblActivityStatus = new javax.swing.JLabel();
    public final javax.swing.JLabel lblErrorCount = new javax.swing.JLabel();
    public final javax.swing.JLabel lblItemsSelected = new javax.swing.JLabel();
    private javax.swing.JMenuItem menuBookmarkOpen;
    private javax.swing.JMenuItem menuBookmarkOpenInNewTab;
    private javax.swing.JMenuItem menuBookmarkOpenLocation;
    private javax.swing.JMenuItem menuBookmarkProperties;
    private javax.swing.JMenuItem menuBookmarkRemove;
    private javax.swing.JMenuItem menuBookmarkRemoveAll;
    private javax.swing.JMenuItem menuBookmarkRename;
    private javax.swing.JMenu menuCreate;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenu menuTab;
    private javax.swing.JMenu menuTheme;
    private javax.swing.JMenu menuView;
    private javax.swing.JMenu menuViewLanguage;
    private javax.swing.JMenuBar menubarMain;
    private javax.swing.JMenuItem menuitemAbout;
    private javax.swing.JMenuItem menuitemBookmarkCurrentFolder;
    private javax.swing.JMenuItem menuitemCloseAllButCurrentTab;
    private javax.swing.JMenuItem menuitemCloseCurrentTab;
    private javax.swing.JMenuItem menuitemConnectRemoteServer;
    private javax.swing.JMenuItem menuitemCreateNewFile;
    private javax.swing.JMenuItem menuitemCreateNewFolder;
    private javax.swing.JMenuItem menuitemEditAddressBar;
    private javax.swing.JMenuItem menuitemExit;
    private javax.swing.JMenuItem menuitemGoToParentFolder;
    private javax.swing.JRadioButtonMenuItem menuitemLangEnglishUSRadioButton;
    private javax.swing.JRadioButtonMenuItem menuitemLangFrenchRadioButton;
    private javax.swing.JRadioButtonMenuItem menuitemLangGermanRadioButton;
    private javax.swing.JMenuItem menuitemNewTab;
    private javax.swing.JMenuItem menuitemReloadCurrentFolder;
    private javax.swing.JMenuItem menuitemSearch;
    private javax.swing.JRadioButtonMenuItem menuitemThemeMetalRadioButton;
    private javax.swing.JRadioButtonMenuItem menuitemThemeMotifCDERadioButton;
    private javax.swing.JRadioButtonMenuItem menuitemThemeNimbuslRadioButton;
    private javax.swing.JRadioButtonMenuItem menuitemThemeWindowsClassicRadioButton;
    private javax.swing.JRadioButtonMenuItem menuitemThemeWindowsRadioButton;
    private javax.swing.JMenuItem menuitemViewDiagnostics;
    private javax.swing.JMenuItem menuitemViewPreferences;
    public final javax.swing.JPanel panelFolderListLoad = new javax.swing.JPanel();
    private javax.swing.JPanel panelStatus;
    private javax.swing.JPopupMenu popupBookmarkedItems;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JScrollPane scrollpaneTreeQuickAccess;
    private final javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
    private javax.swing.JTree treeQuickAccess;
    // End of variables declaration//GEN-END:variables
}
