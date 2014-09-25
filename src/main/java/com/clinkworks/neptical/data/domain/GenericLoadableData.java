package com.clinkworks.neptical.data.domain;

import java.io.Serializable;

import com.clinkworks.neptical.data.datatype.LoadableData;
import com.clinkworks.neptical.data.datatype.MutableData;

public class GenericLoadableData implements LoadableData, MutableData{
	
	private final MutableData data;
	private final Serializable loaderCriterian;
	private boolean dataLoaded;
	
	public GenericLoadableData(Serializable loaderCriterian, MutableData mutableData){
		this.loaderCriterian = loaderCriterian;
		data = mutableData;
		dataLoaded = false;
	}

	@Override
	public Serializable getLoaderCriterian() {
		return loaderCriterian;
	}

	@Override
	public boolean isLoaded() {
		return dataLoaded;
	}

	@Override
	public void toggleLoadedTrue() {
		dataLoaded = true;
	}
	
	@Override
	public void toggleLoadedFalse(){
		dataLoaded = false;
	}

	@Override
	public String getName() {
		return data.getName();
	}

	@Override
	public Object get() {
		return data.get();
	}

	@Override
	public void set(Object object) {
		data.set(object);
	}

	@Override
	public void setName(String name) {
		data.setName(name);
	}

}
