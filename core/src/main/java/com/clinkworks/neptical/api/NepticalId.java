package com.clinkworks.neptical.api;

import java.io.Serializable;

public class NepticalId<T extends Serializable> {
	
	private final T id;
	
	public NepticalId(T id){
		this.id = id;
	}
	
	public T get(){
		return id;
	}
	
	@Override
	public int hashCode(){
		return get().hashCode();
	}
	
	@Override
	public boolean equals(Object object){
		
		if(object == null){
			return false;
		}
		
		if(object instanceof NepticalId<?>){
			return get().equals(((NepticalId<?>)object).get());
		}
		
		return get().equals(object);
	}
	
	@Override
	public String toString(){
		return get().toString();
	}
	
}
