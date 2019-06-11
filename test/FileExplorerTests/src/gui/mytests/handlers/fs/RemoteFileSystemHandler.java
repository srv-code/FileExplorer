package gui.mytests.handlers.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.util.List;


public class RemoteFileSystemHandler extends FileSystemHandler {
	
	protected RemoteFileSystemHandler(final String absolutePath) throws FileNotFoundException, InvalidPathException {
		super(absolutePath);
	}

	@Override
	public boolean delete(FileAttributes file) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes getFileAttributes(String absolutePath) throws FileNotFoundException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected FileAttributes createNew(String name, boolean isDirectory) throws IOException, FileAlreadyExistsException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes[] listFiles(FileAttributes dir) throws InvalidPathException, FileNotFoundException {
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

}
