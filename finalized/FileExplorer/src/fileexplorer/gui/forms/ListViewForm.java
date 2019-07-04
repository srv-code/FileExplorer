package fileexplorer.gui.forms;

import fileexplorer.handlers.fs.FileAttributes;
import fileexplorer.handlers.fs.FileSystemHandler;
import fileexplorer.handlers.fs.nav.NavigationException;
import fileexplorer.handlers.shared.ActivityLogger;
import fileexplorer.handlers.shared.BookmarkHandler;
import fileexplorer.handlers.shared.BookmarkedItem;
import fileexplorer.handlers.shared.SystemResources;
import fileexplorer.handlers.shared.SystemResources.IconRegistry;
import fileexplorer.handlers.shared.SystemResources.IconRegistry.IconSize;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.nio.file.InvalidPathException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;


/**
 *
 * @author soura
 */
public class ListViewForm extends JPanel {
	private FileSystemHandler fileSystemHandler = null;
	private IconRegistry iconRegistry = IconRegistry.getInstance();
	private DefaultTableModel tableModel = null;
	private String lastVisitedPath = null;
	
	private final int FILE_OBJECT_COLUMN_INDEX = 1;
	private final int ICON_COLUMN_INDEX = 0;
	
	private final JLabel lblTabTitle;
	private static final ActivityLogger logger = SystemResources.getActivityLogger();
	private BookmarkHandler bookmarkHandler;
	
	
	/**
	 * Creates new form ListViewForm
	 */
	public ListViewForm(final JLabel lblTabTitle, final FileSystemHandler fileSystemHandler, final BookmarkHandler bookmarkHandler) {
		System.out.println("Info: Initializing ListViewFrame...");
		logger.logInfo("Initializing ListViewFrame...");
		this.lblTabTitle = lblTabTitle;
		this.fileSystemHandler = fileSystemHandler;
		this.bookmarkHandler = bookmarkHandler;
		
//		logger.logInfo("Info: Initializing ListViewFrame components...");
		initComponents();
		tableFileList.setShowHorizontalLines(false);
		tableFileList.setShowVerticalLines(false);
		toolbarOptions.setLayout(new BoxLayout(toolbarOptions, BoxLayout.X_AXIS));
		
//		logger.logInfo("Setting file list table...");
//		System.out.println("Info: Setting file list table..."); // TODO log info
		setTable();
		
//		// setting into global list
//		SystemResources.addressBarList.add(txtPathAddress);
//		SystemResources.bookmarkIndicatorList.add(btnBookmarkIndicator);
		
		/*
		// Setting Actions
		System.out.println("Info: Attaching Actions...");
		menuProperties.setAction(showPropertiesAction);
		menuBookmarkProperties.setAction(showPropertiesAction);
		*/
		
		updateTabTitleBar(fileSystemHandler.getCurrentWorkingDirectory());
	}
	
	void focusAddressBar() {
		txtPathAddress.selectAll();
		txtPathAddress.requestFocus();
	}
	
	private FileAttributes[] selectedFiles = null;
	
