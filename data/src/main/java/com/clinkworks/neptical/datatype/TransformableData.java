package com.clinkworks.neptical.datatype;

import com.clinkworks.neptical.api.NepticalData;


public interface TransformableData extends NepticalData{
	public <T> T getAs(Class<T> type);
}
