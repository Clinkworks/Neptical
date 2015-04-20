package com.clinkworks.neptical.graph;

import java.util.List;

import com.clinkworks.neptical.datatype.IdentifiableDataContainer;
import com.clinkworks.neptical.domain.GenericIdentifiableDataContainer;
import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.spi.GraphComponentFactory;

public class GraphTestDataComponentFactoryStub implements GraphComponentFactory{

	@Override
	public Link createEdge(Node start, Node end) {
		return new Link(start, end);
	}

	@Override
	public Route createPath(List<Edge> pathMakeup) {
		return new Route(pathMakeup);
	}

	@Override
	public Node createNode(IdentifiableDataContainer identifiableDataContainer) {
		return new Node(identifiableDataContainer);
	}

	@Override
	public GenericIdentifiableDataContainer createGenericDataContainer(PublicId publicId) {
		return new GenericIdentifiableDataContainer(publicId);
	}
	
}