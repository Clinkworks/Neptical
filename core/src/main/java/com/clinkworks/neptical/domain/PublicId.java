package com.clinkworks.neptical.domain;

import com.clinkworks.neptical.datatype.NepticalId;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PublicId extends NepticalId<String>{
	
	@Inject
	public PublicId(@Assisted String publicId){
		super(publicId);
	}
	
}
