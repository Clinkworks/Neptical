package com.clinkworks.neptical.modules;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.api.DataLoader;
import com.clinkworks.neptical.loader.FileDataLoader;
import com.clinkworks.neptical.loader.JsonDataLoader;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class NepticalDataModule extends AbstractModule{

	private final FileDataLoader fileDataLoader;
	private final JsonDataLoader jsonDataLoader;
	
	public NepticalDataModule(){
		fileDataLoader = new FileDataLoader();
		jsonDataLoader = new JsonDataLoader();
	}
	
	@Override
	protected void configure() {
		
	}
	
	@Provides
	public Map<Serializable, DataLoader> dataLoaderRegistry(){
		Map<Serializable, DataLoader> dataLoaders = Maps.newHashMap();
		
		dataLoaders.put(File.class, fileDataLoader);
		dataLoaders.put("txt", fileDataLoader);
		dataLoaders.put("json", jsonDataLoader);
		
		return dataLoaders;
	}
	
	
	
}
