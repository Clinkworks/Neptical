package com.clinkworks.neptical;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.data.api.DataElement;
import com.clinkworks.neptical.data.api.DataLoader;
import com.clinkworks.neptical.data.datatypes.LoadableData;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class DataServiceUnitTest {
	
	private DataService dataService;
	
	@Before
	public void setup(){
		Map<Serializable, DataLoader> dataLoaderRegistry = Maps.newHashMap();
		dataLoaderRegistry.put("mock", new MockDataLoader());
		dataService = new DataService(null, null);
	}
	
	@Test
	public void ensureTheDataServiceProperlyCallsLoadersfromTheRegistry(){
		LoadableData node = new MockLoadableData();
		
		assertFalse(node.isLoaded());
		
		dataService.loadData(node);
		
		assertTrue(node.isLoaded());
		
	}
	
	public static class MockDataLoader implements DataLoader{
		
		@Override
		public DataElement loadData(LoadableData loadableNode) {
			loadableNode.toggleLoadedTrue();
			return new DataElement(loadableNode);
		}

		@Override
		public Set<Serializable> getHandledDataLoaderCriterian() {
			return Sets.newHashSet((Serializable)"mock");
		}
		
	}
	
	public static class MockLoadableData implements LoadableData{

		private boolean isLoaded;
		
		public MockLoadableData(){
			isLoaded = false;
		}
		
		@Override
		public Serializable getLoaderCriterian() {
			return "mock";
		}

		@Override
		public boolean isLoaded() {
			return isLoaded;
		}

		@Override
		public void toggleLoadedTrue() {
			isLoaded = true;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public Object get() {
			return null;
		}

		@Override
		public void toggleLoadedFalse() {
			isLoaded = false;
		}
		
	}
	
}
