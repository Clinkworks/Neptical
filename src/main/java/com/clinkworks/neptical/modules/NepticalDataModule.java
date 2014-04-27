package com.clinkworks.neptical.modules;

import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.DataService;
import com.clinkworks.neptical.data.api.Cursor;
import com.clinkworks.neptical.data.api.DataElement;
import com.clinkworks.neptical.data.api.DataLoader;
import com.clinkworks.neptical.data.graph.DataGraph;
import com.clinkworks.neptical.data.loader.FileDataLoader;
import com.clinkworks.neptical.data.loader.JsonDataLoader;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class NepticalDataModule extends AbstractModule{

	
	@Override
	protected void configure() {
		bind(DataService.class);

		FactoryModuleBuilder builder = new FactoryModuleBuilder();

		builder.implement(Cursor.class, DataGraph.class);

		install(builder.build(NepticalDataApiFactory.class));
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
	
	public static interface NepticalDataApiFactory{
		public Cursor create(DataElement dataElement);
	}
	
}
