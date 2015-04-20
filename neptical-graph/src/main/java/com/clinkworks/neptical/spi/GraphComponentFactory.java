package com.clinkworks.neptical.spi;

import java.util.List;

import com.clinkworks.neptical.datatype.IdentifiableDataContainer;
import com.clinkworks.neptical.graph.Edge;
import com.clinkworks.neptical.graph.Link;
import com.clinkworks.neptical.graph.Node;
import com.clinkworks.neptical.graph.Route;
import com.google.inject.assistedinject.Assisted;

public interface GraphComponentFactory extends NepticalComponentFactory {

	public Link createEdge(@Assisted("start") Node start, @Assisted("end") Node end);
	public Route createPath(List<Edge> pathMakup);
	public Node createNode(IdentifiableDataContainer identifiableDataContainer);
	
}
