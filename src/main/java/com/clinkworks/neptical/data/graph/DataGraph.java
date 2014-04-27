package com.clinkworks.neptical.data.graph;

import java.io.Serializable;
import java.util.List;

import com.clinkworks.neptical.DataService;
import com.clinkworks.neptical.data.api.Cursor;
import com.clinkworks.neptical.data.api.DataElement;
import com.clinkworks.neptical.data.datatypes.MutableData;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DataGraph implements MutableData, Cursor{
	
	private final ListMultimap<Serializable, Edge> identifierToEdges;
	private final Node rootNode;
	private final DataService dataService;
	
	private Node current;
	
	@Inject
	public DataGraph(@Assisted DataElement rootData, DataService dataService){
		rootNode = new BasicNode("", "root", rootData);
		identifierToEdges = Multimaps.synchronizedListMultimap(ArrayListMultimap.<Serializable, Edge>create());
		Edge rootEdge = new Edge("root", rootNode, null);
		identifierToEdges.put("", rootEdge);
		identifierToEdges.put("root", rootEdge);
		identifierToEdges.put("//", rootEdge);
		this.dataService = dataService; 
		current = null;
	}

	public final Node getRootNode() {
		return rootNode;
	}
	
	@Override
	public Object get(){
		return getData() == null ? null : getData().get();
	}
	
	@Override
	public DataElement getData(){
		if(current == null){
			return null;
		}
		return current.getContents();
	}

	@Override
	public DataElement find(String path) {
		List<Edge> edges = identifierToEdges.get(path);
		current = edges.size() > 0 ? edges.get(0).getSourceNode() : null;
		return getData();
	}

	@Override
	public void add(String path, DataElement dataElement) {
		
	}
	
	@Override public boolean delete(){
		return false;
	}
	
	public static class Edge{
		
		Serializable identity;
		Node sourceNode;
		Node targetNode;
		
		Edge(Serializable identity, Node sourceNode, Node targetNode){
			this.identity = identity;
			this.sourceNode = sourceNode;
			this.targetNode = targetNode;
		}
		
		public Node getSourceNode(){
			return sourceNode;
		}
		
		public Node getTargetNode(){
			return targetNode;
		}
		
		public Serializable getIdentity(){
			return identity;
		}
		
	}

	@Override
	public String getName() {
		return getData().getName();
	}

	@Override
	public void set(Object object) {
		getData().set(object);
	}

	@Override
	public void setName(String name) {
		getData().setName(name);
	}

}
