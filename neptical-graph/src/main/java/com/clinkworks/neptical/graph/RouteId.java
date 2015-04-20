package com.clinkworks.neptical.graph;

public class RouteId extends EdgeId{

	private static final long serialVersionUID = 1270775037181943691L;
	
	RouteId(Node start, Node end) {
		super(start, end);
	}

	public RouteId(Edge internalEdge) {
		super(internalEdge.getStart(), internalEdge.getEnd());
	}


}
