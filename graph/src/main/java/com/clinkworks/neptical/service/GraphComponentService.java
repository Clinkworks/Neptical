package com.clinkworks.neptical.service;

import java.util.List;
import java.util.UUID;

import com.clinkworks.neptical.graph.Edge;
import com.clinkworks.neptical.graph.Node;
import com.clinkworks.neptical.graph.Path;
import com.clinkworks.neptical.spi.GraphComponentFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GraphComponentService {

	private final GraphComponentFactory gcf;
	
	@Inject
	public GraphComponentService(GraphComponentFactory gcf){
		this.gcf = gcf;
	}
	
	public Node createNode(String publicId){
		return gcf.createNode(gcf.createPublicId(publicId));
	}
	
	public Edge createEdge(String publicId, Node start, Node end){
		return gcf.createEdge(gcf.createPublicId(publicId), start, end);
	}

	public Edge createEdge(Node start, Node end) {
		String publicId = start.getPublicId().get() + end.getPublicId().get();
		return createEdge(publicId, start, end);
	}
	
	public Path createPath(String publicId, List<Edge> pathMakup){
		return gcf.createPath(gcf.createPublicId(publicId), pathMakup);
	}

	public Path createPath(List<Edge> pathMakeUp) {
		return createPath(UUID.randomUUID().toString(), pathMakeUp);
	}
	
	
	
}
