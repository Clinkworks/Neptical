package com.clinkworks.neptical.graph;

import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.domain.PublicId;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class Segment implements Edge{
	
	private final EdgeId edgeId;
	private final PublicId publicId;
	private final Node start;
	private final Node end;
	
	@Inject
	Segment(@Assisted PublicId publicId, @Assisted("start") Node start, @Assisted("end") Node end){
		this.publicId = publicId;
		this.start = start;
		this.end = end;
		edgeId = new EdgeId(start, end);
	}
	
	public PublicId getPublicId() {
		return publicId;
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

	@Override
	public String toString(){
		return publicId.get();
	}
}
