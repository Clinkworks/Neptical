package com.clinkworks.neptical.junit.statements;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.clinkworks.neptical.util.GuiceInjectionUtil;
import com.google.inject.Injector;

public class FrameworkMethodWrapper extends Statement{

	private static final Logger LOGGER = Logger.getLogger(FrameworkMethodWrapper.class);
	
    private final FrameworkMethod fTestMethod;
    private final Object fTarget;
    private Object[] fParams;
    private final String name;

    public FrameworkMethodWrapper(Injector injector, FrameworkMethod testMethod, Object testContext) {
    	fTestMethod = testMethod;
    	fTarget = testContext;
    	this.name = testMethod.getName();
    	LOGGER.debug(" ---- Preparing method with test dependencies ---- ");
    	LOGGER.debug(" ---- Gathering test dependencies ---- ");
    	fParams = GuiceInjectionUtil.getParameterValuesToInject(testMethod, injector).toArray();
    	LOGGER.debug(" ---- Initalizing test statement with required dependencies " + Arrays.toString(fParams) + " ---- ");
    }	
	
	@Override
	public void evaluate() throws Throwable {
		LOGGER.debug(" ---- Invoking Neptical Enhanced Test Method (" + name + ") ---- ");
		fTestMethod.invokeExplosively(fTarget, fParams);
	}
	
	public Object[] getMethodParameters(){
		return fParams;
	}

}
