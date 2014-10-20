package com.clinkworks.neptical.domain;

import com.clinkworks.neptical.api.NepticalData;
import com.clinkworks.neptical.datatype.MutableData;

public class GenericImmutableData implements NepticalData{

	private final NepticalData backingData;
	
	public GenericImmutableData(String name, Object data){
		MutableData mutableData = new GenericMutableData();
		mutableData.set(data);
		mutableData.setName(name);
		backingData = mutableData;
	}
	
	public GenericImmutableData(NepticalData data){
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
