package com.clinkworks.neptical.data.graph;

import java.io.Serializable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class DataMultiGraph{
	
	private final Multimap<String, Edge> identifierToEdges;
	
	public DataMultiGraph(){
		identifierToEdges = Multimaps.synchronizedListMultimap(ArrayListMultimap.<String, Edge>create());
	}
	
	private class Edge{
		Serializable identity;
		Node sourceNode;
		Node targetNode;
	}
}