	private void setFileSelectionContextOptions() {
		// reset all options
		menuOpen.setVisible(true);
		menuPrint.setVisible(true);
		menuOpenUsingSystem.setVisible(true);
		menuOpenInNewTab.setVisible(true);        
		menuBookmark.setVisible(true);
		jSeparator1.setVisible(true);
		menuNew.setVisible(true);
		menuNewFile.setVisible(true);
		menuNewFolder.setVisible(true);
		jSeparator2.setVisible(true);
		menuCut.setVisible(true);
		menuCopy.setVisible(true);
		menuPaste.setVisible(SystemResources.pasteOperation != SystemResources.PasteOperation.NONE);
		menuRename.setVisible(true);
		menuDelete.setVisible(true);
        jSeparator5.setVisible(true);
		menuProperties.setVisible(true);
		
		// set appropriate options
		if(selectedFiles.length == 1) {
			if(selectedFiles[0].equals(fileSystemHandler.getCurrentWorkingDirectory())) {
				menuOpen.setVisible(false);
				menuPrint.setVisible(false);
				jSeparator2.setVisible(false);
				menuCut.setVisible(false);
				menuCopy.setVisible(false);
				menuRename.setVisible(false);
				menuDelete.setVisible(false);
			} else {
				if(selectedFiles[0].isDirectory) {
					menuOpenUsingSystem.setEnabled(fileSystemHandler.isDesktopSupported);
					menuPrint.setVisible(false);
				} else if(selectedFiles[0].isLink) {
					menuOpen.setEnabled(fileSystemHandler.isDesktopSupported);		
					menuOpenInNewTab.setVisible(false);
					menuOpenUsingSystem.setVisible(false);
					menuPrint.setVisible(false);
				} else { // for files
					menuOpen.setEnabled(fileSystemHandler.isDesktopSupported);
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
			menuPrint.setVisible(false);
            menuOpenUsingSystem.setVisible(false);
			menuOpenInNewTab.setVisible(false);
			jSeparator1.setVisible(false);
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
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupFileSelected = new javax.swing.JPopupMenu();
        menuOpen = new javax.swing.JMenuItem();
        menuPrint = new javax.swing.JMenuItem();
        menuOpenUsingSystem = new javax.swing.JMenuItem();
        menuOpenInNewTab = new javax.swing.JMenuItem();
        menuBookmark = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuNew = new javax.swing.JMenu();
        menuNewFile = new javax.swing.JMenuItem();
        menuNewFolder = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuCut = new javax.swing.JMenuItem();
        menuCopy = new javax.swing.JMenuItem();
        menuPaste = new javax.swing.JMenuItem();
        menuRename = new javax.swing.JMenuItem();
        menuDelete = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        menuProperties = new javax.swing.JMenuItem();
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
        jScrollPane1 = new javax.swing.JScrollPane();
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
        menuOpenInNewTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenInNewTabActionPerformed(evt);
            }
        });
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
        menuPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPasteActionPerformed(evt);
            }
        });
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

        setLayout(new java.awt.BorderLayout());

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

        add(toolbarOptions, java.awt.BorderLayout.NORTH);

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
        tableFileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableFileListMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableFileList);
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

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void menuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenActionPerformed
        openFile(selectedFiles[0]);
    }//GEN-LAST:event_menuOpenActionPerformed

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

    private void menuBookmarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBookmarkActionPerformed
		bookmarkHandler.addBookmarks(selectedFiles);
