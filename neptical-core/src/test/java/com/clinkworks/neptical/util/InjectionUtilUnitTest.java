package com.clinkworks.neptical.util;

import static com.clinkworks.neptical.util.Common.noOp;
import static com.clinkworks.neptical.util.GuiceInjectionUtil.createInjector;
import static com.clinkworks.neptical.util.GuiceInjectionUtil.createModule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class InjectionUtilUnitTest {
	
	private static final String HELLO_WORLD = "HELLOOOO WOOORRLLLLD";
	private static final String EVIL_STRING = "I SO EVIL";
	
	@Before
	public void before(){
	}
	
	@Test
	public void canConstructAModuleClass(){
		Module testConfigOne = createModule(TestConfigOne.class);
		assertNotNull(testConfigOne);
	}
	
	@Test(expected = NullPointerException.class)
	public void createModuleCannotBePassedANullModule(){
		createModule(null);
	}
	
	@Test
	public void canCreateInjectorWithModule(){
		
		Injector injector = createInjector(TestConfigOne.class);
		String helloWorld = injector.getInstance(String.class);
		
		assertEquals(HELLO_WORLD, helloWorld);
	} 
	
	@Test
	public void canCreateInjectorWithMultipleModules(){
		
		Injector injector = createInjector(TestConfigOne.class, TestConfigTwo.class);
		
		String worldString = injector.getInstance(String.class);
		String evilString = injector.getInstance(Key.get(String.class, Names.named("EvilString")));
		
		assertEquals(EVIL_STRING, evilString);
		assertEquals(HELLO_WORLD, worldString);
	}
	
	public static class TestConfigOne extends AbstractModule{
		
		@Override
		protected void configure() {
			noOp();
		}
		
		@Provides
		public String helloWorld(){
			return HELLO_WORLD;
		}
	}
	
	public static class TestConfigTwo extends AbstractModule{
		@Override
		protected void configure() {
			noOp();
		}
		
		@Named("EvilString")
		@Provides
		public String helloWorld(){
			return EVIL_STRING;
		}
	}	
	
}