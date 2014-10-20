package com.clinkworks.neptical;

import java.io.Serializable;
import java.util.List;

import com.clinkworks.neptical.api.Cursor;
import com.clinkworks.neptical.api.NepticalData;
import com.clinkworks.neptical.datatype.FileData;
import com.clinkworks.neptical.datatype.ListableData;
import com.clinkworks.neptical.datatype.ListableTransformableData;
import com.clinkworks.neptical.datatype.LoadableData;
import com.clinkworks.neptical.datatype.MutableData;
import com.clinkworks.neptical.datatype.PrimitiveData;
import com.clinkworks.neptical.datatype.TransformableData;
import com.clinkworks.neptical.domain.JsonData;
import com.clinkworks.neptical.util.DataUtil;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class Data implements Cursor, ListableData, ListableTransformableData, TransformableData, MutableData, LoadableData{
	
	private NepticalData backingData;
	
	@Inject
	public Data(@Assisted NepticalData backingData){
		this.backingData = backingData;
	}	
	
	public NepticalData getData(){
		return backingData;
	}
	
	public void setData(NepticalData nepticalData){
		this.backingData = nepticalData;
	}
	
	@Override
	public Object get() {
		return getData().get();
	}

	@Override
	public List<Object> getList() {
		return getAsListableData().getList();
	}

	public boolean isLoadableData(){
		return isDataType(LoadableData.class);
	}
	
	public LoadableData getAsLoadableData(){
		return getAsDataType(LoadableData.class);
	}
	
	public boolean isFileData(){
		return isDataType(FileData.class);
	}
	
	public FileData getAsFileData(){
		return getAsDataType(FileData.class);
	}

	@Override
	public void toggleLoadedTrue() {
		getAsLoadableData().toggleLoadedTrue();
	}

	@Override
	public void toggleLoadedFalse(){
		getAsLoadableData().toggleLoadedFalse();
	}

	public boolean isTransformableData() {
		return isDataType(TransformableData.class);
	}

	public TransformableData getAsTransformableData() {
		return getAsDataType(TransformableData.class);
	}

	@Override
	public <T> T getAs(Class<T> type) {
		return getAsTransformableData().getAs(type);
	}

	public boolean isListableData() {
		return isDataType(ListableData.class);
	}

	public boolean isListableTransformableData() {
		return isDataType(ListableTransformableData.class);
	}

	@Override
	public <T> List<T> getListAs(Class<T> type) {
		return getAsListableTransformableData().getListAs(type);
	}

	public boolean isPrimitiveData() {
		return isDataType(PrimitiveData.class);
	}

	public boolean isMutableData() {
		return isDataType(MutableData.class);
	}
	
	public MutableData getAsMutableData() {
		return getAsDataType(MutableData.class);
	}

	public boolean isDataType(Class<? extends NepticalData> dataType){
		return DataUtil.isDataType(dataType, getData());
	}
	
	public <T extends NepticalData> T getAsDataType(Class<? extends NepticalData> dataType){
		return DataUtil.getAsDataType(dataType, getData());
	}
	
	@Override
	public void setName(String name) {
		getAsMutableData().setName(name);
	}

	@Override
	public String getName() {
		return getData().getName();
	}

	public boolean isJsonData(){
		return isDataType(JsonData.class);
	}

	public JsonData getAsJsonData(){
		return getAsDataType(JsonData.class);
	}
	
	@Override
	public Serializable getLoaderCriterian() {
		return getAsLoadableData().getLoaderCriterian();
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
	public void set(Object object) {
		getAsMutableData().set(object);
	}

	@Override
	public boolean isLoaded() {
		if(!isLoadableData()){
			return true;
		}
		return getAsLoadableData().isLoaded();
	}

	@Override
	public String toString(){
		return getData().toString();
	}

	@Override
	public Data find(String notation) {
		return null;
	}

	@Override
	public void setLoaderCriterian(Serializable loaderCriterian) {
		getAsLoadableData().setLoaderCriterian(loaderCriterian);
	}

}
