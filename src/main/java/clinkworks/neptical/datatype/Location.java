package clinkworks.neptical.datatype;

import java.net.URI;

public interface Location {
	String context();
	String fragment();
	String name();
	URI getResourceIdentity();
	Cursor moveCursorHere();
	NepticalData getData();
}
