package com.clinkworks.neptical.spi;

import java.io.Serializable;
import java.util.Set;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.datatype.LoadableData;


public interface DataLoader {
	
	public Data loadData(LoadableData loadableData);
	
	public Set<Serializable> getHandledDataLoaderCriterian();

}
