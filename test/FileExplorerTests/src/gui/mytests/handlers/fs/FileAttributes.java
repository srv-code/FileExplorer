package gui.mytests.handlers.fs;

import java.io.File;


/** 
 * Encapsulates all the necessary attributes of java.io.File & org.apache.commons.net.ftp.FTPFile
 * Final class. Needs FileAttributeBuilder object to build.
 */
public final class FileAttributes {
	public final boolean isReadable, isWritable, isExecutable, isDirectory, isHidden, isLocal, isLink;
	public final String name, absolutePath, type, sizeInWords, lastModifiedDateString;
	public final long lastModified, size;
	
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
		this.type = getType();
		lastModifiedDateString = getlastModifiedDateString(lastModified);
		sizeInWords = getSizeInWords(size);
	}
	
	public static String getlastModifiedDateString(final long lastModifiedTime) {
		return String.format("%tY/%<tm/%<td %<tI:%<tM:%<tS %<Tp", lastModifiedTime);
	}
	
	private String getType() {
		if(isDirectory)
			return "folder";
		else if(isLink)
			return "link";
		else {
			int dotIdx = absolutePath.lastIndexOf('.');
			return dotIdx == -1 ? "file" : absolutePath.substring(dotIdx+1).toLowerCase();
		}
	}
	
	private static final double KB = Math.pow(1024.0, 1.0);
	private static final double MB = Math.pow(1024.0, 2.0);
	private static final double GB = Math.pow(1024.0, 3.0);
	
	public static String getSizeInWords(final long size) {
		final double s = (double)size;
		if		(s < KB)	return String.format("%d B", size);
		else if	(s < MB)	return String.format("%.2f K", s/KB);
		else if	(s < GB)	return String.format("%.2f M", s/MB);
		else				return String.format("%.2f G", s/GB);
	}
	
	@Override 
	public boolean equals(final Object other) {
		if(other == this)
			return true;
		
		if(!(other instanceof FileAttributes))
			return false;
		
		return ((FileAttributes)other).absolutePath.equals(this.absolutePath); 
	}
	
	@Override 
	public String toString() {
		return name;
	}
}
