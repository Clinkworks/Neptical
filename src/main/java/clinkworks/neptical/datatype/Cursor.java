package clinkworks.neptical.datatype;


public interface Cursor {
	
	Cursor moveToLocation(Location location);

	Location getLocation();

	Location find(String query) throws DataDefinitionException;
}
