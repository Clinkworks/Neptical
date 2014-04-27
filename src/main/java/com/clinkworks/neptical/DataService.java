package com.clinkworks.neptical;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.data.api.Cursor;
import com.clinkworks.neptical.data.api.DataElement;
import com.clinkworks.neptical.data.api.DataLoader;
import com.clinkworks.neptical.data.datatypes.LoadableData;
import com.clinkworks.neptical.data.domain.FileData;
import com.clinkworks.neptical.modules.NepticalDataModule.NepticalDataApiFactory;
import com.clinkworks.neptical.modules.NepticalPropertiesModule.DataDirectory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DataService{
	
	private final Map<Serializable, DataLoader> dataLoaderRegistry;
	private final NepticalDataApiFactory dataApiFactory;
	
	private File dataDirectory;
	
	@Inject
	public DataService(@DataDirectory File dataDirectory, NepticalDataApiFactory dataApiFactory, Map<Serializable, DataLoader> dataLoaderRegistry){
		this.dataLoaderRegistry = dataLoaderRegistry;
		this.dataApiFactory = dataApiFactory;
		this.dataDirectory = dataDirectory;
	}
	
	public Cursor loadData(){
		return dataApiFactory.create(loadFile(dataDirectory));
	}
	
	public DataElement loadFile(File file){
		FileData fileData = new FileData(file);
		return loadData(fileData);
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
