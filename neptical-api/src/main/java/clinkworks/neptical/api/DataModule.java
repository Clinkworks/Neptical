package clinkworks.neptical.api;

import java.util.List;

import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.NepticalData;

public interface DataModule{

  String getName();

  String[] segments();

  void addData(String segment, NepticalData data) throws DataDefinitionException;

  List<NepticalData> getAllData();

  List<NepticalData> getDataAt(String segment);

  NepticalData getDataAt(String segment, int index);

  CursorContext getCursorContext();

}
