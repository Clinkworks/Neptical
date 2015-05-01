package com.clinkworks.neptical.module;

import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.loader.FileDataLoader;
import com.clinkworks.neptical.loader.JsonDataLoader;
import com.clinkworks.neptical.service.CursorService;
import com.clinkworks.neptical.service.NepticalTemplateService;
import com.clinkworks.neptical.spi.Cursor;
import com.clinkworks.neptical.spi.DataLoader;
import com.clinkworks.neptical.spi.GenericModuleTemplate;
import com.github.jknack.handlebars.Handlebars;
import com.google.common.collect.Maps;
import com.google.inject.Provides;
import com.google.inject.Singleton;

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
		install(new VocabularyModule());
		install(new TestDataModule());
		install(new GraphModule());
		bind(Cursor.class).to(CursorService.class);
	}
	
	@Provides
	@Singleton
	public Handlebars handlebars(){
		return new Handlebars();
	}
	
	@Provides
	public Map<Serializable, DataLoader> dataLoaderRegistry(){
		//need to come up with a chainable loader
		Map<Serializable, DataLoader> dataLoaders = Maps.newHashMap();
		loadDataLoader(dataLoaders, fileDataLoader);		
		loadDataLoader(dataLoaders, jsonDataLoader);
		return dataLoaders;
	}


	private void loadDataLoader(Map<Serializable, DataLoader> loaderMap, DataLoader dataLoader){
		//TODO: figure out how we want to expose this method
		for(Serializable handledCriterian : dataLoader.getHandledDataLoaderCriterian()){
			loaderMap.put(handledCriterian, dataLoader);
		}
	}
	
}
