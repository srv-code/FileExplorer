package gui.mytests.handlers.fs;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class LocalFileSystemHandler extends FileSystemHandler {
	
	protected LocalFileSystemHandler(final String absolutePath) throws FileNotFoundException, InvalidPathException {
		super(absolutePath);
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
		File newFile = new File(oldFile.getParent(), newName);
		if(oldFile.renameTo(newFile))
			return getFileAttributes(newFile.getAbsolutePath());
		else
			throw new IOException("Unknown");
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
		File file = new File(absolutePath);
		if(!file.exists())
			throw new FileNotFoundException(absolutePath);
			
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
			return getFileAttributes(file.getAbsolutePath());
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
			fileAttributesList[i] = getFileAttributes(fileList[i].getAbsolutePath());
		}
		return fileAttributesList;
	}

	@Override
	public List<FileAttributes> listRoots() throws FileNotFoundException {
		File[] roots = File.listRoots();
		List<FileAttributes> fileAttributesList = new ArrayList<>();
		for(File root : roots ) {
//		for(int i=0; i<roots.length; i++) {
			if(root.exists()) // Some drives (eg. DVD drives shows non-existent)
				fileAttributesList.add(getFileAttributes(root.getAbsolutePath()));
		}
		return fileAttributesList;
	}

	@Override
	public FileAttributes getUserHomeDirectory() throws FileNotFoundException {
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
	public void printFile(FileAttributes file) throws IllegalArgumentException, IOException {
		if(file.isDirectory) 
			throw new IllegalArgumentException("Not a file");
		else 
			desktop.print(new File(file.absolutePath));
	}
}
