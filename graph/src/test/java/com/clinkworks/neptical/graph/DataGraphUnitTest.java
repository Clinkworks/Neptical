package com.clinkworks.neptical.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import mockit.Expectations;
import mockit.Injectable;

import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.domain.GenericImmutableData;
import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.spi.GraphComponentFactory;

public class DataGraphUnitTest {
	
	private DataGraph dataGraph;
	
	private Map<NepticalId<?>, Edge> edgeLookup;
	private Map<NepticalId<?>, Node> nodeLookup;
	
	private Node directory;
	private Node textFile;
	private Node textInFile;
	private Node jsonDataInTextFile;
	
	@Before
	public void init(){
		edgeLookup = new HashMap<NepticalId<?>, Edge>();
		nodeLookup = new HashMap<NepticalId<?>, Node>();
		dataGraph = new DataGraph(nodeLookup, edgeLookup, new GraphTestDataComponentFactoryStub());
		buildNodesForTestData();
	}

	@Test
	public void dataGraphProperlyCallsTheGuiceFactoryImplementationWhenLinkingNodesByPublicId(@Injectable final GraphComponentFactory gcfMocked){
		
		final Node start = directory;
		final Node end = textFile;
		final String publicIdValue = "NODE1";
				
		new Expectations(){{			
			gcfMocked.createEdge(withSameInstance(start), withSameInstance(end));
			times = 1;
			result = new Link(start, end);
		}};
		
		new DataGraph(new HashMap<NepticalId<?>, Node>(), new HashMap<NepticalId<?>, Edge>(), gcfMocked).linkNodesByPublicId(publicIdValue, start, end);
	}
	
	@Test
	public void dataGraphStoresCreatedEdges(){
		assertNull(dataGraph.getEdgeByPublicId("EDGE_ID"));
		dataGraph.linkNodesByPublicId("EDGE_ID", directory, jsonDataInTextFile);
		assertNotNull(dataGraph.getEdgeByPublicId("EDGE_ID"));
	}

	@Test
	public void dataGraphStoresFreelyCreatedNodes(){
		dataGraph.createNode("NEW_NODE-FOR CREATE NODE TEST");
		assertEquals("NEW_NODE-FOR CREATE NODE TEST", dataGraph.getNodeByPublicId("NEW_NODE-FOR CREATE NODE TEST").getId().get());
		NepticalData nepticalData = new GenericImmutableData("FREE DATA!", new Object());
		dataGraph.createNode(nepticalData);
		assertSame(nepticalData, dataGraph.getNodeByPublicId("FREE DATA!").getNepticalData());
	}
	
	@Test
	public void dataGraphCanFindEdgesStartingWithAGivenNode(){
		
		linkTestNodes();
		
		List<Edge> edges = dataGraph.findEdgesStartingAt(directory);
		Set<Node> actualNodes = new HashSet<Node>();
		
		for(Edge edge : edges){
			actualNodes.add(edge.getStart());
		}
		
		//one edge linked together by an internal link, another by the public id
		assertEquals(4, edges.size());
		assertTrue(actualNodes.contains(directory));
		assertEquals(1, actualNodes.size());
		assertEquals(jsonDataInTextFile, dataGraph.findEdgesStartingAt(textInFile).get(0).getEnd());
		assertEquals(0, dataGraph.findEdgesStartingAt(jsonDataInTextFile).size());
	}
	

	@Test
	public void theGraphCanBuildPathIterators(){
		
	}
	
	@Test
	public void theGraphIsCapableOfReturningAPathBetweenIndirectlyConnectedNodes(){
		linkTestNodes();
		Route route = dataGraph.getRouteBetween(directory, jsonDataInTextFile);
		ListIterator<Node> path = dataGraph.getRouteBetween(directory, jsonDataInTextFile).listIterator();
		
		assertEquals(3, route.getLength());
		assertFalse(path.hasPrevious());
		assertEquals(directory, path.next());		
		assertSame(textInFile, path.next());
		assertSame(jsonDataInTextFile, path.next());
		assertFalse(path.hasNext());
		//found paths should stay found! - index discovered paths
		assertSame(route, dataGraph.getRouteBetween(directory, jsonDataInTextFile));
		assertSame(route, dataGraph.getEdgeById(new EdgeId(route.getStart(), route.getEnd())));
		
	}
	
	private void linkTestNodes() {
		dataGraph.linkNodesByPublicId("DIRECTORY_TO_FILE", directory	, textFile);
		dataGraph.linkNodesByPublicId("DIRECTORY_TO_DATA", directory, textInFile);
		dataGraph.linkNodesByPublicId("DATA-TO-JSON", textInFile, jsonDataInTextFile);
	}

	private void buildNodesForTestData() {
		GraphTestDataComponentFactoryStub gcf = new GraphTestDataComponentFactoryStub();
		directory = gcf.createNode(gcf.createGenericDataContainer(new PublicId("DIRECTORY")));
		textFile = gcf.createNode(gcf.createGenericDataContainer(new PublicId("TEXT_FILE")));
		textInFile = gcf.createNode(gcf.createGenericDataContainer(new PublicId("TEXT_IN_FILE")));
		jsonDataInTextFile = gcf.createNode(gcf.createGenericDataContainer(new PublicId("JSON_DATA ")));
	}
	

}
