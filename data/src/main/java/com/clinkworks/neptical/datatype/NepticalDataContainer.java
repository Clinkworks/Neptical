package com.clinkworks.neptical.datatype;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.api.NepticalData;

public interface NepticalDataContainer {
	public void setNepticalData(NepticalData nepticalData);
	public NepticalData getNepticalData();
	public void setData(Data data);
	public Data getData();
}
