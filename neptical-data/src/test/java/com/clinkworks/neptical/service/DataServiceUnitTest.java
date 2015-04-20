package com.clinkworks.neptical.service;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import mockit.Injectable;

import org.junit.Before;

import com.clinkworks.neptical.graph.DataGraph;
import com.clinkworks.neptical.loader.FileDataLoader;
import com.clinkworks.neptical.spi.DataLoader;


public class DataServiceUnitTest {

	@Injectable
	private DataGraph dataGraph;
	
	@Injectable
	private FileDataLoader fileDataLoader;
	
		
	@Before
	public void setup(){
		Map<Serializable, DataLoader> dataLoaders = new HashMap<Serializable, DataLoader>();
		dataLoaders.put(File.class, fileDataLoader);
//		dataService =  new DataService(new File("test"), null, dataGraph, dataLoaders);
	}
	
	
}
