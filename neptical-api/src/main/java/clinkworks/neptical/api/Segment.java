package clinkworks.neptical.api;

import java.util.List;

import clinkworks.neptical.datatype.NepticalData;


public interface Segment{

  CursorContext cursorContext();

  /**
   * 
   */
  List<NPoint> fragments();

  List<NepticalData> getData();

  String name();

}
