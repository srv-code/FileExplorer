package fileexplorer.handlers.fs.nav;

import java.io.IOException;


public class NavigationException extends Exception {
	NavigationException(final String msg) {
		super(msg);
	}

	NavigationException(final Exception e) {
		super("Unhandled", e);
	}
}
