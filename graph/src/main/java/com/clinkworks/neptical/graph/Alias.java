package com.clinkworks.neptical.graph;

import java.io.Serializable;

import com.clinkworks.neptical.datatype.NepticalId;

public class Alias extends NepticalId<Serializable>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6883838527986612083L;

	Alias(NepticalId<?> alias) {
		super(alias.get());
	}
	
}
