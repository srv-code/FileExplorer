package consoletests;

import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileSystemView;

public class ConsoleTests {
    public static void main(String[] args) throws Exception {
//        new PreferencesTest().setPreferenceTest();
//		stackTraceTest();
		systemDefaultIconTest();
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
