package com.clinkworks.neptical.module;

import java.io.Serializable;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import com.clinkworks.neptical.loader.FileDataLoader;
import com.clinkworks.neptical.loader.JsonDataLoader;
import com.clinkworks.neptical.service.CursorService;
import com.clinkworks.neptical.spi.Cursor;
import com.clinkworks.neptical.spi.DataLoader;
import com.clinkworks.neptical.spi.GenericModuleTemplate;
import com.google.common.collect.Maps;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;

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
		install(new TestDataModule());
		install(new GraphModule());
		install(new VocabularyModule());
		bind(Cursor.class).to(CursorService.class);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Test.class), 
		        new TestMethodInterceptor());
	}
	
	@Provides
	public Map<Serializable, DataLoader> dataLoaderRegistry(){
		//need to come up with a chainable loader
		Map<Serializable, DataLoader> dataLoaders = Maps.newHashMap();
		loadDataLoader(dataLoaders, fileDataLoader);		
		loadDataLoader(dataLoaders, jsonDataLoader);
		return dataLoaders;
	}

	static class TestMethodInterceptor implements MethodInterceptor {
		  public Object invoke(MethodInvocation invocation) throws Throwable {
			  System.out.println("I was called...");
		    return invocation.proceed();
		  }
		}
	
	private void loadDataLoader(Map<Serializable, DataLoader> loaderMap, DataLoader dataLoader){
		//TODO: figure out how we want to expose this method
		for(Serializable handledCriterian : dataLoader.getHandledDataLoaderCriterian()){
			loaderMap.put(handledCriterian, dataLoader);
		}
	}
	
}
