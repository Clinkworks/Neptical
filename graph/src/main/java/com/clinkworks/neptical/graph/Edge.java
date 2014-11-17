package com.clinkworks.neptical.graph;

import com.clinkworks.neptical.datatype.Identifiable;
import com.clinkworks.neptical.datatype.NepticalId;

public interface Edge extends Identifiable{

	public  NepticalId<?> getId();
	public  Node getStart();
	public  Node getEnd();
	
}