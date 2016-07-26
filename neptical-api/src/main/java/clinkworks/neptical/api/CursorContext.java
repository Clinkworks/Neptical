package clinkworks.neptical.api;

import java.util.List;

import clinkworks.neptical.datatype.Cursor;

public interface CursorContext{

  Location getLocation();

  Cursor moveCursorHere();

  List<Location> segmentLocations();

  List<CursorContext> templateLocations();


}
