package clinkworks.neptical.datatype;

import javax.inject.Provider;

public interface CursorContext {
	Provider<Cursor> getContextCursorProvider();
	Location getLocation();
	String[] columns();
	String[] rows();
}
