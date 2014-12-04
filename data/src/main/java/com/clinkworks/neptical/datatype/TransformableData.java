package com.clinkworks.neptical.datatype;




public interface TransformableData extends NepticalData{
	public <T> T getAs(Class<T> type);
}
