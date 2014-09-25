package com.clinkworks.neptical.data.loader;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.data.api.DataLoader;
import com.clinkworks.neptical.data.datatypes.LoadableData;
import com.clinkworks.neptical.data.domain.JsonData;
import com.clinkworks.neptical.util.JsonUtil;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;

public class JsonDataLoader implements DataLoader{

	@Override
	public Data loadData(LoadableData loadableData) {
		
		if(loadableData.isLoaded()){
			return (Data)loadableData;
		}
		
		JsonElement element = null;
		
		if(ObjectUtils.equals(loadableData.getLoaderCriterian(), "json")){
			element = JsonUtil.parse((File)loadableData.get());
		}else{
			element = (JsonElement)loadableData.get();
		}
		
		return new Data(new JsonData(loadableData.getName(), element));
	}

	@Override
	public Set<Serializable> getHandledDataLoaderCriterian() {
		return Sets.newHashSet("json", JsonElement.class);
	}
	

}
