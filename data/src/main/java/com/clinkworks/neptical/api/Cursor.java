package com.clinkworks.neptical.api;

import com.clinkworks.neptical.datatype.NepticalData;

public interface Cursor{
	public NepticalData find(String notation);
}
