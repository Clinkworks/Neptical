package com.clinkworks.neptical.data.domain;

import com.google.gson.JsonElement;

public class JsonData extends GenericImmutableData{

	public JsonData(String name, JsonElement jsonElement) {
		super(name, jsonElement);
	}

	public JsonElement getAsJsonElement(){
		return (JsonElement)get();
	}
}
