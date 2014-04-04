package com.clinkworks.neptical.junit.runners;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.clinkworks.neptical.util.Common.noOp;

import com.clinkworks.neptical.junit.runners.GuiceJUnit4Runner.GuiceConfig;
import com.clinkworks.neptical.junit.runners.GuiceJUnitTestModules.BindingAnnotationForIntegerConfig;
import com.clinkworks.neptical.junit.runners.GuiceJUnitTestModules.BoundIntegerModule;
import com.clinkworks.neptical.junit.runners.GuiceJUnitTestModules.IntegerModule;
import com.clinkworks.neptical.junit.runners.GuiceJUnitTestModules.NamedModule;
import com.clinkworks.neptical.junit.runners.GuiceJUnitTestModules.StringModule;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(GuiceJUnit4Runner.class)
@GuiceConfig({StringModule.class, NamedModule.class, BoundIntegerModule.class})
public class GuiceJUnit4IntegrationTest {
	
	boolean beforeMethodWasCalled = false;
	
	@Before
	public void before(String stringBeforeInjection){
		beforeMethodWasCalled = true;
		assertEquals(GuiceJUnitTestModules.DEFAULT_STRING, stringBeforeInjection);
	}
	
	@After
	public void afterMethodCalled(String stringBeforeInjection){
		beforeMethodWasCalled = true;
		assertEquals(GuiceJUnitTestModules.DEFAULT_STRING, stringBeforeInjection);
	}
	
	@After
	public void afterMethodWithoutParams(){
		noOp();
	}
	
	
	@BindingAnnotationForIntegerConfig
	@Inject
	private Integer leetInteger;
	
	@Test
	public void integrationTestExpectingTheBeforeMethodTohaveRunSuccessfully(){
		assertTrue(beforeMethodWasCalled);
	}
	@Test
	public void integrationTestExpectingTestContextLevelInjectionToSucceed(){
		assertNotNull(leetInteger);
		assertEquals(GuiceJUnitTestModules.LEET_INTEGER, leetInteger);
	}
	
	@Test
	public void integrationTestExpectingTestLevelInjectionToSucceed(@BindingAnnotationForIntegerConfig Integer boundInteger){
		assertNotNull(boundInteger);
		assertEquals(GuiceJUnitTestModules.LEET_INTEGER, boundInteger);
	}
	
	@Test
	public void asdf(){
		String data = Thread.currentThread().getContextClassLoader().getResource("data/").getFile();
		System.out.println(data);
	}
	
	@Test
	public void integrationTestMissingIntegerWithNamedAnnotationConfigurationExpectingNullInteger(Integer nullInteger, String stringA, String stringB, @Named("String1") String string1, @Named("String2") String string2){
		assertTrue(StringUtils.isNotBlank(string1));
		assertEquals(GuiceJUnitTestModules.DEFAULT_STRING, stringA);
		assertEquals(stringA, stringB);
		assertEquals(GuiceJUnitTestModules.STRING_ONE, string1);
		assertEquals(GuiceJUnitTestModules.STRING_TWO, string2);
		assertNull(nullInteger);
	}
	
	@Test
	@GuiceConfig(IntegerModule.class)
	public void integeationConfigTestForTestLevelConfiguration(Integer integer){
		assertEquals(GuiceJUnitTestModules.DEFAULT_INTEGER, integer);
	}
	
}
