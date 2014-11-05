package com.clinkworks.neptical.graph;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.graph.DataGraph;
import com.clinkworks.neptical.graph.Edge;
import com.clinkworks.neptical.graph.Node;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner.NepticalConfiguration;
import com.clinkworks.neptical.modules.NepticalDataModule;
import com.google.inject.Inject;
import com.google.inject.Provider;

@RunWith(NepticalJUnit4Runner.class)
@NepticalConfiguration(NepticalDataModule.class)
public class DataGraphSystemTest {

	@Inject
	private DataGraph dataGraph;
	
	@Inject
	private Provider<DataGraph> dataGraphProvider;
	
	@Test
	public void dataGraphIsProperlyInjectableAndSingletonIsRespected(){
		assertNotNull(dataGraph);
		assertSame(dataGraph, dataGraphProvider.get());
	}
	
	@Test
	public void graphCanLinkNodesToAnEdge(){
		Edge edge = dataGraph.linkNodesBy("NEW EDGE!", new Node(), new Node());
		assertNotNull(edge);
		assertEquals("NEW EDGE!", edge.getIdentifier());
		assertSame(edge, dataGraph.getEdgeByFullyQualifiedId("NEW EDGE!"));
	}
	
}
