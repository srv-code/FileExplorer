package gui.mytests.handlers.fs;

import java.io.File;


/** 
 * Encapsulates all the necessary attributes of java.io.File & org.apache.commons.net.ftp.FTPFile
 * Final class. Needs FileAttributeBuilder object to build.
 */
public final class FileAttributes {
	public boolean isReadable, isWritable, isExecutable, isDirectory, isHidden, isLocal, isLink;
	public String name, absolutePath, type, sizeInWords, lastModifiedDateString;
	public long lastModified, size;
	
	public FileAttributes(	final boolean isReadable, 
							final boolean isWritable, 
							final boolean isExecutable, 
							final boolean isDirectory, 
							final boolean isHidden, 
							final boolean isLink,
							final boolean isLocal,
							final String name,
							final String absolutePath,
							final long lastModified, 
							final long size) {
		this.isReadable = isReadable; 
		this.isWritable = isWritable; 
		this.isExecutable = isExecutable; 
		this.isDirectory = isDirectory; 
		this.isHidden = isHidden; 
		this.isLocal = isLocal;
		this.name = name; 
		this.absolutePath = absolutePath;
		this.lastModified = lastModified;
		this.size = size;
		this.isLink = isLink;
		setType();
		setSizeInWords();
		setlastModifiedDateString();
	}
	
	private void setlastModifiedDateString() {
		lastModifiedDateString = String.format("%tY/%<tm/%<td %<tI:%<tM:%<tS %<Tp", lastModified);
	}
	
	private void setType() {
		if(isDirectory)
			type = "folder";
		else if(isLink)
			type = "link";
		else {
			int dotIdx = absolutePath.lastIndexOf('.');
			type = dotIdx == -1 ? "file" : absolutePath.substring(dotIdx+1).toLowerCase();
		}
	}
	
	private final double KB = Math.pow(1024.0, 1.0);
	private final double MB = Math.pow(1024.0, 2.0);
	private final double GB = Math.pow(1024.0, 3.0);
	
	private void setSizeInWords() {
		final double s = (double)size;
		if		(s < KB)	sizeInWords = String.format("%d B", size);
		else if	(s < MB)	sizeInWords = String.format("%.2f K", s/KB);
		else if	(s < GB)	sizeInWords = String.format("%.2f M", s/MB);
		else				sizeInWords = String.format("%.2f G", s/GB);
	}
	
	@Override 
	public String toString() {
		return name;
	}
}
