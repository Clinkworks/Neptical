package com.clinkworks.neptical.domain;

import java.util.ListIterator;

public class PathIterator implements ListIterator<Segment>{

    private final Object mutex;	
	private final ListIterator<Segment> backingIterator;
	private volatile Segment current;
	
	PathIterator(Object mutex, ListIterator<Segment> backingIterator) {
		this.backingIterator = backingIterator;
		this.mutex = mutex;
	}
	
	public Segment current(){
		return current;
	}
	
	@Override
	public boolean hasNext() {
		return backingIterator.hasNext();
	}

	@Override
	public Segment next() {
		synchronized (mutex) {
			current = backingIterator.next();	
		}
		return current;
	}

	@Override
	public boolean hasPrevious() {
		return backingIterator.hasPrevious();
	}

	@Override
	public Segment previous() {
		synchronized (mutex) {
			current = backingIterator.previous();
			return current;
		}
	}

	@Override
	public int nextIndex() {
		return backingIterator.nextIndex();
	}

	@Override
	public int previousIndex() {
		return backingIterator.previousIndex();
	}

	@Override
	public void remove() {
		backingIterator.remove();
		current = null;
	}

	@Override
	public void set(Segment segment) {
		synchronized (mutex) {
			backingIterator.set(segment);
			current = segment;
		}
	}

	@Override
	public void add(Segment segment) {
		synchronized (mutex) {
			backingIterator.add(segment);	
		}
	}
	
	
}
