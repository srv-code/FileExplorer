/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer.gui.forms;

import fileexplorer.handlers.shared.ActivityLogger;
import fileexplorer.handlers.shared.AppPreferences;
import fileexplorer.handlers.shared.SystemResources;
import javax.swing.JOptionPane;

/**
 *
 * @author soura
 */
public class PreferencesForm extends javax.swing.JFrame {
	private final ActivityLogger logger = ActivityLogger.getInstance();
	/**
	 * Creates new form PreferencesForm
	 */
	public PreferencesForm() {
		initComponents();
		syncUIElementsWithPrefValues();
	}
	
	private void syncUIElementsWithPrefValues() {
		chkConfirmBeforeExit.setSelected(SystemResources.prefs.confirmBeforeExit);
		
//		System.out.printf("  // SystemResources.prefs.themeClassName=%s, SystemResources.prefs.language=%s\n",
//				SystemResources.prefs.themeClassName, SystemResources.prefs.language);
		
		switch(SystemResources.prefs.themeClassName) {
			case AppPreferences.THEME_MACOSX:			comboThemeClass.setSelectedItem("Mac OS X"); break;
			case AppPreferences.THEME_METAL:			comboThemeClass.setSelectedItem("Metal"); break;
			case AppPreferences.THEME_MOTIF:			comboThemeClass.setSelectedItem("Motif/CDE"); break;
			case AppPreferences.THEME_WINDOWS:			comboThemeClass.setSelectedItem("Windows"); break;
			case AppPreferences.THEME_WINDOWSCLASSIC:	comboThemeClass.setSelectedItem("Windows Classic"); break;
			case AppPreferences.THEME_UBUNTU:			comboThemeClass.setSelectedItem("Ubuntu"); break;
			default: 
				JOptionPane.showMessageDialog(	this,
											"Corrupted preference data:\n  key=" + 
											AppPreferences.KEY_THEME_CLASS_NAME + "\n  value=" + 
											SystemResources.prefs.themeClassName,
											"Corrupted preference data",
											JOptionPane.WARNING_MESSAGE);
				logger.logSevere(null, "Corrupted preference data: key=%s, value=%s", 
						AppPreferences.KEY_THEME_CLASS_NAME, 
						SystemResources.prefs.themeClassName);
		}
		
		switch(SystemResources.prefs.language) {
			case AppPreferences.LANG_ENGLISH:	comboLanguage.setSelectedItem("English (US)"); break;
			case AppPreferences.LANG_FRENCH:	comboLanguage.setSelectedItem("French"); break;
			case AppPreferences.LANG_GERMAN:	comboLanguage.setSelectedItem("German"); break;			
			default: 
				JOptionPane.showMessageDialog(	this,
											"Corrupted preference data\n  key=" + 
											AppPreferences.KEY_LANGUAGE + "\n  value=" + 
											SystemResources.prefs.language,
											"Corrupted preference data",
											JOptionPane.WARNING_MESSAGE);
				logger.logSevere(null, "Corrupted preference data: key=%s, value=%s", 
						AppPreferences.KEY_LANGUAGE, 
						SystemResources.prefs.language);
		}
	}
	
