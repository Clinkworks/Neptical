package com.clinkworks.neptical;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.api.Cursor;
import com.clinkworks.neptical.api.DataLoader;
import com.clinkworks.neptical.datatype.FileData;
import com.clinkworks.neptical.datatype.LoadableData;
import com.clinkworks.neptical.domain.GenericFileData;
import com.clinkworks.neptical.modules.NepticalPropertiesModule.DataDirectory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DataService implements Cursor{
	
	private final Map<Serializable, DataLoader> dataLoaderRegistry;	
	private final File dataDirectory;
	
	@Inject
	public DataService(@DataDirectory File dataDirectory, Map<Serializable, DataLoader> dataLoaderRegistry){
		this.dataLoaderRegistry = dataLoaderRegistry;
		this.dataDirectory = dataDirectory;
	}
	
	public Data loadData(){
		return loadFile(dataDirectory);
	}
	
	public Data loadFile(File file){
		FileData fileData = new GenericFileData(File.class, file);
		return loadData(fileData);	
		
	}

	public Data loadData(LoadableData loadableData) {
		
		if(loadableData.isLoaded()){
			return (Data)loadableData;
		}
		
		Serializable loaderCriterian = loadableData.getLoaderCriterian();
		
		DataLoader dataLoader = dataLoaderRegistry.get(loaderCriterian);
		
		if(dataLoader == null){
			throw new IllegalStateException("No data loader exists for criterian " + loadableData.getLoaderCriterian());
		}
		
		Data data = dataLoader.loadData(loadableData);
		
		//note: I realize this is probably an unsafe thing to do,
		//TODO: test the shit out of this method and make sure that it cannot cause a stack overflow.	
		if(data.isLoadableData() && !data.isLoaded()){
			return loadData(data);
		}
		
		return data;
	}

	@Override
	public Data find(String notation) {
		return null;
	}
	
//	private void createAndAddLocations(FileProperty fileProperty) {
//		File file = fileProperty.getAsFile();
//		String absolutePath = file.getAbsolutePath();
//		
//		String dottedDataDirectory = dataDirectory.getAbsolutePath().replace('\\', '/').replace('/', '.');
//		
//		List<Serializable> identifiers = Lists.newArrayList();
//		
//		identifiers.add(file);
//		identifiers.add(absolutePath);
//		//should take care of both linux and windows absolute paths, should test on linux
//		String linkedWithDotNotation = absolutePath.replace('\\', '.').replace('/', '.');
//		String linkedWithDotNotationNoDataDirectory = linkedWithDotNotation.replace(dottedDataDirectory + ".", "");
//		identifiers.add(linkedWithDotNotation);
//		identifiers.add(linkedWithDotNotationNoDataDirectory);
//		
//		
//		if(Common.hasExtension(file)){
//			String linkedWithDotNotationNoExtension = PathUtil.subtractSegment(Common.getExtension(file), linkedWithDotNotation);
//			identifiers.add(linkedWithDotNotationNoExtension);
//			String linkedWithoutDataDirectory = linkedWithDotNotationNoExtension.replace(dottedDataDirectory + ".", "");
//			identifiers.add(linkedWithoutDataDirectory);
//		}
//		
//		Location propertyLocation = new GraphLocation(new GenericLocation(fileProperty.getNepticalId(), fileProperty));
//		
//		graph.addLocation(propertyLocation);
//		
//		for(Serializable identifier : identifiers){
//			graph.aliasProperty(identifier, propertyLocation);
//		}
//		
//	}
//	
//	public void addFileToNepticalData(FileProperty fileProperty) {
//		
//		
//		
//		if (data.isFileData()) {
//			graphNewFileData(edge, data.getAsFileData());
//		}
//	}
//
//	private Set<Location> graphNewFileData(Edge edge, FileData fileData) {
//		
//		File file = fileData.getAsFile();
//		String absolutePath = file.getAbsolutePath();
//		String linkedWithForwardSlashes = absolutePath.replace('\\', '/');
//		String linkedWithBackSlashes = absolutePath.replace('/', '\\');
//		String linkedWithDotNotation = linkedWithForwardSlashes.replace('/', '.');
//
//		
//	}
//	
}
