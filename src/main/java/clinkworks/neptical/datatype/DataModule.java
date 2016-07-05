package clinkworks.neptical.datatype;

import java.util.List;

public interface DataModule {

	String getName();
	
	String[] segments();
	
	void addData(String segment, NepticalData data) throws DataDefinitionException;
	
	List<NepticalData> getAllData();
	
	List<NepticalData> getDataAt(String segment);
	
	NepticalData getDataAt(String segment, int index);

	CursorContext getCursorContext();

}
