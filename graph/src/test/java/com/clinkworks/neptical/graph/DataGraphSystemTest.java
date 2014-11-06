package com.clinkworks.neptical.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner.NepticalConfiguration;
import com.clinkworks.neptical.module.GraphModule;
import com.clinkworks.neptical.service.GraphComponentService;
import com.google.inject.Inject;
import com.google.inject.Provider;

@RunWith(NepticalJUnit4Runner.class)
@NepticalConfiguration(GraphModule.class)
public class DataGraphSystemTest {

	@Inject
	private DataGraph dataGraph;
	
	@Inject
	private Provider<DataGraph> dataGraphProvider;
	
	@Inject
	private GraphComponentService graphComponentService; 
	
	@Test
	public void dataGraphIsProperlyInjectableAndSingletonIsRespected(){
		assertNotNull(dataGraph);
		assertSame(dataGraph, dataGraphProvider.get());
	}
	
	@Test
	public void graphCanLinkNodesToAnEdge(){
		Edge edge = dataGraph.linkNodesByPublicId("NEW EDGE!", graphComponentService.createNode("node1"), graphComponentService.createNode("node2"));
		assertNotNull(edge);
		assertEquals("NEW EDGE!", edge.getPublicId().toString());
		assertSame(edge, dataGraph.getEdgeByPublicId("NEW EDGE!"));
	}
	
}
