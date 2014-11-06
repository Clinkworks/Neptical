package com.clinkworks.neptical.datatype;

import com.clinkworks.neptical.domain.PublicId;


public interface Identifiable {
	public  NepticalId<?> getId();
	public PublicId getPublicId();
}
