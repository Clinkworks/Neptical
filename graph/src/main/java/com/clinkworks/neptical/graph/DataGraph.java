package com.clinkworks.neptical.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.domain.Path;
import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.domain.Segment;
import com.clinkworks.neptical.spi.GraphComponentFactory;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DataGraph {
	
	private final Map<NepticalId<?>, Edge> edgeLookup;
	
 	private final Map<NepticalId<?>, Node> nodeLookup;
 	
	private final GraphComponentFactory gcf;
	
	@Inject
	DataGraph(Map<NepticalId<?>, Node> nodeLookup, Map<NepticalId<?>, Edge> edgeLookup, GraphComponentFactory gcf){
		this.nodeLookup = nodeLookup;
		this.edgeLookup = edgeLookup;
		this.gcf = gcf;
	}

	public Edge findEdgeClosestToEndOfPath(ListIterator<Segment> iterAtEnd) {
		
		if(!iterAtEnd.hasPrevious()){
			return null;
		}
		
		Segment prevousSegment = iterAtEnd.previous();
		
		Edge edge = getEdgeById(prevousSegment.getId());
		
		if(edge == null){
			return findEdgeClosestToEndOfPath(iterAtEnd);
		}
		
		return edge;
		
	}

	
	public Edge graphPath(Path path){
			
		ListIterator<Segment> segmentIter = path.listIterator();
		
		Edge possiblyCreatedPath = getEdgeById(path.end().getId());
		
		if(possiblyCreatedPath != null){
			return possiblyCreatedPath;
		}
	
		Edge startingEdge = findEdgeClosestToEndOfPath(segmentIter);
		Node startingNode = startingEdge == null ? createNode(segmentIter.next()) : startingEdge.getEnd();
		
		List<Edge> createdEdges = new ArrayList<Edge>();
		
		Node lastNode = graphPath(startingNode, segmentIter, createdEdges);
		
		// NOTE: the iter is not incremented to make sure theres an edge for each vertex to link to.  
		
		// starting segments:
		// A  -> A
		// A  -> B
		// B --> C
		// A --> C
		// C --> D
		// C --> E
		
		//add F to B
		// B -> B //the next for a hit on b will now allow finders or b to find f and c.
		// B - > F  //by using the segments as verticies instead of nodes allows more flexability on how the nodes
		// named vs how they are identified by both the graph and the outside world
		
		// resulting paths
		// A.B.C.D
		// A.B.C.D.E
		// resulting edges
		
		// add node F attached to B
		// A - > A
		// A -> B
		// B -> B
		// B -> F
		// B -> C
		
		//theres probably a better way to do this, but I figure since I store off a contexual index
		// I can get away with a slow approach like this
		
		return getEdgeById(lastNode.getId());
	}

	public void dumpGraph(){
		System.out.println("Node count: " + nodeLookup.values().size());
		
		System.out.println("nodes");
		
		for(Node node : nodeLookup.values()){
			System.out.println("Node: ");
			System.out.println("   Id Type: " + node.getId().getClass());
			System.out.println("   Id: " + node.getId());
			System.out.println("   Name: " + node.getNepticalData().getName());
			System.out.println("   Neptical Type: " + node.getNepticalData().getNepticalDataType());
			System.out.println("    JavaType: " + node.getNepticalData().get().getClass());
		}
		
		
		System.out.println("\n\nEdge count: " + edgeLookup.entrySet().size());
		
		for(Map.Entry<NepticalId<?>, Edge> entry : edgeLookup.entrySet()){
			System.out.println("Edge: ");
			System.out.println("    Link: " + entry.getKey());
			System.out.println("    Link Type: " + entry.getKey().getClass());
			System.out.println("    Name:  " + entry.getValue().getEnd().getId());
			System.out.println("    Id: " + entry.getValue().getId());
			System.out.println("   Id Type: " + entry.getValue().getId().getClass());
		}
	}
	
	private Node graphPath(Node lastNode, ListIterator<Segment> segmentIter, List<Edge> createdEdges){
		
		Segment segment = segmentIter.next();
		Node nextNode = createNode(segment);
				
		Edge edge = linkNodesById(lastNode.getId(), lastNode, nextNode);
		
		if(lastNode != nextNode){ //this may be a vertex, if so we don't want to add it to the path
			createdEdges.add(edge);
		}
		
		if(!segmentIter.hasNext()){
			//create the route between nodes
			Route route = createRoute(createdEdges);
			edgeLookup.put(route.getId(), route);
		
			return nextNode;
		}
		
		return graphPath(nextNode, segmentIter, createdEdges);
	}
	
	public Node createNode(Segment segment) {
		Node node = gcf.createNode(segment);
		nodeLookup.put(node.getId(), node);
		return node;
	}

	public Edge linkNodesById(NepticalId<?> nepticalId, Node start, Node end){
		
		Edge edge = getEdgeById(nepticalId);
		
		if(edge == null){
			edge = createEdge(start, end);
			edgeLookup.put(nepticalId, edge);
		}
		
		return edge;
		
	}
	
	

	public Edge linkNodesByPublicId(String publicId, Node start, Node end){
				
		Edge edge = getEdgeByPublicId(publicId);
		
		if(edge == null){
			edge = createEdge(start, end);
			edgeLookup.put(edge.getId(), edge);
			edgeLookup.put(createPublicId(publicId), edge);
		}
		
		return edge;
	}

	public List<Edge> findEdgesStartingAt(Node start){
		
		List<Edge> foundEdges = new ArrayList<Edge>();
		
		if(start == null){
			return foundEdges;
		}
		
		Collection<Edge> edges = edgeLookup.values();
		
		for(Edge edge : edges){
			boolean edgeContainsStartNode = edge.getStart().equals(start);
			if(edgeContainsStartNode){
				foundEdges.add(edge);
			}
		}
		
		return foundEdges;
	}
	
	public boolean containsEdge(Node start, Node end){
		return getEdgeById(createId(start, end)) != null;
	}
	
	public Edge getEdgeById(NepticalId<?> nepticalId){

			//look in aliases first
			Edge edge = getEdgeByAlias(nepticalId);
			
			//then look for the edge
			if(edge == null){
				edge = edgeLookup.get(nepticalId);
			}
			
			return edge;
	}

	public Edge getEdgeByPublicId(String publicId) {
		return getEdgeById(createPublicId(publicId));
	}
	
	public Edge getEdgeByPublicId(PublicId publicId) {
		return getEdgeById(publicId);
	}
		
	public Route getRouteBetween(Node start, Node end) {
		
		Edge possibleRoute = edgeLookup.get(createId(start, end));
		
		if(possibleRoute != null){
			if(possibleRoute instanceof Route){
				return (Route)possibleRoute;
			}
			return createRoute(Lists.newArrayList(possibleRoute));
		}
		
		List<Edge> edgesAssociatedWithStartNode = findEdgesStartingAt(start);
		
		Route path = null;
		
		for(Edge edge : edgesAssociatedWithStartNode){
			List<Edge> pathMakeUp = new ArrayList<Edge>();
			 
			if(edge.getStart() == start && edge.getEnd() == end){
				
				pathMakeUp.add(edge);
				return createRoute(pathMakeUp);
				
			}
			
			path = discoverLinkedEdges(end, edge, pathMakeUp);
			
			if(path != null){
				return path;
			}
			
		}
		
		return path;
	}

	private EdgeId createId(Node start, Node end) {
		return new EdgeId(start, end);
	}

	private Route discoverLinkedEdges(Node end, Edge currentEdge,  List<Edge> pathMakeUp) {
		
		//if the search cannot find the end, the path is unresovable.
		if(currentEdge == null){
			return null;
		}
		
		pathMakeUp.add(currentEdge);
		
		Node start = currentEdge.getEnd();
		
		List<Edge> edgesStartingWithLastLink = findEdgesStartingAt(start);
		
		Route path = null;
		
		for(Edge edge : edgesStartingWithLastLink){

			pathMakeUp.add(edge);
			
			if(edge.getEnd() == end){
				path = createRoute(pathMakeUp);
			}
			
			if(path != null){
				edgeLookup.put(path.getId(), path);
				return path;
			}
			
			return discoverLinkedEdges(end, edge, pathMakeUp);
		}
		
		return null;
	}

	private Route createPath(List<Edge> pathMakup){
		return gcf.createPath(pathMakup);
	}

	public PublicId createPublicId(String publicId) {
		return new PublicId(publicId);
	}
	
	public Edge createEdge(Node start, Node end) {
		return gcf.createEdge(start, end);
	} 
	
	public Node createNode(String nodeName){
		Node node = gcf.createNode(gcf.createGenericDataContainer(createPublicId(nodeName)));
		nodeLookup.put(node.getId(), node);
		return node;
	}
	
	public Node createNode(NepticalData nepticalData){
		Node node = createNode(nepticalData.getName());
		node.setNepticalData(nepticalData);
		return node;
	}

	private Route createRoute(List<Edge> pathMakeUp) {
		return createPath(pathMakeUp);
	}

	public Node getNodeByPublicId(String publicId) {
		return nodeLookup.get(createPublicId(publicId));
	}
	
	public Node getNodeById(NepticalId<?> nepticalId){
		return nodeLookup.get(nepticalId);
	}
	
	public Node getNodeByPublicId(PublicId publicId) {
		return nodeLookup.get(publicId);
	}

	public void alias(Edge edge, String name) {
		Alias edgeAlias = new Alias(createPublicId(name));
		edgeLookup.put(edgeAlias, edge);
	}

	private Edge getEdgeByAlias(NepticalId<?> nepticalId){
		return edgeLookup.get(new Alias(nepticalId));
	}
	
	public void alias(Edge edge, NepticalId<?> aliasedId) {
		Alias edgeAlias = new Alias(aliasedId);
		edgeLookup.put(edgeAlias, edge);
	}
}
