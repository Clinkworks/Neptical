package com.clinkworks.neptical.data.graph;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.clinkworks.neptical.DataService;
import com.clinkworks.neptical.data.api.Cursor;
import com.clinkworks.neptical.data.api.DataElement;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class TraversableDataMap implements Cursor{
	
	private final ListMultimap<Node, Edge> nodesToEdges;
	
	private final Node rootNode;
	private final DataService dataService;
	
	private final Object nodesToEdgesMutex;
	
	
	@Inject
	public TraversableDataMap(@Assisted DataElement rootData, DataService dataService){
		
		rootNode = new BasicNode("", "root", rootData);
		
		nodesToEdges = Multimaps.synchronizedListMultimap(ArrayListMultimap.<Node, Edge>create());
		
		//convience for looking up the root node with dot notation
		nodesToEdges.put(rootNode, new BasicEdge("//", null, rootNode));
		nodesToEdges.put(rootNode, new BasicEdge("root", null, rootNode));
		nodesToEdges.put(rootNode, new BasicEdge("", null, rootNode));
		
		this.dataService = dataService; 
		
		nodesToEdgesMutex = nodesToEdges;
	}

	public final Node getRootNode() {
		return rootNode;
	}
	
	@Override
	public DataElement find(String dotNotation) {
		
		Edge edge = findEdge(getRootNode(), dotNotation);
		
		if(edge != null){
			return edge.getTargetNode().getContents();
		}
		
		return findData();
	}
	
	private Edge matchEdge(Node node, String identity){

		List<Edge> edges = nodesToEdges.get(node);
		
		synchronized (nodesToEdgesMutex) {
			for(Edge possiblePath : edges){
				if(StringUtils.equals(identity, possiblePath.getIdentity())){
					return possiblePath;
				}
			}
		}
		
		return null;
	}
	
	private Edge scan(Node node, String dotNotation){
		
	}
	
	private Edge scan(String currentSegment, String remainingSegment, final String dotNotation, Edge lastKnownEdge){
		boolean 
	}

}
