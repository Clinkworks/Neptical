package com.clinkworks.neptical.data.graph;

import java.io.Serializable;

import com.clinkworks.neptical.data.api.NepticalProperty;

public class EdgeAlias implements Edge{

	private final Edge aliasedEdge;
	private final Serializable alias;
	
	public EdgeAlias(Edge toAlias, Serializable alias){
		aliasedEdge = toAlias;
		this.alias = alias;
	}
	
	@Override
	public Serializable getNepticalId() {
		return alias;
	}

	@Override
	public Node getStart() {
		return aliasedEdge.getStart();
	}

	@Override
	public Node getEnd() {
		return aliasedEdge.getEnd();
	}

	@Override
	public NepticalProperty createAlias(Serializable aliasId) {
		return aliasedEdge.createAlias(aliasId);
	}
	
	public Edge getEdge(){
		return aliasedEdge;
	}

}
