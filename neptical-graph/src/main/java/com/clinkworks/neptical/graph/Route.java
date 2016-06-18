package com.clinkworks.neptical.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import com.clinkworks.neptical.datatype.NepticalId;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

/**
 * NOTE: this class does not support modifications to the graph structure. It reflects only what has been drawn.
 *
 */
public class Route implements Edge, Iterable<Node>{
 
	private final RouteId routeId;
 	private final List<Node> routeMakeup;
 	private int length;
	private Link internalEdge;
 	
 	@Inject
	Route(@Assisted List<Edge> routeEdges) {
				
		List<Node> nodesWithinEdges = new ArrayList<Node>();
		length = 0; 
		
		if(routeEdges.size() == 0){
			throw new RuntimeException("Deatched node detected");			
		}
		
		Node start = routeEdges.get(0).getStart();
		Node end = routeEdges.get(routeEdges.size() - 1).getEnd();
		
		//account for the root node
		length++;
		nodesWithinEdges.add(start);
		
		internalEdge = new Link(start, end);
		routeId = new RouteId(internalEdge);
		
		for(Edge edge : routeEdges){
			nodesWithinEdges.add(edge.getEnd());
			length++;
		}
		routeMakeup = ImmutableList.copyOf(nodesWithinEdges);
	}

	public int getLength() {
		return length;
	}	

	@Override
	public Node getStart() {
		return internalEdge.getStart();
	}

	@Override
	public NepticalId<?> getId() {
		return routeId;
	}

	@Override
	public String toString(){
		return getId().toString();
	}

	@Override
	public Iterator<Node> iterator() {
		return listIterator();
	}
	
	public ListIterator<Node> listIterator(){
		return routeMakeup.listIterator();
	}

	@Override
	public Node getEnd() {
		return internalEdge.getEnd();
	}

}
