package com.clinkworks.neptical.domain;

import java.io.Serializable;

import com.clinkworks.neptical.datatype.NepticalId;

public class GenericId<T extends Serializable> extends NepticalId<T>{

	private static final long serialVersionUID = 1L;

	public GenericId(T id) {
		super(id);
	}

}
