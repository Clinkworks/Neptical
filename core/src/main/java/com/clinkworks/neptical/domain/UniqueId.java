package com.clinkworks.neptical.domain;

import java.io.Serializable;
import java.util.UUID;

/**
 * This id is garenteed to be unique and is directly injectable into your objects
 * 
 *   --- use the ID service for generating and configuring neptical aware ids
 *
 */
public class UniqueId extends GenericId<Serializable>{

	public UniqueId() {
		super(UUID.randomUUID());
	}

}
