package com.clinkworks.neptical.data.graph;

import java.io.Serializable;

import com.clinkworks.neptical.data.api.DataElement;
import com.clinkworks.neptical.data.datatypes.Data;

public interface Node {
	
	public <T extends Data> T getDataAsType(Class<T> dataType);
	
	public Serializable getIdentity();
	
	public Serializable getConflictIdentifier();
	
	public DataElement getContents();
	
}
