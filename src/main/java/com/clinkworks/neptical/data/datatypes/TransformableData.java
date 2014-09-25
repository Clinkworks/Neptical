package com.clinkworks.neptical.data.datatypes;

import com.clinkworks.neptical.data.api.NepticalProperty;


public interface TransformableData extends NepticalProperty{
	public <T> T getAs(Class<T> type);
}
