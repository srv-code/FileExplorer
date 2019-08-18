package fileexplorer.handlers.fs;

import org.apache.commons.net.ftp.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;


/**
 * Needs checking of the connection status at each stage.
 * So all public methods have ensureConnectivity() at the start,
 * therefore all those methods will throw IOException.
 */
public class RemoteFileSystemHandler extends FileSystemHandler {
	private String host, username, password;
	private FTPClient ftpClient;
	
	protected RemoteFileSystemHandler(	final String host, 
										final String username, 
										final String password) throws IOException {
//		super(absolutePath);
		this.host = host;
		this.username = username;
		this.password = password;
		currentWorkingDirectory = getFileAttributes(ROOT_PATH);
		
		ftpClient = new FTPClient();
	}
	
	public void setCredentials(final String host, final String username, final String password) throws IOException {
		if(ftpClient.isConnected()) // muust disconnect last connection to start a new
			throw new IOException("Last connection is alive: host=" + this.host + ", user: " + this.username);
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	@Override 
	public String getCurrentHostname() {
		return host;
	}
	
	@Override 
	public String getCurrentUsername() {
		return username;
	}
	
	private static boolean isSessionAlive = false;
	private boolean reinitialiseSession = false;
	
	private void ensureConnectivity() throws IOException {
		if(isSessionAlive) {
			isSessionAlive = false;
			try {
				ftpClient.getStatus();
				isSessionAlive = true;
			} catch (IOException e) {
				reinitialiseSession();
				isSessionAlive = true;
			}
		}
	}
	
	private void reinitialiseSession() throws IOException {
		reinitialiseSession = true;
		connect();
		reinitialiseSession = false; // reset flag
	}
	
	private final int FTP_PORT = 21;
	
	public void connect() throws IOException {
//		System.out.printf("  // host=%s, FTP_PORT=%d\n", host, FTP_PORT);
		ftpClient.connect(host, FTP_PORT);

		int replyCode = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(replyCode))
			throw new IOException("Connection failed. (Reply code:" + replyCode + ")");

		if (!ftpClient.login(username, password))
			throw new IOException("Log in failed. (Reply code: " + ftpClient.getReplyCode() + ")");
		
		// use local passive mode to pass firewall		
		ftpClient.enterLocalPassiveMode();
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
	public FileAttributes getFileAttributes(String absolutePath) throws IOException {
		if(absolutePath.equals(ROOT_PATH)) // build by hand
			return new FileAttributes(true, true, true, true, false, false, false, "/", "/", 0L, 4096L);
		ensureConnectivity();
		FTPFile file = null; 
		try {
			file = getFile(absolutePath);
		} catch(IOException e) {
			throw new FileNotFoundException(absolutePath + "(" + e.getMessage() + ")");
		}

		if(file == null) 
			throw new FileNotFoundException(absolutePath);

		return getFileAttributes(file, absolutePath);
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
	public FileAttributes[] listFiles(FileAttributes dir) throws IOException {
		ensureConnectivity();
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
	public FileAttributes getHomeDirectory() throws IOException {
		return getRootDirectory();
	}

	@Override
	public FileAttributes getRootDirectory() throws IOException {
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

	@Override
	public void close() throws IOException {		
//		ftpClient.getStatus(); // testing if session is alive
//		System.out.printf("  // Logging out FTP session, host=%s...\n", host);
		ftpClient.logout();
//		System.out.printf("  // Disconnecting FTP session, host=%s...\n", host);
		ftpClient.disconnect();
	}
}
