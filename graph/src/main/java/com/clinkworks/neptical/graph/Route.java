package com.clinkworks.neptical.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.domain.PublicId;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * NOTE: this class does not support modifications to the graph structure. It reflects only what has been drawn.
 *
 */
public class Route implements Edge{
 
 	private final ListIterator<Node> routeMakeup;
 	private int length;
 	
 	private final Edge internalEdge;
	
	private volatile Node current;
	
 	@Inject
	Route(@Assisted PublicId publicId, @Assisted List<Edge> routeEdges) {
				
		List<Node> nodesWithinEdges = new ArrayList<Node>();
		length = 1; //account for the start node;
		
		if(routeEdges.size() == 0){
			throw new RuntimeException("Deatched node detected");			
		}
		
		Node start = routeEdges.get(0).getStart();
		Node end = routeEdges.get(routeEdges.size() - 1).getEnd();
		
		internalEdge = new Link(publicId, start, end);
		
		current = start; 
		
		for(Edge edge : routeEdges){
			length++;
			nodesWithinEdges.add(edge.getEnd());
		}
		routeMakeup = nodesWithinEdges.listIterator();
	}

	public boolean hasNext(){
		return routeMakeup.hasNext();
	}
	
	public boolean hasPrevious(){
		return routeMakeup.hasPrevious() && current != internalEdge.getStart();
	}
	
	public Node previous(){
		
		Node previousNode = null;
		
		if(routeMakeup.hasPrevious()){
			previousNode = routeMakeup.previous();
		}else if(current == internalEdge.getStart()){
			previousNode = null;
		}else{
			previousNode = internalEdge.getStart();
		}
		
		current = previousNode;
		return current;
		
	}
	
	public Node next() {
		current = routeMakeup.next();
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
