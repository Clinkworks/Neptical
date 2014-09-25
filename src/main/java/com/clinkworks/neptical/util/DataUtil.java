package com.clinkworks.neptical.util;

import org.apache.commons.lang3.ClassUtils;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.data.api.NepticalProperty;

public class DataUtil {
	
	private DataUtil(){
		Common.noOp("This class is designed to be used as a static utility");
	}
	
	public static final boolean isDataType(Class<? extends NepticalProperty> dataType, NepticalProperty data){
		
		if(data instanceof Data){
			return ((Data)data).getAsDataType(dataType);
		}
		
		return ClassUtils.isAssignable(data.getClass(), dataType);
	}
	
	@SuppressWarnings("unchecked")
	public static final <T extends NepticalProperty> T getAsDataType(Class<? extends NepticalProperty> dataType, NepticalProperty data){
		if(!isDataType(dataType, data)){
			throw new IllegalStateException("This data is not of type " + dataType + " (" + data + ")");
		}
		
		if(data instanceof Data){
			return ((Data)data).getAsDataType(dataType);
		}
		
		return (T)data;
	}
}
