package com.clinkworks.neptical.data;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

import com.clinkworks.neptical.data.datatypes.DataLoader;
import com.clinkworks.neptical.data.datatypes.LoadableData;
import com.clinkworks.neptical.data.domain.DataElement;
import com.clinkworks.neptical.data.domain.JsonData;
import com.clinkworks.neptical.util.JsonUtil;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;

public class JsonDataLoader implements DataLoader{

	@Override
	public DataElement loadData(LoadableData loadableData) {
		
		if(loadableData.isLoaded()){
			return (DataElement)loadableData;
		}
		
		JsonElement element = null;
		
		if(ObjectUtils.equals(loadableData.getLoaderCriterian(), "json")){
			element = JsonUtil.parse((File)loadableData.get());
		}else{
			element = (JsonElement)loadableData.get();
		}
		
		return new DataElement(new JsonData(loadableData.getName(), element));
	}

	@Override
	public Set<Serializable> getHandledDataLoaderCriterian() {
		return Sets.newHashSet("json", JsonElement.class);
	}
	

}
