package clinkworks.neptical.domain;

import java.net.URI;

import javax.inject.Provider;

import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NepticalData;

public class GenericCursorContext implements Provider<Cursor>, Location, CursorContext{

	private final Location rootLocation;
	private final Provider<Cursor> cursorProvider;
	
	public GenericCursorContext(Provider<Cursor> cursorProvider){
		this(NSpace.DEFAULT_NSPACE, cursorProvider);
	}
	
	public GenericCursorContext(Location rootLocation, Provider<Cursor> cursorProvider){
		this.cursorProvider = cursorProvider;
		this.rootLocation = rootLocation;
	}

	@Override
	public String context() {
		return rootLocation.context();
	}

	@Override
	public String fragment() {
		return rootLocation.fragment();
	}

	@Override
	public String name() {
		return rootLocation.name();
	}

	@Override
	public URI getResourceIdentity() {
		return rootLocation.getResourceIdentity();
	}

	@Override
	public Cursor moveCursorHere() {
		return get().moveToLocation(rootLocation);
	}

	@Override
	public NepticalData getData() {
		return rootLocation.getData();
	}

	@Override
	public Cursor get() {
		return cursorProvider.get();
	}
	
}