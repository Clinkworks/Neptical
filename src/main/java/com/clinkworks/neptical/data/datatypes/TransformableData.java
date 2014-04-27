package com.clinkworks.neptical.data.datatypes;


public interface TransformableData extends Data{
	public <T> T getAs(Class<T> type);
}
