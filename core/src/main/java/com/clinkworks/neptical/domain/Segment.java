package com.clinkworks.neptical.domain;

import java.io.Serializable;

import com.clinkworks.neptical.datatype.IdentifiableDataContainer;
import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.util.PathUtil;

public class Segment implements IdentifiableDataContainer{

	private volatile NepticalData nepticalData;
	private final String name;
	private final PublicId publicId;
	private final GenericId<Serializable> genericId;
	
	public Segment(PublicId publicId, GenericId<Serializable> genericId){
		this.name = PathUtil.lastSegment(publicId.get());
		this.publicId = publicId;
		this.genericId = genericId;
	}
	
	@Override
	public void setNepticalData(NepticalData nepticalData) {
		this.nepticalData = nepticalData;
	}

	@Override
	public NepticalData getNepticalData() {
		return nepticalData;
	}

	@Override
	public NepticalId<?> getId() {
		return genericId;
	}

	@Override
	public PublicId getPublicId() {
		return publicId;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString(){
		return getId().get().toString();
	}
}
