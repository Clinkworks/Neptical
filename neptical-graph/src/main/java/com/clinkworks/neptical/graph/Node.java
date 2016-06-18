package com.clinkworks.neptical.graph;

import javax.inject.Inject;

import com.clinkworks.neptical.datatype.IdentifiableDataContainer;
import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.datatype.NepticalId;
import com.google.inject.assistedinject.Assisted;

public class Node implements IdentifiableDataContainer{
	
	private final IdentifiableDataContainer identifiableDataContainer;

	@Inject
	Node(@Assisted IdentifiableDataContainer identifiableDataContainer){
		this.identifiableDataContainer = identifiableDataContainer;
	}
	
	@Override
	public NepticalId<?> getId() {
		return identifiableDataContainer.getId();
	}
	
	@Override
	public String toString(){
		return getId().toString();
	}

	@Override
	public void setNepticalData(NepticalData nepticalData) {
		identifiableDataContainer.setNepticalData(nepticalData);
	}

	@Override
	public NepticalData getNepticalData() {
		return identifiableDataContainer.getNepticalData();
	}

	@Override
	public boolean containsData() {
		return getNepticalData() != null;
	}

}
