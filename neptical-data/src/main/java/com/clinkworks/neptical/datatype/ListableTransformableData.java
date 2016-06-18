package com.clinkworks.neptical.datatype;

import java.util.List;

public interface ListableTransformableData extends NepticalData{

	public <T> List<T> getListAs(Class<T> type);

}
