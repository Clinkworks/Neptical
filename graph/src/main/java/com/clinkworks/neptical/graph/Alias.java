package com.clinkworks.neptical.graph;

import java.io.Serializable;

import com.clinkworks.neptical.datatype.NepticalId;

public class Alias extends NepticalId<Serializable>{

	Alias(NepticalId<?> alias) {
		super(alias.get());
	}
	
}
