package com.clinkworks.neptical.api;

import org.apache.commons.lang3.StringUtils;

public class PublicId {
	
	private final String publicId;

	public PublicId(String publicId){
		this.publicId = publicId;
	}

	public final String get(){
		return publicId;
	}
	
	@Override
	public String toString(){
		if(StringUtils.isBlank(publicId)){
			return super.toString();
		}
		return publicId;
	}
}
