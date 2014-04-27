package com.clinkworks.neptical.data.domain;

import com.clinkworks.neptical.data.datatypes.MutableData;
import com.clinkworks.neptical.util.Common;


public class GenericMutableData implements MutableData{
	
	private String name;
	private Object contents;
	
	public GenericMutableData(){
		Common.noOp("This object is not meant to have parameters");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object get() {
		return contents;
	}

	@Override
	public void set(Object object) {
		contents = object;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
