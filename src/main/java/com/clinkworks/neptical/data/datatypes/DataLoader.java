package com.clinkworks.neptical.data.datatypes;

import java.io.Serializable;
import java.util.Set;

import com.clinkworks.neptical.data.domain.DataElement;


public interface DataLoader {
	
	public DataElement loadData(LoadableData loadableData);
	public Set<Serializable> getHandledDataLoaderCriterian();
	
}
