/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.mytests;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.*;


public class TestFrame1 extends javax.swing.JFrame {

	/**
	 * Creates new form TestFrame1
	 */
	public TestFrame1() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboServerHostName = new javax.swing.JComboBox<>();
        btnLogin = new javax.swing.JButton();
        btnShow = new javax.swing.JButton();
        btnLoadItems = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDisplay = new javax.swing.JTextArea();
        comboUsername = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        comboServerHostName.setEditable(true);
        comboServerHostName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboServerHostNameItemStateChanged(evt);
            }
        });

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnShow.setText("Show");
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });

        btnLoadItems.setText("Load");
        btnLoadItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadItemsActionPerformed(evt);
            }
        });

        txtDisplay.setEditable(false);
        txtDisplay.setColumns(20);
        txtDisplay.setRows(5);
        jScrollPane1.setViewportView(txtDisplay);

        comboUsername.setEditable(true);

        jLabel1.setText("Address:");

        jLabel2.setText("Username:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboServerHostName, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65))
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(btnLoadItems)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShow)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboServerHostName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoadItems)
                    .addComponent(btnLogin)
                    .addComponent(btnShow))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
	
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String host = comboServerHostName.getSelectedItem().toString().trim();
		String username = comboUsername.getSelectedItem().toString().trim();
		
		saveRemoteServerProfile(host, username);
    }//GEN-LAST:event_btnLoginActionPerformed

	private void saveRemoteServerProfile(final String host, final String username) {		
		System.out.println("  // invoked: saveRemoteServerProfile()");
		
		List<String> usernameList = remoteServerProfileMap.get(host);		
		if(usernameList==null)
			usernameList = new ArrayList<>();
		
		// adding to map: remoteServerProfileMap
		usernameList.add(username);
		StringBuilder sbUsernames = new StringBuilder();
		for(String name: usernameList)
			sbUsernames.append(name).append(usernameListDelimiter);
		
		// adding to prefs: profilesNode
		profilesNode.put(host, sbUsernames.toString());
		
		// updating combo boxes
		updateComboAddressesAndUsernames(host);
		
		System.out.printf("  // saved profile: host=%s, username=%s\n", host, username);
		System.out.printf("  // new: host=%s, map=%s, usernames=%s\n", 
				host, remoteServerProfileMap, sbUsernames.toString());
	}
	
	private void updateComboAddressesAndUsernames(final String host) {
		comboServerHostName.removeAll();
		for(String keyHost: remoteServerProfileMap.keySet())
			comboServerHostName.addItem(keyHost);
		comboUsername.removeAll();		
		for(String username: remoteServerProfileMap.get(host))
			comboUsername.addItem(username);
	}
	
	final Map<String,List<String>> remoteServerProfileMap = new HashMap<>();
	
	private void loadProfiles() throws BackingStoreException {
		
	}
	
    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
//        lblDisplay.setText(comboItems.getSelectedItem().toString());
//		storeData();
    }//GEN-LAST:event_btnShowActionPerformed

    private void btnLoadItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadItemsActionPerformed
        try {
			loadProfiles();
		} catch(BackingStoreException e) { // abort loading of items
		}
    }//GEN-LAST:event_btnLoadItemsActionPerformed

    private void comboServerHostNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboServerHostNameItemStateChanged
        System.out.println("  // invoked: comboAddressesItemStateChanged");
		if(comboServerHostName.getSelectedItem() != null) {
			final String hostString = comboServerHostName.getSelectedItem().toString();
			comboUsername.removeAllItems();
			System.out.println("  // removed all usernames");
			System.out.println("  // selected host: " + hostString);
			List<String> usernameList = remoteServerProfileMap.get(hostString);
			if(usernameList==null || usernameList.size()==0) 
				return;
			for(String username: usernameList) {
				comboUsername.addItem(username);
				System.out.println("  // added user: " + username);
			}
		}
    }//GEN-LAST:event_comboServerHostNameItemStateChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        saveRemoteServerProfilesToPreferences();
    }//GEN-LAST:event_formWindowClosing

	/***** Place these methods in the SystemResources class of the FileExplorer project *****/
	/** Helper */
	private static class Resources {
		static final Preferences userPrefs = Preferences.userNodeForPackage(this);
		static final String remoteServerProfilesNodePath = "remote_servers/profiles";
		static final Preferences profilesNode = userPrefs.node(remoteServerProfilesNodePath);
		static final String usernameListDelimiter = ";";
	}
	
	private void saveRemoteServerProfilesToPreferences() {
		System.out.println("Saving remote server profiles to preferences...");
		
		
		
		
		
		System.out.println("\t[done]");
	}
	
	private void loadRemoteServerProfilesIntoMap() {
		System.out.println("Loading remote server profiles to map from preferences...");
		
		String[] hosts = Resources.profilesNode.keys();
		System.out.printf("  // profilesNode=%s, hosts=%s\n", 
				profilesNode.absolutePath(), Arrays.toString(hosts));
		if(hosts==null || hosts.length==0)
			return;
		
		// load into map
		String usernames;
		List<String> usernameList;
		for(String host: hosts) {
			usernames = profilesNode.get(host, "");			
			usernameList = new ArrayList<>();
			if(usernames.length()>0) {
				for(String username: usernames.split(usernameListDelimiter))
					usernameList.add(username);
			}
			System.out.printf("  // loaded into map: [key=%s, value=%s(%s)]\n", host, usernameList, usernameList.getClass());
			remoteServerProfileMap.put(host, usernameList);
		}
		
		// load into comboAddresses
		for(String host: remoteServerProfileMap.keySet()) {
			System.out.println("  // added host to comboAddresses: " + host);
			comboServerHostName.addItem(host);
		}
	}
	/*---- Place these methods in the SystemResources class of the FileExplorer project ----*/
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
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
			java.util.logging.Logger.getLogger(TestFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(TestFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(TestFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(TestFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new TestFrame1().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoadItems;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnShow;
    private javax.swing.JComboBox<String> comboServerHostName;
    private javax.swing.JComboBox<String> comboUsername;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtDisplay;
    // End of variables declaration//GEN-END:variables
}
