package com.clinkworks.neptical.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;

public class PathUnitTest {

	private Segment firstSegment;
	private Segment secondSegment;
	private Segment thirdSegment;
	private Segment fourthSegment;
	
	@Before
	public void init(){
		firstSegment = createSegment("path");
		secondSegment = createSegment("to");
		thirdSegment = createSegment("my");
		fourthSegment = createSegment("data");
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void ensurePathRespectsNegativeBounds(){
		Path path = new Path(new ArrayList<Segment>());
		path.getSegment(-1);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void ensurePathRespectsPostitiveBounds(){
		Path path = new Path(new ArrayList<Segment>());
		path.getSegment(1);
	}
	
	@Test
	public void pathIsProperlyIterable(){

		Path path = createDefaultPath();
		Segment[] segments = path.getSegments();
		
		ListIterator<Segment> iter = path.listIterator();
		
		//test start state of the created iterator
		assertEquals(-1, iter.previousIndex());
		assertFalse(iter.hasPrevious());
		assertTrue(iter.hasNext());
		
		//ensure iter walks all the way through
		assertEquals(segments[0], iter.next());
		assertEquals(segments[1], iter.next());
		assertEquals(segments[2], iter.next());
		assertEquals(segments[3], iter.next());
		
		//test end state
		assertTrue(iter.hasPrevious());
		assertFalse(iter.hasNext());
		assertEquals(4, iter.nextIndex());

		//insure iter can walk back
		assertEquals(segments[3], iter.previous());
		assertEquals(segments[2], iter.previous());
		assertEquals(segments[1], iter.previous());
		assertEquals(segments[0], iter.previous());

		//test end state
		assertFalse(iter.hasPrevious());
		assertTrue(iter.hasNext());
		assertEquals(-1, iter.previousIndex());
	}
	
	@Test
	public void pathIteratorRespondsToModificationsToThePath(){
		Path path = createDefaultPath();
		ListIterator<Segment> listIterator = path.listIterator();
		
		Segment newStartingSegment = createSegment("new starting segment");
		
		listIterator.add(newStartingSegment);
		assertEquals(newStartingSegment, path.getSegment(0));
		assertEquals(firstSegment, listIterator.next());
		listIterator.add(createSegment("new second segment"));
		assertEquals("new second segment", listIterator.previous().getId().get());
		
		while(listIterator.hasNext()){
			listIterator.next(); //get to the end
		}
		
		assertFalse(listIterator.hasNext());
		path.appendSegment(createSegment("Ending segment"));
		assertEquals("Ending segment", listIterator.next().getId().get());
		
	}
	
	@Test
	public void pathCanBeAppended(){
		Path path = new Path(new ArrayList<Segment>());
		assertNotNull(path.appendSegment(firstSegment).getSegment(0));
	}
	
	private Path createDefaultPath(){
		List<Segment> segments = new ArrayList<Segment>();

		segments.add(firstSegment);
		segments.add(secondSegment);
		segments.add(thirdSegment);
		segments.add(fourthSegment);
		
		Path path = new Path(segments);

		return path;
	}
	
	private Segment createSegment(String qualifiedName){
		PublicId publicId = new PublicId(qualifiedName);
		GenericId<Serializable> genericId = new GenericId<Serializable>(qualifiedName);
		return new Segment(publicId, genericId);
	}
}
