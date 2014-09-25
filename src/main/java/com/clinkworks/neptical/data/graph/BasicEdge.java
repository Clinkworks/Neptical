package com.clinkworks.neptical.data.graph;

import java.io.Serializable;

public class BasicEdge implements Edge{
	
	private final Serializable nepticalId;
	private Node start;
	private Node end;
	
	public BasicEdge(Serializable nepticalId){
		this.nepticalId = nepticalId;
	}
	
	public Node getStart(){
		return start;
	}
	
	public Node getEnd(){
		return end;
	}
	
	@Override
	public Serializable getNepticalId() {
		return nepticalId;
	}
	
	public synchronized void setEnd(Node end) {
		this.end = end;
	}

	public synchronized void setStart(Node start) {
		this.start = start;
	}
	
	public Edge createAlias(Serializable aliasId){
		return new EdgeAlias(this, aliasId);
	}

}
