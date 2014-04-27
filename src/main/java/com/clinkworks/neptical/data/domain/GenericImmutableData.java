package com.clinkworks.neptical.data.domain;

import com.clinkworks.neptical.data.datatypes.Data;
import com.clinkworks.neptical.data.datatypes.MutableData;

public class GenericImmutableData implements Data{

	private final Data backingData;
	
	public GenericImmutableData(String name, Object data){
		MutableData mutableData = new GenericMutableData();
		mutableData.set(data);
		mutableData.setName(name);
		backingData = mutableData;
	}
	
	public GenericImmutableData(Data data){
		backingData = data;
	}
	
	@Override
	public String getName() {
		return backingData.getName();
	}

	@Override
	public Object get() {
		return backingData.get();
	}

}
