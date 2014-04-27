package com.clinkworks.neptical.data.graph;


public class BasicEdge implements Edge{
	
	String identity;
	Node sourceNode;
	Node targetNode;
	
	BasicEdge(String identity, Node sourceNode, Node targetNode){
		this.identity = identity;
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
	}
	
	@Override
	public Node getSourceNode(){
		return sourceNode;
	}
	
	@Override
	public Node getTargetNode(){
		return targetNode;
	}
	
	@Override
	public String getIdentity(){
		return identity;
	}

	

}
