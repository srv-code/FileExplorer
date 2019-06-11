package gui.mytests.handlers.fs;

import java.util.*;


class NavigationHistoryHandler {
	private final List<FileAttributes> list = new ArrayList<>();
	private int position=0;
	
	public NavigationHistoryHandler() {}
	
	void append(final FileAttributes dirPath) {
		list.add(dirPath);
		position=list.size()-1; // reset position to latest element
	}
	
	/**
	 * @return Previous visited path or null if 
	 * no more backward path in history is present
	 */
	FileAttributes backward() {
		return position==0 ? null : list.get(--position);
	}
	
	/**
	 * @return Next visited path or null if 
	 * no more forward path in history is present
	 */
	FileAttributes forward() {
		return position==list.size()-1 ? null : list.get(++position);
	}
	
	@Override 
	public String toString() {
		return String.format("history: {position=%d, paths=%s}", position, list);
	}
	
	/*
	public static void main(FileAttributes[] args) {
		NavigationHistoryHandler history = new NavigationHistoryHandler();
		history.append("a/b/c");
		history.append("a/b/c/d");
		history.append("a/b/c/d/e");
		
		System.out.println(history);
		
		System.out.println("back=" + history.back());
		System.out.println("back=" + history.back());
		System.out.println("back=" + history.back());
		System.out.println("back=" + history.back());
		System.out.println("back=" + history.back());
		System.out.println("forward=" + history.forward());
		System.out.println("forward=" + history.forward());
		System.out.println("forward=" + history.forward());
		System.out.println("forward=" + history.forward());
		System.out.println("forward=" + history.forward());
		
		history.append("p/q");
		history.append("p/q/r");
		
		System.out.println("back=" + history.back());
		System.out.println("back=" + history.back());
		System.out.println("forward=" + history.forward());
		System.out.println("forward=" + history.forward());
		
		System.out.println(history);
	}
	*/
}
