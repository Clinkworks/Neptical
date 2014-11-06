package com.clinkworks.neptical.spi;

import com.clinkworks.neptical.domain.PublicId;

public interface NepticalComponentFactory {
	public PublicId createPublicId(String publicId);
}
