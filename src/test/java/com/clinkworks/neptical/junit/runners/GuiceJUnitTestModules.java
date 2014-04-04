package com.clinkworks.neptical.junit.runners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.name.Names;

public class GuiceJUnitTestModules {

	public static final String DEFAULT_STRING = "I_AM_DEFAULT";
	public static final String STRING_ONE = "I_AM_STRING_ONE";
	public static final String STRING_TWO = "I_AM_STRING_TWO";
	public static final Integer DEFAULT_INTEGER = new Integer(1092);
	public static final Integer LEET_INTEGER = new Integer(1337);
	public static AssertionDependency ASSERTION_DEPENDENCY; 
	

	//Test dependency classes to inject into test
	//----------------------------------------------------------------------------------------
	
	public static class AssertionDependency{
		private boolean wasCalled = false;
		public void assertEquals(Object expectedObject, Object injectedObject){
			wasCalled = true;
			Assert.assertEquals(expectedObject, injectedObject);
		}
		public boolean wasCalled(){
			return wasCalled;
		}
	}
	
	@BindingAnnotation 
	@Target({ FIELD, PARAMETER, METHOD }) 
	@Retention(RUNTIME)
	public @interface BindingAnnotationForIntegerConfig {}
	
	//Guice Conifguration classes for test senarios
	//----------------------------------------------------------------------------------------
	
	public static class MockTestModuleOne extends AbstractModule{
		@Override
		protected void configure(){}
	}

	public static class MockTestModuleTwo extends AbstractModule{
		@Override
		protected void configure(){}
	}
	
	public static class StringModule extends AbstractModule{
		@Override
		protected void configure(){
			bind(String.class).toInstance(DEFAULT_STRING);
		}
	}
	
	public static class BoundIntegerModule extends AbstractModule{
		@Override
		protected void configure() {
			bind(Integer.class).annotatedWith(BindingAnnotationForIntegerConfig.class).toInstance(LEET_INTEGER);
		}
		
	}
	
	public static class IntegerModule extends AbstractModule{
		@Override
		protected void configure(){
			bind(Integer.class).toInstance(DEFAULT_INTEGER);
		}
	}
	
	public static class AssertionModule extends AbstractModule{
		@Override
		protected void configure() {
			ASSERTION_DEPENDENCY = new AssertionDependency();
			bind(AssertionDependency.class).toInstance(ASSERTION_DEPENDENCY);
		}
	}
	
	public static class NamedModule extends AbstractModule{
		@Override
		protected void configure() {
			bind(String.class).annotatedWith(Names.named("String1")).toInstance(STRING_ONE);
			bind(String.class).annotatedWith(Names.named("String2")).toInstance(STRING_TWO);
		}
	}

}
