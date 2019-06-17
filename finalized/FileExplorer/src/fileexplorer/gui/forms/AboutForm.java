package fileexplorer.gui.forms;

import javax.swing.ImageIcon;


public class AboutForm extends javax.swing.JFrame {

    /**
     * Creates new form AboutForm
     */
    public AboutForm() {
        initComponents();
		fileexplorer.handlers.shared.SystemResources.getActivityLogger().logInfo("AboutForm initialized");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aboutIconLabel = new javax.swing.JLabel();
        appTitleLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        appDescTextPane = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("lang/strings"); // NOI18N
        setTitle(bundle.getString("AboutForm.title")); // NOI18N
        setAlwaysOnTop(true);
        setResizable(false);
        setSize(new java.awt.Dimension(140, 300));
        setType(java.awt.Window.Type.UTILITY);

        aboutIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/about_big.png"))); // NOI18N

        appTitleLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        appTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        appTitleLabel.setText(bundle.getString("AboutForm.appTitleLabel.text")); // NOI18N

        appDescTextPane.setEditable(false);
        appDescTextPane.setBorder(null);
        appDescTextPane.setContentType("text/html"); // NOI18N
        appDescTextPane.setText(bundle.getString("AboutForm.appDesc")); // NOI18N
        appDescTextPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                appDescTextPaneHyperlinkUpdate(evt);
            }
        });
        jScrollPane2.setViewportView(appDescTextPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(aboutIconLabel)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addComponent(appTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(aboutIconLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(appTitleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void appDescTextPaneHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_appDescTextPaneHyperlinkUpdate
        if(evt.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
//            System.out.println("Clicked on link: " + evt.getURL() + ", desc=" + evt.getDescription());
            try {
                if(evt.getDescription().startsWith("mailto:"))
                    java.awt.Desktop.getDesktop().mail(new java.net.URI(evt.getDescription()));
                else 
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(evt.getDescription()));
            } catch (java.io.IOException | java.net.URISyntaxException exc) {
                System.err.println("Exc: " + exc);
            }
        }
    }//GEN-LAST:event_appDescTextPaneHyperlinkUpdate

    public static AboutForm init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AboutForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AboutForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AboutForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AboutForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
        AboutForm form = new AboutForm();
        form.setVisible(true);
        form.setIconImage(new ImageIcon(AboutForm.class.getResource("/images/about.png")).getImage());
        return form;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aboutIconLabel;
    private javax.swing.JTextPane appDescTextPane;
    private javax.swing.JLabel appTitleLabel;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}