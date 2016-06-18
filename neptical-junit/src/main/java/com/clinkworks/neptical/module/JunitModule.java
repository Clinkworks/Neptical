package com.clinkworks.neptical.module;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class JunitModule extends AbstractModule {

	@Override
	protected void configure() {
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Test.class),
				new TestMethodInterceptor());
	}

	static class TestMethodInterceptor implements MethodInterceptor {
		  public Object invoke(MethodInvocation invocation) throws Throwable {
			  System.out.println("I was called...");
		    return invocation.proceed();
		  }
		}
	
}
