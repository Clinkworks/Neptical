package com.clinkworks.neptical.util;

import org.apache.commons.lang3.ClassUtils;

import com.clinkworks.neptical.data.api.DataElement;
import com.clinkworks.neptical.data.datatypes.Data;

public class DataUtil {
	
	private DataUtil(){
		Common.noOp("This class is designed to be used as a static utility");
	}
	
	public static final boolean isDataType(Class<? extends Data> dataType, Data data){
		
		if(data instanceof DataElement){
			return ((DataElement)data).getAsDataType(dataType);
		}
		
		return ClassUtils.isAssignable(data.getClass(), dataType);
	}
	
	@SuppressWarnings("unchecked")
	public static final <T extends Data> T getAsDataType(Class<? extends Data> dataType, Data data){
		if(!isDataType(dataType, data)){
			throw new IllegalStateException("This data is not of type " + dataType + " (" + data + ")");
		}
		
		if(data instanceof DataElement){
			return ((DataElement)data).getAsDataType(dataType);
		}
		
		return (T)data;
	}
}
