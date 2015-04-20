package com.clinkworks.neptical.graph;

import com.clinkworks.neptical.datatype.NepticalId;

public class EdgeId extends NepticalId<String>{

	private static final long serialVersionUID = -9002144972455654371L;

	EdgeId(Node start, Node end){
		super(start.getId().toString() + end.getId().toString());
	}
	
}
