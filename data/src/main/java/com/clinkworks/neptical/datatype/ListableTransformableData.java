package com.clinkworks.neptical.datatype;

import java.util.List;

import com.clinkworks.neptical.api.NepticalData;

public interface ListableTransformableData extends NepticalData{

	public <T> List<T> getListAs(Class<T> type);

}
