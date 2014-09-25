package com.clinkworks.neptical.data.datatype;

import java.util.List;

import com.clinkworks.neptical.data.api.NepticalData;

public interface ListableData extends NepticalData{

	public List<Object> getList();

}
