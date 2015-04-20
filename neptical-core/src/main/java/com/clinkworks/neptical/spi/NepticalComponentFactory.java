package com.clinkworks.neptical.spi;

import com.clinkworks.neptical.domain.GenericIdentifiableDataContainer;
import com.clinkworks.neptical.domain.PublicId;

public interface NepticalComponentFactory {
	public GenericIdentifiableDataContainer  createGenericDataContainer(PublicId publicId);
}