//		for(FileAttributes file : selectedFiles) {
//				bookmarkHandler.add(treeNodeBookmarks,
//										new BookmarkedItem(	file.name,
//																file.type,
//			//													iconRegistry.getFileIcon(file, IconSize.BIG),
//																file.absolutePath));
//			}
		if(selectedFiles[0].equals(fileSystemHandler.getCurrentWorkingDirectory()))
			btnBookmarkIndicator.setSelected(true);
    }//GEN-LAST:event_menuBookmarkActionPerformed

    private void menuNewFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewFileActionPerformed
        createNew(false);
    }//GEN-LAST:event_menuNewFileActionPerformed

    private void menuNewFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewFolderActionPerformed
        createNew(true);
    }//GEN-LAST:event_menuNewFolderActionPerformed

    private void menuCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCutActionPerformed
		SystemResources.pasteOperation = SystemResources.PasteOperation.CUT;
		SystemResources.filesToPaste = selectedFiles;
    }//GEN-LAST:event_menuCutActionPerformed

    private void menuCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCopyActionPerformed
		SystemResources.pasteOperation = SystemResources.PasteOperation.COPY;
		SystemResources.filesToPaste = selectedFiles;
    }//GEN-LAST:event_menuCopyActionPerformed

    private void menuRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRenameActionPerformed
        String type = selectedFiles[0].isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE;
        String newName = (String)JOptionPane.showInputDialog(	this,
            "Enter new "+ type +" name:",
            "Rename " + type,
            JOptionPane.QUESTION_MESSAGE,
            null, null, selectedFiles[0].name);
		if(newName != null)
			rename(selectedFiles[0], newName);
    }//GEN-LAST:event_menuRenameActionPerformed

	FileAttributes rename(final FileAttributes file, String newName) {
		FileAttributes newFile = null;
		String type = file.isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE;
		try {
			newName = newName.trim();
			if((!file.name.equals(newName)) && newName.length()>0) {
				newFile = fileSystemHandler.rename(file, newName);
				updateTableList(true);
				findAndSelectInList(newFile);
				logger.logInfo("Modified name attribute of %s: %s", type, file.absolutePath);
			}
		} catch(IOException e) {
			JOptionPane.showMessageDialog(	this,
							"Cannot rename the " + type + ": "+ file.name,
					"File operation failure",
					JOptionPane.ERROR_MESSAGE);
			logger.logSevere(e, "Failed renaming a %s: %s", type, file.absolutePath);
		}
		return newFile;
	}
	
	FileAttributes setExecuteFlag(final FileAttributes file, final boolean value) {
		FileAttributes newFile = null;
		final String type = file.isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE;
		try {
			newFile = fileSystemHandler.setExecuteFlag(file, value);
			updateTableList(true);
			findAndSelectInList(newFile);
			logger.logInfo("Modified execute permission bit of %s: %s", type, file.absolutePath);
		} catch(IOException e) {
			JOptionPane.showMessageDialog(	this,
				"Cannot change execute flag of " + type + ": " + file.name,
				"File operation failure",
				JOptionPane.ERROR_MESSAGE);
			logger.logSevere(e, "Failed changing execute flag of %s: %s", type, file.absolutePath);
		}
		return newFile;
	}
	
	FileAttributes setReadFlag(final FileAttributes file, final boolean value) {
		FileAttributes newFile = null;
		final String type = file.isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE;
		try {
			newFile = fileSystemHandler.setReadFlag(file, value);
			updateTableList(true);
			findAndSelectInList(newFile);
			logger.logInfo("Modified read permission bit of %s: %s", type, file.absolutePath);
		} catch(IOException e) {
			JOptionPane.showMessageDialog(	this,
				"Cannot change read flag of " + type + ": " + file.name,
				"File operation failure",
				JOptionPane.ERROR_MESSAGE);
			logger.logSevere(e, "Failed changing read flag of %s %s", type, file.absolutePath);
		}
		return newFile;
	}
	
	FileAttributes setWriteFlag(final FileAttributes file, final boolean value) {
		FileAttributes newFile = null;
		final String type = file.isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE;
		try {
			newFile = fileSystemHandler.setWriteFlag(file, value);
			updateTableList(true);
			findAndSelectInList(newFile);
			logger.logInfo("Modified write permission bit of %s: %s", type, file.absolutePath);
		} catch(IOException e) {
			JOptionPane.showMessageDialog(	this,
				"Cannot change write flag of " + type + ": " + file.name,
				"File operation failure",
				JOptionPane.ERROR_MESSAGE);
			logger.logSevere(e, "Failed changing write flag of %s '%s'.", type, file.absolutePath);
		}
		return newFile;
	}
	
    private void menuDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeleteActionPerformed
        StringBuilder tmpMsg = new StringBuilder("Delete ");
        if(selectedFiles.length == 1)
			tmpMsg.append(selectedFiles[0].isDirectory ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE);
        else
			tmpMsg.append(selectedFiles.length).append(" items");
        tmpMsg.append('?');

        if(JOptionPane.showConfirmDialog(this,
							tmpMsg.toString(),
							"Delete confirmation",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			java.util.List<FileAttributes> undeletedFiles = new ArrayList<>();
			boolean anyDeleted = false;
			for(FileAttributes file : selectedFiles) {
				try {
//					System.out.println("  // del: " + file);
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
				logger.logSevere(null, "Failed to delete at path='%s': %s",
					fileSystemHandler.getCurrentWorkingDirectory().absolutePath,
					undeletedFiles);
			}
        }
    }//GEN-LAST:event_menuDeleteActionPerformed

    private void menuPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPropertiesActionPerformed
        FilePropertiesForm.init(	selectedFiles,
									selectedFiles[0].type,
									this);
    }//GEN-LAST:event_menuPropertiesActionPerformed

    private void btnGoBackInHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackInHistoryActionPerformed
        FileAttributes dir = null;
		NavigationException exc = null;
		try {
			dir = fileSystemHandler.historyHandler.backward();
		} catch(NavigationException e) {
			exc = e;
		}
