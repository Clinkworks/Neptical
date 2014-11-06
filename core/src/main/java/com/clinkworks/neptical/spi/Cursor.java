package com.clinkworks.neptical.spi;

import com.clinkworks.neptical.datatype.NepticalData;

public interface Cursor{
	public NepticalData find(String notation);
}
