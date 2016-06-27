package clinkworks.neptical.datatype;

import java.util.List;

public interface DataModule {

	String getName();
	
	String[] segments();
	
	void addData(String segment, NepticalData data) throws DataDefinitionException;
	
	List<NepticalData> getData();
	
	List<NepticalData> getData(String segment);
	
	NepticalData getData(String segment, int index);

}
