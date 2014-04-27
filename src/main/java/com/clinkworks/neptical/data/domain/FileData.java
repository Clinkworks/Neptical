package com.clinkworks.neptical.data.domain;

import java.io.File;

public class FileData extends GenericLoadableData{

	public FileData(File file) {
		super(File.class, new GenericMutableData());
		set(file);
		setName(file.getName());
	}
	
	public File getAsFile(){
		return (File)get();
	}
	
	public boolean isDirectory(){
		return getAsFile().isDirectory();
	}
	
	public boolean isFile(){
		return getAsFile().isFile();
	}

}
