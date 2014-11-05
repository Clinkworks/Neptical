package com.clinkworks.neptical.util;

import com.clinkworks.neptical.graph.Edge;
import com.clinkworks.neptical.graph.Node;
import com.google.inject.assistedinject.Assisted;

public interface DataComponentFactory extends NepticalComponentFactory{
	public Edge createEdge(String identifiedBy, @Assisted("start") Node start, @Assisted("end") Node end);
}
