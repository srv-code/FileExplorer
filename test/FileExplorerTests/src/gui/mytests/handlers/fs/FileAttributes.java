package gui.mytests.handlers.fs;


/** 
 * Encapsulates all the necessary attributes of java.io.File & org.apache.commons.net.ftp.FTPFile
 * Final class. Needs FileAttributeBuilder object to build.
 */
public final class FileAttributes {
	public boolean isReadable, isWritable, isExecutable, isDirectory, isHidden, isLocal;
	public String name, absolutePath;
	long lastModified, length;
	
	public FileAttributes(	final boolean isReadable, 
							final boolean isWritable, 
							final boolean isExecutable, 
							final boolean isDirectory, 
							final boolean isHidden, 
							final boolean isLocal,
							final String name,
							final String absolutePath,
							final long lastModified, 
							final long length) {
		this.isReadable = isReadable; 
		this.isWritable = isWritable; 
		this.isExecutable = isExecutable; 
		this.isDirectory = isDirectory; 
		this.isHidden = isHidden; 
		this.isLocal = isLocal;
		this.name = name; 
		this.absolutePath = absolutePath;
		this.lastModified = lastModified;
		this.length = length;
	}
	
	@Override 
	public String toString() {
		return name;
	}
}
