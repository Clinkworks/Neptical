package com.clinkworks.neptical.domain;

import static com.clinkworks.neptical.util.PathUtil.firstSegment;

import org.apache.commons.lang3.StringUtils;

import com.clinkworks.neptical.datatype.IdentifiableDataContainer;
import com.clinkworks.neptical.datatype.NepticalData;
import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.spi.TraversableData;
import com.clinkworks.neptical.util.PathUtil;

public class Segment implements IdentifiableDataContainer{

	private volatile NepticalData nepticalData;
	private final String name;
	private final NepticalId<?> nepticalId;
	
	public Segment(String name, String publicId){
		this.name = name;
		this.nepticalId = new PublicId(publicId);
	}
	
	public Segment(PublicId publicId){
		this.name = PathUtil.lastSegment(publicId.get());
		this.nepticalId = publicId;
	}
	
	@Override
	public void setNepticalData(NepticalData nepticalData) {
		this.nepticalData = nepticalData;
	}

	@Override
	public NepticalData getNepticalData() {
		return nepticalData;
	}

	@Override
	public NepticalId<?> getId() {
		return nepticalId;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString(){
		return getId().get().toString();
	}
	
	@Override
	public boolean containsData() {
		return getNepticalData() != null;
	}
		
	public boolean containsDataAtPath(String notation){
		return getId().equals(notation);
	}
	
	public boolean containsTraversableData(){
		return getNepticalData() instanceof TraversableData;
	}
	
	//TODO: refactor this to use a path instead of the strings.
	public boolean canHandleNextSegment(String notation){
		return StringUtils.equals(firstSegment(toString()), firstSegment(notation));
	}

	public NepticalData find(String notation) {
		
		if(containsDataAtPath(notation)){
			return getNepticalData();
		}
		
		boolean canFind = canHandleNextSegment(notation);
		
		canFind = canFind && containsTraversableData();
		
		return ((TraversableData)getNepticalData()).find(notation);
	}
}
