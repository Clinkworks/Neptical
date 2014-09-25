package com.clinkworks.neptical.data.graph;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.DataService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class NepticalData{
	
	private final ListMultimap<Node, Edge> nodesToEdges;
	
	private final Node rootNode;
	private final DataService dataService;
	
	private final Object nodesToEdgesMutex;
	
	
	@Inject
	public NepticalData(@Assisted Data rootData, DataService dataService){
		
		rootNode = new BasicNode("", "root", rootData);
		
		nodesToEdges = Multimaps.synchronizedListMultimap(ArrayListMultimap.<Node, Edge>create());
				
		this.dataService = dataService; 
		
		nodesToEdgesMutex = nodesToEdges;
	}
	
	

	public final Node getRootNode() {
		return rootNode;
	}

}
