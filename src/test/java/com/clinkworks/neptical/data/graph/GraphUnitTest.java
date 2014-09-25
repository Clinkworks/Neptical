package com.clinkworks.neptical.data.graph;

import java.io.File;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.property.FileProperty;

public class GraphUnitTest {

	private static final String DIRECTORY_NAME = "test-directory";
	private static final String FILE_NAME = "file.txt";
	
	private Graph graph;

	private File testFile;
	private File testDirectory;
		
	@Before
	public void setup(){
		graph = new Graph();
		testFile = new MockDataFile().getMockInstance();
		testDirectory = new MockDataDirectory().getMockInstance();
	}
	
	@Test
	public void addingFileDataBuildsCorrectEdgesForADisconnectedDirectoryNode(){
		graph.add	
	}	
	
	public static class MockDataDirectory extends MockUp<File>{
		@Mock
		public String getName(){
			return DIRECTORY_NAME;
		}
	}
	
	public static class MockDataFile extends MockUp<File>{

		@Mock
		public String getName(){
			return FILE_NAME;
		}
		
	}
}
