package com.clinkworks.neptical.data.datatype;

import java.util.List;

import com.clinkworks.neptical.data.api.NepticalData;

public interface ListableTransformableData extends NepticalData{

	public <T> List<T> getListAs(Class<T> type);

}
