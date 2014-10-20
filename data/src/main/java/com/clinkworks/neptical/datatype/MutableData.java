package com.clinkworks.neptical.datatype;

import com.clinkworks.neptical.api.NepticalData;

public interface MutableData extends NepticalData{

	public void set(Object object);
	
	public void setName(String name);

}
