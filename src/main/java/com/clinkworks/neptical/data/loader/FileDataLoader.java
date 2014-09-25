package com.clinkworks.neptical.data.loader;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.data.api.DataLoader;
import com.clinkworks.neptical.data.api.NepticalData;
import com.clinkworks.neptical.data.datatype.FileData;
import com.clinkworks.neptical.data.datatype.LoadableData;
import com.clinkworks.neptical.data.domain.GenericFileData;
import com.clinkworks.neptical.property.FileProperty;
import com.clinkworks.neptical.util.Common;
import com.clinkworks.neptical.util.PathUtil;
import com.google.common.collect.Sets;

public class FileDataLoader implements DataLoader{

	public static enum FileDataLoaderCriterian{
		READ_AS_TEXT;
	}
	
	public static final Set<String> HANDLED_EXTENSIONS;
	private static final Set<Serializable> HANDLED_CRITERIAN;

	static{
		HANDLED_EXTENSIONS= Sets.newHashSet("txt", "csv");
		
		Set<Serializable> criterian = Sets.newHashSet((Serializable[])FileDataLoaderCriterian.values());
		criterian.addAll(HANDLED_EXTENSIONS);
		criterian.add(File.class);
		HANDLED_CRITERIAN = criterian;
	}
	
	public FileDataLoader(){
		
	}
	
	@Override
	public Data loadData(LoadableData loadableData) {
		
		if(loadableData.isLoaded()){
			if(isAlreadyDataWrapped(loadableData)){
				return (Data)loadableData;
			}else{
				return new Data(loadableData);
			}
		}
		
		return handleLoaderCriterian(loadableData);

	}
	
	private Data handleLoaderCriterian(LoadableData loadableData){
		
		Serializable loaderCriterian = loadableData.getLoaderCriterian();
		
		if(loaderCriterianIsFileClass(loaderCriterian)){
			return handleFile(loadableData);
		}
		
		if(shouldReadFile(loaderCriterian)){
			return handleRead(loadableData);
		}
		
		throw new UnsupportedOperationException("The file data loader cannot handle loader criterian: " + loaderCriterian);
	}
	
	private Data handleFile(LoadableData loadableData){
		
		File file = getFile(loadableData);
		
		if(file.isDirectory()){
			
			loadableData.toggleLoadedTrue();
			
			if(isAlreadyDataWrapped(loadableData)){
				Data loadedData = (Data)loadableData;
				if(loadedData.isDataType(FileProperty.class)){
					return (Data)loadableData;
				}
			}
			
			if(isFileResource(loadableData)){
				return new Data(loadableData);
			}else if(isFileData(loadableData)){
				LoadableData newResource = new FileProperty(new FileProperty((FileData)loadableData));
				newResource.toggleLoadedTrue();
				return new Data(newResource);
			}
			
		}
		
		return createUnloadedDataWithExtensionAsCriterian(loadableData);
	}



	private boolean isFileData(LoadableData loadableData) {
		return ClassUtils.isAssignable(loadableData.getClass(), FileData.class);
	}

	private Data createUnloadedDataWithExtensionAsCriterian(LoadableData loadableData) {
		return new Data(buildFileResource(getExtension(getFile(loadableData)), loadableData));
	}

	private boolean loaderCriterianIsFileClass(Serializable loaderCriterian) {
		return ObjectUtils.equals(loaderCriterian, File.class);
	}
	
	private Data handleRead(LoadableData loadableData){
		
		File file = getFile(loadableData);
		Data data = getOrCreateFileResourceDataFromLoadableData(loadableData);
		
		if(file.isDirectory()){
			data.set(file);
		}else{
			String fileData = Common.readFile(file);
			data.set(fileData);
			data.setName(hasExtension(file) ? PathUtil.chompLastSegment(file.getName()) : file.getName());
		}
		
		data.toggleLoadedTrue();
		
		return data;
	}
	
	private Data getOrCreateFileResourceDataFromLoadableData(LoadableData loadableData) {
		
		FileProperty fileResource = null;
		
		if(isAlreadyDataWrapped(loadableData)){
			Data data = (Data)loadableData;
				
			if(data.isDataType(FileProperty.class)){
				fileResource = data.getAsDataType(FileProperty.class);
			}else if(data.isFileData()){
				fileResource = new FileProperty(data.getAsFileData());
			}else{
				//assuming a generic NepticalData element with a file as its stored object
				fileResource = new FileProperty((File)data.get()); //will result in a class cast if somehow
				//this is possible
			}
		}else{
			if(isFileResource(loadableData)){
				fileResource = (FileProperty)loadableData;
			}
			
			if(fileResource == null){
				//should not have ever made it this far without a file actually being in the NepticalData
				fileResource = new FileProperty((File)loadableData.get());
			}
		}
		
		return isAlreadyDataWrapped(fileResource) ? (Data)(NepticalData)fileResource : new Data(fileResource);
		
	}

	private boolean isFileResource(LoadableData loadableData) {
		return ClassUtils.isAssignable(loadableData.getClass(), FileProperty.class);
	}

	private boolean isAlreadyDataWrapped(LoadableData loadableData) {
		return ClassUtils.isAssignable(loadableData.getClass(), Data.class);
	}

	private boolean shouldReadFile(Serializable loaderCriterian){
		return getHandledDataLoaderCriterian().contains(loaderCriterian);
	}
	
	private FileProperty buildFileResource(Serializable loaderCriterian, LoadableData loadableData){
		return new FileProperty(getNepticalId(loadableData), loaderCriterian, getFile(loadableData));
	}
	
	private Serializable getNepticalId(LoadableData loadableData) {
		if(ClassUtils.isAssignable(loadableData.getClass(), FileProperty.class)){
			return ((FileProperty)loadableData).getLoaderCriterian();
		}
		return getFile(loadableData).getAbsolutePath();
	}
	
	private boolean hasExtension(File file){
		return file.getName().lastIndexOf(PathUtil.DOT) > 0;
	}
	
	private String getExtension(File file){
		if(!hasExtension(file)){
			return "";
		}
		return PathUtil.lastSegment(file.getName()).toLowerCase();
	}
	
	private File getFile(LoadableData loadableData){
		//intentional unchecked cast, only raw system files should be handled by this class by contract
		return (File)loadableData.get();
	}

	@Override
	public Set<Serializable> getHandledDataLoaderCriterian() {
		return HANDLED_CRITERIAN;
	}

}
