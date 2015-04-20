package com.clinkworks.neptical.domain;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.clinkworks.neptical.datatype.MutableData;
import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.datatype.PrimitiveData;
import com.clinkworks.neptical.util.JsonUtil;
import com.google.gson.JsonElement;

public final class GenericPrimitiveData implements PrimitiveData, MutableData{

	private String name;
	
	//we are using json element as a backing store to ensure proper primitives,
	//GSON does very well.
	private JsonElement wrappedContents; 

	@Override
	public Object get() {
		if(wrappedContents == null || wrappedContents.isJsonNull()){
			return null;
		}
		
		return wrappedContents;
	}

	@Override
	public String getAsString() {
		return get() == null ? null : get().toString();
	}

	@Override
	public void set(Object object) {
		
		JsonElement jsonElement = JsonUtil.toJson(object);
		
		boolean validElement = jsonElement.isJsonPrimitive() || jsonElement.isJsonNull();
		
		if(!validElement){
			throw new IllegalArgumentException("Only string, number, java prims, and null are allowed");
		}
		
		wrappedContents = jsonElement;
		
	}
	
	@Override
	public boolean getAsBoolean() {
		return wrappedContents.getAsBoolean();
	}

	@Override
	public Number getAsNumber() {
		return wrappedContents.getAsNumber();
	}

	@Override
	public double getAsDouble() {
		return wrappedContents.getAsDouble();
	}

	@Override
	public float getAsFloat() {
		return wrappedContents.getAsFloat();
	}

	@Override
	public long getAsLong() {
		return wrappedContents.getAsLong();
	}

	@Override
	public int getAsInt() {
		return wrappedContents.getAsInt();
	}

	@Override
	public byte getAsByte() {
		return wrappedContents.getAsByte();
	}

	@Override
	public char getAsCharacter() {
		return wrappedContents.getAsCharacter();
	}

	@Override
	public BigDecimal getAsBigDecimal() {
		return wrappedContents.getAsBigDecimal();
	}

	@Override
	public BigInteger getAsBigInteger() {
		return wrappedContents.getAsBigInteger();
	}

	@Override
	public short getAsShort() {
		return wrappedContents.getAsShort();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Class<? extends NepticalData> getNepticalDataType() {
		return getClass();
	}

}
