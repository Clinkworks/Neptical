package com.clinkworks.neptical.graph;

import com.clinkworks.neptical.api.Identifiable;
import com.clinkworks.neptical.api.NepticalId;
import com.clinkworks.neptical.api.PublicId;

public interface Edge extends Identifiable{

	public  Node getStart();

	public  Node getEnd();

	public PublicId getPublicId();
	
	public  NepticalId<?> getId();

}