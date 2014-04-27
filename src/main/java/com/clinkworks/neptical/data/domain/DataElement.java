package com.clinkworks.neptical.data.domain;

import java.io.Serializable;
import java.util.List;

import com.clinkworks.neptical.data.datatypes.Data;
import com.clinkworks.neptical.data.datatypes.ListableData;
import com.clinkworks.neptical.data.datatypes.ListableTransformableData;
import com.clinkworks.neptical.data.datatypes.LoadableData;
import com.clinkworks.neptical.data.datatypes.MutableData;
import com.clinkworks.neptical.data.datatypes.PrimitiveData;
import com.clinkworks.neptical.data.datatypes.TransformableData;
import com.clinkworks.neptical.util.DataUtil;

public class DataElement implements ListableData, ListableTransformableData, TransformableData, MutableData, LoadableData{
	
	private Data backingData;
	
	public DataElement(Data backingData){
		this.backingData = backingData;
	}
	
	public final Data getData(){
		return backingData;
	}
	
	@Override
	public Object get() {
		return getData().get();
	}

	@Override
	public List<Object> getList() {
		return getAsListableData().getList();
	}

	@Override
	public <T> T getAs(Class<T> type) {
		return getAsTransformableData().getAs(type);
	}
	
	@Override
	public <T> List<T> getListAs(Class<T> type) {
		return getAsListableTransformableData().getListAs(type);
	}

	public boolean isLoadableData(){
		return isDataType(LoadableData.class);
	}
	
	public boolean isTransformableData() {
		return isDataType(TransformableData.class);
	}

	public boolean isListableData() {
		return isDataType(ListableData.class);
	}

	public boolean isListableTransformableData() {
		return isDataType(ListableTransformableData.class);
	}

	public boolean isPrimitiveData() {
		return isDataType(PrimitiveData.class);
	}

	public boolean isMutableData() {
		return isDataType(MutableData.class);
	}
	
	public boolean isDataType(Class<? extends Data> dataType){
		return DataUtil.isDataType(dataType, getData());
	}
	
	public <T extends Data> T getAsDataType(Class<? extends Data> dataType){
		return DataUtil.getAsDataType(dataType, getData());
	}
	
	public LoadableData getAsLoadableData(){
		return getAsDataType(LoadableData.class);
	}
	
	public MutableData getAsMutableData() {
		return getAsDataType(MutableData.class);
	}
	
	public TransformableData getAsTransformableData() {
		return getAsDataType(TransformableData.class);
	}

	public ListableData getAsListableData() {
		return getAsDataType(ListableData.class);
	}

	public PrimitiveData getAsPrimitiveData() {
		return getAsDataType(PrimitiveData.class);
	}

	public ListableTransformableData getAsListableTransformableData() {
		return getAsDataType(ListableTransformableData.class);
	}

	@Override
	public void setName(String name) {
		getAsMutableData().setName(name);
	}

	@Override
	public void set(Object object) {
		if(isMutableData()){
			getAsMutableData().set(object);
		}else{
			throw new IllegalStateException("This is not mutable data " + getData());
		}
	}

	@Override
	public String getName() {
		return getData().getName();
	}

	@Override
	public Serializable getLoaderCriterian() {
		return getAsLoadableData().getLoaderCriterian();
	}

	@Override
	public boolean isLoaded() {
		return getAsLoadableData().isLoaded();
	}

	@Override
	public void toggleLoadedTrue() {
		getAsLoadableData().toggleLoadedTrue();
	}
	
	@Override
	public void toggleLoadedFalse(){
		getAsLoadableData().toggleLoadedFalse();
	}

	@Override
	public String toString(){
		return getData().toString();
	}
	
}
