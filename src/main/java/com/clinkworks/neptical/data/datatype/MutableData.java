package com.clinkworks.neptical.data.datatype;

import com.clinkworks.neptical.data.api.NepticalData;

public interface MutableData extends NepticalData{

	public void set(Object object);
	
	public void setName(String name);

}
