package gui.mytests.handlers.fs;

/**
 * Builds the final class FileAttributes's object.
 * Package private.
 * Contains setters for all fields of FileAttributes, each returning this object for chaining.
 */
@Deprecated
class FileAttributeBuilder {
	public boolean isReadable, isWritable, isExecutable, isDirectory, isHidden, isLocal, isLink;
	public String name, absolutePath;
	long lastModified, length;
	
	FileAttributes build() {
		return new FileAttributes(	isReadable, isWritable, isExecutable, 
									isDirectory, isHidden, isLink,
									isLocal,
									name, absolutePath, lastModified, length);
	}
	
	FileAttributeBuilder setStatusBits(final boolean r, final boolean w, boolean x) {
		isReadable = r;
		isWritable = w;
		isExecutable = x;
		return this;
	}
	
//	FileAttributeBuilder 
}