package com.clinkworks.neptical.data.graph;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.data.api.Cursor;
import com.clinkworks.neptical.data.api.NepticalProperty;
import com.clinkworks.neptical.data.datatype.FileData;
import com.clinkworks.neptical.data.domain.GenericImmutableData;
import com.clinkworks.neptical.property.FileProperty;
import com.clinkworks.neptical.util.PathUtil;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

public class Graph implements Cursor {

	public final Map<Serializable, NepticalProperty> identifiersToProperties;
	private final Set<Edge> edges;
	private final Set<Node> nodes;

	private final Object MUTEX;

	public Graph() {
		edges = Collections.synchronizedSet(Sets.<Edge> newHashSet());
		nodes = Collections.synchronizedSet(Sets.<Node> newHashSet());
		identifiersToProperties = new MapMaker().makeMap();
		MUTEX = new Object();
	}

	public void addData(Data data) {
		Edge edge = createDefaultEdgeForData(data);
		
		if (data.isFileData()) {
			graphNewFileData(edge, data.getAsFileData());
		}
	}

	private void graphNewFileData(Edge edge, FileData fileData) {
		
		File file = fileData.getAsFile();
		String absolutePath = file.getAbsolutePath();
		String linkedWithForwardSlashes = absolutePath.replace('\\', '/');
		String linkedWithBackSlashes = absolutePath.replace('/', '\\');
		String linkedWithDotNotation = linkedWithForwardSlashes.replace('/', '.');

		
	}

	private void createNameLookupEdges(Edge edge, FileData fileData) {
		String name = hasExtension(fileData.getAsFile()) ? PathUtil.chompLastSegment(fileData.getName()) : fileData.getName();
		
		if(identifiersToProperties.get(name) == null){	
			identifiersToProperties.put(name, edge.createAlias(name));
		}
		
		if(identifiersToProperties.get(fileData.getAsFile().getName()) != null){
			identifiersToProperties.put(name, edge.createAlias(fileData.getAsFile().getName()));
		}
	
	}

	private Edge createDefaultEdgeForData(Data data) {
		BasicEdge edge = new BasicEdge(createIdForData(data));
		edge.setEnd(new BasicNode(data));
		identifiersToProperties.put(edge.getNepticalId(), edge);
		return edge;
	}

	private Serializable createIdForData(Data data) {
		boolean isFileProperty = data.isDataType(FileProperty.class);

		if (isFileProperty) {
			return data.getAs(FileProperty.class).getNepticalId();
		}

		if (data.isFileData()) {
			return data.getAsFileData().getAsFile().getAbsolutePath();
		}

		return data.getName();
	}

	private Edge findClosestEdgeToFileData(FileProperty fileResource) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean hasExtension(File file){
		return file.getName().lastIndexOf(PathUtil.DOT) > 0;
	}
	
	private String getExtension(File file){
		if(!hasExtension(file)){
			return "";
		}
		return PathUtil.lastSegment(file.getName()).toLowerCase();
	}	

	@Override
	public Data find(String path) {
		return null;
	}

}
