package com.clinkworks.neptical.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import com.clinkworks.neptical.api.NepticalId;
import com.clinkworks.neptical.api.PublicId;
import com.clinkworks.neptical.util.DataComponentFactory;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * NOTE: this class does not support modifications to the graph structure. It reflects only what has been drawn.
 *
 */
public class Path implements Edge{
 
 	private final ListIterator<Node> pathMakeup;
 	private int length;
 	
 	private final Edge internalEdge;
	
	private volatile Node current;
	
 	@Inject
	Path(@Assisted List<Edge> pathEdges, DataComponentFactory dataComponentFactory) {
				
		List<Node> nodesWithinEdges = new ArrayList<Node>();
		length = 1; //account for the start node;
		
		if(pathEdges.size() == 0){
			throw new RuntimeException("Deatched node detected");			
		}
		
		Node start = pathEdges.get(0).getStart();
		Node end = pathEdges.get(pathEdges.size() - 1).getEnd();
		current = start; 

		internalEdge = dataComponentFactory.createSegment(UUID.randomUUID().toString(), start, end);
		
		for(Edge edge : pathEdges){
			length++;
			nodesWithinEdges.add(edge.getEnd());
		}

		pathMakeup = nodesWithinEdges.listIterator();
	}

	public boolean hasNext(){
		return pathMakeup.hasNext();
	}
	
	public boolean hasPrevious(){
		return pathMakeup.hasPrevious() && current != internalEdge.getStart();
	}
	
	public Node previous(){
		
		Node previousNode = null;
		
		if(pathMakeup.hasPrevious()){
			previousNode = pathMakeup.previous();
		}else if(current == internalEdge.getStart()){
			previousNode = null;
		}else{
			previousNode = internalEdge.getStart();
		}
		
		current = previousNode;
		return current;
		
	}
	
	public Node next() {
		current = pathMakeup.next();
		return current;
	}

	@Override
	public Node getEnd() {
		return internalEdge.getEnd();
	}

	public int getLength() {
		return length;
	}	

	public Node get(){
		return current;
	}

	@Override
	public Node getStart() {
		return internalEdge.getStart();
	}

	@Override
	public NepticalId<?> getId() {
		return internalEdge.getId();
	}

	@Override
	public PublicId getPublicId() {
		return internalEdge.getPublicId();
	}
	
	@Override
	public String toString(){
		return getId().toString();
	}
}
