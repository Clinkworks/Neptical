package com.clinkworks.neptical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.clinkworks.neptical.modules.NepticalPropertiesModule;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.data.api.DataElement;
import com.clinkworks.neptical.data.domain.JsonData;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner.NepticalConfiguration;
import com.clinkworks.neptical.modules.NepticalDataModule;
import com.google.inject.Inject;

@RunWith(NepticalJUnit4Runner.class)
@NepticalConfiguration({NepticalPropertiesModule.class, NepticalDataModule.class})
public class DataServiceIntegrationTest {
	
	@Inject
	private DataService dataService;

	@Test
	public void ensureDataServiceCanLoadDirectories(){
		File file = getFile("");
		DataElement data = dataService.loadFile(file);
		assertTrue(data.isFileData());
		assertTrue(data.isLoaded());
		assertEquals("data", data.getName());
		assertTrue(data.getAsFileData().getAsFile().isDirectory());
	}
	
	@Test
	public void ensureDataServiceCanLoadFiles() throws IOException{

		File file = getFile("hello neptical.txt");
		DataElement data = dataService.loadFile(file);
		
		assertTrue(data.isLoaded());
		
		assertEquals("hello neptical", data.getName());
		assertEquals("HELLO NEPTICAL!", data.get());
	}
	
	@Test
	public void ensureDataServiceCanLoadJson(){
		File file = getFile("example.json");
		
		String expectedValue = "Neptical";
		
		JsonData jsonData = dataService.loadFile(file).getAsJsonData();
		
		assertEquals(expectedValue, jsonData.getAsJsonElement().getAsJsonObject().get("hello").getAsString());
	}
	
	private File getFile(String relPath){
		URL classesRootDir = getClass().getProtectionDomain().getCodeSource().getLocation();
		return new File(classesRootDir.getFile() + "/data/" + relPath);
	}
}
