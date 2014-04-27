package com.clinkworks.neptical.data.graph;

import com.clinkworks.neptical.data.domain.DataElement;


public interface Node {
	
	public String getIdentity();
	
	//use this interface is its possible for two identities to be shared.
	//arcs are made from the relationships between identities.
	public String getConflictIdentifier();
	
	public DataElement getContents();
	
	public String getContentsAsString();
	
}
