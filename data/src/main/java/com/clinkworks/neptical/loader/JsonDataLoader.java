package com.clinkworks.neptical.loader;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.datatype.LoadableData;
import com.clinkworks.neptical.domain.JsonData;
import com.clinkworks.neptical.spi.DataLoader;
import com.clinkworks.neptical.spi.DataLoaderCriterian;
import com.clinkworks.neptical.util.JsonUtil;
import com.google.gson.JsonElement;

public class JsonDataLoader implements DataLoader{

	public enum JsonDataLoaderCriterian implements DataLoaderCriterian{
		JSON_ELEMENT(JsonElement.class),
		JSON_DATA(JsonData.class),
		JSON_EXTENSION("json");
		
		private Serializable handledCriterian;
		
		private JsonDataLoaderCriterian(Serializable criterian){
			handledCriterian = criterian;
		}

		public static JsonDataLoaderCriterian getEnumeratedCriterian(Serializable criterian){
			for(JsonDataLoaderCriterian dataLoaderCriterian : JsonDataLoaderCriterian.values()){
				if(dataLoaderCriterian.getCriterian().equals(criterian)){
					return dataLoaderCriterian;
				}
			}
			return null;
		}
		
		public static Set<Serializable> getCriteria(){
			Set<Serializable> criteria = new HashSet<Serializable>();
			for(DataLoaderCriterian dataLoaderCriterian : JsonDataLoaderCriterian.values()){
				criteria.add(dataLoaderCriterian.getCriterian());
			}
			return criteria;
		}
		
		@Override
		public Serializable getCriterian() {
			return handledCriterian;
		}
	}
	
	@Override
	public Data loadData(LoadableData loadableData) {
		
		if(loadableData.isLoaded()){
			return (Data)loadableData;
		}
		
 		JsonElement element = null;
		
 		element = loadByCriterian(loadableData);
 				
		return new Data(new JsonData(loadableData.getName(), element));
	}

	@Override
	public Set<Serializable> getHandledDataLoaderCriterian() {
		return JsonDataLoaderCriterian.getCriteria();
	}
	
	private JsonElement loadByCriterian(LoadableData loadableData) {
		
		JsonDataLoaderCriterian jsonDataLoaderCriterian = JsonDataLoaderCriterian.getEnumeratedCriterian(loadableData.getLoaderCriterian());
		
		switch(jsonDataLoaderCriterian){
		case JSON_DATA:
			return JsonUtil.toJson(loadableData.get());
		case JSON_ELEMENT:
			return JsonUtil.toJson(loadableData.get());
		case JSON_EXTENSION:
			return JsonUtil.parse((File)loadableData.get());
		}
		
		return null;
	}

}
