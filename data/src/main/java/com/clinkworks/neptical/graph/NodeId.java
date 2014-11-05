package com.clinkworks.neptical.graph;

import java.util.UUID;

import com.clinkworks.neptical.api.NepticalId;

public class NodeId extends NepticalId<String>{

	public NodeId() {
		super(UUID.randomUUID().toString());
	}

}
