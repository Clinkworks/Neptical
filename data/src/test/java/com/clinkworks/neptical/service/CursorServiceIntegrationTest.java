package com.clinkworks.neptical.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner.NepticalConfiguration;
import com.clinkworks.neptical.module.NepticalDataModule;
import com.google.inject.Inject;

@RunWith(NepticalJUnit4Runner.class)
@NepticalConfiguration(NepticalDataModule.class)
public class CursorServiceIntegrationTest {

	@Inject
	private CursorService cursorService;
	
	@Test
	public void cursorServiceCorrectlyFindsTheAddressNodeWithinTheDefaultTestData(){
		Data data = cursorService.find("neptical-data.contacts.addresses.genericAddress.addressLine1");
		assertEquals("1234 my place drive", data.get());
	}
}
