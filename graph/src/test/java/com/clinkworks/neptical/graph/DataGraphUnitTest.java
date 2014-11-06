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
import java.util.Map;
import java.util.Set;

import mockit.Expectations;
import mockit.Injectable;

import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.domain.UniqueId;
import com.clinkworks.neptical.service.GraphComponentService;
import com.clinkworks.neptical.spi.GraphComponentFactory;

public class DataGraphUnitTest {
	
	private DataGraph dataGraph;
	
	private Map<NepticalId<?>, Edge> edgeLookup;
	
	private Node directory;
	private Node textFile;
	private Node textInFile;
	private Node jsonDataInTextFile;
	
	@Before
	public void init(){
		edgeLookup = new HashMap<NepticalId<?>, Edge>();
		dataGraph = new DataGraph(edgeLookup, new GraphComponentService(new GraphTestDataComponentFactoryStub()));
		buildNodesForTestData();
	}

	@Test
	public void dataGraphProperlyCallsTheGuiceFactoryImplementationWhenLinkingNodesByPublicId(@Injectable final GraphComponentService graphComponentServiceMocked){
		
		final Node start = directory;
		final Node end = textFile;
		final String publicIdValue = "NODE1";
		final PublicId publicId = new PublicId(publicIdValue);
				
		new Expectations(){{			
			graphComponentServiceMocked.createEdge(withSameInstance(publicIdValue), withSameInstance(start), withSameInstance(end));
			times = 1;
			result = new Segment(publicId, start, end);
		}};
		
		new DataGraph(new HashMap<NepticalId<?>, Edge>(), graphComponentServiceMocked).linkNodesByPublicId(publicIdValue, start, end);
	}
	
	@Test
	public void dataGraphStoresCreatedEdges(){
		assertNull(dataGraph.getEdgeByPublicId("EDGE_ID"));
		dataGraph.linkNodesByPublicId("EDGE_ID", directory, jsonDataInTextFile);
		assertNotNull(dataGraph.getEdgeByPublicId("EDGE_ID"));
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
		assertEquals(2, edges.size());
		assertTrue(actualNodes.contains(directory));
		assertEquals(1, actualNodes.size());
		assertEquals(jsonDataInTextFile, dataGraph.findEdgesStartingAt(textInFile).get(0).getEnd());
		assertEquals(0, dataGraph.findEdgesStartingAt(jsonDataInTextFile).size());
	}
	


	@Test
	public void theGraphIsCapableOfReturningAPathBetweenIndirectlyConnectedNodes(){

		linkTestNodes();
		
		Path path = dataGraph.getPathBetween(directory, jsonDataInTextFile);
		
		assertEquals(3, path.getLength());
		assertFalse(path.hasPrevious());
		assertEquals(textInFile, path.next());		
		assertSame(jsonDataInTextFile, path.next());
		assertFalse(path.hasNext());
		//found paths should found only once!
		assertSame(path, dataGraph.getPathBetween(directory, jsonDataInTextFile));
		
	}
	
	private void linkTestNodes() {
		dataGraph.linkNodesByPublicId("DIRECTORY_TO_FILE", directory	, textFile);
		dataGraph.linkNodesByPublicId("DIRECTORY_TO_DATA", directory, textInFile);
		dataGraph.linkNodesByPublicId("DATA-TO-JSON", textInFile, jsonDataInTextFile);
	}

	private void buildNodesForTestData() {
		GraphTestDataComponentFactoryStub gcf = new GraphTestDataComponentFactoryStub();
		directory = gcf.createNode(new PublicId("DIRECTORY"));
		textFile = gcf.createNode(new PublicId("TEXT_FILE"));
		textInFile = gcf.createNode(new PublicId("TEXT_IN_FILE"));
		jsonDataInTextFile = gcf.createNode(new PublicId("TEXT_IN_FILE"));
	}
	
	public static class GraphTestDataComponentFactoryStub implements GraphComponentFactory{

		@Override
		public Segment createEdge(PublicId publicId, Node start, Node end) {
			return new Segment(publicId, start, end);
		}

		@Override
		public Path createPath(PublicId publicId, List<Edge> pathMakeup) {
			return new Path(publicId, pathMakeup);
		}

		@Override
		public Node createNode(PublicId publicId) {
			return new Node(new UniqueId(), publicId);
		}

		@Override
		public PublicId createPublicId(String publicId) {
			return new PublicId(publicId);
		}
		
	}
}
