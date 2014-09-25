package com.clinkworks.neptical.data.graph;

import java.io.Serializable;

import com.clinkworks.neptical.data.api.NepticalProperty;

public interface Edge extends NepticalProperty{
		
	public Node getStart();
	
	public Node getEnd();
		
	public NepticalProperty createAlias(Serializable aliasId);
	
}
