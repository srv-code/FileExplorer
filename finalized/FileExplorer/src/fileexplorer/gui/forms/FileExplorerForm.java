package fileexplorer.gui.forms;

import fileexplorer.gui.forms.AboutForm;
import fileexplorer.handlers.shared.SystemHandler;
import javax.swing.*;

import fileexplorer.handlers.shared.*;


public class FileExplorerForm extends javax.swing.JFrame {
	ActivityLogger logger = SystemResources.getActivityLogger();
	
    /**
     * Creates new form FileExplorer
     */
    public FileExplorerForm() {
        initComponents();
		setIconImage(new ImageIcon(getClass().getResource("/images/app_icon.png")).getImage());
		setVisible(true);
    }
	
	private long errorCount = 0L;
	
	private void reportError(final Exception e) {
		errorCountLabel.setText(String.valueOf(++errorCount));		
		logger.logSevere("[errorCount=%d] %s", errorCount, logger.getStackTrace(e));
	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewLayoutStyleButtonGroup = new javax.swing.ButtonGroup();
        appLanguageButtonGroup = new javax.swing.ButtonGroup();
        appThemeButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        itemsSelectedLabel = new javax.swing.JLabel();
        activityStatusLabel = new javax.swing.JLabel();
        errorCountLabel = new javax.swing.JLabel();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        createMenu = new javax.swing.JMenu();
        createNewFolderMenuItem = new javax.swing.JMenuItem();
        createNewFileMenuItem = new javax.swing.JMenuItem();
        tabMenu = new javax.swing.JMenu();
        newTabMenuItem = new javax.swing.JMenuItem();
        closeCurrentTabMenuItem = new javax.swing.JMenuItem();
        closeAllButCurrentTabMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        searchMenuItem = new javax.swing.JMenuItem();
        editAddressBarMenuItem = new javax.swing.JMenuItem();
        bookmarkCurrentFolderMenuItem = new javax.swing.JMenuItem();
        goToParentFolderMenuItem = new javax.swing.JMenuItem();
        reloadCurrentFolderMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        viewDiagnosticsMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        themeMenu = new javax.swing.JMenu();
        themeMetalRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        themeNimbuslRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        themeMotifCDERadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        themeWindowsRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        themeWindowsClassicRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        viewLayoutStyleMenu = new javax.swing.JMenu();
        listLayoutRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        detailsLayoutRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        iconLayoutRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        treeLayoutRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        viewLanguageMenu = new javax.swing.JMenu();
        langEnglishUSRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        langFrenchRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        langGermanRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        viewPreferencesMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("lang/strings"); // NOI18N
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

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new java.awt.BorderLayout());

        itemsSelectedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/item_count.png"))); // NOI18N
        itemsSelectedLabel.setText("0"); // NOI18N
        itemsSelectedLabel.setToolTipText(bundle.getString("FileExplorerForm.itemsSelectedLabel.toolTipText")); // NOI18N
        itemsSelectedLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(itemsSelectedLabel, java.awt.BorderLayout.LINE_START);

        activityStatusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/activity_status.png"))); // NOI18N
        activityStatusLabel.setText("System not started yet"); // NOI18N
        activityStatusLabel.setToolTipText(bundle.getString("FileExplorerForm.activityStatusLabel.toolTipText")); // NOI18N
        activityStatusLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(activityStatusLabel, java.awt.BorderLayout.CENTER);

        errorCountLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/errro_count.png"))); // NOI18N
        errorCountLabel.setText("0"); // NOI18N
        errorCountLabel.setToolTipText(bundle.getString("FileExplorerForm.errorCountLabel.toolTipText")); // NOI18N
        errorCountLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(errorCountLabel, java.awt.BorderLayout.LINE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        fileMenu.setText(bundle.getString("FileExplorerForm.fileMenu.text")); // NOI18N

        createMenu.setText(bundle.getString("FileExplorerForm.createMenu.text")); // NOI18N

        createNewFolderMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        createNewFolderMenuItem.setText(bundle.getString("FileExplorerForm.createNewFolderMenuItem.text")); // NOI18N
        createMenu.add(createNewFolderMenuItem);

        createNewFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        createNewFileMenuItem.setText(bundle.getString("FileExplorerForm.createNewFileMenuItem.text")); // NOI18N
        createMenu.add(createNewFileMenuItem);

        fileMenu.add(createMenu);

        tabMenu.setText(bundle.getString("FileExplorerForm.tabMenu.text")); // NOI18N

        newTabMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        newTabMenuItem.setText(bundle.getString("FileExplorerForm.newTabMenuItem.text")); // NOI18N
        tabMenu.add(newTabMenuItem);

        closeCurrentTabMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        closeCurrentTabMenuItem.setText(bundle.getString("FileExplorerForm.closeCurrentTabMenuItem.text")); // NOI18N
        tabMenu.add(closeCurrentTabMenuItem);

        closeAllButCurrentTabMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        closeAllButCurrentTabMenuItem.setText(bundle.getString("FileExplorerForm.closeAllButCurrentTabMenuItem.text")); // NOI18N
        tabMenu.add(closeAllButCurrentTabMenuItem);

        fileMenu.add(tabMenu);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText(bundle.getString("FileExplorerForm.exitMenuItem.text")); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        mainMenuBar.add(fileMenu);

        editMenu.setText(bundle.getString("FileExplorerForm.editMenu.text")); // NOI18N

        searchMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        searchMenuItem.setText(bundle.getString("FileExplorerForm.searchMenuItem.text")); // NOI18N
        editMenu.add(searchMenuItem);

        editAddressBarMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        editAddressBarMenuItem.setText(bundle.getString("FileExplorerForm.editAddressBarMenuItem.text")); // NOI18N
        editMenu.add(editAddressBarMenuItem);

        bookmarkCurrentFolderMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        bookmarkCurrentFolderMenuItem.setText(bundle.getString("FileExplorerForm.bookmarkCurrentFolderMenuItem.text")); // NOI18N
        editMenu.add(bookmarkCurrentFolderMenuItem);

        goToParentFolderMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, java.awt.event.InputEvent.ALT_MASK));
        goToParentFolderMenuItem.setText(bundle.getString("FileExplorerForm.goToParentFolderMenuItem.text")); // NOI18N
        editMenu.add(goToParentFolderMenuItem);

        reloadCurrentFolderMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        reloadCurrentFolderMenuItem.setText(bundle.getString("FileExplorerForm.reloadCurrentFolderMenuItem.text")); // NOI18N
        editMenu.add(reloadCurrentFolderMenuItem);

        mainMenuBar.add(editMenu);

        viewMenu.setText(bundle.getString("FileExplorerForm.viewMenu.text")); // NOI18N

        viewDiagnosticsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        viewDiagnosticsMenuItem.setText(bundle.getString("FileExplorerForm.viewDiagnosticsMenuItem.text")); // NOI18N
        viewMenu.add(viewDiagnosticsMenuItem);
        viewMenu.add(jSeparator1);

        themeMenu.setText(bundle.getString("FileExplorerForm.themeMenu.text")); // NOI18N

        appThemeButtonGroup.add(themeMetalRadioButtonMenuItem);
        themeMetalRadioButtonMenuItem.setText("Metal"); // NOI18N
        themeMenu.add(themeMetalRadioButtonMenuItem);

        appThemeButtonGroup.add(themeNimbuslRadioButtonMenuItem);
        themeNimbuslRadioButtonMenuItem.setText("Nimbus"); // NOI18N
        themeMenu.add(themeNimbuslRadioButtonMenuItem);

        appThemeButtonGroup.add(themeMotifCDERadioButtonMenuItem);
        themeMotifCDERadioButtonMenuItem.setText("Motif/CDE"); // NOI18N
        themeMenu.add(themeMotifCDERadioButtonMenuItem);

        appThemeButtonGroup.add(themeWindowsRadioButtonMenuItem);
        themeWindowsRadioButtonMenuItem.setText("Windows"); // NOI18N
        themeMenu.add(themeWindowsRadioButtonMenuItem);

        appThemeButtonGroup.add(themeWindowsClassicRadioButtonMenuItem);
        themeWindowsClassicRadioButtonMenuItem.setText("Windows Classic"); // NOI18N
        themeMenu.add(themeWindowsClassicRadioButtonMenuItem);

        viewMenu.add(themeMenu);

        viewLayoutStyleMenu.setText(bundle.getString("FileExplorerForm.viewLayoutStyleMenu.text")); // NOI18N

        listLayoutRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
        viewLayoutStyleButtonGroup.add(listLayoutRadioButtonMenuItem);
        listLayoutRadioButtonMenuItem.setText(bundle.getString("FileExplorerForm.listLayoutRadioButtonMenuItem.text")); // NOI18N
        viewLayoutStyleMenu.add(listLayoutRadioButtonMenuItem);

        detailsLayoutRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        viewLayoutStyleButtonGroup.add(detailsLayoutRadioButtonMenuItem);
        detailsLayoutRadioButtonMenuItem.setText(bundle.getString("FileExplorerForm.detailsLayoutRadioButtonMenuItem.text")); // NOI18N
        viewLayoutStyleMenu.add(detailsLayoutRadioButtonMenuItem);

        iconLayoutRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK));
        viewLayoutStyleButtonGroup.add(iconLayoutRadioButtonMenuItem);
        iconLayoutRadioButtonMenuItem.setText(bundle.getString("FileExplorerForm.iconLayoutRadioButtonMenuItem.text")); // NOI18N
        viewLayoutStyleMenu.add(iconLayoutRadioButtonMenuItem);

        treeLayoutRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
        viewLayoutStyleButtonGroup.add(treeLayoutRadioButtonMenuItem);
        treeLayoutRadioButtonMenuItem.setText(bundle.getString("FileExplorerForm.treeLayoutRadioButtonMenuItem.text")); // NOI18N
        viewLayoutStyleMenu.add(treeLayoutRadioButtonMenuItem);

        viewMenu.add(viewLayoutStyleMenu);

        viewLanguageMenu.setText(bundle.getString("FileExplorerForm.viewLanguageMenu.text")); // NOI18N

        appLanguageButtonGroup.add(langEnglishUSRadioButtonMenuItem);
        langEnglishUSRadioButtonMenuItem.setText("English (US)"); // NOI18N
        viewLanguageMenu.add(langEnglishUSRadioButtonMenuItem);

        appLanguageButtonGroup.add(langFrenchRadioButtonMenuItem);
        langFrenchRadioButtonMenuItem.setText("French"); // NOI18N
        viewLanguageMenu.add(langFrenchRadioButtonMenuItem);

        appLanguageButtonGroup.add(langGermanRadioButtonMenuItem);
        langGermanRadioButtonMenuItem.setText("German"); // NOI18N
        viewLanguageMenu.add(langGermanRadioButtonMenuItem);

        viewMenu.add(viewLanguageMenu);
        viewMenu.add(jSeparator2);

        viewPreferencesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        viewPreferencesMenuItem.setText(bundle.getString("FileExplorerForm.viewPreferencesMenuItem.text")); // NOI18N
        viewMenu.add(viewPreferencesMenuItem);

        mainMenuBar.add(viewMenu);

        helpMenu.setText(bundle.getString("FileExplorerForm.helpMenu.text")); // NOI18N

        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/about.png"))); // NOI18N
        aboutMenuItem.setText(bundle.getString("FileExplorerForm.aboutMenuItem.text")); // NOI18N
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        System.out.println("Info: Maximizing FileExplorerForm window...");
        // sets window state to maximized state
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		logger.logInfo("Window opened");
    }//GEN-LAST:event_formWindowOpened

	private AboutForm aboutForm = null;
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        this.setEnabled(false);
		if(aboutForm == null) {
			aboutForm = AboutForm.init();
		} else {
			aboutForm.setVisible(true);
		}
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int result = JOptionPane.showConfirmDialog(this,
				"Do you want to Exit ?", "Exit Confirmation",
				JOptionPane.YES_NO_OPTION);
		
		if (result == JOptionPane.YES_OPTION) {
          this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  logger.logInfo("Window closing...");
		SystemHandler.getInstance().close();
		} else if (result == JOptionPane.NO_OPTION)
          this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }//GEN-LAST:event_formWindowClosing

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        this.dispatchEvent(new java.awt.event.WindowEvent(this, java.awt.event.WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_exitMenuItemActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JLabel activityStatusLabel;
    private javax.swing.ButtonGroup appLanguageButtonGroup;
    private javax.swing.ButtonGroup appThemeButtonGroup;
    private javax.swing.JMenuItem bookmarkCurrentFolderMenuItem;
    private javax.swing.JMenuItem closeAllButCurrentTabMenuItem;
    private javax.swing.JMenuItem closeCurrentTabMenuItem;
    private javax.swing.JMenu createMenu;
    private javax.swing.JMenuItem createNewFileMenuItem;
    private javax.swing.JMenuItem createNewFolderMenuItem;
    private javax.swing.JRadioButtonMenuItem detailsLayoutRadioButtonMenuItem;
    private javax.swing.JMenuItem editAddressBarMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JLabel errorCountLabel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem goToParentFolderMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JRadioButtonMenuItem iconLayoutRadioButtonMenuItem;
    private javax.swing.JLabel itemsSelectedLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JRadioButtonMenuItem langEnglishUSRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem langFrenchRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem langGermanRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem listLayoutRadioButtonMenuItem;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenuItem newTabMenuItem;
    private javax.swing.JMenuItem reloadCurrentFolderMenuItem;
    private javax.swing.JMenuItem searchMenuItem;
    private javax.swing.JMenu tabMenu;
    private javax.swing.JMenu themeMenu;
    private javax.swing.JRadioButtonMenuItem themeMetalRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem themeMotifCDERadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem themeNimbuslRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem themeWindowsClassicRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem themeWindowsRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem treeLayoutRadioButtonMenuItem;
    private javax.swing.JMenuItem viewDiagnosticsMenuItem;
    private javax.swing.JMenu viewLanguageMenu;
    private javax.swing.ButtonGroup viewLayoutStyleButtonGroup;
    private javax.swing.JMenu viewLayoutStyleMenu;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem viewPreferencesMenuItem;
    // End of variables declaration//GEN-END:variables
}
