package com.clinkworks.neptical.datatype;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.api.Cursor;
import com.clinkworks.neptical.graph.DataGraph;

public class Path implements Cursor{
 
	private final DataGraph dataGraph;
	
    Path(DataGraph dataGraph){
    	this.dataGraph = dataGraph;
    }
	
	@Override
	public Data find(String path) {
		return null;
	}
	

}
