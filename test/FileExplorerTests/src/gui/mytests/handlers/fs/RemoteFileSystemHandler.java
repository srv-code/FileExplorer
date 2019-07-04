package gui.mytests.handlers.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.util.List;


public class RemoteFileSystemHandler extends FileSystemHandler {
	
	protected RemoteFileSystemHandler(final String absolutePath) throws FileNotFoundException, InvalidPathException {
		super(absolutePath);
//		fileSystemID = String.format("Remote@", )
	}

	@Override
	public FileAttributes getFileAttributes(String absolutePath) throws FileNotFoundException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes createNew(String name, boolean isDirectory) throws IOException, FileAlreadyExistsException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes[] listFiles(FileAttributes dir) throws InvalidPathException, FileNotFoundException, AccessDeniedException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<FileAttributes> listRoots() throws FileNotFoundException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes getUserHomeDirectory() throws FileNotFoundException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean canGoToParent() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void openFile(FileAttributes file) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void printFile(FileAttributes file) throws IllegalArgumentException, IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

//	@Override
//	public void openDirectoryUsingSystem(final FileAttributes dir) throws IllegalArgumentException, IOException {
//		throw new UnsupportedOperationException("Not supported for remote folders");
//	}

	@Override
	public FileAttributes getParent(FileAttributes file) throws FileNotFoundException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes rename(FileAttributes file, String newName) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void delete(FileAttributes file) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
