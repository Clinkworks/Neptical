package com.clinkworks.neptical.spi;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class DataLoaderCriteria {
	
	private final Set<DataLoaderCriterian> dataLoaderCriteria;
	
	DataLoaderCriteria(DataLoaderCriterian... criterians){
		dataLoaderCriteria = ImmutableSet.copyOf(criterians);
	}

	public Set<DataLoaderCriterian> getDataLoaderCriteria() {
		return dataLoaderCriteria;
	}
}
