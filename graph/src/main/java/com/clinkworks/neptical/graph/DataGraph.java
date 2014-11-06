package com.clinkworks.neptical.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.spi.GraphComponentFactory;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DataGraph {
	
	private final Map<NepticalId<?>, Edge> edgeLookup;
	
	private final GraphComponentFactory gcf;
	
	@Inject
	DataGraph(Map<NepticalId<?>, Edge> edgeLookup, GraphComponentFactory gcf){
		this.edgeLookup = edgeLookup;
		this.gcf = gcf;
	}
	
	public Edge linkNodesByNepticalId(NepticalId<?> nepticalId, Node start, Node end){
		
		Edge edge = getEdgeByFullyQualifiedId(nepticalId);
		
		if(edge == null){
			edge = createEdge(start, end);
			edgeLookup.put(nepticalId, edge);
		}
		
		return edge;
		
	}
	
	

	public Edge linkNodesByPublicId(String publicId, Node start, Node end){
				
		Edge edge = getEdgeByPublicId(publicId);
		
		if(edge == null){
			edge = createEdge(publicId, start, end);
			edgeLookup.put(edge.getPublicId(), edge);
			edgeLookup.put(edge.getId(), edge);
		}
		
		return edge;
	}

	public List<Edge> findEdgesStartingAt(Node start){
		
		List<Edge> foundEdges = new ArrayList<Edge>();
		
		if(start == null){
			return foundEdges;
		}
		
		Collection<Edge> edges = edgeLookup.values();
		
		for(Edge edge : edges){
			boolean edgeContainsStartNode = edge.getStart().equals(start);
			if(edgeContainsStartNode){
				foundEdges.add(edge);
			}
		}
		
		return foundEdges;
	}
	
	public Edge getEdgeByFullyQualifiedId(NepticalId<?> nepticalId){
		return edgeLookup.get(nepticalId);
	}

	public Edge getEdgeByPublicId(String publicId) {
		return edgeLookup.get(createPublicId(publicId));
	}
		
	public Path getPathBetween(Node start, Node end) {
		
		Edge possiblePath = edgeLookup.get(createId(start, end));
		
		if(possiblePath != null){
			if(possiblePath instanceof Path){
				return (Path)possiblePath;
			}
			return createPath(Lists.newArrayList(possiblePath));
		}
		
		List<Edge> edgesAssociatedWithStartNode = findEdgesStartingAt(start);
		
		Path path = null;
		
		for(Edge edge : edgesAssociatedWithStartNode){
			List<Edge> pathMakeUp = new ArrayList<Edge>();
			 
			if(edge.getStart() == start && edge.getEnd() == end){
				
				pathMakeUp.add(edge);
				return createPath(pathMakeUp);
				
			}
			
			path = discoverLinkedEdges(end, edge, pathMakeUp);
			
			if(path != null){
				return path;
			}
			
		}
		
		return path;
	}

	private NepticalId<?> createId(Node start, Node end) {
		return new EdgeId(start, end);
	}

	private Path discoverLinkedEdges(Node end, Edge currentEdge,  List<Edge> pathMakeUp) {
		
		//if the search cannot find the end, the path is unresovable.
		if(currentEdge == null){
			return null;
		}
		
		pathMakeUp.add(currentEdge);
		
		Node start = currentEdge.getEnd();
		
		List<Edge> edgesStartingWithLastLink = findEdgesStartingAt(start);
		
		Path path = null;
		
		for(Edge edge : edgesStartingWithLastLink){

			pathMakeUp.add(edge);
			
			if(edge.getEnd() == end){
				path = createPath(pathMakeUp);
			}
			
			if(path != null){
				edgeLookup.put(path.getId(), path);
				return path;
			}
			
			return discoverLinkedEdges(end, edge, pathMakeUp);
		}
		
		return null;
	}
	
	private Edge createEdge(String publicId, Node start, Node end){
		return gcf.createEdge(createPublicId(publicId), start, end);
	}
	
	private Path createPath(String publicId, List<Edge> pathMakup){
		return gcf.createPath(createPublicId(publicId), pathMakup);
	}

	private PublicId createPublicId(String publicId) {
		return new PublicId(publicId);
	}

	private Path createPath(List<Edge> pathMakeUp) {
		return createPath(UUID.randomUUID().toString(), pathMakeUp);
	}
	
	private Edge createEdge(Node start, Node end) {
		return createEdge(UUID.randomUUID().toString(), start, end);
	}
	
}
