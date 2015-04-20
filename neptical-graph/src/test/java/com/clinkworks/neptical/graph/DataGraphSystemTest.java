package com.clinkworks.neptical.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.api.NepticalContext;
import com.clinkworks.neptical.module.GraphModule;
import com.clinkworks.neptical.spi.GraphComponentFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

@RunWith(NepticalJUnit4Runner.class)
@NepticalContext(GraphModule.class)
public class DataGraphSystemTest {

	@Inject
	private DataGraph dataGraph;
	
	@Inject
	private Provider<DataGraph> dataGraphProvider;
	 
	@Inject
	private GraphComponentFactory gcf; 
	
	@Test
	public void dataGraphIsProperlyInjectableAndSingletonIsRespected(){
		assertNotNull(dataGraph);
		assertSame(dataGraph, dataGraphProvider.get());
	}
	
	@Test
	public void graphCanLinkNodesToAnEdge(){
		Node nodeA = gcf.createNode(gcf.createGenericDataContainer(new PublicId("Node-A")));
		Node nodeB = gcf.createNode(gcf.createGenericDataContainer(new PublicId("Node-B")));
		
		Edge edge = dataGraph.linkNodesByPublicId("NEW EDGE!", nodeA, nodeB);
		
		EdgeId edgeId = new EdgeId(nodeA, nodeB);
		
		assertNotNull(edge);
		assertSame(edge, dataGraph.getEdgeByPublicId("NEW EDGE!"));
		assertEquals(edge.getId(), edgeId);
		assertSame(edge, dataGraph.getEdgeById(new PublicId("NEW EDGE!")));
		assertSame(edge, dataGraph.getEdgeById(edgeId));
	}
	
}
