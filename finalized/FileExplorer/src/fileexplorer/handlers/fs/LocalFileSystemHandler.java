package fileexplorer.handlers.fs;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class LocalFileSystemHandler extends FileSystemHandler {
	
	protected LocalFileSystemHandler(final String absolutePath) throws IOException {
//		super(absolutePath);
		userHomeDirectory = getFileAttributes(getUserHomeDirectoryPath());
//		isRemoteHandler = false;
		currentWorkingDirectory = absolutePath==null ? userHomeDirectory : getFileAttributes(absolutePath);
	}	
	
	@Override
	public String getCurrentUsername() {
		return System.getProperty("user.name");
	}
	
	@Override 
	public String getCurrentHostname() {
		return System.getProperty("os.name");
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
	public FileAttributes[] listFiles(final FileAttributes dir) throws IOException {
		File dirToList = new File(checkIfDirectory(dir).absolutePath);
		File[] fileList = dirToList.listFiles();
		if(fileList == null) 
			throw new AccessDeniedException(dir.absolutePath);
		FileAttributes[] fileAttributesList = new FileAttributes[fileList.length];
		for(int i=0, len=fileList.length; i<len; i++) {
			fileAttributesList[i] = getFileAttributes(fileList[i]);
		}
		return fileAttributesList;
	}

//	@Override
	public List<FileAttributes> listRoots() throws IOException {
		File[] roots = File.listRoots();
		List<FileAttributes> fileAttributesList = new ArrayList<>();
		for(File root : roots ) {
//		for(int i=0, len=roots.length; i<len; i++) {
			if(root.exists()) // Some drives (eg. DVD drives shows non-existent)
				fileAttributesList.add(getFileAttributes(root));
		}
		return fileAttributesList;
	}

	@Override
	public FileAttributes getHomeDirectory() throws FileNotFoundException {
		if(userHomeDirectory == null) {
			try {
				userHomeDirectory = getFileAttributes(getUserHomeDirectoryPath());
			} catch(FileNotFoundException e) {
				userHomeDirectory = getRootDirectory();
			}
		}
		return userHomeDirectory;
	}
	
	@Override 
	public FileAttributes getRootDirectory() throws FileNotFoundException {
		return getFileAttributes(ROOT_PATH);
	};

//	@Override
//	public FileAttributes getTrashBinDirectory() {
//		
//	}

	/**
	 * @param file Can be a file or directory
	 */
	@Override
	public void openFile(final FileAttributes file) throws UnsupportedOperationException, IOException {
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
	public FileAttributes setWriteFlag(final FileAttributes file, final boolean value)  throws IOException {
		File newFile = new File(file.absolutePath);
		if(new File(file.absolutePath).setWritable(value))
			return getFileAttributes(newFile);
		else
			throw new IOException();
	}

	/**
	 * Shows the count of the files and directories from the root.
	 * If the root is a file then the count is always 1, but if the
	 *   root is a directory then recursively calculates the count of
	 *   the entries.
	 * @param root Starting point for the operation. Can be a file or directory.
	 * */
	@Override
	public long[] count(final FileAttributes root) throws IOException {
		File rootFile = new File(root.absolutePath);
		if(!rootFile.exists())
			throw new IOException("non-existent path: " + root.absolutePath);
		dirCount = fileCount = 0L; // initialize counters
		count(rootFile);
		return new long[] { dirCount, fileCount };
	}
	
	/**
	 * Recursive method to get the count of entries from a directory node.
	 * */
	private void count(final File root) {
//		System.out.printf("  // root=%s, exists?=%b, isDir=%b, listFiles=%s\n",
//				root, root.exists(), root.isDirectory(),
//				root.listFiles()==null ? "null" : "not null");
		if(root.isDirectory()) {
			dirCount++;
			for(File f : root.listFiles())
				count(f);
		} else
			fileCount++;
	}
	
	/**
	 * Main handler for copy operation.
	 */
	@Override
	public long[] copy(final FileAttributes src, final FileAttributes dst) throws IOException {
		return paste(src, dst, false);
	}
	
	/**
	 * Handles both copy and move operations.
	 */
	private long[] paste(final FileAttributes srcPath, final FileAttributes dstPath, final boolean move) throws IOException {
		File src = new File(srcPath.absolutePath).getCanonicalFile();
		File dst = new File(dstPath.absolutePath).getCanonicalFile();

		if(!src.exists())
			throw new IOException("Non-existent source path: " + src);
		if(!dst.exists())
			throw new IOException("Non-existent destination path: " + dst);
		if(dst.getPath().startsWith(src.getPath()))
			throw new IOException("Destination path is a subpath of the source path");

		// Renames dst file name if already present in dst path
		int renameAttempt = 0;
		final String actFname = src.getName();
		String modFname = actFname;
		while(new File(dst, modFname).exists()) {
//			if(showDebugInfo)
//				System.out.println("Already exists in dst dir: " + modFname);
			StringBuilder tmpName = new StringBuilder(actFname.length() + 5);

			if(src.isDirectory()) { // No extension checking for directories
				tmpName .insert(0, actFname)
						.append(" (").append(++renameAttempt).append(")");
			} else { // Extension checking for files
				int dotIdx = actFname.lastIndexOf('.');
				String ext = actFname.substring(dotIdx == -1 ? actFname.length() : dotIdx);
				String base = actFname.substring(0, dotIdx == -1 ? actFname.length() : dotIdx);
				tmpName	.insert(0, base)
						.append(" (").append(++renameAttempt).append(")")
						.append(ext);
			}
			modFname = tmpName.toString();
		}
		dst = new File(dst, modFname);

//		if(showDebugInfo)
//			System.out.printf("[Initiating %s:]\n[\tsrc='%s']\n[\tdst='%s']\n", move ? "move" : "copy", src, dst);

		cpDirCount = cpFileCount = rmDirCount = rmFileCount = 0L; // initialize counters

		if(move)
			move(src, dst);
		else
			copy(src, dst);

//		if(showDebugInfo) {
//			System.out.printf("[Copied:  dirs=%d, files=%d]\n", cpDirCount, cpFileCount);
//			if (move)
//				System.out.printf("[Removed: dirs=%d, files=%d]\n", rmDirCount, rmFileCount);
//		}

		return new long[] { cpDirCount, cpFileCount };
	}
	
	/**
	 * Recursively copies source to destination.
	 */
	private void copy(final File src, final File dst) throws IOException {
		copyFile(src, dst);
		if(src.isDirectory()) {
			for(File f : src.listFiles())
				copy(f, new File(dst, f.getName()));
		}
	}
	
	/**
	 * Recursively copies and deletes source to destination to achieve move operation.
	 */
	private void move(final File src, final File dst) throws IOException {
		copyFile(src, dst);
		if(src.isDirectory()) {
			for(File f : src.listFiles())
				move(f, new File(dst, f.getName()));
		}
		deleteFile(src);
	}
	
	/**
	 * If source is a file then copies it into destination,
	 *   else if directory, creates in the destination.
	 */
	private void copyFile(final File src, final File dst) throws IOException {
//		if(showDebugInfo)
//			System.out.printf("%5s:  '%s' \n", src.isDirectory() ? "mkdir" : "cp", dst);

		if(src.isDirectory()) {
			if(!dst.mkdir())
				throw new IOException("Cannot create directory: " + dst);
			cpDirCount++;
		} else {
			try (   FileInputStream fin = new FileInputStream(src);
			        FileOutputStream fout = new FileOutputStream(dst)) { // TODO [IGNORING PROGRESS REPORTING FOR NOW]
				// copying by block transfers
				int bytesRead;
				byte[] buf = new byte[PASTE_BUF_SIZE];
				while((bytesRead=fin.read(buf)) != -1) {
					fout.write(buf, 0, bytesRead);
				}
			}
			cpFileCount++;
		}
	}
	
	/**
	 * Deletes specified file/directory.
	 */
	private void deleteFile(final File f) throws IOException {
//		if(showDebugInfo)
//			System.out.printf("%5s:  '%s' \n", f.isDirectory() ? "rmdir" : "rm", f);
		if(f.isDirectory())
			rmDirCount++;
		else
			rmFileCount++;
		if(allowDelete) {
			if (!f.delete())
				throw new IOException(
						String.format("Cannot remove %s: '%s'", 
								f.isDirectory() ? FileAttributes.TYPE_FOLDER : FileAttributes.TYPE_FILE, f));
		}
	}

	@Override
	public long [] move(final FileAttributes src, final FileAttributes dst) throws IOException {
		return paste(src, dst, true);
	}

	/**
	 * Main handler for delete operation.
	 */
	@Override
	public long [] delete(final FileAttributes root) throws IOException {
		File file = new File(root.absolutePath).getCanonicalFile();
		if(!file.exists())
			throw new IOException("non-existent path: " + root);

//		if(showDebugInfo)
//			System.out.printf("[Initiating removal: root='%s']\n", file);

		rmDirCount = rmFileCount = 0L;
		delete(file);

//		if(showDebugInfo)
//			System.out.printf("[Removed: dirs=%d, files=%d]\n", rmDirCount, rmFileCount);

		return new long[] { rmDirCount, rmFileCount };
	}
	
	/**
	 * Recursively deletes from source path.
	 */
	private void delete(final File file) throws IOException {
		if(file.isDirectory()) {
			for(File f : file.listFiles())
				delete(f);
		}
		deleteFile(file);
	}

	@Override
	public void close() throws IOException { // nothing to do
	}
}
