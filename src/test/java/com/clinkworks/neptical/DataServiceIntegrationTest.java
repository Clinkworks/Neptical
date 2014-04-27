package com.clinkworks.neptical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.data.api.DataElement;
import com.clinkworks.neptical.data.domain.JsonData;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner.NepticalConfiguration;
import com.clinkworks.neptical.modules.NepticalDataModule;
import com.google.inject.Inject;

@RunWith(NepticalJUnit4Runner.class)
@NepticalConfiguration({NepticalDataModule.class})
public class DataServiceIntegrationTest {
	
	@Inject
	private DataService dataService;

	@Test
	public void ensureDataServiceCanLoadDirectories(){
		File file = new File(System.getProperty("user.home") + "/neptical-data");
		
		DataElement data = dataService.loadFile(file);
		
		assertTrue(data.isLoaded());
		assertEquals("neptical-data", data.getName());
	}
	
	@Test
	public void ensureDataServiceCanLoadFiles(){
		File file = getFile("neptical-data/hello neptical.txt");
		DataElement data = dataService.loadFile(file);
		
		assertTrue(data.isLoaded());
		
		assertEquals("hello neptical", data.getName());
		assertEquals("HELLO NEPTICAL!", data.get());
	}
	
	@Test
	public void ensureDataServiceCanLoadJson(){
		File file = getFile("/neptical-data/example.json");
		
		String expectedValue = "Neptical";
		
		JsonData jsonData = dataService.loadFile(file).getAsDataType(JsonData.class);
		
		assertEquals(expectedValue, jsonData.getAsJsonElement().getAsJsonObject().get("hello").getAsString());
	}
	
	private File getFile(String relPath){
		return new File(System.getProperty("user.home") + "/" + relPath); 
	}
}
