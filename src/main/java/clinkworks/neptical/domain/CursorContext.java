package clinkworks.neptical.domain;

import javax.inject.Inject;
import javax.inject.Provider;

import clinkworks.neptical.datatype.Cursor;

public class CursorContext implements Provider<Cursor>{

	private final Provider<Cursor> cursorProvider;
	
	@Inject
	public CursorContext(Provider<Cursor> cursorProvider){
		this.cursorProvider = cursorProvider;
	}

	@Override
	public Cursor get() {
		return cursorProvider.get();
	}
	
}
