package com.clinkworks.neptical.service;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import javax.inject.Inject;

import mockit.Deencapsulation;

import org.apache.commons.configuration.AbstractConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.module.NepticalDataModule;
import com.clinkworks.neptical.util.GuiceInjectionUtil;
import com.google.inject.Injector;
import com.netflix.config.ConfigurationBasedDeploymentContext;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;

public class CursorServiceSystemTest {

	@Inject
	private CursorService cursorService;

	@Before
	public void setup(){
		Injector injector = GuiceInjectionUtil.createInjector(NepticalDataModule.class);
		injector.injectMembers(this);
		
		//since the junit runtimes are not avail... we have to do it the hard way :(
	}
	
	@Test
	public void cursorServiceCorrectlyFindsTheAddressNodeWithinTheDefaultTestData(){
		Data data = cursorService.find("neptical.contacts.addresses.genericAddress.addressLine1");
		assertEquals("1234 my place drive", data.getAsJsonData().getAsString());	
	}
		
}
