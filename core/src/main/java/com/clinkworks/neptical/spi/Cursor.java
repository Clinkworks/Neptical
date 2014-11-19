package com.clinkworks.neptical.spi;

import java.io.Serializable;

import com.clinkworks.neptical.datatype.NepticalData;

public interface Cursor{
	public NepticalData find(Serializable notation);
}
