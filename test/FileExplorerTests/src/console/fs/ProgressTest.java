package console.fs;

import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingWorker;


public class ProgressTest extends javax.swing.JFrame implements PropertyChangeListener {
	public ProgressTest() {
		initComponents();
//		execute();
	}
	
	ProgressWorker progressWorker = null;
	
	private void execute() {
		System.out.println("starting progressWorker...");
		progressWorker = new ProgressWorker(0, 1024);
		progressWorker.addPropertyChangeListener(this);
		progressWorker.execute();
	}
	
	/* Invoked when task's progress property changes. */
	@Override 
	public void propertyChange(PropertyChangeEvent evt) {
//				System.out.println("In propertyChange()");
		if("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			System.out.println("In propertyChange(): progress=" + progress);
			progressBar.setValue(progress);

			if (progressWorker.isCancelled() || progressWorker.isDone()) {
				Toolkit.getDefaultToolkit().beep();
				if (progressWorker.isCancelled()) {
					progressWorker.cancel(true);
					System.out.println("Task cancelled");
				} else {
					System.out.println("Task completed");
				}
			}
		}
	}
	
	
	public class ProgressWorker extends SwingWorker<Void,Void> {
		private long min, max;
//		private ProgressWorker progressWorker;
		
		public ProgressWorker(final long min, final long max) {
			this.min = min;
			this.max = max;
//			addPropertyChangeListener(progressListener);
		}
		
		/* Main task. Executed in background thread. */
		@Override
		public Void doInBackground() {
//			System.out.println("In doInBackground()");
			int percentage = 0;
			long current = 0;
			setProgress(percentage);
			
			// demo 
			final long increment = 256;
			while(percentage < 100) { // TODO comment out
				try {
					Thread.sleep(300);
				} catch(InterruptedException e) {
					e.printStackTrace(System.err);
				}
				current+=increment;
				percentage = (int)(Math.min(current, max)/max)*100;
				System.out.printf("In doBackground(): percentage=%s, current=%d\n", percentage, current);
				setProgress(percentage);
			}
			
			return null;
		}
		
		public void updateProgress(final int progress) {
			setProgress(progress);
		}
		
		/* Executed in event dispatch thread */
		@Override
		public void done() {
//			System.out.println("In done()");
			Toolkit.getDefaultToolkit().beep();
			System.out.println("done");
		}
		
//		PropertyChangeListener progressListener = new PropertyChangeListener() {
//			/* Invoked when task's progress property changes. */
//			@Override 
//			public void propertyChange(PropertyChangeEvent evt) {
////				System.out.println("In propertyChange()");
//				if("progress".equals(evt.getPropertyName())) {
//					int progress = (Integer) evt.getNewValue();
//					System.out.println("In propertyChange(): progress=" + progress);
//					progressBar.setValue(progress);
//					
//					if (isCancelled() || isDone()) {
//						Toolkit.getDefaultToolkit().beep();
//						if (isCancelled()) {
//							cancel(true);
//							System.out.println("Task cancelled");
//						} else {
//							System.out.println("Task completed");
//						}
//					}
//				}
//			}
//		};
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();
        btnStart = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        progressBar.setOpaque(true);
        progressBar.setStringPainted(true);

        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(btnStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap(269, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStart)
                    .addComponent(btnCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
		execute();
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        progressWorker.cancel(true);
    }//GEN-LAST:event_btnCancelActionPerformed

	
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
			java.util.logging.Logger.getLogger(ProgressTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ProgressTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ProgressTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ProgressTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ProgressTest().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnStart;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
