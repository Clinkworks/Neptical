package com.clinkworks.neptical.domain;

import java.io.File;
import java.io.Serializable;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.datatype.FileData;
import com.clinkworks.neptical.spi.Cursor;
import com.clinkworks.neptical.util.DataUtil;

public class GenericDirectoryData implements Cursor, FileData{

	private final Data directoryData;
	
	public GenericDirectoryData(File file) {
		this.directoryData = new Data(this);
	}
	
	public GenericDirectoryData(FileData fileData){
		this.directoryData = DataUtil.wrap(fileData);
	}
	
	@Override
	public Serializable getLoaderCriterian() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLoaded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void toggleLoadedTrue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toggleLoadedFalse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public File getAsFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDirectory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Data find(String notation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLoaderCriterian(Serializable loaderCriterian) {
		// TODO Auto-generated method stub
		
	}

}
