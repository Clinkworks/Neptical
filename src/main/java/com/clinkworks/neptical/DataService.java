package com.clinkworks.neptical;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.data.api.Cursor;
import com.clinkworks.neptical.data.api.DataLoader;
import com.clinkworks.neptical.data.datatype.LoadableData;
import com.clinkworks.neptical.modules.NepticalPropertiesModule.DataDirectory;
import com.clinkworks.neptical.property.FileProperty;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DataService implements Cursor{
	
	private final Map<Serializable, DataLoader> dataLoaderRegistry;	
	private File dataDirectory;
	
	@Inject
	public DataService(@DataDirectory File dataDirectory, Map<Serializable, DataLoader> dataLoaderRegistry){
		this.dataLoaderRegistry = dataLoaderRegistry;
		this.dataDirectory = dataDirectory;
	}
	
	public Data loadData(){
		return loadFile(dataDirectory);
	}
	
	public Data loadFile(File file){
		FileProperty fileData = new FileProperty(file);
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

	@Override
	public Data find(String notation) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
