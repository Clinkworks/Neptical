package com.clinkworks.neptical.data.datatypes;

import java.util.List;

public interface ListableTransformableData extends Data{
	public <T> List<T> getListAs(Class<T> type);
}
