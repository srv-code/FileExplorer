package fileexplorer.handlers.shared;



public final class BookmarkedItem {
	/* abandoned in favour of String constants */ /*
	public static enum ItemType {
		SYS_DRIVE("system drive"),
		LIBRARY_FOLDER("library folder"),
		PATH(null),
		REMOTE_SERVER("remote server");
		
		private String desc;
		
		ItemType(final String desc) {
			this.desc = desc;
		}
		
		@Override 
		public String toString() {
			return desc;
		}
	}
	*/
	
	// String type constants 
	public static final String TYPE_SYSTEM_DRIVE	= "system drive";
	public static final String TYPE_LIBRARY_FOLDER	= "library folder";
	public static final String TYPE_REMOTE_SERVER	= "remote server";
	
	
	public final String name; // display name
	public final String type;
	public final String absolutePath;
//	public final Icon icon;
//	public TreePath path; // path from JTree root
	
	
	public BookmarkedItem(final String name, final String type, final String absolutePath) {
		this.name = name;
		this.type = type;
		this.absolutePath = absolutePath;
//		this.icon = icon;
	}
	
	@Override 
	public String toString() {
//		return String.format("[%s: name=%s, path=%s]",
//				getClass().getSimpleName(), name, absolutePath);
		return name;
	}
	
	@Override 
	public boolean equals(final Object other) {
		if(other == this) 
			return true;
		if(!(other instanceof BookmarkedItem))
			return false;
		return ((BookmarkedItem)other).type.equals(this.type) && 
				((BookmarkedItem)other).absolutePath.equals(this.absolutePath);
	}
}