package com.clinkworks.neptical.data.datatypes;

import java.util.List;

import com.clinkworks.neptical.data.api.NepticalProperty;

public interface ListableData extends NepticalProperty{

	public List<Object> getList();

}
