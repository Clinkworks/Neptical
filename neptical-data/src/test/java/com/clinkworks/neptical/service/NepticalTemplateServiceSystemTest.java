package com.clinkworks.neptical.service;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.module.NepticalHandlebarsModule;
import com.clinkworks.neptical.util.GuiceInjectionUtil;
import com.google.inject.Injector;

public class NepticalTemplateServiceSystemTest {

	private static final String TEST_PROPERTY = "an.archaius.property";
	private static final String TEST_VALUE = "with a value";
	
	private NepticalTemplateService nepticalTemplateService;
	
	@Before
	public void setup(){
		System.setProperty(TEST_PROPERTY, TEST_VALUE);
		Injector injector = GuiceInjectionUtil.createInjector(NepticalHandlebarsModule.class);
		nepticalTemplateService = injector.getInstance(NepticalTemplateService.class);
	}
	
	@Test
	public void ensureTemplateServiceCanBeInjected(){
		Assert.assertNotNull(nepticalTemplateService);
	}
	
	@Test
	public void ensureTemplateIntegrationWithArchaiusWorksProperly(){		
		String template = "{{[" + TEST_PROPERTY + "]}}";
		String resolvedValue = nepticalTemplateService.resolve(template);
		
		Assert.assertEquals(TEST_VALUE, resolvedValue);
	}
	
	public static class TemplatedData{
		private String myData = "{{[" + TEST_PROPERTY + "]}}";
	}
	
	@Test
	public void ensureTemplateIntegrationWithAarchaiusWorksWithhRawJavaObjects(){
		TemplatedData templatedData = new TemplatedData();
		TemplatedData resolvedTemplate = nepticalTemplateService.resolve(templatedData);
		assertEquals(TEST_VALUE, resolvedTemplate.myData);
	}
	
}
