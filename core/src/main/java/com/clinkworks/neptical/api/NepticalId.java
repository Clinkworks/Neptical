package com.clinkworks.neptical.api;

import java.io.Serializable;
import java.util.UUID;

public class NepticalId<T extends Serializable> {
	
	private final T id;
	private final String uniqueId;
	
	public NepticalId(T id){
		this.id = id;
		this.uniqueId = UUID.randomUUID().toString();
	}
	
	public T getId(){
		return id;
	}
	
	@Override
	public int hashCode(){
		return getId().hashCode();
	}
	
	@Override
	public boolean equals(Object object){
		
		if(object == null){
			return false;
		}
		
		if(object instanceof NepticalId<?>){
			return getId().equals(((NepticalId<?>)object).getId());
		}
		
		return getId().equals(object);
	}

	public String getUniqueId() {
		return uniqueId;
	}
	
}
