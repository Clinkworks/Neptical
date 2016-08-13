package clinkworks.neptical.domain;

import java.util.List;


import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NSpace;
import clinkworks.neptical.datatype.NepticalData;

public class GraphDataModule implements DataModule, CursorContext{
	
	private final DataModule dataModule;
	private final CursorContext cursorContext;
	
	public GraphDataModule(String name) {
		dataModule = new GenericDataModule(name);
		cursorContext = new GraphCursor();
		
		NSpace defaultNspace = GenericSpace.DEFAULT_NSPACE;
		
		if(!defaultNspace.containsModule(name)){
			defaultNspace.addModule(this);
		}
		
	}

	@Override
	public String getName() {
		return dataModule.getName();
	}

	@Override
	public String[] segments() {
		return dataModule.segments();
	}

	@Override
	public void addData(String segment, NepticalData data) throws DataDefinitionException {
		dataModule.addData(segment, data);
	}

	@Override
	public List<NepticalData> getAllData() {
		return dataModule.getAllData();
	}

	@Override
	public List<NepticalData> getDataAt(String segment) {
		return dataModule.getDataAt(segment);
	}

	@Override
	public NepticalData getDataAt(String segment, int index) {
		return dataModule.getDataAt(segment, index);
	}

	@Override
	public CursorContext getCursorContext() {
		return this;
	}


	@Override
	public Location getLocation() {
		return cursorContext.getLocation();
	}


	@Override
	public String[] templates() {
		return cursorContext.templates();
	}
	
	
	
}
