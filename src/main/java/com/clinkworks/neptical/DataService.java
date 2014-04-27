package com.clinkworks.neptical;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.data.datatypes.Data;
import com.clinkworks.neptical.data.datatypes.DataLoader;
import com.clinkworks.neptical.data.datatypes.LoadableData;
import com.clinkworks.neptical.data.datatypes.MutableData;
import com.clinkworks.neptical.data.domain.DataElement;
import com.clinkworks.neptical.data.domain.GenericLoadableData;
import com.clinkworks.neptical.data.domain.GenericMutableData;
import com.clinkworks.neptical.data.graph.DataMultiGraph;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DataService{
	
	//TODO: reimplement this
	public static final Data loadData(){
		return null;
	}
	
	private final Map<Serializable, DataLoader> dataLoaderRegistry;
	private final DataMultiGraph dataMultiGraph;
	
	@Inject
	public DataService(DataMultiGraph dataMultiGraph, Map<Serializable, DataLoader> dataLoaderRegistry){
		this.dataLoaderRegistry = dataLoaderRegistry;
		this.dataMultiGraph = dataMultiGraph;
	}
	
	public DataElement loadFile(File file){
		MutableData mutableData = new GenericMutableData();
		LoadableData loadableData = new GenericLoadableData(File.class, mutableData);
		mutableData.set(file);
		return loadData(loadableData);
	}

	public DataElement loadData(LoadableData loadableData) {
		
		if(loadableData.isLoaded()){
			return (DataElement)loadableData;
		}
		
		Serializable loaderCriterian = loadableData.getLoaderCriterian();
		
		DataLoader dataLoader = dataLoaderRegistry.get(loaderCriterian);
		
		if(dataLoader == null){
			throw new IllegalStateException("No data loader exists for criterian " + loadableData.getLoaderCriterian());
		}
		
		DataElement data = dataLoader.loadData(loadableData);
		
		//note: I realize this is probably an unsafe thing to do,
		//TODO: test the shit out of this method and make sure that it cannot cause a stack overflow.	
		if(data.isLoadableData() && !data.isLoaded()){
			return loadData(data);
		}
		
		return data;
	}
	
}
