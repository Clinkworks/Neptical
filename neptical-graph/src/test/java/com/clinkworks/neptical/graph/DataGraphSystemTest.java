package com.clinkworks.neptical.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.module.GraphModule;
import com.clinkworks.neptical.spi.GraphComponentFactory;
import com.clinkworks.neptical.util.GuiceInjectionUtil;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class DataGraphSystemTest {

	@Inject
	private DataGraph dataGraph;
	
	@Inject
	private Provider<DataGraph> dataGraphProvider;
	 
	@Inject
	private GraphComponentFactory gcf; 

	@Before
	public void setup(){
		Injector injector = GuiceInjectionUtil.createInjector(GraphModule.class);
		injector.injectMembers(this);
	}
	
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
