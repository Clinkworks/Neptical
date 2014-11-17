package com.clinkworks.neptical.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
import com.clinkworks.neptical.util.PathUtil;
import com.google.inject.Inject;
import com.google.inject.Singleton;

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
	public Data find(String notation) {
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
		
		Node node = dataGraph.getNodeById(currentPath.current().getId());
				
		if(!currentPath.hasNext() && node != null){
			return DataUtil.wrap(node.getNepticalData());
		}
		
		Edge edge = dataGraph.getEdgeByPublicId(pathToSearchFor);
		
		if(edge != null){
			//TODO: we should check for aliasses here
			return DataUtil.wrap(edge.getEnd().getNepticalData());
		}
		
		Data data = DataUtil.wrap(lastFoundEdge.getEnd().getNepticalData());
		
		Data nestedData = data.find(pathToSearchFor);
		
		if(nestedData != null){
			dataGraph.linkNodesByPublicId(pathToSearchFor, dataGraph.createNode(data), dataGraph.createNode(nestedData));
			return nestedData;
		}
		
		//maybe just the next segment can be found
		nestedData = data.find(pa);
		
		if(nestedData == null){
			return null;
		}
		//the next segment of the path has been found,
		currentPath.next();
		lastFoundEdge = dataGraph.linkNodesByPublicId(pathToSearchFor, dataGraph.createNode(data), dataGraph.createNode(nestedData));
		
		return find(PathUtil.chompFirstSegment(pathToSearchFor), lastFoundEdge, currentPath.next());
	}

	void startCursorService(){
		Data dataDirectory = dataService.loadData();
		Path startingPath = convertToDotNotation(dataDirectory.getAsFile());
		startingPath.end().setNepticalData(dataDirectory);
		
		ListIterator<Segment> iterAtEnd = startingPath.listIteratorAtEnd();
		
		File dataFile = dataDirectory.getAsFile();
		
		while(iterAtEnd.hasPrevious()){
			Segment prevousSegment = iterAtEnd.previous();
			if(!prevousSegment.containsData()){
				prevousSegment.setNepticalData(dataService.loadFile(dataFile));
			}
			dataFile = dataFile.getParentFile();
		}
		
		Edge edge = dataGraph.graphPath(startingPath);
		dataGraph.alias(edge, startingPath.end().getName());		
		
		List<Path> paths = createFilePaths(dataDirectory);
		
		for(Path path : paths){
			dataGraph.graphPath(path);
		}
		
		dataGraph.dumpGraph();
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
	
	private Path convertToDotNotation(File file) {
		String fileAsDotNotation = file.toString().replace('\\', '.');
		fileAsDotNotation = fileAsDotNotation.replace('/', '.'); //theres probably a utility for this, ill look it up later
		return vocabularyService.parse(fileAsDotNotation);
	}
	
	private Segment createSegment(File file){
		return createSegment(dataService.loadFile(file));
	}
	
	private Segment createSegment(NepticalData nepticalData) {
		Segment segment =  new Segment(new PublicId(nepticalData.getName()));
		segment.setNepticalData(nepticalData);
		return segment;
	}
}
