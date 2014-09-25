package com.clinkworks.neptical.data.graph;

import com.clinkworks.neptical.Data;

public class BasicNode implements Node{

	private Data data;
	
	public BasicNode(Data data){
		this.data = data;
	}
	
	@Override
	public Data getData() {
		return data;
	}
	
	public void setData(Data data){
		synchronized (this) {
			this.data = data;	
		}
	}


	@Override
	public String getName() {
		return data.getName();
	}


	@Override
	public Object get() {
		return data.get();
	}

}
