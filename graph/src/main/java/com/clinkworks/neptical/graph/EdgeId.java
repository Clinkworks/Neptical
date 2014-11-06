package com.clinkworks.neptical.graph;

import com.clinkworks.neptical.datatype.NepticalId;

public class EdgeId extends NepticalId<String>{

	EdgeId(Node start, Node end){
		super(start.getId().toString() + end.getId().toString());
	}
	
}
