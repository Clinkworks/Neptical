package com.clinkworks.neptical.domain;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

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
		return new PathIterator();
	}
	
	public ListIterator<Segment> iteratorStartingAt(int index){
		return new PathIterator(index);
	}
	
	public ListIterator<Segment> iteratorStartingAt(Segment segment){
		return new PathIterator(segments.indexOf(segment));
	}
	
	private class PathIterator implements ListIterator<Segment>{

		private final AtomicInteger currentIndex;
		
		public PathIterator(int startingIndex){
			currentIndex = new AtomicInteger(startingIndex - 1);
		}
		
		public PathIterator(){
			currentIndex = new AtomicInteger(-1);
		}
		
		@Override
		public boolean hasNext() {
			return nextIndex() < length();
		}

		@Override
		public Segment next() {
			Segment next = getSegment(currentIndex.incrementAndGet());
			if(!hasNext()){
				currentIndex.set(nextIndex());
			}
			return next;
		}

		@Override
		public boolean hasPrevious() {
			return previousIndex() > -1;
		}

		@Override
		public Segment previous() {
			Segment segment = getSegment(currentIndex.decrementAndGet());
			if(!hasPrevious()){
				currentIndex.set(previousIndex());
			}
			return segment;
		}

		@Override
		public int nextIndex() {
			int nextIndex = currentIndex.get() + 1;
			int length = length();
			return nextIndex >= length ? length : nextIndex;
		}

		@Override
		public int previousIndex() {
			int previousIndex = currentIndex.get() -1;
			return previousIndex < 0 ? -1 : previousIndex;
		}

		@Override
		public void remove() {
			segments.remove(currentIndex);
		}

		@Override
		public void set(Segment segment) {
			segments.set(currentIndex.get(), segment);
		}

		@Override
		public void add(Segment segment) {
			int newIndex = hasPrevious() ? currentIndex.getAndIncrement() : currentIndex.incrementAndGet();
			segments.add(newIndex, segment);
		}
				
	}
	
}
