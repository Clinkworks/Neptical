package com.clinkworks.neptical.service;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner.NepticalConfiguration;
import com.clinkworks.neptical.module.NepticalDataModule;
import com.clinkworks.neptical.service.DataService;
import com.google.inject.Inject;

@RunWith(NepticalJUnit4Runner.class)
@NepticalConfiguration(NepticalDataModule.class)
public class DataServiceSystemTest {

	@Inject
	private DataService dataService;
	
	
	@Test
	public void dataServiceCanProperlyInitNepticalTestData(){
	}
}
