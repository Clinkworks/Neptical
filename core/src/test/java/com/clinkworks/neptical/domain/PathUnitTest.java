package com.clinkworks.neptical.domain;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PathUnitTest {

	private Segment firstSegment;
	
	@Before
	public void init(){
		firstSegment = createSegment("path");
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
	public void pathCanBeAppended(){
		Path path = new Path(new ArrayList<Segment>());
		assertNotNull(path.appendSegment(firstSegment).getSegment(0));
	}
		
	private Segment createSegment(String qualifiedName){
		return new Segment(new PublicId(qualifiedName));
	}
}