//        System.out.println("  // back: " + dir.absolutePath);
        if(dir == null) {
			logger.logFatal(exc, "(Logic Error) Cannot move backward in history from: %s. Reason: %s", 
					fileSystemHandler.getCurrentWorkingDirectory().absolutePath,
					exc==null? "Navigation limit reached" : exc.toString());
			btnGoBackInHistory.setEnabled(false);
//			JOptionPane.showMessageDialog(	this,
//							"Cannot move back in history: Navigation limit reached",
//							"Navigation error",
//							JOptionPane.ERROR_MESSAGE);
            return;
        }
        loadPath(dir, false);
    }//GEN-LAST:event_btnGoBackInHistoryActionPerformed

    private void btnGoForwardInHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoForwardInHistoryActionPerformed
        FileAttributes dir = null;
		NavigationException exc = null;
		try {
			dir = fileSystemHandler.historyHandler.forward();
		} catch(NavigationException e) {
			exc = e;
		}
//        System.out.println("  // fwd: " + dir.absolutePath);
        if(dir == null) {
			logger.logFatal(exc, "(Logic Error) Cannot move forward in history from: %s. Reason: %s", 
					fileSystemHandler.getCurrentWorkingDirectory().absolutePath,
					exc==null? "Navigation limit reached" : exc.toString());
			btnGoForwardInHistory.setEnabled(false);
//            JOptionPane.showMessageDialog(	this,
//                "Cannot move forward in history: Navigation limit reached",
//                "Navigation error",
//                JOptionPane.ERROR_MESSAGE);
            return;
        }
        loadPath(dir, false);
    }//GEN-LAST:event_btnGoForwardInHistoryActionPerformed

    private void btnGoToParentDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoToParentDirActionPerformed
        try {
            loadPath(fileSystemHandler.getCurrentParent(), true);
        } catch(FileNotFoundException e) {
			logger.logSevere(e, "(Logic) Cannot move to parent directory from '%s': ", 
					fileSystemHandler.getCurrentWorkingDirectory().absolutePath, e);
            JOptionPane.showMessageDialog(	this,
                "Cannot move to parent folder from '"+ 
						fileSystemHandler.getCurrentWorkingDirectory().absolutePath +"': " + 
						e.getMessage(),
                "Navigation error",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGoToParentDirActionPerformed

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

    private void btnGoToHometDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoToHometDirActionPerformed
        try {
            //			System.out.println("  // home: " + userHomeDirectoryPath);
            loadPath(fileSystemHandler.getHomeDirectory(), true);
        } catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog(	this,
                "Cannot move to user home directory: " + e,
                "Navigation error",
                JOptionPane.ERROR_MESSAGE);
            System.err.println("Err: Cannot move to user home directory: " + e);
        }
    }//GEN-LAST:event_btnGoToHometDirActionPerformed

    private void txtPathAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPathAddressActionPerformed
        loadPath(txtPathAddress.getText().trim(), true);
    }//GEN-LAST:event_txtPathAddressActionPerformed

    private void btnBookmarkIndicatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookmarkIndicatorActionPerformed
		if(btnBookmarkIndicator.isSelected()) {
			bookmarkHandler.addBookmarks(new FileAttributes[] { fileSystemHandler.getCurrentWorkingDirectory() });
		} else {
			bookmarkHandler.removeBookmark(new FileAttributes[] { fileSystemHandler.getCurrentWorkingDirectory() });
		}
    }//GEN-LAST:event_btnBookmarkIndicatorActionPerformed

    private void tableFileListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableFileListMouseReleased
        //        JTable source = (JTable)evt.getSource();		
        int row = tableFileList.rowAtPoint(evt.getPoint());
        if(row == -1) { // clicked on empty space
            if (evt.isPopupTrigger()) {
				tableFileList.clearSelection();
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
			if(SystemResources.fileExplorerForm != null)
				SystemResources.fileExplorerForm.lblActivityStatus.setText(selectedRows.length + " items selected");
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

    private void menuOpenInNewTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenInNewTabActionPerformed
        SystemResources.fileExplorerForm.addNewTab(selectedFiles[0].absolutePath);
    }//GEN-LAST:event_menuOpenInNewTabActionPerformed

    private void menuPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPasteActionPerformed
        
    }//GEN-LAST:event_menuPasteActionPerformed

	private void updateTabTitleBar(final FileAttributes dir) {
		lblTabTitle.setText("".equals(dir.name) ? dir.absolutePath : dir.name);
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
			logger.logSevere(e, "Cannot load directory: Path does not exist: %s (%s)", dirPath, e);
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
	
	class UpdateTableListWorker extends SwingWorker<Void, Void> implements PropertyChangeListener {
		private final boolean loadFresh;
		
		UpdateTableListWorker(final boolean loadFresh) {
			this.loadFresh = loadFresh;
		}
		
		
		/* Main task. Executed in background thread. */
		@Override
		public Void doInBackground() {
			try {
				FileAttributes cwd = fileSystemHandler.getCurrentWorkingDirectory();
				logger.logInfo("%s table list for path='%s'", 
						loadFresh ? "Updating" : "Restoring", cwd.absolutePath);

				if(loadFresh)
					fileList = setDirectoryFirstArrangement(fileSystemHandler.listFiles(cwd));

				tableFileList.getRowSorter().setSortKeys(null);
				setTableRows(fileList);
				txtPathAddress.setText(cwd.absolutePath);

				// update navigation button states
				btnGoBackInHistory.setEnabled(fileSystemHandler.historyHandler.canGoBackward());
				btnGoForwardInHistory.setEnabled(fileSystemHandler.historyHandler.canGoForward());
				btnGoToParentDir.setEnabled(fileSystemHandler.canGoToParent());
				btnBookmarkIndicator.setSelected(bookmarkHandler.containsBookmarkFile(cwd));
				updateTabTitleBar(cwd);
				logger.logInfo("Folder list %s for path: %s", loadFresh ? "loaded" : "restored", cwd.absolutePath);
			} catch(Exception e) {
				JOptionPane.showMessageDialog(	SystemResources.fileExplorerForm,
												String.format("Cannot update table list!\n  Path: %s\n  Reason: %s",
														fileSystemHandler.getCurrentWorkingDirectory().absolutePath, e),
												"Directory listing error",
												JOptionPane.ERROR_MESSAGE);
				logger.logSevere(e, "Cannot update table list: %s",  e);

	//			System.out.println("  // reverting...");
	//			System.out.println("  // before: cwd=" + fileSystemHandler.getCurrentWorkingDirectory() + 
	//					", history=" + fileSystemHandler.historyHandler);			
				try {
					fileSystemHandler.revertLastNavigation();
				} catch(NavigationException e1) {
					logger.logSevere(e1, "Cannot revert to last navigated path from: %s, defaulting to home directory. Reason: %s", 
							fileSystemHandler.getCurrentWorkingDirectory().absolutePath,
							e1);
				}
				try {
					fileSystemHandler.setCurrentWorkingDirectoryToHomeDirectory();
				} catch(FileNotFoundException e1) {
					JOptionPane.showMessageDialog(	SystemResources.fileExplorerForm,
													String.format("Cannot move to home directory from: %s!",
															fileSystemHandler.getCurrentWorkingDirectory().absolutePath),
													"Directory listing error",
													JOptionPane.ERROR_MESSAGE);
					logger.logFatal(e1, "Cannot move to home directory from %s: %s",
							fileSystemHandler.getCurrentWorkingDirectory().absolutePath, e1);
				}

	//			System.out.println("  // after: cwd=" + fileSystemHandler.getCurrentWorkingDirectory() + 
	//					", history=" + fileSystemHandler.historyHandler);
	//			setTableRows(lastFileListing); // populating with last successful file listing

				updateTableList(false); // load from cache

	//			System.out.println("  // done");
	//			System.out.println("Info: Reverted to last folder listing"); // TODO log info
			}
			return null;
		}

		/* Executed in event dispatch thread */
		public void done() {
//			Toolkit.getDefaultToolkit().beep();
			if(SystemResources.fileExplorerForm != null) {
				SystemResources.fileExplorerForm.lblItemsSelected.setText(String.valueOf(fileList.length));
				SystemResources.fileExplorerForm.lblActivityStatus.setText(fileList.length + " items selected");
				SystemResources.fileExplorerForm.panelFolderListLoad.setVisible(false);
			}	
		}
		
		private boolean panelNotSetVisible = true;
		/* Invoked when task's progress property changes. */
		public void propertyChange(PropertyChangeEvent evt) {
			if(panelNotSetVisible) { // eliminates irrelevant property setting 
				if ("progress".equals(evt.getPropertyName())) {
					if(SystemResources.fileExplorerForm != null) {
						SystemResources.fileExplorerForm.panelFolderListLoad.setVisible(true);
						panelNotSetVisible = false;
					}
				}
			}
		}
	}
	
	UpdateTableListWorker currentUpdateTableListWorker = null;

	FileAttributes[] fileList = null;
	
	/** Updates with currentWorkingDirectory */
	private void updateTableList(final boolean loadFresh) {
		if(SystemResources.fileExplorerForm != null)
			SystemResources.fileExplorerForm.panelFolderListLoad.setVisible(true);
		
		currentUpdateTableListWorker = new UpdateTableListWorker(loadFresh);
		currentUpdateTableListWorker.execute();		
	}

	/* Only change the currentWorkingDirectory */
	private void navigateTo(final FileAttributes dir, final boolean registerInHistory) {
		System.out.println("Info: Navigating to " + dir.absolutePath);  // TODO log info
		try {
			fileSystemHandler.navigateTo(dir, registerInHistory);			
//			lastVisitedPath = dir.absolutePath;
			logger.logInfo("Navigated to folder: %s", dir.absolutePath);
		} catch(FileNotFoundException|InvalidPathException e) {
			logger.logSevere(e, "Cannot navigate to path %s. Reason: %s", dir.absolutePath, e);
			JOptionPane.showMessageDialog(	this,
											String.format("Cannot navigate to path!\n  Path: %s\n  Reason: %s", 
													dir.absolutePath, e),
											"Navigation error",
											JOptionPane.ERROR_MESSAGE);
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

	void removeCurrentDirectoryIfBookmarked(final DefaultMutableTreeNode selectedBookmarkNode) {
		if(btnBookmarkIndicator.isSelected()) {
            if(((BookmarkedItem)selectedBookmarkNode.getUserObject()).absolutePath
                .equals(fileSystemHandler.getCurrentWorkingDirectory().absolutePath))
            removeCurrentDirectoryBookmark();
        }
	}

	void removeCurrentDirectoryBookmark() {
		btnBookmarkIndicator.setSelected(false);
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
	
	private void findAndSelectInList(final FileAttributes file) {
		for(int row=0; row<tableModel.getRowCount(); row++) {
			if(file.equals(tableModel.getValueAt(row, FILE_OBJECT_COLUMN_INDEX))) {
				tableFileList.changeSelection(row, 0, false, false);
				return;
			}	
		}
		JOptionPane.showMessageDialog(	this,
					"Cannot locate " + (file.isDirectory ? "folder" : "file") + " in its parent folder!",
					"Cannot locate file",
					JOptionPane.ERROR_MESSAGE);
		logger.logSevere(null, "Cannot locate %s in its parent directory: %s", 
				file.isDirectory ? "directory" : "file", file.absolutePath);
	}
	
	private void bookmarkCurrentWorkingDirectory() {
//		FileAttributes cwd = fileSystemHandler.getCurrentWorkingDirectory(); // TODO complete
//		bookmarkHandler.add(treeNodeBookmarks,
//							new BookmarkedItem(	cwd.name, 
//												cwd.type,
////												BookmarkedItem.ItemType.PATH, 
////												iconRegistry.getFileIcon(cwd, IconSize.BIG), 
//												cwd.absolutePath));
//		if(!btnBookmarkIndicator.isSelected())
//			btnBookmarkIndicator.setSelected(true);
	}
	
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
	
	void openBookmarkNode(final DefaultMutableTreeNode node, final boolean openLocation) {
//		System.out.printf("  // open bookmark: %s, openLocation=%b\n", 
//				((BookmarkedItem)node.getUserObject()).absolutePath, openLocation);
		BookmarkedItem item = (BookmarkedItem)node.getUserObject();
		FileAttributes file = null, targetFile = null;
		try {
			file = fileSystemHandler.getFileAttributes(item.absolutePath);
			targetFile = openLocation ? fileSystemHandler.getParent(file) : file;
			openFile(targetFile);
            if(openLocation)
				findAndSelectInList(file);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(	this,
                "Cannot retieve bookmarked item: " + item.absolutePath + "\nReason: " + e,
                "File operation failure",
                JOptionPane.ERROR_MESSAGE);
            logger.logSevere(e, "Cannot open %s bookmarked item %s. Reason: %s", 
					openLocation ? "location of" : "", e);
					
        }
	}
	
	public static ListViewForm init(final JLabel lblTabTitle, final FileSystemHandler fileSystemHandler, final BookmarkHandler bookmarkHandler) {
		/* Create and display the form */		
		return new ListViewForm(lblTabTitle, fileSystemHandler, bookmarkHandler);
	}
	
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnBookmarkIndicator;
    private javax.swing.JButton btnGoBackInHistory;
    private javax.swing.JButton btnGoForwardInHistory;
    private javax.swing.JButton btnGoToHometDir;
    private javax.swing.JButton btnGoToParentDir;
    private javax.swing.JButton btnReloadPath;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JLabel lblAddressIcon;
    private javax.swing.JLabel lblSearchIcon;
    private javax.swing.JMenuItem menuBookmark;
    private javax.swing.JMenuItem menuCopy;
    private javax.swing.JMenuItem menuCut;
    private javax.swing.JMenuItem menuDelete;
    private javax.swing.JMenu menuNew;
    private javax.swing.JMenuItem menuNewFile;
    private javax.swing.JMenuItem menuNewFolder;
    private javax.swing.JMenuItem menuOpen;
    private javax.swing.JMenuItem menuOpenInNewTab;
    private javax.swing.JMenuItem menuOpenUsingSystem;
    private javax.swing.JMenuItem menuPaste;
    private javax.swing.JMenuItem menuPrint;
    private javax.swing.JMenuItem menuProperties;
    private javax.swing.JMenuItem menuRename;
    private javax.swing.JPopupMenu popupFileSelected;
    private javax.swing.JTable tableFileList;
    private javax.swing.JToolBar toolbarOptions;
    private javax.swing.JTextField txtPathAddress;
    private javax.swing.JTextField txtSearchFiles;
    // End of variables declaration//GEN-END:variables
}
