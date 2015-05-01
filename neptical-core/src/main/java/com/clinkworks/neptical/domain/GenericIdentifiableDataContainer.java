package com.clinkworks.neptical.domain;

import javax.inject.Inject;

import com.clinkworks.neptical.datatype.DataContainer;
import com.clinkworks.neptical.datatype.IdentifiableDataContainer;
import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.datatype.NepticalId;
import com.google.inject.assistedinject.Assisted;

public class GenericIdentifiableDataContainer implements IdentifiableDataContainer{
	
	private final NepticalId<?> nepticalId;
	private final DataContainer internalContainer;
	
	@Inject
	public GenericIdentifiableDataContainer(@Assisted PublicId publicId) {
		nepticalId = publicId;
		internalContainer = new GenericDataContainer();
	}
	
	public GenericIdentifiableDataContainer(PublicId publicId, DataContainer dataContainer) {
		nepticalId = publicId;
		internalContainer = dataContainer;
	}
	
	@Override
	public NepticalId<?> getId() {
		return nepticalId;
	}

	@Override
	public void setNepticalData(NepticalData nepticalData) {
		internalContainer.setNepticalData(nepticalData);
	}

	@Override
	public NepticalData getNepticalData() {
		return internalContainer.getNepticalData();
	}

	@Override
	public boolean containsData() {
		return internalContainer.containsData();
	}

}
