package com.clinkworks.neptical.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.graph.DataGraph;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.api.NepticalContext;
import com.clinkworks.neptical.module.NepticalDataModule;
import com.google.inject.Inject;

@RunWith(NepticalJUnit4Runner.class)
@NepticalContext(NepticalDataModule.class)
public class CursorServiceIntegrationTest {

	@Inject
	private CursorService cursorService;
	
	@Inject
	private DataGraph dataGraph;
	
	@Test
	public void cursorServiceCorrectlyFindsTheAddressNodeWithinTheDefaultTestData(){
		Data data = cursorService.find("neptical-data.contacts.addresses.genericAddress.addressLine1");
		assertEquals("1234 my place drive", data.getAsJsonData().getAsString());
		dataGraph.dumpGraph();
	}
	
}
