package fileexplorer.handlers.fs;

import fileexplorer.handlers.fs.nav.NavigationException;
import fileexplorer.handlers.fs.nav.NavigationHistoryHandler;
import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.util.List;


/**
 * Abstract class for general reference.
 */
public abstract class FileSystemHandler {
	protected String fileSystemID;
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
	
	public FileAttributes setCurrentWorkingDirectoryToHomeDirectory() throws FileNotFoundException {
		currentWorkingDirectory = getHomeDirectory();
		return currentWorkingDirectory;
	}
	
	protected FileAttributes checkIfDirectory(final FileAttributes file) throws InvalidPathException {
		if(!file.isDirectory)
			throw new InvalidPathException(file.absolutePath, "Path is not a directory");
		return file;
	}
	
	public abstract FileAttributes setExecuteFlag(final FileAttributes file, final boolean value) throws IOException;
	public abstract FileAttributes setReadFlag(final FileAttributes file, final boolean value) throws IOException;
	public abstract FileAttributes setWriteFlag(final FileAttributes file, final boolean value) throws IOException;
	
//	public abstract Object getAttribute(final String key);
	
//	protected FileSystemHandler(final String fileSystemID) throws FileNotFoundException, InvalidPathException {
//		this.fileSystemID = fileSystemID;
////		navigateTo(absolutePath, true);
////		historyHandler.clear();
//	}
	
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

	public FileAttributes navigateTo(final String path, final boolean registerInHistory) throws FileNotFoundException, InvalidPathException {
		return navigateTo(getFileAttributes(path), registerInHistory);
	}
	
	public FileAttributes navigateTo(final FileAttributes file, final boolean registerInHistory) throws FileNotFoundException, InvalidPathException {		
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
	
	public FileAttributes getCurrentParent() throws FileNotFoundException {
		return getParent(currentWorkingDirectory);
	}
		
	public abstract FileAttributes getParent(final FileAttributes file) throws FileNotFoundException;
	
	public abstract boolean canGoToParent();
	
	public abstract FileAttributes getFileAttributes(final String absolutePath) throws FileNotFoundException;
	
//	public FileAttributes createNewFile(final String name) throws IOException, FileAlreadyExistsException {
//		return createNew(name, false);
//	}
//	
//	public FileAttributes createNewDirectory(final String name) throws IOException, FileAlreadyExistsException {
//		return createNew(name, true);
//	}
	
	public abstract FileAttributes createNew(final String name, final boolean isDirectory) throws IOException, FileAlreadyExistsException;
	
	public abstract FileAttributes[] listFiles(final FileAttributes dir) throws InvalidPathException, FileNotFoundException, AccessDeniedException;
//	public abstract List<FileAttributes> listRoots() throws FileNotFoundException;
	
	public abstract FileAttributes getHomeDirectory() throws FileNotFoundException;
	public abstract FileAttributes getRootDirectory() throws FileNotFoundException;
	
	public abstract void openFile(final FileAttributes file) throws UnsupportedOperationException, IOException;
	
	public abstract void printFile(FileAttributes file) throws IllegalArgumentException, IOException;
	
	/** 
	 * Returns the proper FileSystem subclass object. Detects for local/remote file.
	 */
//	public static FileSystemHandler getHandler(final String absolutePath) throws FileNotFoundException, InvalidPathException {
//		return absolutePath.toLowerCase().startsWith("ftp") ? 
//				new RemoteFileSystemHandler(absolutePath) : new LocalFileSystemHandler(absolutePath);
//	}
	
	public static FileSystemHandler getRemoteHandler(final String address, final String username, final String password) throws FileNotFoundException, InvalidPathException {
		return new RemoteFileSystemHandler(address, username, password);
	}
	
	public static FileSystemHandler getLocalHandler(final String path) throws FileNotFoundException, InvalidPathException {
		return new LocalFileSystemHandler(path);
	}
}
