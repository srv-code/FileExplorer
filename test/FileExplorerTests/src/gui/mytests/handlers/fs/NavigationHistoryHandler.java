package gui.mytests.handlers.fs;

import java.util.*;


public class NavigationHistoryHandler {
	private final List<FileAttributes> list = new ArrayList<>();
	private int position=-1;
	
	NavigationHistoryHandler() {}
	
	/* package-private methods */
	void append(final FileAttributes dir) {
		list.add(dir);
		position=list.size()-1; // reset backPosition to latest element
	}
	
	FileAttributes removeCurrent() {
		list.remove(position--);
		return list.get(position);
	}
	
	void clear() {
		list.clear();
		position=-1;
	}
	
	/**
	 * @return Previous visited path or null if 
	 * no more backward path in history is present
	 */
	public FileAttributes backward() {
		return canGoBackward() ? list.get(--position) : null;
	}
	
	public boolean canGoBackward() {
		return position>0;
	}
	
	/**
	 * @return Next visited path or null if 
	 * no more forward path in history is present
	 */
	public FileAttributes forward() {
		return canGoForward() ? list.get(++position) : null;
	}
	
	public boolean canGoForward() {
		return position<list.size()-1;
	}
	
	@Override 
	public String toString() {
		return String.format("{position=%d, paths=%s}", position, list);
	}
	
	/*
	public static void main(String[] args) throws Exception {
		System.out.println("FileSystemHandler.<init>");
		FileSystemHandler fsh = FileSystemHandler.getHandler("C:/Users/");
		NavigationHistoryHandler history = fsh.historyHandler;
		
		System.out.println("append:");
		history.append(fsh.getFileAttributes("C:/Users/soura"));
		System.out.println("  history=" + history + "\n");
		
		System.out.println("append:");
		history.append(fsh.getFileAttributes("C:/Users/soura/Desktop"));
		System.out.println("  history=" + history + "\n");
		
		System.out.println("append:");
		history.append(fsh.getFileAttributes("C:/Users/soura/Desktop/Transfer to ext HDD"));
		System.out.println("  history=" + history + "\n");
		
		System.out.println("back:");
		System.out.println("  dir=" + history.backward());
		System.out.println("  history=" + history + "\n");
		
		System.out.println("back:");
		System.out.println("  dir=" + history.backward());
		System.out.println("  history=" + history + "\n");
		
		System.out.println("back:");
		System.out.println("  dir=" + history.backward());
		System.out.println("  history=" + history + "\n");
		
		System.out.println("back:");
		System.out.println("  dir=" + history.backward());
		System.out.println("  history=" + history + "\n");
		
		
		System.out.println("forward:");
		System.out.println("  dir=" + history.forward());
		System.out.println("  history=" + history + "\n");
		
		System.out.println("forward:");
		System.out.println("  dir=" + history.forward());
		System.out.println("  history=" + history + "\n");
		
		System.out.println("forward:");
		System.out.println("  dir=" + history.forward());
		System.out.println("  history=" + history + "\n");
		
		System.out.println("forward:");
		System.out.println("  dir=" + history.forward());
		System.out.println("  history=" + history + "\n");
	}
	*/
}
