package com.clinkworks.neptical.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.clinkworks.neptical.datatype.ListableTransformableData;
import com.clinkworks.neptical.datatype.PrimitiveData;
import com.clinkworks.neptical.datatype.TransformableData;
import com.clinkworks.neptical.spi.TraversableData;
import com.clinkworks.neptical.util.JsonUtil;
import com.clinkworks.neptical.util.PathUtil;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonData extends GenericMutableData implements TraversableData, TransformableData, ListableTransformableData, PrimitiveData{

	public JsonData(Object object){
		set(object);
	}
	
	public JsonData(String name, Object object) {
		setName(name);
		set(object);
	}

	public boolean isJsonArray(){
		return getAsJsonElement().isJsonArray();
	}
		
	public boolean isJsonPrimitive(){
		return getAsJsonElement().isJsonPrimitive();
	}
	
	public boolean isJsonObject(){
		return getAsJsonElement().isJsonObject();
	}

	
	public JsonElement getAsJsonElement(){
		return (JsonElement)get();
	}
	
	public JsonArray getAsJsonArray(){
		return getAsJsonElement().getAsJsonArray();
	}
	
	public JsonPrimitive getAsJsonPrimitive(){
		return getAsJsonElement().getAsJsonPrimitive();
	}
	
	public JsonObject getAsJsonObject(){
		return getAsJsonElement().getAsJsonObject();
	}

	@Override
	public void set(Object object){
		if(!(object instanceof JsonElement)){
			super.set(JsonUtil.toJson(object));
		}
		super.set(object);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getListAs(Class<T> type) {
		List<T> listOfT = Lists.newArrayList();
		return JsonUtil.toObject(listOfT.getClass(), getAsJsonElement());
	}

	@Override
	public <T> T getAs(Class<T> type) {
		return JsonUtil.toObject(type, getAsJsonElement());
	}

	@Override
	public boolean getAsBoolean() {
		return getAsJsonElement().getAsBoolean();
	}

	@Override
	public Number getAsNumber() {
		return getAsJsonElement().getAsNumber();
	}

	@Override
	public String getAsString() {
		
		JsonElement jsonElement = getAsJsonElement();
		
		if(jsonElement.isJsonPrimitive()){
			return jsonElement.getAsString();
		}
		
		return getAsJsonElement().toString();
	}

	@Override
	public double getAsDouble() {
		return getAsJsonElement().getAsDouble();
	}

	@Override
	public float getAsFloat() {
		return getAsJsonElement().getAsFloat();
	}

	@Override
	public long getAsLong() {
		return getAsJsonElement().getAsLong();
	}

	@Override
	public int getAsInt() {
		return getAsJsonElement().getAsInt();
	}

	@Override
	public byte getAsByte() {
		return getAsJsonElement().getAsByte();
	}

	@Override
	public char getAsCharacter() {
		return getAsJsonElement().getAsCharacter();
	}

	@Override
	public BigDecimal getAsBigDecimal() {
		return getAsJsonElement().getAsBigDecimal();
	}

	@Override
	public BigInteger getAsBigInteger() {
		return getAsJsonElement().getAsBigInteger();
	}

	@Override
	public short getAsShort() {
		return getAsJsonElement().getAsShort();
	}

	@Override
	public JsonData find(Serializable notation) {

		//note: json data can only handle dot noation at this point.
		String path = notation.toString();
			
		if(StringUtils.equals(getName(), path)){
			return this;
		}
		
		String currentSegment = PathUtil.firstSegment(path);
		
		String remainingPath = PathUtil.chompFirstSegment(path);
		
		JsonElement jsonElement = find(getAsJsonElement(), currentSegment, remainingPath);
		
		if(jsonElement == null){
			return null;
		}
		
		return jsonElement == null ? null : new JsonData(PathUtil.lastSegment(path), jsonElement);
	}
	
	private JsonElement find(JsonElement currentElement, String currentSegment, String remainingPath){
		
		if(currentElement == null){
			return null;
		}
		
		JsonElement foundElement = getNestedElement(currentElement, null, currentSegment);
		
		if(foundElement == null){
			return null;
		}else{
			currentSegment = PathUtil.firstSegment(remainingPath);
			remainingPath = PathUtil.chompFirstSegment(remainingPath);
		}

		if(StringUtils.isBlank(currentSegment)){
			if(StringUtils.isBlank(remainingPath)){
				return foundElement; //we found it!
			}
		}

		
		return find(foundElement, currentSegment, remainingPath);
	}

	private JsonElement getNestedElement(JsonElement currentElement, String parentName, String propertyName) {
				
		//this should take care of the primitive cases... nothing to search in those, this may change in the case of validators
		// but this method will not handle that use case
		if(StringUtils.equals(parentName, propertyName)){
			return currentElement;
		}
		
		if(currentElement.isJsonObject()){
			return currentElement.getAsJsonObject().get(propertyName);
		}
		
		if(currentElement.isJsonArray()){
			int index = PathUtil.getIndex(propertyName);
			return currentElement.getAsJsonArray().get(index);
		}
		
		return null;
	}
}
