package com.clinkworks.neptical.data.api;

import java.io.Serializable;
import java.util.Set;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.data.datatypes.LoadableData;


public interface DataLoader {
	
	public Data loadData(LoadableData loadableData);
	
	public Set<Serializable> getHandledDataLoaderCriterian();

}
