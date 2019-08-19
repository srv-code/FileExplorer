package fileexplorer.gui.forms;

import fileexplorer.handlers.fs.FileAttributes;
import fileexplorer.handlers.shared.ActivityLogger;
import fileexplorer.handlers.shared.BookmarkedItem;
import fileexplorer.handlers.shared.SystemResources.IconRegistry;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;


public class PropertiesForm extends javax.swing.JFrame {
	private static final ActivityLogger logger = ActivityLogger.getInstance();
	private FileAttributes[] files;
	private final ListViewPanel listViewPanel;
	private final IconRegistry iconRegistry = IconRegistry.getInstance();
	private final boolean bookmarkOperation;
	private final DefaultMutableTreeNode bookmarkedNode;
	private final BookmarkedItem bookmarkedItem;
	
	/**
	 * Creates new form PropertiesForm for remote servers.
	 */
	public PropertiesForm(final DefaultMutableTreeNode bookmarkedNode, final ListViewPanel listViewPanel) {
		this.bookmarkOperation = true;
		this.bookmarkedNode = bookmarkedNode;
		this.bookmarkedItem = (BookmarkedItem)bookmarkedNode.getUserObject();
		this.listViewPanel = listViewPanel;
		this.files = null;
		
		
		// change button's text and functionality
		if(BookmarkedItem.TYPE_REMOTE_SERVER.equals(bookmarkedItem.type)) {
			btnDynamicAction.setText("Connect...");
			btnDynamicAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					dispose();
					RemoteLoginForm.init(bookmarkedItem.absolutePath);
				}
			});
		} else {
			btnDynamicAction.setText("Apply");
			btnDynamicAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					saveValuesIfModified();
				}
			});
		}
		
		txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
				renameIfModified(bookmarkedNode, txtName.getName().trim());
            }
        });
		
		// hide unnecessary labels and textboxes
		lblSize.setVisible(false);
		txtSize.setVisible(false);
		lblLastModified.setVisible(false);
		txtLastModified.setVisible(false);
		lblPermissions.setVisible(false);
		chkReadable.setVisible(false);
		chkWritable.setVisible(false);
		chkExecutable.setVisible(false);
		chkHidden.setVisible(false);
	}
	
	/**
	 * Creates new form PropertiesForm for local bookmarked files and folders.
	 * @param type If multiples files are specified then this parameter has no 
	 *		significance as a constant icon will be used.
	 */
	public PropertiesForm(final FileAttributes[] files, final String type, final ListViewPanel listViewForm) {
		this.bookmarkOperation = false;
		this.bookmarkedNode = null;
		this.bookmarkedItem = null;
		this.files = files;
		this.listViewPanel = listViewForm;
		initComponents();
		btnDynamicAction.setText("Apply");
		btnDynamicAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveValuesIfModified();
            }
        });
		
		txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {                
				if(files.length == 1)
					renameIfModified(files[0], txtName.getName().trim());
            }
        });
		
		boolean canEdit = listViewForm!=null;
		txtName.setEditable(canEdit && files.length==1);
		chkExecutable.setEnabled(canEdit);
		chkReadable.setEnabled(canEdit);
		chkWritable.setEnabled(canEdit);
		
		// set values to GUI components
		if(files.length == 1) {
			lblTypeIcon.setIcon(iconRegistry.getTypeIcon(type));
			txtName.setText(BookmarkedItem.TYPE_SYSTEM_DRIVE.equals(type) ? files[0].absolutePath : files[0].name);
				txtName.setCaretPosition(0);
			txtType.setText(type);
				txtType.setCaretPosition(0);
			txtSize.setText(files[0].sizeInWords + " (" + files[0].size + " B)"); 
				txtSize.setCaretPosition(0);
			txtLastModified.setText(files[0].lastModifiedDateString); 
				txtLastModified.setCaretPosition(0);
			chkReadable.setSelected(files[0].isReadable);
			chkWritable.setSelected(files[0].isWritable);
			chkExecutable.setSelected(files[0].isExecutable);
			chkHidden.setSelected(files[0].isHidden);
		} else { // ignore type
			lblTypeIcon.setIcon(iconRegistry.multipleFilesIcon_big);
			
			StringBuilder names = new StringBuilder();
			List<String> types = new ArrayList<>();
			long totalSize = 0L;
			long minLastModified = files[0].lastModified, maxLastModified = files[0].lastModified;
			for(FileAttributes file : files) {
				names.append(file.name).append("; ");
				if(!types.contains(file.type))
					types.add(file.type);
				totalSize += file.size;
				minLastModified = Math.min(file.lastModified, minLastModified);
				maxLastModified = Math.max(file.lastModified, maxLastModified);
			}
			
			txtName.setText(names.toString()); 
				txtName.setCaretPosition(0);
			StringBuilder tmpTypes = new StringBuilder();
			for(String ftype : types)
				tmpTypes.append(ftype).append("; ");
			txtType.setText(tmpTypes.toString());
				txtType.setCaretPosition(0);
			txtSize.setText(FileAttributes.getSizeInWords(totalSize) + " (" + totalSize + " B)"); 
				txtSize.setCaretPosition(0);
			txtLastModified.setText(	FileAttributes.getlastModifiedDateString(minLastModified) + 
										" - " + 
										FileAttributes.getlastModifiedDateString(maxLastModified)); 
			txtLastModified.setCaretPosition(0);
		}
		
		// global values
		txtPath.setText(BookmarkedItem.TYPE_SYSTEM_DRIVE.equals(type) ? "root" : files[0].absolutePath); 
			txtPath.setCaretPosition(0);
		txtLocation.setText(files[0].isLocal ? "Local" : "Remote"); 
			txtLocation.setCaretPosition(0);
 	}

	private void saveValuesIfModified() {
		if(bookmarkOperation) {
			renameIfModified(bookmarkedNode, txtName.getText().trim());
		} else {
			if(files.length == 1)
				renameIfModified(files[0], txtName.getName().trim());
		
			FileAttributes tmpFile;
			for(int i=0; i<files.length; i++) {			
				if(files[i].isExecutable!=chkExecutable.isSelected()) {
					tmpFile = listViewPanel.setExecuteFlag(files[i], chkExecutable.isSelected());
					if(tmpFile == null)
						chkExecutable.setSelected(!chkExecutable.isSelected()); // reverse the current selection status
					else	
						files[i] = tmpFile;
				}

				if(files[i].isReadable!=chkReadable.isSelected()) {
					tmpFile = listViewPanel.setReadFlag(files[i], chkReadable.isSelected());
					if(tmpFile == null)
						chkReadable.setSelected(!chkReadable.isSelected()); // reverse the current selection status
					else
						files[i] = tmpFile;
				}

				if(files[i].isWritable!=chkWritable.isSelected()) {
					tmpFile = listViewPanel.setWriteFlag(files[i], chkWritable.isSelected());
					if(tmpFile == null)
						chkWritable.setSelected(!chkWritable.isSelected()); // reverse the current selection status
					else
						files[i] = tmpFile;
				}
			}
		}
	}
	
	/** For files */
	private void renameIfModified(final FileAttributes file, final String newName) {
		if(newName.length()==0) {
			JOptionPane.showMessageDialog(	this,
									"Please enter a non-blank name!",
									"File renaming failure",
									JOptionPane.ERROR_MESSAGE);
			logger.logSevere(null, "Failed renaming a %s: %s", files[0].type, files[0].absolutePath);
			return;
		}
		
		if(!file.name.equals(newName)) {
			FileAttributes tmpFile = listViewPanel.renameFile(file, newName);
			if(tmpFile == null)
				txtName.setName(file.name);
			else
				files[0] = tmpFile; // accesses global member
		}
	}
	
	/** For bookmarked items */
	private void renameIfModified(final DefaultMutableTreeNode node, final String newName) {
		if(newName.length()==0) {
			JOptionPane.showMessageDialog(	this,
									"Please enter a non-blank name!",
									"Bookmark renaming failure",
									JOptionPane.ERROR_MESSAGE);
			logger.logSevere(null, "Blank input provided for renaming a bookmared item.");
			return;
		}
		
		BookmarkedItem item = (BookmarkedItem)node.getUserObject();
		if(!item.name.equals(newName)) {
			listViewPanel.renameBookmarkedNode(node, newName);
		}
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTypeIcon = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblPath = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        lblLocation = new javax.swing.JLabel();
        lblSize = new javax.swing.JLabel();
        lblLastModified = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblPermissions = new javax.swing.JLabel();
        chkReadable = new javax.swing.JCheckBox();
        chkWritable = new javax.swing.JCheckBox();
        chkExecutable = new javax.swing.JCheckBox();
        chkHidden = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnDynamicAction = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        txtType = new javax.swing.JTextField();
        txtPath = new javax.swing.JTextField();
        txtLocation = new javax.swing.JTextField();
        txtSize = new javax.swing.JTextField();
        txtLastModified = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Properties");
        setAlwaysOnTop(true);
        setResizable(false);

        lblName.setText("Name:");

        lblPath.setText("Path:");

        lblType.setText("Type:");

        lblLocation.setText("Location:");

        lblSize.setText("Size:");

        lblLastModified.setText("Last modified:");

        lblPermissions.setText("Permissions:");

        chkReadable.setText("Readable");

        chkWritable.setText("Writable");

        chkExecutable.setText("Executable");

        chkHidden.setText("Hidden");
        chkHidden.setEnabled(false);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnDynamicAction.setText("???");
        btnDynamicAction.setMaximumSize(new java.awt.Dimension(67, 28));
        btnDynamicAction.setMinimumSize(new java.awt.Dimension(67, 28));
        btnDynamicAction.setPreferredSize(new java.awt.Dimension(67, 28));

        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(67, 28));
        btnOK.setMinimumSize(new java.awt.Dimension(67, 28));
        btnOK.setPreferredSize(new java.awt.Dimension(67, 28));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        txtType.setEditable(false);

        txtPath.setEditable(false);

        txtLocation.setEditable(false);

        txtSize.setEditable(false);

        txtLastModified.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTypeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblName)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtName))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtType, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPath)
                            .addComponent(lblLocation)
                            .addComponent(lblSize))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPath, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLocation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDynamicAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblPermissions)
                                .addGap(18, 18, 18)
                                .addComponent(chkReadable)
                                .addGap(18, 18, 18)
                                .addComponent(chkWritable)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(chkExecutable)
                                .addGap(18, 18, 18)
                                .addComponent(chkHidden)
                                .addGap(2, 2, 2))
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblLastModified)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                .addComponent(txtLastModified, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lblName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTypeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPath)
                    .addComponent(txtPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblType)
                    .addComponent(txtType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLocation)
                    .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSize)
                    .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLastModified)
                    .addComponent(txtLastModified, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPermissions)
                    .addComponent(chkReadable)
                    .addComponent(chkWritable)
                    .addComponent(chkExecutable)
                    .addComponent(chkHidden))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDynamicAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );

        lblTypeIcon.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
		saveValuesIfModified();
		dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
	
