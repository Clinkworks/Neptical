package com.clinkworks.neptical.service;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.api.NepticalContext;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.module.NepticalDataModule;

@RunWith(NepticalJUnit4Runner.class)
@NepticalContext(NepticalDataModule.class)
public class CursorServiceIntegrationTest {

	@Inject
	private CursorService cursorService;
	
	@Test
	public void cursorServiceCorrectlyFindsTheAddressNodeWithinTheDefaultTestData(){
		Data data = cursorService.find("neptical.contacts.addresses.genericAddress.addressLine1");
		assertEquals("1234 my place drive", data.getAsJsonData().getAsString());	
	}
	
}
