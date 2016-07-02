package clinkworks.neptical.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;

import clinkworks.neptical.component.NSpaceManager;
import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.datatype.NepticalData;

public class GenericDataModule implements DataModule {

	private static final List<NepticalData> EMPTY_LIST = ImmutableList.of();
	
	private final String name;
	private final ListMultimap<String, NepticalData> data;
	private final List<String> fragments;

	public GenericDataModule(String name) {
		this.name = name;
		this.data = ArrayListMultimap.create();
		this.fragments = new CopyOnWriteArrayList<>();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] segments() {
		return fragments.toArray(new String[fragments.size()]);
	}

	@Override
	public void addData(String segment, NepticalData data) throws DataDefinitionException {
		if(this.data.get(segment) == null){
			fragments.add(segment);
		}
		this.data.put(segment, data);
	}

	@Override
	public List<NepticalData> getAllData() {
		return new ArrayList<>(data.values());
	}

	@Override
	public List<NepticalData> getDataAt(String segment) {
		List<NepticalData> dataList = data.get(segment);
		return dataList == null ? EMPTY_LIST : new ArrayList<>(dataList);
	}

	@Override
	public NepticalData getDataAt(String segment, int index) {
		
		List<NepticalData> dataList = data.get(segment);
		
		if(dataList == null || dataList.isEmpty() || index > dataList.size() -1){
			return NepticalData.NULL_DATA;
		}
		
		return dataList.get(index);
		
	}

	@Override
	public CursorContext getCursorContext() {
		return NSpaceManager.getCursorContext(this);
	}
	
	@Override
	public String toString(){
		return name + "(" + fragments + ")";
	}
	
}
