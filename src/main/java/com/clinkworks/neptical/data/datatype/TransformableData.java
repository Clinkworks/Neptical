package com.clinkworks.neptical.data.datatype;

import com.clinkworks.neptical.data.api.NepticalData;


public interface TransformableData extends NepticalData{
	public <T> T getAs(Class<T> type);
}
