package com.clinkworks.neptical.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import com.google.inject.Injector;
import com.google.inject.Module;

public class JUnitIntegrationUtil {
	
	private static final Logger LOGGER = Logger.getLogger(GuiceInjectionUtil.class);
	
    public static Injector createInjector(TestClass junitTestClass, FrameworkMethod method){
    	return createInjectorWithConfiguredModules(junitTestClass, method);
    }
    
    public static Class<? extends Module>[] getConfiguredContextModules(TestClass junitTestClass){
    	return GuiceInjectionUtil.getConfiguredContextModules(junitTestClass.getJavaClass());
    }
    
    public static Class<? extends Module>[] getConfiguredContextModules(FrameworkMethod method) throws InitializationError{
    	return GuiceInjectionUtil.getConfiguredTestModules(method.getMethod());
    }
    
    static Injector createInjectorWithConfiguredModules(TestClass junitTestClass, FrameworkMethod method){
    	LOGGER.debug(" ---- Initalizing Guice Injector ---- ");
    	return GuiceInjectionUtil.createInjectorWithConfiguredModules(junitTestClass.getJavaClass(), method.getMethod());
    }

	public static List<Object> getParameterValuesToInject(FrameworkMethod testMethod, Injector injector) {
		return GuiceInjectionUtil.getParameterValuesToInject(testMethod.getMethod(), injector);
	}
    
}
