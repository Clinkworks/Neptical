package clinkworks.neptical.datatype;

import java.net.URI;

public interface Location {
	DataModule parentModule();
	String name();
	int rowId();
	URI getResourceIdentity();
	Cursor moveCursorHere();
	NepticalData get();
}
