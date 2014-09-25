package com.clinkworks.neptical.data.graph;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.data.api.NepticalProperty;


public class BasicNode implements Node{
	
	private final String identity;
	private final String conflictIdentifier;
	private final Data contents;
	
	BasicNode(String identity, String conflictIdentifier, Data contents){
		this.identity = identity;
		this.conflictIdentifier = conflictIdentifier;
		this.contents = contents;
	}

	@Override
	public <T extends NepticalProperty> T getDataAsType(Class<T> dataType){
		return getContents().getAsDataType(dataType);
	}
	
	@Override
	public String getIdentity(){
		return identity;
	}
	
	@Override
	public String getConflictIdentifier(){
		return conflictIdentifier;
	}
	
	@Override
	public Data getContents(){
		return contents;
	}
	
}
