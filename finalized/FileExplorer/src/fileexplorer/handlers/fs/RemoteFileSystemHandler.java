package fileexplorer.handlers.fs;

import org.apache.commons.net.ftp.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.util.List;


public class RemoteFileSystemHandler extends FileSystemHandler {
	final private String host, username, password;
	final private FTPClient ftpClient;
	
	protected RemoteFileSystemHandler(	final FTPClient ftpClient, 
										final String host, 
										final String username, 
										final String password) 
									throws FileNotFoundException, InvalidPathException {
//		super(absolutePath);
		this.ftpClient=ftpClient;
		this.host=host;
		this.username=username;
		this.password=password;
		this.fileSystemID = "Remote@" + host;
		
		currentWorkingDirectory = getFileAttributes(ROOT_PATH);
	}

	@Override
	public FileAttributes setExecuteFlag(FileAttributes file, boolean value) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes setReadFlag(FileAttributes file, boolean value) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes setWriteFlag(FileAttributes file, boolean value) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes rename(FileAttributes file, String newName) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public long[] count(FileAttributes root) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public long[] copy(FileAttributes src, FileAttributes dst) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public long[] move(FileAttributes src, FileAttributes dst) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public long[] delete(FileAttributes root) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes getParent(FileAttributes file) throws FileNotFoundException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	
	@Override
	public boolean canGoToParent() {
		return !currentWorkingDirectory.absolutePath.equals(ROOT_PATH);
	}

	@Override
	public FileAttributes getFileAttributes(String absolutePath) throws FileNotFoundException {
		if(absolutePath.equals(ROOT_PATH)) { // build by hand
			return new FileAttributes(true, true, true, true, false, false, false, "/", "/", 0L, 4096L);
		} else {
			FTPFile file = null; 
			try{
				file = getFile(absolutePath);
			} catch(IOException e) {
				throw new FileNotFoundException(absolutePath + "(" + e.getMessage() + ")");
			}
				
			if(file == null) 
				throw new FileNotFoundException(absolutePath);
			
			return getFileAttributes(file, absolutePath);
		}
	}
	
	private FileAttributes getFileAttributes(final FTPFile file, final String absolutePath) throws FileNotFoundException {
		return new FileAttributes(
					file.hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION),
					file.hasPermission(FTPFile.USER_ACCESS, FTPFile.WRITE_PERMISSION),
					file.hasPermission(FTPFile.USER_ACCESS, FTPFile.EXECUTE_PERMISSION),
					file.isDirectory(),
					false, // cannot determine /* @future */
					file.isSymbolicLink(),
					false, 
					file.getName(),
					absolutePath,
					file.getTimestamp().getTimeInMillis(),
					file.getSize());
	}
	
	private boolean pathExists(final String path) throws IOException {
		return getFile(path) != null;
	}
	
	/**
	 * <p>Gets the FTPFile object for the specified path, if exists. <br>
	 * Using ftpClient.mlistFile(String pathname) should be producing
	 * identical result acc to theory but its not. So this method is preferred.</p>
	 * <p><b>Note: This method wont work if path is root i.e. /</b></p>
	 * @return FTPFile object if exists else null
	 * */
	private FTPFile getFile(final String path) throws IOException {
		int divIdx = path.lastIndexOf('/')+1;
		String parent = path.substring(0, divIdx);
		String child = path.substring(divIdx);

		FTPFile file = null;
		for(FTPFile f : ftpClient.listFiles(parent))
			if(f.getName().equals(child)) {
				file = f;
				break;
			}
		return file;
	}

	@Override
	public FileAttributes createNew(String name, boolean isDirectory) throws IOException, FileAlreadyExistsException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FileAttributes[] listFiles(FileAttributes dir) throws InvalidPathException, FileNotFoundException, AccessDeniedException {
		FileAttributes[] fileAttributesList = null;
		try {
			checkIfDirectory(dir);
			FTPFile[] fileList = ftpClient.listFiles(dir.absolutePath);
			fileAttributesList = new FileAttributes[fileList.length];
			for(int i=0; i<fileList.length; i++)
				fileAttributesList[i] = getFileAttributes(fileList[i], dir.absolutePath+"/"+fileList[i].getName());
		} catch(Exception e) {
			throw new AccessDeniedException(dir.absolutePath);
		}
		return fileAttributesList;
	}

	@Override
	public FileAttributes getHomeDirectory() throws FileNotFoundException {
		return getRootDirectory();
	}

	@Override
	public FileAttributes getRootDirectory() throws FileNotFoundException {
		return getFileAttributes(ROOT_PATH);
	}

	@Override
	public void openFile(FileAttributes file) throws UnsupportedOperationException, IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void printFile(FileAttributes file) throws IllegalArgumentException, IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	
}
