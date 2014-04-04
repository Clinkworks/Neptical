package com.clinkworks.neptical.data.file;

import java.io.File;
import java.util.List;

import com.clinkworks.neptical.data.Data;
import static com.clinkworks.neptical.util.PathUtil.*;

final public class FileData extends Data{
	
	private final File data;
	
	public FileData(String segment, String path, Data head, Data parent, File file) {
		super(segment, path, parent, head, file);
		data = file;
	}

	@Override
	public Data find(String path) {
		
		if(getPath().equals(path)){
			return this;
		}
		
		String remainingSegment = subtractSegment(getPath(), path);
		String nextSegment = firstSegment(remainingSegment);
		
		if(isLoaded()){
			return getNext().find(remainingSegment);
		}else{
			return loadFile(nextSegment, remainingSegment);
		}
		
	}
	
	private Data loadFile(String nextSegment, String remainingSegment) {
		return null;
	}

	public boolean isFile(){
		return data.isFile();
	}
	
	public boolean isDirectory(){
		return data.isDirectory();
	}
	
	public boolean isLoaded(){
		return (isDirectory() || !(get() instanceof File));
	}

	@Override
	public <T> T get(Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getList(Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

}
