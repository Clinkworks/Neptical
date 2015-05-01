package com.clinkworks.neptical.domain;

import javax.inject.Inject;

import com.clinkworks.neptical.datatype.NepticalId;
import com.google.inject.assistedinject.Assisted;

public class PublicId extends NepticalId<String>{
	private static final long serialVersionUID = -7000248247572354247L;

	@Inject
	public PublicId(@Assisted String publicId){
		super(publicId);
	}
	
}
