package com.clinkworks.neptical.data.graph;

import java.io.Serializable;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.data.api.NepticalProperty;

public interface Node {
	
	public <T extends NepticalProperty> T getDataAsType(Class<T> dataType);
	
	public Serializable getIdentity();
	
	public Serializable getConflictIdentifier();
	
	public Data getContents();
	
}
