package com.clinkworks.neptical.data;

import java.io.File;
import java.util.Map;

import com.clinkworks.neptical.DataLoader;
import com.clinkworks.neptical.data.file.FileData;
import com.clinkworks.neptical.data.loaders.json.JsonData;
import com.clinkworks.neptical.data.loaders.json.JsonDataLoader;
import com.clinkworks.neptical.util.PathUtil;
import com.google.common.collect.Maps;

public class DataRegistry {
	//TODO: find a way not to hard code the loaders
	private static final Map<String, DataLoader> registeredDataLoaders;
	static{
		registeredDataLoaders = Maps.newConcurrentMap();
		registeredDataLoaders.put("json", new JsonDataLoader());
	}
	
	public static final Data loadData(String segment, String path, Data parent, File file){
		Data data = null;
		
		if(file.isDirectory()){
			return data = new FileData("", "", parent, parent, file);
		}
		
		String extension = PathUtil.lastSegment(file.getName()).toLowerCase();
		
		DataLoader dataLoader = registeredDataLoaders.get(extension);
		
		if(dataLoader == null){
			throw new IllegalStateException("No data loader has been registered for the extension: " + extension);
		}
		
		
		return dataLoader.loadData(segment, path, parent, parent, file);
	}
	
}
