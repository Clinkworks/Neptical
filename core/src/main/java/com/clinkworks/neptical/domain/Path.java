package com.clinkworks.neptical.domain;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Path implements Iterable<Segment>{
	
	private final List<Segment> segments;
	
	public Path(List<Segment> segments){
		this.segments = segments;
	}
	
	public Path appendSegment(Segment segment){
		segments.add(segment);
		return this;
	}
	
	public int length(){
		return segments.size();
	}

	public Segment getSegment(int index) {
		return segments.get(index);
	}
	
	/**
	 * @return a copy of the segments in this path
	 */
	public Segment[] getSegments(){
		return segments.toArray(new Segment[0]);
	}
	
	@Override
	public Iterator<Segment> iterator() {
		return listIterator();
	}
	
	public ListIterator<Segment> listIterator(){
		return segments.listIterator();
	}
	
}
