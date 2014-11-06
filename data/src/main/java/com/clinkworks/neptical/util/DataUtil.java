package com.clinkworks.neptical.util;

import org.apache.commons.lang3.ClassUtils;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.datatype.NepticalData;

public class DataUtil {

	private DataUtil() {
		Common.noOp("This class is designed to be used as a static utility");
	}

	public static final Data wrap(NepticalData nepticalData) {
		
		if(nepticalData == null){
			return null;
		}

		if (nepticalDataIsWrapped(nepticalData)) {
			return (Data) nepticalData;
		}

		return new Data(nepticalData);
	}

	private static final boolean nepticalDataIsWrapped(NepticalData nepticalData) {
		return ClassUtils.isAssignable(nepticalData.getClass(), Data.class);
	}

	public static final boolean isDataType(Class<? extends NepticalData> dataType, NepticalData nepticalData) {

		if (nepticalDataIsWrapped(nepticalData)) {
			return ((Data) nepticalData).isDataType(dataType);
		}

		return ClassUtils.isAssignable(nepticalData.getClass(), dataType);
	}

	@SuppressWarnings("unchecked")
	public static final <T extends NepticalData> T getAsDataType(Class<? extends NepticalData> dataType, NepticalData nepticalData) {

		if (!isDataType(dataType, nepticalData)) {
			throw new IllegalStateException("This data is not of type "
					+ dataType + " (" + nepticalData + ")");
		}

		return (T) nepticalData;
	}
}
