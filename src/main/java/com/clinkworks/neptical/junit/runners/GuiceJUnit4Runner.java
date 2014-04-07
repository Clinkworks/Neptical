package com.clinkworks.neptical.junit.runners;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.clinkworks.neptical.junit.statements.FrameworkMethodWrapper;
import com.clinkworks.neptical.junit.statements.FrameworkMethodsWrapper;
import com.clinkworks.neptical.util.InjectionUtil;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceJUnit4Runner extends BlockJUnit4ClassRunner {
	private static final Logger LOGGER = Logger.getLogger(GuiceJUnit4Runner.class);

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface GuiceConfig {
        Class<? extends Module>[] value();
    }
    
    public GuiceJUnit4Runner(Class<?> klass) throws InitializationError {
        super(klass);
    }   
    
    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
        Description description = describeChild(method);
        if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
        } else {

            runLeaf(methodBlock(method), description, notifier);
        }
    }
    
    protected Statement methodInvoker(Injector injector, FrameworkMethod method, Object testContext) {
    	String testContextName = testContext.getClass().getSimpleName();
    	String testName = method.getMethod().getName();
    	String fullName = testContextName + "." + testName;
    	LOGGER.debug(" ---- Building test: " + fullName + " ---- ");
    	LOGGER.debug(" ---- Initalizing context level dependencies for test context: " + testContextName + " ---- ");
    	injector.injectMembers(testContext);
    	Statement statement = new FrameworkMethodWrapper(injector, method, testContext);
    	LOGGER.debug(" ---- Test " + fullName + " initalized, handing the test to JUnitBlockRunner ---- ");
    	return statement;
    }
    
    @Override
    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation,
            boolean isStatic, List<Throwable> errors) {
    	
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);

        for (FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoid(isStatic, errors);
        }
    }
    
    //TODO: Don't do this. 
    //the reasoning behind overriding this method is to allow the injector to be passed
    //in a thread safe manner. I did not want to hold references to it anywhere
    //the real need in doing this was to implement the @Before annotation.
    //will implement rules once I get time and stop supporting @Before annotations
    @Override
    @Deprecated
    protected Statement methodBlock(FrameworkMethod method) {
        Object test;
        try {
            test = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    return createTest();
                }
            }.run();
        } catch (Throwable e) {
            return new Fail(e);
        }
        Injector injector = initalizeInjector(method, test);
        Statement statement = methodInvoker(injector, method, test);
        statement = possiblyExpectingExceptions(method, test, statement);
        statement = withPotentialTimeout(method, test, statement);
        statement = withBefores(injector, method, test, statement);
        statement = withAfters(injector, method, test, statement);
        return statement;
    }
    
    @Deprecated
    protected Statement withBefores(Injector injector, FrameworkMethod frameworkMethod, Object testContext, Statement statement){
        List<FrameworkMethod> befores = getTestClass().getAnnotatedMethods(
                Before.class);
        return befores.isEmpty() ? statement : new FrameworkMethodsWrapper(injector, statement,
                befores, testContext);
    }
    
    @Deprecated
    protected Statement withAfters(Injector injector, FrameworkMethod frameworkMethod, Object testContext, Statement statement){
        List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(
                After.class);
        return afters.isEmpty() ? statement : new FrameworkMethodsWrapper(injector, statement,
                afters, testContext);
    }
        
    
    protected Injector initalizeInjector(FrameworkMethod method, Object testContext){
    	String testContextName = testContext.getClass().getSimpleName();
    	String testName = method.getMethod().getName();
    	String fullName = testContextName + "." + testName;
    	LOGGER.debug(" ---- Building test: " + fullName + " ---- ");
    	return InjectionUtil.createInjector(getTestClass(), method);
    }

}