package com.clinkworks.neptical.data.datatypes;

import java.util.List;

import com.clinkworks.neptical.data.api.NepticalProperty;

public interface ListableTransformableData extends NepticalProperty{

	public <T> List<T> getListAs(Class<T> type);

}
