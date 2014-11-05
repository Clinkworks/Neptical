package com.clinkworks.neptical.util;

import java.util.List;

import com.clinkworks.neptical.graph.Edge;
import com.clinkworks.neptical.graph.Node;
import com.clinkworks.neptical.graph.Path;
import com.clinkworks.neptical.graph.Segment;
import com.google.inject.assistedinject.Assisted;

public interface DataComponentFactory extends NepticalComponentFactory{
	public Segment createSegment(String identifiedBy, @Assisted("start") Node start, @Assisted("end") Node end);
	public Path createPath(List<Edge> pathMakup);
	public Node createNode(String publicId);
}
