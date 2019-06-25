package console.fs;

import java.io.*;


public class FileSystemOperations {
	static boolean showDebugInfo = false;
	static boolean allowDelete = true; // safe guard against any fatal operation

	/**
	 * Main dispatcher for all supported file operations.
	 */
	public static void main(String[] args) throws Exception {
		showDebugInfo = true;
		FileSystemOperations op = new FileSystemOperations();
		switch(args[0]) {
			case "-mv": op.move(args[1], args[2]); break;
			case "-cp": op.copy(args[1], args[2]); break;
			case "-rm": op.delete(args[1]); break;
			case "-count": op.count(args[1]); break;
			default: throw new IllegalArgumentException("Invalid option: " + args[0]);
		}
	}

	private long cpFileCount=0L, cpDirCount=0L, rmFileCount=0L, rmDirCount=0L;

	/**
	 * Shows the count of the files and directories from the root.
	 * If the root is a file then the count is always 1, but if the
	 *   root is a directory then recursively calculates the count of
	 *   the entries.
	 * @param root Starting point for the operation. Can be a file or directory.
	 * */
	private void count(final String root) {
		File rootFile = new File(root);
		if(!rootFile.exists())
			throw new IllegalArgumentException("Root does not exist: " + root);
		dirCount = fileCount = 0L; // initialize counters
		count(rootFile);
		System.out.printf("Count: dirs=%d, files=%d, total=%d \n",
				dirCount, fileCount, dirCount+fileCount);
	}

	private long dirCount=0L, fileCount=0L;

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
	 * Handles both copy and move operations.
	 */
	private void paste(final String srcPath, final String dstPath, final boolean move) throws IllegalArgumentException, IOException {
		File src = new File(srcPath).getCanonicalFile();
		File dst = new File(dstPath).getCanonicalFile();

		if(!src.exists())
			throw new IllegalArgumentException("Source path does not exist: " + src);
		if(!dst.exists())
			throw new IllegalArgumentException("Destination path does not exist: " + dst);
		if(dst.getPath().startsWith(src.getPath()))
			throw new IllegalArgumentException("Destination path is a subpath of the source path");

		// Renames dst file name if already present in dst path
		int renameAttempt = 0;
		final String actFname = src.getName();
		String modFname = src.getName();
		while(new File(dst, modFname).exists()) {
			if(showDebugInfo)
				System.out.println("Already exists in dst dir: " + modFname);
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

		if(showDebugInfo)
			System.out.printf("[Initiating %s:]\n[\tsrc='%s']\n[\tdst='%s']\n", move ? "move" : "copy", src, dst);

		if(move)
			move(src, dst);
		else
			copy(src, dst);

		if(showDebugInfo) {
			System.out.printf("[Copied:  dirs=%d, files=%d]\n", cpDirCount, cpFileCount);
			if (move)
				System.out.printf("[Removed: dirs=%d, files=%d]\n", rmDirCount, rmFileCount);
		}
	}

	/**
	 * Main handler for move operation.
	 */
	private void move(final String srcPath, final String dstPath) throws IllegalArgumentException, IOException {
		paste(srcPath, dstPath, true);
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
	 * Main handler for copy operation.
	 */
	private void copy(final String srcPath, final String dstPath) throws IllegalArgumentException, IOException {
		paste(srcPath, dstPath, false);
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

	private final int BUF_SIZE = 8192; // 8KB
	/**
	 * If source is a file then copies it into destination,
	 *   else if directory, creates in the destination.
	 */
	private void copyFile(final File src, final File dst) throws IOException {
		if(showDebugInfo)
			System.out.printf("%5s:  '%s' \n", src.isDirectory() ? "mkdir" : "cp", dst);

		if(src.isDirectory()) {
			if(!dst.mkdir())
				throw new IOException("Cannot create directory: " + dst);
			cpDirCount++; // TODO [REMOVE LATER]
		} else {
			try (   FileInputStream fin = new FileInputStream(src);
			        FileOutputStream fout = new FileOutputStream(dst)) { // TODO [IGNORING PROGRESS REPORTING FOR NOW]
				// copying by block transfers
				int bytesRead;
				byte[] buf = new byte[BUF_SIZE];
				while((bytesRead=fin.read(buf)) != -1) {
					fout.write(buf, 0, bytesRead);
				}
			}
			cpFileCount++; // TODO [REMOVE LATER]
		}
	}

	/**
	 * Main handler for delete operation.
	 */
	private void delete(final String root) throws IllegalArgumentException, IOException {
		File file = new File(root).getCanonicalFile();
		if(!file.exists())
			throw new IllegalArgumentException("Does not exist: " + root);

		if(showDebugInfo)
			System.out.printf("[Initiating removal: root='%s']\n", file);

		delete(file);

		if(showDebugInfo)
			System.out.printf("[Removed: dirs=%d, files=%d]\n", rmDirCount, rmFileCount);
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

	/**
	 * Deletes specified file/directory.
	 */
	private void deleteFile(final File f) throws IOException {
		if(showDebugInfo)
			System.out.printf("%5s:  '%s' \n", f.isDirectory() ? "rmdir" : "rm", f);
		if(f.isDirectory())
			rmDirCount++;
		else
			rmFileCount++;
		if(allowDelete) {
			if (!f.delete())
				throw new IOException(
						String.format("Cannot remove %s: '%s'", f.isDirectory() ? "directory" : "file", f));
		}
	}
}
