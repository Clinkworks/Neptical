package com.clinkworks.neptical.modules;

import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.DataService;
import com.clinkworks.neptical.data.FileDataLoader;
import com.clinkworks.neptical.data.JsonDataLoader;
import com.clinkworks.neptical.data.datatypes.DataLoader;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class NepticalDataModule extends AbstractModule{

	
	@Override
	protected void configure() {
		bind(DataService.class);
	}
	

	@Provides
	@Singleton
	public Map<Serializable, DataLoader> dataLoaderRegistry(){

		Map<Serializable, DataLoader> registry = Maps.newHashMap();
		registerLoader(registry, new FileDataLoader());
		registerLoader(registry, new JsonDataLoader());
		
		return registry;
	}
	
	private static void registerLoader(Map<Serializable, DataLoader> registry, DataLoader dataLoader){
		for(Serializable handledCriterian : dataLoader.getHandledDataLoaderCriterian()){
			registry.put(handledCriterian, dataLoader);
		}
	}
	
	
}
