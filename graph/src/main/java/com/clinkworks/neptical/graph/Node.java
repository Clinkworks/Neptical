package com.clinkworks.neptical.graph;

import com.clinkworks.neptical.datatype.DataContainer;
import com.clinkworks.neptical.datatype.Identifiable;
import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.domain.UniqueId;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class Node implements Identifiable, DataContainer{

	private final UniqueId nodeId;
	private final PublicId publicId;
	private volatile NepticalData nepticalData;
	
	@Inject
	Node(UniqueId nodeId, @Assisted PublicId publicId){
		this.nodeId = nodeId;
		this.publicId = publicId;
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

	@Override
	public void setNepticalData(NepticalData nepticalData) {
		this.nepticalData = nepticalData;
	}

	@Override
	public NepticalData getNepticalData() {
		return nepticalData;
	}


}
