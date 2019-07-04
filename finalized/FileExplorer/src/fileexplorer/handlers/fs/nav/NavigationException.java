package fileexplorer.handlers.fs.nav;


public class NavigationException extends Exception {
	NavigationException(final String msg) {
		super(msg);
	}

	NavigationException(final Exception e) {
		super("Unhandled", e);
	}
}
