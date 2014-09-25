package com.clinkworks.neptical.data.datatypes;

import com.clinkworks.neptical.data.api.NepticalProperty;

public interface MutableData extends NepticalProperty{

	public void set(Object object);
	
	public void setName(String name);

}
