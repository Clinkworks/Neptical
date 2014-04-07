package com.clinkworks.neptical.data.loaders.json;

import java.io.File;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.DataLoader;
import com.clinkworks.neptical.util.JsonUtil;
import com.google.gson.JsonElement;

public class JsonDataLoader implements DataLoader{
	
	@Override
	public Data loadData(String segment, String path, Data root, Data parent, File file) {
		JsonElement element = JsonUtil.parse(file);
		JsonData data = new JsonData(segment, path, root, parent, element);
		return data;
	}

}
