package fileexplorer.handlers.fs;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class LocalFileSystemHandler extends FileSystemHandler {
	
	protected LocalFileSystemHandler(final String absolutePath) throws FileNotFoundException, InvalidPathException {
//		super(absolutePath);
		userHomeDirectory = getFileAttributes(getUserHomeDirectoryPath());
//		isRemoteHandler = false;
		currentWorkingDirectory = absolutePath==null ? userHomeDirectory : getFileAttributes(absolutePath);
				
		fileSystemID = String.format("Local@%s.%s",
				System.getProperty("os.name"), System.getProperty("os.version"));
	}
	
	public String getCurrentUsername() {
		return System.getProperty("user.name");
	}
	
	public String getUserHomeDirectoryPath() {
		return System.getProperty("user.home");
	}
	
	public String getTempDirectoryPath() {
		return System.getProperty("java.io.tmpdir");
	}
		
//	@Override
//	public boolean isPathValid(final String absolutePath) {
//		return new File(absolutePath).exists();
//	}

	
	/** Does not check for path validity */
//	@Override
//	public boolean isPathAbsolute(final String path) {
//		return new File(path).isAbsolute();
//	}

	@Override 
	public FileAttributes rename(final FileAttributes file, final String newName) throws IOException {
		File oldFile = new File(file.absolutePath);
//		System.out.printf("  // oldFile=%s, oldFile.exists()=%b\n", oldFile.getAbsolutePath(), oldFile.exists());
		File newFile = new File(oldFile.getParent(), newName);
		if(oldFile.renameTo(newFile))
			return getFileAttributes(newFile.getAbsolutePath());
		else
			throw new IOException();
	}
	
	@Override 
	public void delete(final FileAttributes file) throws IOException {
		if(!new File(file.absolutePath).delete())
			throw new IOException("Unknown");
	}

	/**
	 * @param absolutePath Must be an absolute path
	 */
	@Override
	public FileAttributes getFileAttributes(final String absolutePath) throws FileNotFoundException {
		return getFileAttributes(new File(absolutePath));
	}
	
	private FileAttributes getFileAttributes(final File file) throws FileNotFoundException {
		if(file.exists())
			return new FileAttributes(
							file.canRead(), 
							file.canWrite(),
							file.canExecute(),
							file.isDirectory(),
							file.isHidden(),
							Files.isSymbolicLink(file.toPath()),
							true,
							file.getName(),
							file.getAbsolutePath(),
							file.lastModified(),
							file.length());
		else 
			throw new FileNotFoundException(file.getAbsolutePath());
	}
	
	@Override 
	public FileAttributes getParent(final FileAttributes file) throws FileNotFoundException {
		String parentPath = new File(file.absolutePath).getParent();
		if(parentPath == null)
			throw new FileNotFoundException("reached root");
		return getFileAttributes(parentPath);
	}
	
	@Override 
	public boolean canGoToParent() {
		return new File(currentWorkingDirectory.absolutePath).getParent() != null;
	}

	/**
	 * @return If created successfully then 
	 *		returns the FileAttributes object else null.
	 */
	public FileAttributes createNew(final String name, final boolean isDirectory) throws IOException, FileAlreadyExistsException {
		File file = new File(currentWorkingDirectory.absolutePath, name);
		if(file.exists())
			throw new FileAlreadyExistsException(file.getAbsolutePath());
		if(isDirectory ? file.mkdir() : file.createNewFile()) {
			return getFileAttributes(file);
		} else {
			throw new IOException("Specified name '"+ name +"' not supported by current platform");
		}
	}

	@Override
	public FileAttributes[] listFiles(final FileAttributes dir) throws InvalidPathException, FileNotFoundException, AccessDeniedException {
		File dirToList = new File(checkIfDirectory(dir).absolutePath);		
		File[] fileList = dirToList.listFiles();
		if(fileList == null) 
			throw new AccessDeniedException(dir.absolutePath);
		FileAttributes[] fileAttributesList = new FileAttributes[fileList.length];
		for(int i=0; i<fileList.length; i++) {
			fileAttributesList[i] = getFileAttributes(fileList[i]);
		}
		return fileAttributesList;
	}

//	@Override
	public List<FileAttributes> listRoots() throws FileNotFoundException {
		File[] roots = File.listRoots();
		List<FileAttributes> fileAttributesList = new ArrayList<>();
		for(File root : roots ) {
//		for(int i=0; i<roots.length; i++) {
			if(root.exists()) // Some drives (eg. DVD drives shows non-existent)
				fileAttributesList.add(getFileAttributes(root));
		}
		return fileAttributesList;
	}

	@Override
	public FileAttributes getHomeDirectory() throws FileNotFoundException {
		if(userHomeDirectory == null)
			userHomeDirectory = getFileAttributes(System.getProperty("user.home"));
		return userHomeDirectory;
	}

//	@Override
//	public FileAttributes getTrashBinDirectory() {
//		
//	}

	/**
	 * @param file Can be a file or directory
	 */
	@Override
	public void openFile(FileAttributes file) throws UnsupportedOperationException, IOException {
		if(isDesktopSupported) {
			desktop.open(new File(file.absolutePath));
		} else {
			throw new UnsupportedOperationException("Opening file is not supported in this platform!");
		}
	}

	@Override
	public void printFile(final FileAttributes file) throws IllegalArgumentException, IOException {
		if(file.isDirectory) 
			throw new IllegalArgumentException("Not a file");
		else 
			desktop.print(new File(file.absolutePath));
	}

	@Override
	public FileAttributes setExecuteFlag(final FileAttributes file, final boolean value) throws IOException {
		File newFile = new File(file.absolutePath);
		if(newFile.setExecutable(value))
			return getFileAttributes(newFile);
		else
			throw new IOException();
	}

	@Override
	public FileAttributes setReadFlag(final FileAttributes file, final boolean value) throws IOException {
		File newFile = new File(file.absolutePath);		
		if(newFile.setReadable(value))
			return getFileAttributes(newFile);
		else
			throw new IOException();
	}

	@Override
	public FileAttributes setWriteFlag(FileAttributes file, final boolean value)  throws IOException {
		File newFile = new File(file.absolutePath);
		if(new File(file.absolutePath).setWritable(value))
			return getFileAttributes(newFile);
		else
			throw new IOException();
	}
}
