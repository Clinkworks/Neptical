package com.clinkworks.neptical.domain;

import com.clinkworks.neptical.datatype.NepticalData;

public class GenericImmutableData implements NepticalData{
	
	private final String name;
	private final Object data;
	
	public GenericImmutableData(String name, Object data){
		this.name = name;
		this.data = data;
	}
	
	public GenericImmutableData(NepticalData data){
		this.name = data.getName();
		this.data = data.get();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object get() {
		return data;
	}

	@Override
	public Class<? extends NepticalData> getNepticalDataType() {
		return getClass();
	}

}
