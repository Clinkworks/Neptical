package com.clinkworks.neptical.data.file;

import java.io.File;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.DataRegistry;
import com.google.common.collect.Sets;

import static com.clinkworks.neptical.util.PathUtil.*;

final public class FileData extends Data{
	
	private final File data;
	
	public FileData(String segment, String path, Data head, Data parent, File file) {
		super(segment, path, head, parent, file);
		data = file;
	}

	@Override
	public Data find(String path) {
		
		if(getPath().equals(path)){
			return this;
		}
		
		String remainingSegment = subtractSegment(getPath(), path);
		String nextSegment = firstSegment(remainingSegment);

		if(StringUtils.equals(getExtension(), nextSegment)){

			remainingSegment = chompFirstSegment(remainingSegment);

		}
		
		if(isLoaded()){
			return next().find(remainingSegment);
		}
		
		Data data = loadFile(nextSegment);
		
		if(StringUtils.isBlank(nextSegment)){
			return data;
		}
		
		if(StringUtils.equals(remainingSegment, nextSegment)){
			return data;
		}
		
		return data.find(remainingSegment);
	}
		
	
	private Data loadFile(String nextSegment) {
		File file = getAsFile();
		if(isDirectory()){
			setNext(new Directory(nextSegment, getPath(), this, this, file));
		}
		if(isFile()){
			setNext(DataRegistry.loadData(nextSegment, getPath(), this, file));
		}
		
		return next();
	}

	public boolean isFile(){
		return getAsFile().isFile();
	}
	
	public boolean isDirectory(){
		return getAsFile().isDirectory();
	}
	
	public File getAsFile(){
		return data;
	}
	
	private boolean isLoaded(){
		return next() != null;
	}
	
	@Override
	public String getExtension(){
		return lastSegment(getAsFile().getName());
	}

	private static final class Directory extends Data{
		
		private final Set<Data> linkedNodes;
		
		public Directory(String segment, String path, Data head, Data parent, File data) {
			super(segment, path, head, parent, data);
			linkedNodes = Sets.newConcurrentHashSet();
			loadDirectory(data);
		}
		
		@Override
		public Data find(String path) {
			if(getPath().equals(path)){
				return this;
			}
			
			String remainingSegment = subtractSegment(getPath(), path);
			String nextSegment = firstSegment(remainingSegment);			
			
			for(Data data : linkedNodes){
				if(StringUtils.equals(nextSegment, data.getSegment())){
					return data.find(remainingSegment);
				}
			}
			
			return null;
		}
		
		@Override
		public File getAsFile(){
			return (File)get();
		}
		
		public File[] getFileList(){
			return getAsFile().listFiles();
		}
		
		private void loadDirectory(File data) {
			for(File file : getFileList()){
				
				String fileName = file.getName();
				String segment = fileName;
				
				if(file.isFile()){
					if(segment.indexOf(DOT) > -1){
						segment = chompLastSegment(fileName);
					}
				}
				
				String path = clean(getPath() + DOT + segment);
				
				Data dataInDiectory = new FileData(segment, path, root(), this, file);
				
				linkedNodes.add(dataInDiectory);
			}
		}
	}
}
