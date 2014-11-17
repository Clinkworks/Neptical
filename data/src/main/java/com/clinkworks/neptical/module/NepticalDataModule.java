package com.clinkworks.neptical.module;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.api.DataLoader;
import com.clinkworks.neptical.loader.FileDataLoader;
import com.clinkworks.neptical.loader.JsonDataLoader;
import com.clinkworks.neptical.spi.GenericModuleTemplate;
import com.google.common.collect.Maps;
import com.google.inject.Provides;

public class NepticalDataModule extends GenericModuleTemplate{

	private final FileDataLoader fileDataLoader;
	private final JsonDataLoader jsonDataLoader;
	
	public NepticalDataModule(){
		fileDataLoader = new FileDataLoader();
		jsonDataLoader = new JsonDataLoader();
	}
	
	@Override
	protected void configure() {
		install(new NepticalPropertiesModule());
		install(new GraphModule());
		install(new VocabularyModule());
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
