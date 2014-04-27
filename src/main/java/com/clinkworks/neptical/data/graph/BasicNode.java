package com.clinkworks.neptical.data.graph;

import com.clinkworks.neptical.data.api.DataElement;
import com.clinkworks.neptical.data.datatypes.Data;


public class BasicNode implements Node{
	
	private final String identity;
	private final String conflictIdentifier;
	private final DataElement contents;
	
	BasicNode(String identity, String conflictIdentifier, DataElement contents){
		this.identity = identity;
		this.conflictIdentifier = conflictIdentifier;
		this.contents = contents;
	}

	@Override
	public <T extends Data> T getDataAsType(Class<T> dataType){
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
	public DataElement getContents(){
		return contents;
	}
	
}
