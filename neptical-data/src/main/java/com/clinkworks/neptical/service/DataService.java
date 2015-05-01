package com.clinkworks.neptical.service;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.datatype.FileData;
import com.clinkworks.neptical.datatype.LoadableData;
import com.clinkworks.neptical.domain.GenericFileData;
import com.clinkworks.neptical.module.NepticalPropertiesModule.DataDirectory;
import com.clinkworks.neptical.spi.DataLoader;

@Singleton
public class DataService{
	
	private final Map<Serializable, DataLoader> dataLoaderRegistry;	
	private final File dataDirectory;
	
	@Inject
	public DataService(@DataDirectory File dataDirectory, Map<Serializable, DataLoader> dataLoaderRegistry){
		this.dataLoaderRegistry = dataLoaderRegistry;
		this.dataDirectory = dataDirectory;
	}
	
	protected Data loadData(){
		return loadFile(dataDirectory);
	}
	
	public Data loadFile(File file){
		FileData fileData = new GenericFileData(File.class, file);
		return loadData(fileData);	
		
	}

	public Data loadData(LoadableData loadableData) {
		
		if(loadableData.isLoaded()){
			return (Data)loadableData;
		}
		
		Serializable loaderCriterian = loadableData.getLoaderCriterian();
		
		DataLoader dataLoader = dataLoaderRegistry.get(loaderCriterian);
		
		if(dataLoader == null){
			throw new IllegalStateException("No data loader exists for criterian " + loadableData.getLoaderCriterian());
		}
		
		Data data = dataLoader.loadData(loadableData);
		
		//note: I realize this is probably an unsafe thing to do,
		//TODO: test the shit out of this method and make sure that it cannot cause a stack overflow.	
		if(data.isLoadableData() && !data.isLoaded()){
			return loadData(data);
		}
		
		return data;
	}
	
}
