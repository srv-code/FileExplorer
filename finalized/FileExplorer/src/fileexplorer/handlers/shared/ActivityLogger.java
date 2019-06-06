package fileexplorer.handlers.shared;

import java.io.*;
import java.util.*;


public class ActivityLogger implements AutoCloseable {
    private File file = null;
    private FileWriter writer;
    private List<String[]> logs = new ArrayList<>();
	private boolean isClosed = true;

    
    private static ActivityLogger instance = null;
    public static ActivityLogger getInstance() throws IOException {
        if(instance == null) {
            instance = new ActivityLogger(SystemResources.APP_TEMP_DIR_PATH, SystemResources.LOG_FILE_NAME);
//            System.out.println("Info: ActivityLogger initialized, log file: "+ instance.file.getAbsolutePath());
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

    private static enum Level {
    	FATAL,
		SEVERE,
		WARNING,
		INFO,
		CONFIG
    }
    
    public void logConfig(final String formatString, final Object... args) {
        log(Level.CONFIG, formatString, args);
    }
    
    public void logInfo(final String formatString, final Object... args) {
        log(Level.INFO, formatString, args);
    }
    
    public void logWarning(final String formatString, final Object... args) {
        log(Level.WARNING, formatString, args);
    }
    
    public void logSevere(final String formatString, final Object... args) {
        log(Level.SEVERE, formatString, args);
    }
    
    public void logFatal(final String formatString, final Object... args) {
        log(Level.FATAL, formatString, args);
    }

    /** Does the main logging job */
    private void log(final Level level,
                    final String formatString, final Object... args) {
        try {
            // build the record 
            String[] record = new String[4];
            record[0] = String.format("%tY/%<tm/%<td %<tH:%<tM:%<tS.%<tL.%<tN %<ta", new Date()); // date
            record[1] = level.toString(); // level

            Exception e = new Exception();
            e.fillInStackTrace();
            record[2] = e.getStackTrace()[2].toString(); // caller location
            record[3] = String.format(formatString, args); // log string

            // add record to list
            logs.add(record);

            // write record to file
            for(String field : record) {
                writer.write(field);
                writer.write("    ");
				
				// Print to console for east debugging
				System.out.print(field);
				System.out.print("    ");
            }
            writer.write(SystemResources.PLATFORM_LINE_SEPARATOR);
			System.out.println();
            writer.flush();
        } catch(Exception e) {
            System.err.printf("Err: Cannot write to log file. File='%s', Timestamp=%s, Exception=%s\n", 
                    file.getAbsolutePath(), new Date(), e);
        }
    }
    
    public String getStackTrace(final Exception e) {
		StringBuilder stackTrace = new StringBuilder();
        stackTrace	.append(e)
					.append(SystemResources.PLATFORM_LINE_SEPARATOR);
		
        for(StackTraceElement ste : e.getStackTrace())
            stackTrace  .append("    ")
						.append(ste)
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
        writer.close();
		isClosed = true;
//        System.out.println("Info: ActivityLogger closed");
    }
}
