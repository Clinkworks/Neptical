package com.clinkworks.neptical.datatype;

import java.util.List;

import com.clinkworks.neptical.api.NepticalData;

public interface ListableData extends NepticalData{

	public List<Object> getList();

}
