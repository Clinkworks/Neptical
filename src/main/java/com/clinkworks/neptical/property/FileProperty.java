package com.clinkworks.neptical.property;

import java.io.File;
import java.io.Serializable;

import com.clinkworks.neptical.data.api.NepticalProperty;
import com.clinkworks.neptical.data.datatype.FileData;
import com.clinkworks.neptical.data.domain.GenericFileData;

public class FileProperty implements FileData, NepticalProperty{

	private final Serializable nepticalId;
	private final FileData fileData;

	public FileProperty(Serializable nepticalId, Serializable loaderCriterian, File file){
		this.nepticalId = nepticalId;
		fileData = new GenericFileData(loaderCriterian, file);
	}
	
	public FileProperty(Serializable nepticalId, File file){
		this.nepticalId = nepticalId;
		this.fileData = new GenericFileData(file);
	}
	
	public FileProperty(File file){
		this.fileData = new GenericFileData(file);
		this.nepticalId = file.getAbsolutePath();
	}
	
	public FileProperty(Serializable nepticalId, FileData fileData){
		this.nepticalId = nepticalId;
		this.fileData = fileData;
	}
	
	public FileProperty(FileData fileData){
		this.nepticalId = fileData.getAsFile().getAbsolutePath();
		this.fileData = fileData;
	}

	@Override
	public Serializable getNepticalId() {
		return nepticalId;
	}

	@Override
	public String getName() {
		return fileData.getName();
	}

	@Override
	public Object get() {
		return fileData.get();
	}

	@Override
	public void set(Object object) {
		fileData.set(object);
	}

	@Override
	public void setName(String name) {
		fileData.set(name);
	}

	@Override
	public Serializable getLoaderCriterian() {
		return fileData.getLoaderCriterian();
	}

	@Override
	public boolean isLoaded() {
		return fileData.isLoaded();
	}

	@Override
	public void toggleLoadedTrue() {
		fileData.toggleLoadedTrue();
	}

	@Override
	public void toggleLoadedFalse() {
		fileData.toggleLoadedFalse();
	}

	@Override
	public File getAsFile() {
		return fileData.getAsFile();
	}

	@Override
	public boolean isDirectory() {
		return fileData.isDirectory();
	}

	@Override
	public boolean isFile() {
		return fileData.isFile();
	}

}
