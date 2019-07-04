/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.mytests;

import gui.mytests.handlers.fs.FileAttributes;
import gui.mytests.handlers.fs.FileSystemHandler;
import java.awt.Color;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.attribute.FileAttribute;
import javax.swing.SwingWorker;
import javax.swing.plaf.ComponentUI;

/**
 * JFrame to show file progresses for operations such as paste (copy/cut) & delete.
 * The frame title changes based on the operation being perframeed which is set by
 * the enum constant specified to the constructor.
 * @author soura
 */
public class OperationProgressForm extends javax.swing.JFrame {

	private OperationProgressForm(	final OperationType operationType, 
									final FileSystemHandler fileSystemHandler,
									final FileAttributes src,
									final FileAttributes dst) {
		initComponents();
		
		switch(operationType) {
			case MOVE:		setTitle("Moving..."); break;
			case COPY:		setTitle("Copying..."); break;
			case DELETE:	setTitle("Deleting..."); break;
			default:		throw new IllegalArgumentException("undefined operation for setting frame title");
		}
	}
	
	private Task task;
	
	private void execute() {
		progressbarOverall.setIndeterminate(true);
		progressbarCurrent.setIndeterminate(true);
		
//		int total = 
		
		task = new Task();
        task.addPropertyChangeListener(new PropertyChangeListener() {
			/**
			* Invoked when task's progress property changes.
			*/
			@Override 
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress" == evt.getPropertyName()) {
					int progress = (Integer) evt.getNewValue();
					progressbarOverall.setIndeterminate(false);
					progressbarCurrent.setIndeterminate(false);
//					progressBar.setValue(progress);
					taskLog.append(String.format(
								"Completed %d%% of task.\n", progress));
				}
			}
		});
        task.execute();
	}

	/**
	 * This method is called from within the constructor to initialize the frame.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        progressbarOverall = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        progressbarCurrent = new javax.swing.JProgressBar();
        btnCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taskLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Overall:");

        progressbarOverall.setStringPainted(true);

        jLabel2.setText("Current:");

        progressbarCurrent.setStringPainted(true);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        taskLog.setEditable(false);
        taskLog.setColumns(20);
        taskLog.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
        taskLog.setForeground(new java.awt.Color(0, 51, 204));
        taskLog.setRows(5);
        jScrollPane1.setViewportView(taskLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressbarOverall, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                            .addComponent(progressbarCurrent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCancel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressbarOverall, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(progressbarCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
//        progressbarOverall.setIndeterminate(true);
//		taskLog.append("C:\\Users\\soura\\Desktop\\a.txt\n");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        performRollbackTask();		
    }//GEN-LAST:event_formWindowClosing

	private void performRollbackTask() { // prepare for temp task roll back
		// TODO complete
	}
	
	/**
	 * Tester
	 */
	public static void main(String args[]) throws Exception {		
		
		// set L&F
//		ListViewFrame.setAppLookAndFeel();
		// for easy portability
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			System.err.printf("Err: Cannot set predefined L&F");
		}
		
		
		OperationType operationType;
		switch(args[0]) {
			case "cp": operationType = OperationType.COPY; break;
			case "mv": operationType = OperationType.MOVE; break;
			case "rm": operationType = OperationType.DELETE; break;
			default: throw new IllegalArgumentException(args[2]);
		}
		FileSystemHandler fileSystemHandler = FileSystemHandler.getHandler("C:\\");
		FileAttributes src = fileSystemHandler.getFileAttributes(args[0]);
		FileAttributes dst = null;
		if(operationType!=OperationType.DELETE) {
			dst = fileSystemHandler.getFileAttributes(args[1]);
		}
		
		init(operationType, fileSystemHandler, src, dst);
	}
	
	
	public static enum OperationType { COPY, MOVE, DELETE };
	
	private static OperationProgressForm frame;
	
	public static OperationProgressForm init(	final OperationType operationType, 
												final FileSystemHandler fileSystemHandler,
												final FileAttributes src,
												final FileAttributes dst) {
		/* Create and display the frame */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame = new OperationProgressForm(operationType, fileSystemHandler, src, dst);
				frame.setVisible(true);
				frame.execute();
			}
		});
		return frame;
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progressbarCurrent;
    private javax.swing.JProgressBar progressbarOverall;
    private javax.swing.JTextArea taskLog;
    // End of variables declaration//GEN-END:variables


	class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
			
			return null;
        }

        /*
         * Executed in event dispatch thread
         */
        public void done() {
            Toolkit.getDefaultToolkit().beep();
//            startButton.setEnabled(true);
//            taskLog.append("Done!\n");
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }
}