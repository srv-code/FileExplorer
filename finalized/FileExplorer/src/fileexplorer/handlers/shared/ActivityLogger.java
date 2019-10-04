package fileexplorer.handlers.shared;

//import com.sun.org.apache.xpath.internal.XPathVisitable;
import java.io.*;
import java.util.*;


public class ActivityLogger implements AutoCloseable {
    private File file = null;
    private FileWriter writer;
    private List<String[]> logs = new ArrayList<>();
	private boolean isClosed = true;	
	private final boolean writeToLogFile;
    private final boolean showDebugInfo;
	
	private static ActivityLogger instance = null;
	
    public static ActivityLogger getInstance(final boolean logWrite, final boolean showDebug) {
        if(instance == null)
			instance = new ActivityLogger(logWrite, showDebug);
        return instance;
    }
	
	/** To simplify getting the instance of the singleton object, 
	 *  however can be null if not initialized using  the 
	 *  getInstance(boolean logWrite, boolean showDebug) constructor. */
	public static ActivityLogger getInstance() {
		return instance;
	}
    
    private ActivityLogger(final boolean logWrite, final boolean showDebug) { /* disable external instantiation */
		this.writeToLogFile = logWrite;
		this.showDebugInfo = showDebug;
		this.isClosed = false;
    }
    
    public File getFile() throws IOException {
		if(writeToLogFile && file == null) { /* initialize all IO objects */
            printToConsole(LogLevel.CONFIG, "Initializing I/O objects...");            
			file = new File(SystemResources.APP_DIR, SystemResources.LOG_FILE_NAME);
	        writer = new FileWriter(file);
		}		
        return file;
    }

	private void updateStatusBar(final String msg) {
		if(SystemResources.formFileExplorer != null && SystemResources.formFileExplorer.lblActivityStatus != null)
			SystemResources.formFileExplorer.lblActivityStatus.setText(msg);
	}

    public static enum LogLevel {
    	FATAL,
		SEVERE,
		WARNING,
		INFO,
		CONFIG
    }
    
    public ActivityLogger logConfig(final String formatString, final Object... args) {
        log(null, LogLevel.CONFIG, formatString, args);
		return this;
    }
    
    public ActivityLogger logInfo(final String formatString, final Object... args) {
        log(null, LogLevel.INFO, formatString, args);		
		return this;
    }
    
    public ActivityLogger logWarning(final String formatString, final Object... args) {
        log(null, LogLevel.WARNING, formatString, args);
		return this;
    }
    
    public ActivityLogger logSevere(final Throwable throwable, final String formatString, final Object... args) {
        log(throwable, LogLevel.SEVERE, formatString, args);
		updateErrorCounter();
		return this;
    }
    
    public ActivityLogger logFatal(final Throwable throwable, final String formatString, final Object... args) {
        log(throwable, LogLevel.FATAL, formatString, args);
		updateErrorCounter();
		return this;
    }
	
    private void printToConsole(final String formatString, final Object... args) {
        if(showDebugInfo)
            System.out.printf(formatString, args);
    }
    
	public void printToConsole(final LogLevel level, final String formatString, final Object... args) {
		if(showDebugInfo)
			System.out.printf("%tY/%<tm/%<td %<tH:%<tM:%<tS.%<tL %<ta    %8s    " + formatString + "\n", 
                    new Date(), level, args);
	}
    
    public void printErrorToConsole(final LogLevel level, final String message, final Throwable throwable) {
        if(showDebugInfo) {
            System.err.printf("%tY/%<tm/%<td %<tH:%<tM:%<tS.%<tL %<ta    %8s    %s%s\n", 
                    new Date(), level, message, throwable==null ? "" : (" (" + throwable.getMessage() + ")"));
            if(throwable != null)
                throwable.printStackTrace(System.err);
        }
    }

    /** Does the main logging job */
    private void log(final Throwable throwable, final LogLevel level,
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
			
			if(level == LogLevel.INFO)
				updateStatusBar(record[3]);

            // add record to list
            logs.add(record);

            // write record to file
            for(String field : record) {
				if(writer!=null)
					writer	.append(field)
							.append("    ");
				
				printToConsole(field);
				printToConsole("    ");
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
				
				if(showDebugInfo) {
					System.err.println("\nStack trace:");
					throwable.printStackTrace(System.err);
				}
			}
			
			if(writer!=null) {
				writer.write(SystemResources.PLATFORM_LINE_SEPARATOR);
				writer.flush();
			}
			
			printToConsole("\n");
        } catch(Exception e) {
			if(showDebugInfo) {
				System.err.printf("ERR: Cannot write to log file. File='%s', Timestamp=%s, Exception=%s\n", 
	                    file.getAbsolutePath(), new Date(), e);
				e.printStackTrace(System.err);
			}
        }
    }
	
	private long errorCount = 0L;
	private void updateErrorCounter() {
		// System.out.println("  // updateErrorCounter() called"); // DEBUG
		if(SystemResources.formFileExplorer != null && SystemResources.formFileExplorer.lblErrorCount != null)
			SystemResources.formFileExplorer.lblErrorCount.setText(String.valueOf(++errorCount));
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
//        System.out.println("CONFIG: ActivityLogger closed");
    }
}