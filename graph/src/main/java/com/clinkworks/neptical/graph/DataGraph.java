package com.clinkworks.neptical.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.service.GraphComponentService;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DataGraph {
	
	private final Map<NepticalId<?>, Edge> edgeLookup;
	
	private final GraphComponentService graphComponentService;
	
	@Inject
	DataGraph(Map<NepticalId<?>, Edge> edgeLookup, GraphComponentService graphComponentService){
		this.edgeLookup = edgeLookup;
		this.graphComponentService = graphComponentService;
	}
		
	public Edge linkNodesByPublicId(String publicId, Node start, Node end){
				
		Edge edge = getEdgeByPublicId(publicId);
		
		if(edge == null){
			edge =graphComponentService.createEdge(publicId, start, end);
			edgeLookup.put(edge.getPublicId(), edge);
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

	public Edge getEdgeByPublicId(String qualifiedIdentifier) {
		return edgeLookup.get(getPublicIdForString(qualifiedIdentifier));
	}
		
	public Path getPathBetween(Node start, Node end) {
		
		Edge possiblePath = edgeLookup.get(createId(start, end));
		
		if(possiblePath != null){
			if(possiblePath instanceof Path){
				return (Path)possiblePath;
			}
			return graphComponentService.createPath(Lists.newArrayList(possiblePath));
		}
		
		List<Edge> edgesAssociatedWithStartNode = findEdgesStartingAt(start);
		
		Path path = null;
		
		for(Edge edge : edgesAssociatedWithStartNode){
			List<Edge> pathMakeUp = new ArrayList<Edge>();
			 
			if(edge.getStart() == start && edge.getEnd() == end){
				
				pathMakeUp.add(edge);
				return graphComponentService.createPath(pathMakeUp);
				
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
				path = graphComponentService.createPath(pathMakeUp);
			}
			
			if(path != null){
				edgeLookup.put(path.getId(), path);
				return path;
			}
			
			return discoverLinkedEdges(end, edge, pathMakeUp);
		}
		
		return null;
	}
	
	private PublicId getPublicIdForString(String id){
		return new PublicId(id);
	}
	
}
