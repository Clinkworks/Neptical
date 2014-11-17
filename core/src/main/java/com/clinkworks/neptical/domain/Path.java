package com.clinkworks.neptical.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 */
public class Path implements Iterable<Segment>{
	
	final List<Segment> segments;

    public Path(){
		segments = new ArrayList<Segment>();
	}
	
	//this constructor is only thread safe if you make it that way...
	// only recomended if you are making a thread safe path, iterators still respect your implementations iterator documentation
	public Path(List<Segment> segments){
		this.segments = segments;
	}

	public Path addSegment(Segment segment){
		segments.add(segment);
		return this;
	}
	
	public int length(){
		return segments.size();
	}

	public Segment start(){
		return segments.get(0);
	}
	
	public Segment end(){
		return length() > 0 ? segments.get(length() - 1) : null;
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
	
	public ListIterator<Segment> listIterator(int placement) {
		return segments.listIterator(placement);
	}
	
	public PathIterator pathIterator(int placement){
		return createPathIterator(placement);
	}
	
	@Override
	public Iterator<Segment> iterator() {
		return listIterator();
	}
	
	public ListIterator<Segment> listIteratorAtEnd(){
		return length() > 0 ? listIterator(length()) : listIterator();
	}
	
	public PathIterator pathIteratorAtEnd(){
		return length() > 0 ? createPathIterator((length())) : createPathIterator();
	}
	
	public ListIterator<Segment> listIterator(){
		return segments.listIterator();
	}
	
	public PathIterator pathIterator(){
		return createPathIterator();
	}

	public Path appendSegment(Segment segment){
		segments.add(segment);
		return this;
	}
	
	public Path appendSegments(Collection<Segment> segments){
		segments.addAll(segments);
		return this;
	}
	
	public Path appendSegments(Path path) {
		segments.addAll(path.segments);
		return this;
	}
	
	@Override
	public String toString(){
		return length() > 0 ? end().getId().toString() : "[empty]";
	}

	
	private PathIterator createPathIterator(int placement){
		return new PathIterator(this, listIterator(placement));
	}

	private PathIterator createPathIterator(){
		return new PathIterator(this, listIterator());
	}

}
