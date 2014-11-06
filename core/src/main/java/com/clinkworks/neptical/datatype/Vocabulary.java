package com.clinkworks.neptical.datatype;

import java.io.Serializable;

import com.clinkworks.neptical.domain.Segment;

public interface Vocabulary {
	public Segment[] parse(Serializable notation);
}
