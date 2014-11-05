package com.clinkworks.neptical.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.clinkworks.neptical.api.NepticalId;
import com.clinkworks.neptical.util.DataComponentFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DataGraph {
	
	private final Map<NepticalId<?>, Edge> edgeLookup;
	
	private final DataComponentFactory dataComponentFactory;
	
	@Inject
	DataGraph(Map<NepticalId<?>, Edge> edgeLookup, DataComponentFactory dataComponentFactory){
		this.edgeLookup = edgeLookup;
		this.dataComponentFactory = dataComponentFactory;
	}
	
	public Edge linkNodesBy(String qualifiedName, Node start, Node end){
		
		Edge edge = getEdgeByFullyQualifiedId(qualifiedName);
		
		if(edge == null){
			edge = dataComponentFactory.createEdge(qualifiedName, start, end);
			edgeLookup.put(createId(qualifiedName), edge);
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

	public Edge getEdgeByFullyQualifiedId(String qualifiedIdentifier) {
		return edgeLookup.get(createId(qualifiedIdentifier));
	}
		
	private NepticalId<?> createId(String identifier) {
		return new NepticalId<String>(identifier);
	}
}
