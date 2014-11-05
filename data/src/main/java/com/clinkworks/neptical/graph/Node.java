package com.clinkworks.neptical.graph;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.api.Identifiable;
import com.clinkworks.neptical.api.NepticalId;
import com.clinkworks.neptical.api.PublicId;
import com.clinkworks.neptical.domain.GenericMutableData;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class Node extends Data implements Identifiable{

	private final NodeId nodeId;
	private final PublicId publicId;
	
	@Inject
	Node(NodeId nodeId, @Assisted String publicId){
		super(new GenericMutableData());
		this.nodeId = nodeId;
		this.publicId = new PublicId(publicId);
	}
	
	@Override
	public NepticalId<?> getId() {
		return nodeId;
	}
	
	@Override
	public PublicId getPublicId() {
		return publicId;
	}
	
	@Override
	public String toString(){
		return getId().toString();
	}

}
