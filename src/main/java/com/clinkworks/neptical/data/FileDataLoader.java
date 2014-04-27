package com.clinkworks.neptical.data;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

import com.clinkworks.neptical.data.datatypes.DataLoader;
import com.clinkworks.neptical.data.datatypes.LoadableData;
import com.clinkworks.neptical.data.datatypes.MutableData;
import com.clinkworks.neptical.data.domain.DataElement;
import com.clinkworks.neptical.data.domain.GenericLoadableData;
import com.clinkworks.neptical.data.domain.GenericMutableData;
import com.clinkworks.neptical.data.domain.GenericPrimitiveData;
import com.clinkworks.neptical.util.Common;
import com.clinkworks.neptical.util.PathUtil;
import com.google.common.collect.Sets;

public class FileDataLoader implements DataLoader{

	public static enum FileDataLoaderCriterian{
		READ_AS_TEXT;
	}
	
	private static final Set<Serializable> HANDLED_CRITERIAN;
	static{
		Set<Serializable> criterian = Sets.newHashSet((Serializable[])FileDataLoaderCriterian.values());
		criterian.add("txt");
		criterian.add("csv");
		criterian.add(File.class);
		HANDLED_CRITERIAN = criterian;
	}
	
	public FileDataLoader(){
		
	}
	
	@Override
	public DataElement loadData(LoadableData loadableData) {
		
		if(loadableData.isLoaded()){
			return (DataElement)loadableData;
		}
		
		return handleLoaderCriterian(loadableData);

	}
	
	private DataElement handleLoaderCriterian(LoadableData loadableData){
		
		Serializable loaderCriterian = loadableData.getLoaderCriterian();
		
		File file = getFile(loadableData);
		
		if(ObjectUtils.equals(loadableData.getLoaderCriterian(), File.class)){
			
			if(file.isDirectory()){
				LoadableData loadedData = buildGenericLoadableData(File.class, file);
				loadableData.toggleLoadedTrue();
				loadedData.toggleLoadedTrue();
				return new DataElement(loadedData);
			}
			
			return initNewDataElementForLoadByExtension(file);
		}
		
		if(shouldReadFile(loaderCriterian)){
			return handleRead(loadableData);
		}
		
		throw new UnsupportedOperationException("The file data loader cannot handle loader criterian: " + loaderCriterian);
	}
	
	private DataElement initNewDataElementForLoadByExtension(File file){
		GenericLoadableData genericLoadableData = buildGenericLoadableData(getExtension(file), file);
		
		return new DataElement(genericLoadableData);
	}
	
	private DataElement handleRead(LoadableData loadableData){
		
		File file = getFile(loadableData);
		String fileData = Common.readFile(file);
		String name = getName(file);
		
		MutableData mutableData = new GenericPrimitiveData();
		
		mutableData.setName(name);
		
		mutableData.set(fileData);
		
		LoadableData newData = new GenericLoadableData(File.class, mutableData);
		
		newData.toggleLoadedTrue();
		
		return new DataElement(newData);
		
	}
	
	private boolean shouldReadFile(Serializable loaderCriterian){
		return getHandledDataLoaderCriterian().contains(loaderCriterian);
	}
	
	private GenericLoadableData buildGenericLoadableData(Serializable loaderCriterian, File file){
		return new GenericLoadableData(loaderCriterian, buildGenericData(file));
	}
	
	private MutableData buildGenericData(File file){

		MutableData mutableData = new GenericMutableData();
		
		String name = getName(file);
		
		mutableData.setName(name);
		mutableData.set(file);
		
		return mutableData;
	}
	
	private boolean hasExtension(File file){
		return file.getName().indexOf(PathUtil.DOT) > 0;
	}
	
	private String getExtension(File file){
		if(!hasExtension(file)){
			return "";
		}
		return PathUtil.lastSegment(file.getName()).toLowerCase();
	}
	
	private File getFile(LoadableData loadableData){
		//intentional unchecked cast, only the data service should have called this method
		return (File)loadableData.get();
	}
	
	private String getName(File file){
		return hasExtension(file) ? 
				PathUtil.chompLastSegment(file.getName()) : 
					file.getName();
	}

	@Override
	public Set<Serializable> getHandledDataLoaderCriterian() {
		return HANDLED_CRITERIAN;
	}

}
