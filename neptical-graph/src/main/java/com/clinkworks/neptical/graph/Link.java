package com.clinkworks.neptical.graph;

import javax.inject.Inject;

import com.clinkworks.neptical.datatype.NepticalId;
import com.google.inject.assistedinject.Assisted;

public class Link implements Edge{
	
	private final EdgeId edgeId;
	private final Node start;
	private final Node end;
	private String name;
	
	@Inject
	Link(@Assisted("start") Node start, @Assisted("end") Node end){
		this.end = end;
		this.start = start;
		edgeId = new EdgeId(start, end);
	}

	@Override
	public Node getStart() {
		return start;
	}

	@Override
	public Node getEnd() {
		return end;
	}

	@Override
	public NepticalId<?> getId() {
		return edgeId;
	}

	public String getName(){
		return name;
	}
	
	@Override
	public String toString(){
		return getEnd().toString();
	}	
	
}
