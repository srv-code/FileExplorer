/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer.gui.forms;

//import gui.mytests.SystemResources.IconRegistry;
//import gui.mytests.handlers.BookmarkedItem;
//import gui.mytests.handlers.fs.FileAttributes;
//import gui.mytests.handlers.fs.FileSystemHandler;
//import gui.mytests.handlers.fs.LocalFileSystemHandler;
import fileexplorer.handlers.fs.FileAttributes;
import fileexplorer.handlers.fs.FileSystemHandler;
import fileexplorer.handlers.shared.BookmarkedItem;
import fileexplorer.handlers.shared.SystemResources;
import fileexplorer.handlers.shared.SystemResources.IconRegistry;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class FilePropertiesForm extends javax.swing.JFrame {

	private FileAttributes[] files;
	private final ListViewPanel listViewForm;
	private final IconRegistry iconRegistry = IconRegistry.getInstance();
	
	/**
	 * Creates new form PropertiesForm
	 * @param type If multiples files are specified then this parameter has no 
	 *		significance as a constant icon will be used.
	 */
	public FilePropertiesForm(final FileAttributes[] files, final String type, final ListViewPanel listViewForm) {
		this.files = files;
		this.listViewForm = listViewForm;
		initComponents();
		
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
		renameIfModified();
		
		FileAttributes tmpFile;
		for(int i=0; i<files.length; i++) {			
			if(files[i].isExecutable!=chkExecutable.isSelected()) {
				tmpFile = listViewForm.setExecuteFlag(files[i], chkExecutable.isSelected());
				if(tmpFile == null)
					chkExecutable.setSelected(!chkExecutable.isSelected()); // reverse the current selection status
				else	
					files[i] = tmpFile;
			}
			
			if(files[i].isReadable!=chkReadable.isSelected()) {
				tmpFile = listViewForm.setReadFlag(files[i], chkReadable.isSelected());
				if(tmpFile == null)
					chkReadable.setSelected(!chkReadable.isSelected()); // reverse the current selection status
				else
					files[i] = tmpFile;
			}
			
			if(files[i].isWritable!=chkWritable.isSelected()) {
				tmpFile = listViewForm.setWriteFlag(files[i], chkWritable.isSelected());
				if(tmpFile == null)
					chkWritable.setSelected(!chkWritable.isSelected()); // reverse the current selection status
				else
					files[i] = tmpFile;
			}
		}
	}
	
	private void renameIfModified() {
		if(files.length==1 && !files[0].name.equals(txtName.getText())) {
			FileAttributes tmpFile = listViewForm.rename(files[0], txtName.getText());
			if(tmpFile == null)
				txtName.setName(files[0].name);
			else
				files[0] = tmpFile;
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
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        chkReadable = new javax.swing.JCheckBox();
        chkWritable = new javax.swing.JCheckBox();
        chkExecutable = new javax.swing.JCheckBox();
        chkHidden = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
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

        jLabel1.setText("Name:");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        jLabel2.setText("Path:");

        jLabel3.setText("Type:");

        jLabel4.setText("Location:");

        jLabel5.setText("Size:");

        jLabel6.setText("Last modified:");

        jLabel7.setText("Permissions:");

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

        btnApply.setText("Apply");
        btnApply.setMaximumSize(new java.awt.Dimension(67, 28));
        btnApply.setMinimumSize(new java.awt.Dimension(67, 28));
        btnApply.setPreferredSize(new java.awt.Dimension(67, 28));
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

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
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtName))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtType, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
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
                        .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel7)
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
                                .addComponent(jLabel6)
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
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTypeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtLastModified, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(chkReadable)
                    .addComponent(chkWritable)
                    .addComponent(chkExecutable)
                    .addComponent(chkHidden))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
		saveValuesIfModified();
    }//GEN-LAST:event_btnApplyActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        renameIfModified();
    }//GEN-LAST:event_txtNameActionPerformed

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
//	private void rename() {
//		if(txtName.isEditable()) {
//			listViewForm.rename(files[0], txtName.getText());
//		}
//	}
	
	
//	static FilePropertiesForm init(final FileAttributes[] files, final BookmarkedItem.ItemType type, final FileSystemHandler fsHandler) {
//		return init(files, type.toString(), fsHandler);
//	}
	
	static void init(final FileAttributes[] files, final String type, final ListViewPanel listViewForm) {
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
//				System.out.println("files=" + files + ", icon=" + icon + ", type=" + type + ", fsHandler=" + fsHandler);
				 new FilePropertiesForm(files, type, listViewForm).setVisible(true);
			}
		});
	}
	
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JCheckBox chkExecutable;
    private javax.swing.JCheckBox chkHidden;
    private javax.swing.JCheckBox chkReadable;
    private javax.swing.JCheckBox chkWritable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblTypeIcon;
    private javax.swing.JTextField txtLastModified;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPath;
    private javax.swing.JTextField txtSize;
    private javax.swing.JTextField txtType;
    // End of variables declaration//GEN-END:variables
}
