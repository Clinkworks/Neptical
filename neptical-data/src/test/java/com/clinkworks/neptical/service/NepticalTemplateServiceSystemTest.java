package com.clinkworks.neptical.service;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import mockit.Deencapsulation;
import mockit.Mock;
import mockit.MockUp;

import org.apache.commons.configuration.AbstractConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.module.NepticalDataModule;
import com.clinkworks.neptical.util.GuiceInjectionUtil;
import com.google.inject.Injector;
import com.netflix.config.ConfigurationBasedDeploymentContext;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;

public class NepticalTemplateServiceSystemTest {

	private static final String TEST_PROPERTY = "an.archaius.property";
	private static final String TEST_VALUE = "with a value";

	private NepticalTemplateService nepticalTemplateService;

	@Before
	public void setup() {
		
		System.setProperty(TEST_PROPERTY, TEST_VALUE);
		reinitArchiaus();

		Injector injector = GuiceInjectionUtil
				.createInjector(NepticalDataModule.class);
		nepticalTemplateService = injector
				.getInstance(NepticalTemplateService.class);
		injector = null;
	}

	@Test
	public void ensureTemplateServiceCanBeInjected() {
		Assert.assertNotNull(nepticalTemplateService);
	}

	@Test
	public void ensureTemplateIntegrationWithArchaiusWorksProperly() {
		String template = "{{[" + TEST_PROPERTY + "]}}";
		String resolvedValue = nepticalTemplateService.resolve(template);

		Assert.assertEquals(TEST_VALUE, resolvedValue);
	}

	public static class TemplatedData {
		private String myData = "{{[" + TEST_PROPERTY + "]}}";
	}

	@Test
	public void ensureTemplateIntegrationWithAarchaiusWorksWithhRawJavaObjects() {
		TemplatedData templatedData = new TemplatedData();
		TemplatedData resolvedTemplate = nepticalTemplateService
				.resolve(templatedData);
		assertEquals(TEST_VALUE, resolvedTemplate.myData);
	}

	private void reinitArchiaus(){
		
		//clear static state in archaius - NOTE... this is only nessessary since this is using the actual apis instead of mocks AKA... SystemTest
		Deencapsulation.setField(ConfigurationManager.class, "context", null);
		Deencapsulation.setField(ConfigurationManager.class, "instance", null);
		Deencapsulation.setField(ConfigurationManager.class, "configMBean", null);
		Deencapsulation.setField(ConfigurationManager.class, "customConfigurationInstalled", false);

		try {
	            String className = System.getProperty("archaius.default.configuration.class");
	            if (className != null) {
	                Deencapsulation.setField(ConfigurationManager.class, "instance", (AbstractConfiguration) Class.forName(className).newInstance());
	                Deencapsulation.setField(ConfigurationManager.class, "customConfigurationInstalled", true);
	            } else {
	                String factoryName = System.getProperty("archaius.default.configuration.factory");
	                if (factoryName != null) {
	                    Method m = Class.forName(factoryName).getDeclaredMethod("getInstance", new Class[]{});
	                    m.setAccessible(true);
	                    Deencapsulation.setField(ConfigurationManager.class, "instance", (AbstractConfiguration) m.invoke(null, new Object[]{}));
	                    Deencapsulation.setField(ConfigurationManager.class, "customConfigurationInstalled", true);
	                }
	            }
	            String contextClassName = System.getProperty("archaius.default.deploymentContext.class");
	            if (contextClassName != null) {
	            	ConfigurationManager.setDeploymentContext((DeploymentContext) Class.forName(contextClassName).newInstance());
	            } else {
	                String factoryName = System.getProperty("archaius.default.deploymentContext.factory");
	                if (factoryName != null) {
	                    Method m = Class.forName(factoryName).getDeclaredMethod("getInstance", new Class[]{});
	                    m.setAccessible(true);
	                    ConfigurationManager.setDeploymentContext((DeploymentContext) m.invoke(null, new Object[]{}));
	                } else {
	                	ConfigurationManager.setDeploymentContext(new ConfigurationBasedDeploymentContext());
	                }
	            }

	        } catch (Exception e) {
	            throw new RuntimeException("Error initializing configuration", e);
	        }
	}

	
}
