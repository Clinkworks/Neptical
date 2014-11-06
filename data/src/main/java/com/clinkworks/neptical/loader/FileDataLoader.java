package com.clinkworks.neptical.loader;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.api.DataLoader;
import com.clinkworks.neptical.datatype.FileData;
import com.clinkworks.neptical.datatype.LoadableData;
import com.clinkworks.neptical.domain.GenericFileData;
import com.clinkworks.neptical.util.Common;
import com.clinkworks.neptical.util.DataUtil;
import com.clinkworks.neptical.util.PathUtil;
import com.google.common.collect.Sets;

public class FileDataLoader implements DataLoader{

	public static enum FileDataLoaderCriterian{
		READ_AS_TEXT;
	}
	
	public static final Set<String> HANDLED_EXTENSIONS;
	private static final Set<Serializable> HANDLED_CRITERIAN;

	static{
		HANDLED_EXTENSIONS = Sets.newHashSet("txt", "csv");
		
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
			return handleRead(getLoadableDataAsWrappedData(loadableData));
		}
		
		throw new UnsupportedOperationException("The file data loader cannot handle loader criterian: " + loaderCriterian);
	}
	
	private Data handleFile(LoadableData loadableData){
		
		FileData fileData = DataUtil.getAsDataType(FileData.class, loadableData);
		
		File file = fileData.getAsFile();
		
		if(fileData.isDirectory()){			
			loadableData.toggleLoadedTrue();
		}else{
			if(Common.hasExtension(file)){
				fileData.setLoaderCriterian(PathUtil.chompLastSegment(file.getName()));
			}else{
				fileData.setLoaderCriterian(file.getName());
			}
		}
		
		return DataUtil.wrap(loadableData);
	}
	

	private Data getLoadableDataAsWrappedData(LoadableData loadableData) {
		
		File file = getFile(loadableData);
		
		if(file == null){
			throw new RuntimeException("Loadable data cannot be handled by the file data loader");
		}
		
		if(isAlreadyDataWrapped(loadableData)){
			return DataUtil.getAsDataType(Data.class, loadableData);
		}
		
		if(DataUtil.isDataType(FileData.class, loadableData)){
			return new Data(loadableData);
		}
		
		FileData fileData = new GenericFileData(file);
		
		return new Data(fileData);
		
	}

	private boolean loaderCriterianIsFileClass(Serializable loaderCriterian) {
		return ObjectUtils.equals(loaderCriterian, File.class);
	}
	
	private Data handleRead(Data data){
		
		File file = getFile(data);
		
		if(file.isDirectory()){
			data.set(file);
		}else{
			String fileData = Common.readFile(file);
			data.set(fileData);
			data.setName(Common.hasExtension(file) ? PathUtil.chompLastSegment(file.getName()) : file.getName());
		}
		
		data.toggleLoadedTrue();
		
		return data;
	}

	private boolean isAlreadyDataWrapped(LoadableData loadableData) {
		return ClassUtils.isAssignable(loadableData.getClass(), Data.class);
	}

	private boolean shouldReadFile(Serializable loaderCriterian){
		return getHandledDataLoaderCriterian().contains(loaderCriterian);
	}

	private File getFile(LoadableData loadableData){
		
		if(DataUtil.isDataType(FileData.class, loadableData)){
			FileData fileData = DataUtil.getAsDataType(FileData.class, loadableData);
			return fileData.getAsFile();
		}
		
		//Whoever constructed this guy didn't implement file data or use the generic file data or file property classes, so may just need the data from the file
		//but he should have at least stored the file in the getter
		return (File)loadableData.get();
	}

	@Override
	public Set<Serializable> getHandledDataLoaderCriterian() {
		return HANDLED_CRITERIAN;
	}

}