	public static void init() {
		/* Create and display the form */        
        SystemResources.formFileExplorer.formPreferences = new PreferencesForm();
		SystemResources.formFileExplorer.formPreferences.setVisible(true);
		SystemResources.formFileExplorer.setEnabled(false);
    }

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        appTitleLabel = new javax.swing.JLabel();
        chkConfirmBeforeExit = new javax.swing.JCheckBox();
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboThemeClass = new javax.swing.JComboBox<>();
        comboLanguage = new javax.swing.JComboBox<>();
        btnReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/prefs_big.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 100));
        jLabel1.setMinimumSize(new java.awt.Dimension(100, 100));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 100));

        appTitleLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        appTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("lang/strings"); // NOI18N
        appTitleLabel.setText(bundle.getString("AboutForm.appTitleLabel.text")); // NOI18N

        chkConfirmBeforeExit.setText("Confirm before exit");

        btnApply.setText("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(57, 28));
        btnCancel.setMinimumSize(new java.awt.Dimension(57, 28));
        btnCancel.setPreferredSize(new java.awt.Dimension(57, 28));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        jLabel2.setText("Theme:");

        jLabel3.setText("Language:");

        comboThemeClass.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mac OS X", "Metal", "Motif/CDE", "Windows", "Windows Classic", "Ubuntu" }));

        comboLanguage.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "English (US)", "French", "German" }));

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(appTitleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(23, 23, 23)
                                .addComponent(comboThemeClass, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chkConfirmBeforeExit)
                                .addGap(99, 99, 99)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 158, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnApply)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOK)
                            .addComponent(btnReset)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(appTitleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(comboThemeClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(comboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkConfirmBeforeExit)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        SystemResources.formFileExplorer.formPreferences = null;
        SystemResources.formFileExplorer.setEnabled(true);
		SystemResources.formFileExplorer.requestFocus();
    }//GEN-LAST:event_formWindowClosed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
		savePreferences();
		dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
        savePreferences();
    }//GEN-LAST:event_btnApplyActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        if(JOptionPane.showConfirmDialog(this,
					"Sure to reset all application preferences?",
					"Reset preferences",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					SystemResources.prefs.resetAll();
		}
    }//GEN-LAST:event_btnResetActionPerformed

	private void savePreferences() {		
		if(SystemResources.prefs.confirmBeforeExit != chkConfirmBeforeExit.isSelected()) {
			logger.logConfig("Changing preference: key='%s', old value='%b', new value='%b' ...", 
					AppPreferences.KEY_CONFIRM_BEFORE_EXIT, 
					SystemResources.prefs.confirmBeforeExit,
					chkConfirmBeforeExit.isSelected());
			SystemResources.prefs.confirmBeforeExit = chkConfirmBeforeExit.isSelected();
		}
		
		String mappedSelectedValue = null;
		switch(comboThemeClass.getSelectedItem().toString()) {
			case "Mac OS X":		mappedSelectedValue = AppPreferences.THEME_MACOSX; break;
			case "Metal":			mappedSelectedValue = AppPreferences.THEME_METAL; break;
			case "Motif/CDE":		mappedSelectedValue = AppPreferences.THEME_MOTIF; break;
			case "Windows":			mappedSelectedValue = AppPreferences.THEME_WINDOWS; break;
			case "Windows Classic":	mappedSelectedValue = AppPreferences.THEME_WINDOWSCLASSIC; break;
			case "Ubuntu":			mappedSelectedValue = AppPreferences.THEME_UBUNTU; break;
			default: 
				throw new AssertionError("Should not get here: Unhandled value for theme class name: " + 
						comboThemeClass.getSelectedItem());
		}
		if(!SystemResources.prefs.themeClassName.equals(mappedSelectedValue)) {
			logger.logConfig("Changing preference: key='%s', old value='%s', new value='%s' ...",
					AppPreferences.KEY_THEME_CLASS_NAME,
					SystemResources.prefs.themeClassName,
					mappedSelectedValue);
			SystemResources.prefs.themeClassName = mappedSelectedValue;
			JOptionPane.showMessageDialog(	this,
											"Changes to application theme will be effective from next startup.",
											"Theme preference saved",
											JOptionPane.INFORMATION_MESSAGE);
		}
		
		mappedSelectedValue = null;
		switch(comboLanguage.getSelectedItem().toString()) {
			case "English (US)":	mappedSelectedValue = AppPreferences.LANG_ENGLISH; break;
			case "French":			mappedSelectedValue = AppPreferences.LANG_FRENCH; break;
			case "German":			mappedSelectedValue = AppPreferences.LANG_GERMAN; break;
			default: 
				throw new AssertionError("Should not get here: Unhandled value for app language: " +
						comboLanguage.getSelectedItem());
		}
		if(!SystemResources.prefs.language.equals(mappedSelectedValue)) {
			logger.logConfig("Changing preference: key='%s', old value='%s', new value='%s' ...", 
					AppPreferences.KEY_LANGUAGE,
					SystemResources.prefs.language,
					mappedSelectedValue);
			SystemResources.prefs.language = mappedSelectedValue;
			JOptionPane.showMessageDialog(	this,
											"Changes to application language will be effective from next startup.",
											"Language preference saved",
											JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel appTitleLabel;
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnReset;
    private javax.swing.JCheckBox chkConfirmBeforeExit;
    private javax.swing.JComboBox<String> comboLanguage;
    private javax.swing.JComboBox<String> comboThemeClass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
