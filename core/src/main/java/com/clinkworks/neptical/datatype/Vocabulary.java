package com.clinkworks.neptical.datatype;

import java.io.Serializable;

import com.clinkworks.neptical.domain.Path;

public interface Vocabulary {
	public Path parse(Serializable notation);
}
