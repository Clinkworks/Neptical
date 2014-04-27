package com.clinkworks.neptical.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtil {
	private static final Gson GSON = new Gson();
	private static final JsonParser PARSER = new JsonParser();
	
	private JsonUtil(){
		Common.noOp("This class is designed to be used as a static utility");
	}
	
	public static final JsonElement toJson(Object object){
		return GSON.toJsonTree(object);
	}
	
	public static final JsonElement parseFromFile(String path){
		return parse(new File(path));
	}
	
	public static final <T> T toObject(Class<T> type, JsonElement json){
		return GSON.fromJson(json, type);
	}
	
	public static final <T> T toObject(Class<T> type, String json){
		return GSON.fromJson(json, type);
	}
	
	public static final JsonElement parse(String json){
		return PARSER.parse(json);
	}
	
	 public static final JsonElement parse(File file){
		try {
			return parse(new BufferedReader(new FileReader(file)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final JsonElement parse(InputStream inputStream){
		return parse(new BufferedReader(new InputStreamReader(inputStream)));
	}

	private static final JsonElement parse(BufferedReader bufferedReader){
		
		JsonElement element = null;
		try{
			element = PARSER.parse(bufferedReader);
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			try {
				bufferedReader.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		return element;
	}
}
