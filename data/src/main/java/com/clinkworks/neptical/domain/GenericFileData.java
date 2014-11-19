package com.clinkworks.neptical.domain;

import java.io.File;
import java.io.Serializable;

import com.clinkworks.neptical.datatype.FileData;

public class GenericFileData extends GenericLoadableData implements FileData{

	private final File file;
	
	public GenericFileData(Serializable loaderCriterian, File file) {
		super(loaderCriterian, new GenericMutableData());
		setName(file.getName());
		set(file);
		this.file = file;
	}
	
	public GenericFileData(File file){
		super(File.class, new GenericMutableData());
		setName(file.getName());
		set(file);
		this.file = file;
	}

	public GenericFileData(Serializable loaderCriterian, FileData fileData) {
		super(loaderCriterian, fileData);
		this.file = fileData.getAsFile();
	}

	@Override
	public File getAsFile() {
		return file;
	}

	@Override
	public boolean isDirectory() {
		return file.isDirectory();
	}

	@Override
	public boolean isFile() {
		return file.isFile();
	}

}
