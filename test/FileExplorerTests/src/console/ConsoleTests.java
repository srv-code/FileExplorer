package console;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.swing.filechooser.FileSystemView;


public class ConsoleTests {
    public static void main(String[] args) throws Exception {
//        new PreferencesTest().setPreferenceTest();
//		stackTraceTest();
//		systemDefaultIconTest();
//		testDesktop();
//		changePermBits();
		waiting();
    }
	
	private static void waiting() throws InterruptedException {
		System.out.println("starting...");
		ProgressIndeterminate.start();
		Thread.sleep(3000); // simulate wait 
		System.out.println("Stopping...");
		ProgressIndeterminate.stop();
	}
	
	private static void changePermBits() {
		File file = new File("c:/users/soura/desktop/aaa.txt");
		System.out.printf("file.exists()=%s\n", file.exists());
		System.out.printf("file.canWrite()=%b, file.setWritable(%b)=%b\n", 
				file.canWrite(), !file.canWrite(), file.setWritable(!file.canWrite()));
		System.out.printf("file.canRead()=%b, file.setReadable(%b)=%b\n", 
				file.canRead(), !file.canRead(), file.setReadable(!file.canRead()));
		System.out.printf("file.canExecute()=%b, file.setExecutable(%b)=%b\n", 
				file.canExecute(), !file.canExecute(), file.setExecutable(!file.canExecute()));
	}
	
	private static void testDesktop() throws Exception {
		Desktop desktop = Desktop.getDesktop();
		
//		System.out.println("Desktop supported?: " + Desktop.isDesktopSupported());
		
//		final URI fileURI = new URI("file:///D:/tmp/diff_result.txt");
//		final URI dirURI = new URI("file:///D:/tmp");

//		desktop.browse(new URI(filePath));
//		desktop.browse(new URI(dirPath));

		final File file = new File("D:\\tmp\\ActionDemo.java");
		final File file2 = new File("D:\\tmp\\diff_result.txt");
		final File file3 = new File("D:\\tmp\\a.html");
		final File file4 = new File("D:/MCA/summer seminar & project/MCA-5104 -Seminar Instructions.pdf");
		final File file5 = new File("D:/MCA/summer seminar & project/seminar/seminar_report2019.docx");
		final File dir = new File("C:\\");
		
//
//		desktop.edit(file);

//		desktop.open(file2);
//		desktop.open(dir);

		desktop.print(file5);
	}

	private static void stackTraceTest() throws Exception {
		Exception exc = new IOException("main exc", new NullPointerException("cause exc"));
		exc.addSuppressed(new ArrayIndexOutOfBoundsException("suppressed exc"));
		
//		throw exc;
//		exc.printStackTrace(System.err);
		
		StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(exc).append("\n");
        for(StackTraceElement ste : exc.getStackTrace())
            stackTrace  .append("    ")
						.append(ste.toString())
                        .append("\n");
		System.out.println("Stack trace: " + stackTrace);
	}

	private static void systemDefaultIconTest() {
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		File file = new File("C:\\Users\\soura\\Downloads\\ChromeSetup.exe");
		System.out.println(fileSystemView.getSystemIcon(file));
	}
	
    
    private void testMethodName() {
        new Test1().showMethodName();
//        Exception e = new Exception();
//        e.fillInStackTrace();
//        for(int i=0; i<e.getStackTrace().length; i++) {
//            System.out.printf("%d: %s \n", i+1, e.getStackTrace()[i].getMethodName());
//        }
        
//        String methodName = e.getStackTrace()[0].getMethodName();
        
//        System.out.println("methodName=" + methodName);
//        System.out.println("method=" + ConsoleTests.class.getEnclosingMethod());
//        test1(getClass().getEnclosingMethod());
        
    }
    
    private void test1(final java.lang.reflect.Method method) {
//        System.out.println("method=" + method.getName());
    }
}
