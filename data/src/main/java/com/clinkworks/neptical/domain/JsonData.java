package com.clinkworks.neptical.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.clinkworks.neptical.datatype.ListableTransformableData;
import com.clinkworks.neptical.datatype.PrimitiveData;
import com.clinkworks.neptical.datatype.TransformableData;
import com.clinkworks.neptical.util.JsonUtil;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonData extends GenericMutableData implements TransformableData, ListableTransformableData, PrimitiveData{

	public JsonData(Object object){
		set(object);
	}
	
	public JsonData(String name, Object object) {
		setName(name);
		set(object);
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
	
}
