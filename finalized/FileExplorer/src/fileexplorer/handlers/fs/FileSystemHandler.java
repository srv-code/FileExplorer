package fileexplorer.handlers.fs;

import org.apache.commons.net.ftp.*;
import fileexplorer.handlers.fs.nav.NavigationException;
import fileexplorer.handlers.fs.nav.NavigationHistoryHandler;
import java.awt.Desktop;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;


/**
 * Abstract class for general reference.
 */
public abstract class FileSystemHandler implements Closeable {
//	protected String fileSystemID;
	protected FileAttributes userHomeDirectory = null, defaultStartLocation = null;
	protected final Desktop desktop = Desktop.getDesktop();
	public final boolean isDesktopSupported = Desktop.isDesktopSupported();
	protected final String ROOT_PATH = "/";
//	public boolean isRemoteHandler;
	
	public final NavigationHistoryHandler historyHandler = new NavigationHistoryHandler();
	
	protected FileAttributes currentWorkingDirectory = null;
	
	public FileAttributes getCurrentWorkingDirectory() {
		return currentWorkingDirectory;
	}
	
	public FileAttributes resetCurrentWorkingDirectory() throws IOException {
		currentWorkingDirectory = getHomeDirectory();
		return currentWorkingDirectory;
	}
	
	protected FileAttributes checkIfDirectory(final FileAttributes file) throws IOException {
		if(!file.isDirectory)
			throw new IOException("Path is not a directory: " + file.absolutePath);
		return file;
	}
	
	public abstract FileAttributes setExecuteFlag(final FileAttributes file, final boolean value) throws IOException;
	public abstract FileAttributes setReadFlag(final FileAttributes file, final boolean value) throws IOException;
	public abstract FileAttributes setWriteFlag(final FileAttributes file, final boolean value) throws IOException;
	
//	public abstract Object getAttribute(final String key);
	
//	protected FileSystemHandler() throws IOException {
//		this.fileSystemID = ;
//		navigateTo(absolutePath, true);
//		historyHandler.clear();
//	}
	
	public abstract String getCurrentUsername();
	public abstract String getCurrentHostname();
	
	public abstract FileAttributes rename(final FileAttributes file, final String newName) throws IOException;
	
	protected final int PASTE_BUF_SIZE = 8192; // 8KB
	protected long dirCount=0L, fileCount=0L, cpFileCount=0L, cpDirCount=0L, rmFileCount=0L, rmDirCount=0L;
	protected boolean allowDelete = true; // [For test purpose] safe guard against any fatal operation
	
	public abstract long[] count(final FileAttributes root) throws IOException;
	public abstract long[] copy(final FileAttributes src, final FileAttributes dst) throws IOException;
	public abstract long[] move(final FileAttributes src, final FileAttributes dst) throws IOException;
	public abstract long[] delete(final FileAttributes root) throws IOException;
	
//	public abstract boolean isPathValid(final String absolutePath);
	
//	public abstract boolean isPathAbsolute(final String path);

	public FileAttributes navigateTo(final String path, final boolean registerInHistory) throws IOException {
		return navigateTo(getFileAttributes(path), registerInHistory);
	}
	
	public FileAttributes navigateTo(final FileAttributes file, final boolean registerInHistory) throws IOException {
		currentWorkingDirectory = checkIfDirectory(file);
		if(registerInHistory) 
			historyHandler.append(file);
//		System.out.printf("  // navigating to: %s, registerInHistory=%b, history=%s, canGoBackward=%b, canGoForward=%b\n", 
//				file, registerInHistory, historyHandler, historyHandler.canGoBackward(), historyHandler.canGoForward());
		return currentWorkingDirectory;
	}
	
	public FileAttributes revertLastNavigation() throws NavigationException {
		currentWorkingDirectory = historyHandler.removeCurrent();
		return currentWorkingDirectory;
	}
		
	
//	/** Consumes entry from history */
//	public FileAttributes previousInHistory() throws EndInNavigationHistoryException {
//		FileAttributes dir = history.forward();
//		if(dir == null) 
//			throw new EndInNavigationHistoryException();
//		return dir;
//	}
//	
//	/** Consumes entry from history */
//	public FileAttributes nextInHistory() throws EndInNavigationHistoryException {
//		FileAttributes dir = history.backward();
//		if(dir == null) 
//			throw new EndInNavigationHistoryException();
//		return dir;
//	}
	
	public FileAttributes getCurrentParent() throws IOException {
		return getParent(currentWorkingDirectory);
	}
		
	public abstract FileAttributes getParent(final FileAttributes file) throws IOException;
	
	public abstract boolean canGoToParent();
	
	public abstract FileAttributes getFileAttributes(final String absolutePath) throws IOException;
	
//	public FileAttributes createNewFile(final String name) throws IOException {
//		return createNew(name, false);
//	}
//	
//	public FileAttributes createNewDirectory(final String name) throws IOException {
//		return createNew(name, true);
//	}
	
	public abstract FileAttributes createNew(final String name, final boolean isDirectory) throws IOException;
	
	public abstract FileAttributes[] listFiles(final FileAttributes dir) throws IOException;
//	public abstract List<FileAttributes> listRoots() throws FileNotFoundException;
	
	public abstract FileAttributes getHomeDirectory() throws IOException;
	public abstract FileAttributes getRootDirectory() throws IOException;
	
	public abstract void openFile(final FileAttributes file) throws UnsupportedOperationException, IOException;
	
	public abstract void printFile(FileAttributes file) throws IllegalArgumentException, IOException;
	
	public abstract void close() throws IOException;
	
	@Override
	public String toString() {
		return String.format("%s#%x@%s$%s",
						getClass().getSimpleName(),
						hashCode(),
						getCurrentHostname(),
						getCurrentUsername());
	}
	
	/** 
	 * Returns the proper FileSystem subclass object. Detects for local/remote file.
	 */
//	public static FileSystemHandler getHandler(final String absolutePath) throws FileNotFoundException {
//		return absolutePath.toLowerCase().startsWith("ftp") ? 
//				new RemoteFileSystemHandler(absolutePath) : new LocalFileSystemHandler(absolutePath);
//	}
	
	public static FileSystemHandler getRemoteHandler(	final String host, 
														final String username, 
														final String password) throws IOException {
		return new RemoteFileSystemHandler(host, username, password);
	}
	
	public static FileSystemHandler getLocalHandler(final String path) throws IOException {
		return new LocalFileSystemHandler(path);
	}
}
