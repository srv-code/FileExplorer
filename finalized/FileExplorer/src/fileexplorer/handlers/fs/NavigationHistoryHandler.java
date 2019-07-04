package fileexplorer.handlers.fs;

import java.util.*;


public class NavigationHistoryHandler {
	private final List<FileAttributes> list = new ArrayList<>();
	private int position=-1;
	private int lastPosition = -1;
	
	NavigationHistoryHandler() {}
	
	/* package-private methods */
	void append(final FileAttributes dir) {
		list.add(dir);
		lastPosition = position;
		position=list.size()-1;
	}
			
	FileAttributes removeCurrent() {		
		list.remove(position);
		position = lastPosition==list.size() ? --lastPosition : lastPosition;
		return list.get(position);
	}
	
	void clear() {
		list.clear();
		lastPosition=position=-1;
	}
	
	/**
	 * @return Previous visited path or null if 
	 * no more backward path in history is present
	 */
	public FileAttributes backward() {
		if(canGoBackward()) {
			lastPosition = position;
			return list.get(--position);
		} else 
			return null;
	}
	
	public boolean canGoBackward() {
		return position>0; // or return lastPosition>=0;
	}
	
	/**
	 * @return Next visited path or null if 
	 * no more forward path in history is present
	 */
	public FileAttributes forward() {
		if(canGoForward()) {
			lastPosition = position;
			return list.get(++position);
		} else 
			return null;
	}
	
	public boolean canGoForward() {
		return position<list.size()-1;
	}
	
	@Override 
	public String toString() {
		return String.format("{lastPosition=%d, position=%d, paths=%s}", lastPosition, position, list);
	}
	
	
	/** Tester */
	public static void main(String[] args) throws Exception {
		System.out.println("<init>");
		FileSystemHandler fsh = FileSystemHandler.getLocalHandler("C:/Users");		
		NavigationHistoryHandler history = fsh.historyHandler;
		System.out.println("  history=" + history);
		
		System.out.println("<appending>");
		history.append(fsh.getFileAttributes("C:/Users/soura"));
		System.out.println("  history=" + history);
		
		history.append(fsh.getFileAttributes("C:/Users/soura/Desktop"));
		System.out.println("  history=" + history);
		
		System.out.println("<back>");
		history.backward();
		System.out.println("  history=" + history);
		
		System.out.println("<appending>");
		history.append(fsh.getFileAttributes("C:/Users/soura/AppData"));
		System.out.println("  history=" + history);
		
		System.out.println("<remove current>");
		history.removeCurrent();
		System.out.println("  history=" + history);
	}
}
