package gui.mytests.handlers.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.util.List;


/**
 * Abstract class for general reference.
 */
public abstract class FileSystemHandler {
	protected FileAttributes userHomeDirectory = null, defaultStartLocation = null;
			
	public final NavigationHistoryHandler historyHandler = new NavigationHistoryHandler();
	
	protected FileAttributes currentWorkingDirectory = null;
	
	public FileAttributes getCurrentWorkingDirectory() {
		return currentWorkingDirectory;
	}
	
	protected FileAttributes checkIfDirectory(final FileAttributes file) throws InvalidPathException {
		if(!file.isDirectory)
			throw new InvalidPathException(file.absolutePath, "Path is not a directory");
		return file;
	}
	
	protected FileSystemHandler(final String absolutePath) throws FileNotFoundException, InvalidPathException {
		navigateTo(absolutePath, true);
//		historyHandler.clear();
	}
	
//	public abstract boolean isPathValid(final String absolutePath);
	
//	public abstract boolean isPathAbsolute(final String path);

	public FileAttributes navigateTo(final String path, final boolean registerInHistory) throws FileNotFoundException, InvalidPathException {
		return navigateTo(getFileAttributes(path), registerInHistory);
	}
	
	public FileAttributes navigateTo(final FileAttributes file, final boolean registerInHistory) throws FileNotFoundException, InvalidPathException {		
		currentWorkingDirectory = checkIfDirectory(file);
		if(registerInHistory) 
			historyHandler.append(file);
		System.out.printf("  // navigating to: %s, registerInHistory=%b, history=%s, canGoBackward=%b, canGoForward=%b\n", 
				file, registerInHistory, historyHandler, historyHandler.canGoBackward(), historyHandler.canGoForward());
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
	
	public abstract FileAttributes getParent() throws FileNotFoundException;
	
	public abstract boolean canGoToParent();
	
	public abstract boolean delete(final FileAttributes file);
	
	public abstract FileAttributes getFileAttributes(final String absolutePath) throws FileNotFoundException;
	
	public FileAttributes createNewFile(final String fileName) throws IOException, FileAlreadyExistsException {
		return createNew(fileName, false);
	}
	
	public FileAttributes createNewDirectory(final String dirName) throws IOException, FileAlreadyExistsException {
		return createNew(dirName, true);
	}
	
	protected abstract FileAttributes createNew(final String name, final boolean isDirectory) throws IOException, FileAlreadyExistsException;
	
	public abstract FileAttributes[] listFiles(final FileAttributes dir) throws InvalidPathException, FileNotFoundException;
	public abstract List<FileAttributes> listRoots() throws FileNotFoundException;
	
	public abstract FileAttributes getUserHomeDirectory() throws FileNotFoundException;
//	public abstract FileAttributes getTrashBinDirectory() throws FileNotFoundException;

	/** 
	 * Returns the proper FileSystem subclass object. Detects for local/remote file.
	 */
	public static FileSystemHandler getHandler(final String absolutePath) throws FileNotFoundException, InvalidPathException {
		return absolutePath.toLowerCase().startsWith("ftp") ? 
				new RemoteFileSystemHandler(absolutePath) : new LocalFileSystemHandler(absolutePath);
	}
}
