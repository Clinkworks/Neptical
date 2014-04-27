package com.clinkworks.neptical.data.api;

import com.clinkworks.neptical.data.datatypes.MutableData;

public interface Cursor extends MutableData{
	public DataElement find(String dotNotation);
	public void add(String path, DataElement dataElement);
	public DataElement getData();
	public boolean delete();
}
