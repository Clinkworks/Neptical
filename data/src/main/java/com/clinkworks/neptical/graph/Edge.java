package com.clinkworks.neptical.graph;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class Edge {
	
	private final String identifier;
	private final Node start;
	private final Node end;
	
	@Inject
	public Edge(@Assisted String identifier, @Assisted("start") Node start, @Assisted("end") Node end){
		this.identifier = identifier;
		this.start = start;
		this.end = end;
	}

	public String getIdentifier() {
		return identifier;
	}

	public Node getStart() {
		return start;
	}

	public Node getEnd() {
		return end;
	}

}
