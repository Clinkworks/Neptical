package com.clinkworks.neptical.domain;

import java.io.Serializable;

import com.clinkworks.neptical.datatype.NepticalId;

public class GenericId<T extends Serializable> extends NepticalId<T>{

	public GenericId(T id) {
		super(id);
	}

}
