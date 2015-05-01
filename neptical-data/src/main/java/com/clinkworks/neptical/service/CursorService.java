package com.clinkworks.neptical.service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.datatype.FileData;
import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.domain.Path;
import com.clinkworks.neptical.domain.PathIterator;
import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.domain.Segment;
import com.clinkworks.neptical.graph.DataGraph;
import com.clinkworks.neptical.graph.Edge;
import com.clinkworks.neptical.graph.Node;
import com.clinkworks.neptical.spi.Cursor;
import com.clinkworks.neptical.util.DataUtil;

@Singleton
public class CursorService implements Cursor{
	
	private final VocabularyService vocabularyService;
	private final DataGraph dataGraph;
	private final DataService dataService;
	
	@Inject
    CursorService(DataGraph dataGraph, VocabularyService vocabularyService, DataService dataService) {
    	this.vocabularyService = vocabularyService;
    	this.dataService = dataService;
    	this.dataGraph = dataGraph;
    	startCursorService();
    }
	
	@Override
	public Data find(Serializable notation) {
		Path path = vocabularyService.parse(notation);	

		Node node = dataGraph.getNodeById(path.end().getId());
		
		if(node != null){
			return DataUtil.wrap(node.getNepticalData());
		}
		
		PathIterator iterAtEnd = path.pathIteratorAtEnd();
		Edge closestEdgeToEndOfPath = dataGraph.findEdgeClosestToEndOfPath(iterAtEnd);
		
		if(closestEdgeToEndOfPath == null){
			return null; // no edges were in the area of this path
		}
		
		return find(closestEdgeToEndOfPath, iterAtEnd);		
	}

	private Data find(Edge lastFoundEdge, PathIterator currentPath) {
		
		if(lastFoundEdge == null){
			//TODO: search for links from last discoverd node, there may be a path
			return null;
		}
		
		Segment next = currentPath.hasNext() ? currentPath.next() : null;
		Node lastFoundNode = lastFoundEdge.getEnd();
				
		Data discoveredData = DataUtil.wrap(lastFoundNode.getNepticalData());
		
		//going to go one segment at a time, just to ensure at least parent to child nodes are mapped.
		Data nestedData = discoveredData.find(next.getName());
		
		lastFoundEdge = dataGraph.linkNodesById(next.getId(), lastFoundNode, dataGraph.createNode(nestedData));
		
		if(currentPath.hasNext()){
			return find(lastFoundEdge, currentPath);
		}else{
			return nestedData;
		}
	}

	void startCursorService(){
		
		Data dataDirectory = dataService.loadData();
		
		List<Path> paths = createFilePaths(dataDirectory);
		
		for(Path path : paths){
			path = vocabularyService.clonePathAsDotNotation(path);
			dataGraph.graphPath(path);
		}
		
	}

	private List<Path> createFilePaths(FileData directory) {
		
		List<Path> pathsFromDataDirectory = new ArrayList<Path>();
		
		Segment startingSegment = createSegment(directory);
		
		for(File file : directory.getAsFile().listFiles()){
			Path newPathFromStartingDirectory = new Path();
			newPathFromStartingDirectory.appendSegment(startingSegment);
			pathsFromDataDirectory.add(newPathFromStartingDirectory);
			linkRecursively(pathsFromDataDirectory, newPathFromStartingDirectory, file);
		}
		
		return pathsFromDataDirectory;
	}
	
	private List<Path> createPaths(List<Path> pathsToCreate, Segment startingSegment, File fileToLoad) {
		Path path = new Path(new ArrayList<Segment>());
		path.appendSegment(startingSegment);
		path.appendSegment(createSegment(dataService.loadFile(fileToLoad)));
		
		if(fileToLoad.isDirectory()){
			for(File file : fileToLoad.listFiles()){
				pathsToCreate.add(path);
				path = createNewPath(pathsToCreate, path, file);
			}
		}
		
		return pathsToCreate;
	}
	
	private Path createNewPath(final List<Path> pathsToCreate, final Path currentPath, File nextFile){
		currentPath.appendSegment(createSegment(dataService.loadFile(nextFile)));

		if(nextFile.isDirectory()){
			for(File file : nextFile.listFiles()){
				if(file.isDirectory()){
					createPaths(pathsToCreate, currentPath.end(), file);
				}else{
					return createNewPath(pathsToCreate, currentPath, file);
				}
			}
		}
		return currentPath;
	}
	

	private void linkRecursively(List<Path> pathsFromDataDirectory, Path newPathFromStartingDirectory,  File file) {
		
		Data loadedData = dataService.loadFile(file);
		newPathFromStartingDirectory.appendSegment(createSegment(loadedData));
		
		if(!loadedData.isFileData()){
			return;
		}
		
		if(loadedData.isDirectory()){
			for(File nextFile : loadedData.getAsFile().listFiles()){
				Path branch = new Path();
				branch.appendSegments(newPathFromStartingDirectory);
				pathsFromDataDirectory.add(branch);
				linkRecursively(pathsFromDataDirectory, branch, nextFile);
			}
		}
		
	}
	
	private Segment createSegment(NepticalData nepticalData) {
		Segment segment =  new Segment(new PublicId(nepticalData.getName()));
		segment.setNepticalData(nepticalData);
		return segment;
	}

}
