package com.clinkworks.neptical.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mockit.Expectations;
import mockit.Injectable;

import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.api.NepticalId;
import com.clinkworks.neptical.util.DataComponentFactory;

public class DataGraphUnitTest {
	
	private DataGraph dataGraph;
	
	private Map<NepticalId<?>, Edge> edgeLookup;
	
	private Node directory;
	private Node textFile;
	private Node textInFile;
	private Node jsonDataInTextFile;
	
	@Before
	public void init(){
		dataGraph = new DataGraph(edgeLookup, new GraphTestDataComponentFactoryStub());
		buildNodesForTestData();
	}

	@Test
	public void dataGraphProperlyCallsTheGuiceFactoryImplementation(@Injectable final DataComponentFactory dataComponentFactoryMocked){
		
		final Node start = directory;
		final Node end = textFile;
		final String id = "EDGE1";
				
		new Expectations(){{
			dataComponentFactoryMocked.createEdge(withSameInstance(id), withSameInstance(start), withSameInstance(end));
			times = 1;
			result = new Edge(null, null, null);
		}};
		
		new DataGraph(new HashMap<NepticalId<?>, Edge>(), dataComponentFactoryMocked).linkNodesBy(id, start, end);
	}
	
	@Test
	public void dataGraphStoresCreatedEdges(){
		assertNull(dataGraph.getEdgeByFullyQualifiedId("EDGE_ID"));
		dataGraph.linkNodesBy("EDGE_ID", new Node(), new Node());
		assertNotNull(dataGraph.getEdgeByFullyQualifiedId("EDGE_ID"));
	}
	
	@Test
	public void dataGraphCanFindEdgesStartingWithAGivenNode(){
		
		linkTestNodes();
		
		List<Edge> edges = dataGraph.findEdgesStartingAt(directory);
		Set<Node> actualNodes = new HashSet<Node>();
		
		for(Edge edge : edges){
			actualNodes.add(edge.getStart());
		}
		
		assertEquals(2, edges.size());
		assertTrue(actualNodes.contains(directory));
		assertEquals(1, actualNodes.size());
		assertEquals(jsonDataInTextFile, dataGraph.findEdgesStartingAt(textInFile).get(0).getEnd());
		assertEquals(0, dataGraph.findEdgesStartingAt(jsonDataInTextFile).size());
	}
	


	@Test
	public void theGraphIsCapableOfReturningAPathBetweenIndirectlyConnectedNodes(){
		linkTestNodes();
		
		
		
	}
	
	private void linkTestNodes() {
		dataGraph.linkNodesBy("DIRECTORY_TO_FILE", directory	, textFile);
		dataGraph.linkNodesBy("DIRECTORY_TO_DATA", directory, textInFile);
		dataGraph.linkNodesBy("DATA-TO-JSON", textInFile, jsonDataInTextFile);
	}

	private void buildNodesForTestData() {
		directory = new Node();
		textFile = new Node();
		textInFile= new Node();
		jsonDataInTextFile = new Node();
	}
	
	public static class GraphTestDataComponentFactoryStub implements DataComponentFactory{

		@Override
		public Edge createEdge(String identifiedBy, Node start, Node end) {
			return new Edge(identifiedBy, start, end);
		}

	}
}
