package com.clinkworks.neptical.data.loaders.json;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.util.JsonUtil;

import static com.clinkworks.neptical.util.PathUtil.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonData extends Data{

	public JsonData(String segment, String path, Data root, Data parent,
			JsonElement data) {
		super(segment, path, root, parent, data);
	}
	
	@Override
	public JsonElement getAsJsonElement(){
		return (JsonElement)get();
	}
	
	@Override
	public Data find(String startingPath) {
		
		String path = startingPath;
		
		if(getPath().equals(path)){
			return this;
		}
		
		if(StringUtils.isBlank(path)){
			return this;
		}
		
		String remainingSegment = subtractSegment(getPath(), path);
		String nextSegment = firstSegment(remainingSegment);

		if(isLoaded()){
			return next().find(remainingSegment);
		}
		
		Data data = loadJson(nextSegment);
		
		Data returnedData = data.find(remainingSegment);
		
		if(returnedData == null){
			throw new NullPointerException("Couldn't find " + remainingSegment + " last found node is " + getPath());
		}
		
		return returnedData;
	}
		
	private boolean isLoaded(){
		return next() != null;
	}
	
	@Override
	public Data copyDeep(){
		JsonElement json = getAsJsonElement();
		
		JsonElement clone = JsonUtil.toObject(JsonElement.class, json);
		
		Data data = new JsonData(getSegment(), getPath(), root(), parent(), clone);
		return data;
	}
	
	@Override
	public boolean isPrimitive(){
		return getAsJsonElement().isJsonPrimitive();
	}
	
	@Override
	public String getAsString(){
		return getAsJsonElement().getAsString();
	}
	
	private Data loadJson(String nextSegment) {
		JsonElement json = getAsJsonElement();
		
		if(json.isJsonPrimitive()){
			setNext(new JsonData(nextSegment, getPath() + DOT + nextSegment, root(), this, json));
		}else{
			setNext(new JsonDataNode(nextSegment, getPath(), root(), this, json));
		}
		
		return next();
	}
	
	public Data find(String path, JsonElement jsonElementToSearch){
		if(getPath().equals(path)){
			return this;
		}
		
		String remainingSegment = subtractSegment(getPath(), path);
		String nextSegment = firstSegment(remainingSegment);

		if(isLoaded()){
			return next().find(remainingSegment);
		}
		
		Data data = loadJson(nextSegment);
		
		if(StringUtils.isBlank(nextSegment)){
			return data;
		}
		
		if(StringUtils.equals(remainingSegment, nextSegment)){
			return data;
		}
		
		return data.find(remainingSegment);
	}
	
	@Override
	public <T> T get(Class<T> type) {
		return JsonUtil.toObject(type, getAsJsonElement());
	}

	@Override
	public <T> List<T> getList(Class<T> type) {
		
		JsonElement ele = getAsJsonElement();
		
		if(ele.isJsonArray()){
			JsonArray array = ele.getAsJsonArray();
			Iterator<JsonElement> iter = array.iterator();
			List<T> listOfT = Lists.newArrayList();
			while(iter.hasNext()){
				listOfT.add(JsonUtil.toObject(type, iter.next()));
			}
			return listOfT;
		}
		
		return null;
	}
	
	private static final class JsonDataNode extends Data{
		
		private final Set<Data> linkedNodes;
		
		public JsonDataNode(String segment, String path, Data head, Data parent, JsonElement data) {
			super(segment, path, head, parent, data);
			linkedNodes = Sets.newConcurrentHashSet();
			linkNodes(data);
		}
		
		@Override
		public Data find(String path) {
			
			String remainingSegment = subtractSegment(getPath(), path);
			String nextSegment = firstSegment(remainingSegment);
			
			int indexOfBracket = nextSegment.indexOf(LEFT_BRACKET);
			
			if(segmentContainsArraySyntax(nextSegment)){
				nextSegment = nextSegment.substring(0, indexOfBracket);
			}
			
			if(indexOfBracket == 0){
				nextSegment = getIndexAsString(nextSegment);
			}
			
			for(Data data : linkedNodes){
				if(StringUtils.equals(nextSegment, data.getSegment())){
					remainingSegment = subtractSegment(nextSegment, remainingSegment);
					return data.find(remainingSegment);
				}
			}
			
			return null;
		}
		
		@Override
		public JsonElement getAsJsonElement(){
			return (JsonElement)get();
		}
		
		private void linkNodes(JsonElement data) {
			
			if(data.isJsonObject()){
				for(Map.Entry<String, JsonElement> properties : data.getAsJsonObject().entrySet()){
					String segment = properties.getKey();
					String path = clean(getPath() + DOT + segment);
					Data jsonProperty = new JsonData(segment, path, root(), parent(), properties.getValue());
					linkedNodes.add(jsonProperty);
				}
			}
			
			if(data.isJsonArray()){
				Iterator<JsonElement> arrayElements = data.getAsJsonArray().iterator();
				int i = 0;
				while(arrayElements.hasNext()){
					JsonElement arrayElement = arrayElements.next();
					String segment = "[" + i++ + "]";
					String path = clean(getPath() + segment);
					JsonData jsonProperty = new JsonData(segment, path, root(), parent(), arrayElement);
					linkedNodes.add(jsonProperty);
				}
			}
			
		}
	}
	
}
