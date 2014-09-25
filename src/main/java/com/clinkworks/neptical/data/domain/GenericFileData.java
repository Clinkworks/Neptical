package com.clinkworks.neptical.data.domain;

import java.io.File;
import java.io.Serializable;

import com.clinkworks.neptical.data.datatype.FileData;
import com.clinkworks.neptical.data.datatype.MutableData;

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
