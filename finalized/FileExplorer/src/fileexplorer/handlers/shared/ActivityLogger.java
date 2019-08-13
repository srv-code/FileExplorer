package fileexplorer.handlers.shared;

//import com.sun.org.apache.xpath.internal.XPathVisitable;
import java.io.*;
import java.util.*;


public class ActivityLogger implements AutoCloseable {
    private File file = null;
    private FileWriter writer;
    private List<String[]> logs = new ArrayList<>();
	private boolean isClosed = true;

    
    private static ActivityLogger instance = null;
    public static ActivityLogger getInstance() {
        if(instance == null) {
			try {
				instance = new ActivityLogger(SystemResources.APP_DIR, SystemResources.LOG_FILE_NAME);
			} catch(IOException e) {
				System.err.println("Err: Cannot initialise ActivityLogger. Reason: " + e);
			}
        }
        return instance;
    }
    
    private ActivityLogger(final File dir, final String filename) throws IOException { // disable external instantiation
        file = new File(dir, filename);
        writer = new FileWriter(file);
		isClosed = false;
    }
    
    public File getFile() {
        return file;
    }

	private void updateStatusBar(final String msg) {
		if(SystemResources.formFileExplorer != null && SystemResources.formFileExplorer.lblActivityStatus != null)
			SystemResources.formFileExplorer.lblActivityStatus.setText(msg);
	}

    private static enum Level {
    	FATAL,
		SEVERE,
		WARNING,
		INFO,
		CONFIG
    }
    
    public ActivityLogger logConfig(final String formatString, final Object... args) {
        log(null, Level.CONFIG, formatString, args);
		return this;
    }
    
    public ActivityLogger logInfo(final String formatString, final Object... args) {
        log(null, Level.INFO, formatString, args);		
		return this;
    }
    
    public ActivityLogger logWarning(final String formatString, final Object... args) {
        log(null, Level.WARNING, formatString, args);
		return this;
    }
    
    public ActivityLogger logSevere(final Throwable throwable, final String formatString, final Object... args) {
        log(throwable, Level.SEVERE, formatString, args);
		updateErrorCounter();
		return this;
    }
    
    public ActivityLogger logFatal(final Throwable throwable, final String formatString, final Object... args) {
        log(throwable, Level.FATAL, formatString, args);
		updateErrorCounter();
		return this;
    }

    /** Does the main logging job */
    private void log(final Throwable throwable, final Level level,
                    final String formatString, final Object... args) {
        try {
            // build the record 
            String[] record = new String[4];
            record[0] = String.format("%tY/%<tm/%<td %<tH:%<tM:%<tS.%<tL %<ta", new Date()); // date
            record[1] = String.format("%8s", level); // level

            Exception e = new Exception();
            e.fillInStackTrace();
            record[2] = e.getStackTrace()[2].toString(); // caller location
            record[3] = String.format(formatString, args); // log string
			
			if(level == Level.INFO) {
				updateStatusBar(record[3]);
			}
				

            // add record to list
            logs.add(record);

            // write record to file
            for(String field : record) {
				if(writer!=null)
					writer	.append(field)
							.append("    ");
				
				// Print to console for east debugging
				System.out.print(field);
				System.out.print("    ");
            }
			
			if(throwable != null) {
				if(writer!=null) {
					writer	.append(SystemResources.PLATFORM_LINE_SEPARATOR)
							.append("Stack trace:")
							.append(SystemResources.PLATFORM_LINE_SEPARATOR);
					for(StackTraceElement element : throwable.getStackTrace())
						writer	.append("    ")
								.append(element.toString())
								.append(SystemResources.PLATFORM_LINE_SEPARATOR);					
				}
				System.err.println("\nStack trace:");
				throwable.printStackTrace(System.err);
			}
			
			if(writer!=null) {
				writer.write(SystemResources.PLATFORM_LINE_SEPARATOR);
				writer.flush();
			}
			
			System.out.println();
        } catch(Exception e) {
            System.err.printf("ERR: Cannot write to log file. File='%s', Timestamp=%s, Exception=%s\n", 
                    file.getAbsolutePath(), new Date(), e);
        }
    }
	
	private long errorCount = 0L;
	private void updateErrorCounter() {
		errorCount++;
		if(SystemResources.formFileExplorer != null && SystemResources.formFileExplorer.lblErrorCount != null)
			SystemResources.formFileExplorer.lblErrorCount.setText(String.valueOf(errorCount));
	}
    
	@Deprecated 
    public String getStackTrace(final Throwable throwable) {
		StringBuilder stackTrace = new StringBuilder();
        stackTrace	.append(throwable)
					.append(SystemResources.PLATFORM_LINE_SEPARATOR);
		
        for(StackTraceElement element : throwable.getStackTrace())
            stackTrace  .append("    ")
						.append(element)
                        .append(SystemResources.PLATFORM_LINE_SEPARATOR);

        return stackTrace.toString();
    }

    public int getCount() {
    	return logs.size();
    }
	
	public boolean isClosed() {
		return isClosed;
	}

    @Override
    public void close() throws IOException {
        if(writer != null)
			writer.close();
		isClosed = true;
//        System.out.println("Info: ActivityLogger closed");
    }
}
