package clinkworks.neptical.domain;

import java.net.URI;

import javax.inject.Provider;

import clinkworks.neptical.component.Origin;
import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NepticalData;

public class GraphCursor implements CursorContext, Location, Provider<Cursor>{
	
	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public String[] segments() {
		return null;
	}

	@Override
	public String[] templates() {
		return null;
	}

	@Override
	public Cursor get() {
		return Origin.getCursor();
	}

	@Override
	public String context() {
		return null;
	}

	@Override
	public String fragment() {
		return null;
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public URI getResourceIdentity() {
		return null;
	}

	@Override
	public NepticalData getData() {
		return null;
	}

	@Override
	public Cursor moveCursorHere() {
		return null;
	}

}
