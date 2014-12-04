package com.clinkworks.neptical;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.clinkworks.neptical.datatype.DataContainer;
import com.clinkworks.neptical.datatype.FileData;
import com.clinkworks.neptical.datatype.ListableData;
import com.clinkworks.neptical.datatype.ListableTransformableData;
import com.clinkworks.neptical.datatype.LoadableData;
import com.clinkworks.neptical.datatype.MutableData;
import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.datatype.PrimitiveData;
import com.clinkworks.neptical.datatype.TransformableData;
import com.clinkworks.neptical.domain.JsonData;
import com.clinkworks.neptical.spi.TraversableData;
import com.clinkworks.neptical.util.DataUtil;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class Data implements DataContainer, TraversableData, ListableData, ListableTransformableData, TransformableData, MutableData, LoadableData, FileData{
	
	private volatile NepticalData backingData;
	
	@Inject
	public Data(@Assisted NepticalData backingData){
		this.backingData = backingData;
	}
		
	@Override
	public NepticalData getNepticalData(){
		return backingData;
	}
	
	@Override
	public void setNepticalData(NepticalData nepticalData){
		this.backingData = nepticalData;
	}
	
	@Override
	public Object get() {
		return getNepticalData().get();
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
		return DataUtil.isDataType(dataType, getNepticalData());
	}
	
	public <T extends NepticalData> T getAsDataType(Class<? extends NepticalData> dataType){
		return DataUtil.getAsDataType(dataType, getNepticalData());
	}
	
	@Override
	public void setName(String name) {
		getAsMutableData().setName(name);
	}

	@Override
	public String getName() {
		return getNepticalData().getName();
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
		NepticalData data = getNepticalData();
		
		if(data == null){
			return super.toString();
		}
		
		if(data.getName() != null){
			return data.getName();
		}
		
		return data.toString();
	}

	public boolean isTraversableData(){
		return isDataType(TraversableData.class);
	}
	
	public TraversableData getAsTraversableData(){
		return getAsDataType(TraversableData.class);
	}
	
	@Override
	public Data find(Serializable notation) {
		
		if(!isTraversableData()){
			if(StringUtils.equals(getName(), notation.toString())){
				return this;
			}
			return null;
		}
		
		return DataUtil.wrap(getAsTraversableData().find(notation));
	}

	@Override
	public void setLoaderCriterian(Serializable loaderCriterian) {
		getAsLoadableData().setLoaderCriterian(loaderCriterian);
	}
	
	@Override
	public File getAsFile() {
		return getAsFileData().getAsFile();
	}

	@Override
	public boolean isDirectory() {
		return getAsFile().isDirectory();
	}

	@Override
	public boolean isFile() {
		return getAsFile().isFile();
	}

	@Override
	public Class<? extends NepticalData> getNepticalDataType() {
		return getNepticalData() == null ? getClass() :  getNepticalData().getNepticalDataType();
	}
	
	@Override
	public boolean containsData() {
		return getNepticalData() != null;
	}
	
	public static @interface DataGet{
		
	}
}