//	private void setExecuteFlag() {
//		for(FileAttributes file : files)
//			listViewForm.setExecuteFlag(file, chkReadable.isSelected());
//	}
//	
//	private void setReadFlag() {
//		listViewForm.setReadFlag(files, chkReadable.isSelected());
//	}
//	
//	private void setWriteFlag() {
//		listViewForm.setWriteFlag(files, chkReadable.isSelected());
//	}
//	
//	private void renameFile() {
//		if(txtName.isEditable()) {
//			listViewForm.renameFile(files[0], txtName.getText());
//		}
//	}
	
	
//	static PropertiesForm init(final FileAttributes[] files, final BookmarkedItem.ItemType type, final FileSystemHandler fsHandler) {
//		return init(files, type.toString(), fsHandler);
//	}
	
	/**
	 * Specifically for files/folders (supports multiple items)
	 */
	static void init(final FileAttributes[] files, final String type, final ListViewPanel listViewPanel) {
		/* Create and display the form */
		logger.logInfo("Initializing PropertiesForm...");
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
//				System.out.println("files=" + files + ", icon=" + icon + ", type=" + type + ", fsHandler=" + fsHandler);
				 new PropertiesForm(files, type, listViewPanel).setVisible(true);
			}
		});
	}
	
	/**
	 * Specifically for bookmarked items, i.e. for local files/folders 
	 * or remote servers (supports only singular operation)
	 */
	static void init(final DefaultMutableTreeNode bookmarkNode, final ListViewPanel listViewPanel) {
		/* Create and display the form */
		logger.logInfo("Initializing PropertiesForm...");
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				 new PropertiesForm(bookmarkNode, listViewPanel).setVisible(true);
			}
		});
	}
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDynamicAction;
    private javax.swing.JButton btnOK;
    private javax.swing.JCheckBox chkExecutable;
    private javax.swing.JCheckBox chkHidden;
    private javax.swing.JCheckBox chkReadable;
    private javax.swing.JCheckBox chkWritable;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblLastModified;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPath;
    private javax.swing.JLabel lblPermissions;
    private javax.swing.JLabel lblSize;
    private javax.swing.JLabel lblType;
    private javax.swing.JLabel lblTypeIcon;
    private javax.swing.JTextField txtLastModified;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPath;
    private javax.swing.JTextField txtSize;
    private javax.swing.JTextField txtType;
    // End of variables declaration//GEN-END:variables
}
