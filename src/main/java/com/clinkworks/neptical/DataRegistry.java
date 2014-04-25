package com.clinkworks.neptical;

import java.io.File;
import java.util.Map;

import com.clinkworks.neptical.data.file.FileData;
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
	
	public static final Data loadData(){
		File file = new File(System.getProperty("user.home") + "/neptical-data");
	
		if(file.exists()){
			return loadData(file);
		}
	
		if(!file.mkdir()){
			throw new IllegalStateException("Neptical expects \"for now\" the data directory to be the ${user.home}/neptical-data directory on your filesystem");
		}
		
		return loadData(file);
	}
	
	
	public static final Data loadData(String segment, String path, Data root, Data parent, File file){
		
		if(file.isDirectory()){
			return new FileData(segment, path, root, parent, file);
		}
		
		String extension = PathUtil.lastSegment(file.getName()).toLowerCase();
		
		DataLoader dataLoader = registeredDataLoaders.get(extension);
		
		if(dataLoader == null){
			throw new IllegalStateException("No data loader has been registered for the extension: " + extension);
		}
		
		
		return dataLoader.loadData(segment, path, root, parent, file);
	}
	
}
