package com.clinkworks.neptical.graph;

import com.clinkworks.neptical.datatype.Identifiable;
import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.domain.PublicId;

public interface Edge extends Identifiable{

	public  NepticalId<?> getId();
	public PublicId getPublicId();
	public  Node getStart();
	public  Node getEnd();
	
}