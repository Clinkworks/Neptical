package com.clinkworks.neptical.domain;

import com.clinkworks.neptical.datatype.DataContainer;
import com.clinkworks.neptical.datatype.NepticalData;

public class GenericDataContainer implements DataContainer{

	private volatile NepticalData nepticalData;
	
	@Override
	public void setNepticalData(NepticalData nepticalData) {
		this.nepticalData = nepticalData;
	}

	@Override
	public NepticalData getNepticalData() {
		return nepticalData;
	}

	@Override
	public boolean containsData() {
		return getNepticalData() != null;
	}

}
