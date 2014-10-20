package com.clinkworks.neptical.junit.runners;

import static com.clinkworks.neptical.util.GuiceInjectionUtil.createInjector;
import static com.clinkworks.neptical.util.GuiceInjectionUtil.getConfiguredContextModules;
import static com.clinkworks.neptical.util.GuiceInjectionUtil.getConfiguredTestModules;
import static com.clinkworks.neptical.util.GuiceInjectionUtil.getParameterValuesToInject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestFullIntegrationMethodLevelClassLevelConfigurations;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestModuleOne;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestModuleTwo;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestRequiresUnConfiguredDependency;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestThatEnsuresInstancesArePassedToTestMethods;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestThatNeedsSupportForNamedAnnotation;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestWithBeforeMethod;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestWithBoundAnnotationDependency;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestWithClassLevelGuiceConfig;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestWithMethodLevelGuiceConfig;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestWithMethodParams;
import com.clinkworks.neptical.test.modules.NepticalJUnitTestModules.MockTestWithStringDependency;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;

@RunWith(BlockJUnit4ClassRunner.class)
public class NepticalJUnit4RunnerUnitTest {

    private static final String BEFORE_METHOD_TEST_METHOD = "testMethodToEnsureBeforeMethodInjection";
    private static final String BINDING_ANNOTATION_TEST_METHOD = "testMethodWithUnConfiguredIntegerAndBoundInteger";
    private static final String ANNOTATED_TEST_METHOD = "annotatedTestMethod";
    private static final String DEPENDENCY_METHOD_INJECTION_TEST_METHOD = "testMethodToTestInjectedDependencies";
    private static final String STRING_DEPENDENCY_TEST_METHOD = "testMethodWithStringDependency";
    private static final String FULL_INTEGRATION_TEST_METHOD = "testMethodWithStringAndIntegerDependencys";
    private static final String NAMED_CONFIGURATION_TEST_METHOD = "testNamedConfiguration";

    @Test
    public void canCreateTestContextThatHasParametersInTestMethodSignatures() {
        try {
            new NepticalJUnit4Runner(MockTestWithMethodParams.class);
        } catch (InitializationError e) {
            fail("Guice Junit Runner MUST be able to pass dependencies to test methods, JUnit disallows");
        }
    }
 
    @Test
    public void canRetrieveContextLevelConfiguration() throws InitializationError {
        NepticalJUnit4Runner runner = new NepticalJUnit4Runner(MockTestWithClassLevelGuiceConfig.class);
        Class<?>[] modules = getConfiguredContextModules(runner.getTestClass());
        assertEquals(1, modules.length);
        assertEquals(MockTestModuleOne.class, modules[0]);
    }

    @Test
    public void canRetrieveTestLevelConfiguration() throws InitializationError {

        Class<MockTestWithMethodLevelGuiceConfig> test = MockTestWithMethodLevelGuiceConfig.class;

        FrameworkMethod frameworkMethod = getFrameworkMethod(test, ANNOTATED_TEST_METHOD);

        Class<?>[] modules = getConfiguredTestModules(frameworkMethod);

        assertEquals(1, modules.length);
        assertEquals(MockTestModuleTwo.class, modules[0]);
    }

    @Test
    public void canCreateInstancesOfParameterDependencies() throws InitializationError {

        Class<MockTestWithStringDependency> test = MockTestWithStringDependency.class;
        NepticalJUnit4Runner runner = new NepticalJUnit4Runner(test);

        FrameworkMethod method = getFrameworkMethod(test, STRING_DEPENDENCY_TEST_METHOD);
        Injector injector = createInjector(runner.getTestClass(), method);

        List<Object> parametersToInject = getParameterValuesToInject(method, injector);

        assertEquals(1, parametersToInject.size());
        assertEquals(NepticalJUnitTestModules.DEFAULT_STRING, parametersToInject.get(0));

    }

    @Test
    public void canPassDependencyInstancesToTestMethods() throws Throwable {

        Class<MockTestThatEnsuresInstancesArePassedToTestMethods> test = MockTestThatEnsuresInstancesArePassedToTestMethods.class;

        NepticalJUnit4Runner runner = new NepticalJUnit4Runner(test);

        runTestMethod(runner, test, DEPENDENCY_METHOD_INJECTION_TEST_METHOD);

        assertTrue(NepticalJUnitTestModules.ASSERTION_DEPENDENCY.wasCalled());
    }

    @Test
    public void canConfigureBothContextLevelAndTestLevelConfigurations() throws Throwable {
        Class<MockTestFullIntegrationMethodLevelClassLevelConfigurations> test = MockTestFullIntegrationMethodLevelClassLevelConfigurations.class;

        NepticalJUnit4Runner runner = new NepticalJUnit4Runner(test);

        runTestMethod(runner, test, FULL_INTEGRATION_TEST_METHOD);

        assertTrue(NepticalJUnitTestModules.ASSERTION_DEPENDENCY.wasCalled());

    }

    @Test
    public void namedAnnotationIsSupportedForTestLevelInjection() throws Throwable {
        Class<MockTestThatNeedsSupportForNamedAnnotation> test = MockTestThatNeedsSupportForNamedAnnotation.class;

        NepticalJUnit4Runner runner = new NepticalJUnit4Runner(test);

        runTestMethod(runner, test, NAMED_CONFIGURATION_TEST_METHOD);

        assertTrue(NepticalJUnitTestModules.ASSERTION_DEPENDENCY.wasCalled());
    }

    @Test
    public void bindingAnnotationsAreSupported() throws Throwable {
        Class<MockTestWithBoundAnnotationDependency> test = MockTestWithBoundAnnotationDependency.class;

        NepticalJUnit4Runner runner = new NepticalJUnit4Runner(test);

        runTestMethod(runner, test, BINDING_ANNOTATION_TEST_METHOD);

        assertTrue(NepticalJUnitTestModules.ASSERTION_DEPENDENCY.wasCalled());

    }

    @Test
    public void runnerSupportsInjectionIntoBeforeMethods() throws Throwable {
        Class<MockTestWithBeforeMethod> test = MockTestWithBeforeMethod.class;

        NepticalJUnit4Runner runner = new NepticalJUnit4Runner(test);

        runTestMethod(runner, test, BEFORE_METHOD_TEST_METHOD);

        assertTrue(NepticalJUnitTestModules.ASSERTION_DEPENDENCY.wasCalled());
    }


    @Test
    public void reverseTest() {
        Integer[] ints = { 1, 2, 3, 4 };
        Integer[] reversedInts = reverse(ints);
        assertEquals(Integer.valueOf(1), reversedInts[3]);
        assertEquals(Integer.valueOf(4), reversedInts[0]);
    }

    public static <T> T[] reverse(T[] arrayToReverse) {
        List<T> listOfType = Arrays.asList(arrayToReverse);
        Collections.reverse(listOfType);
        return ArrayUtils.toArray(arrayToReverse);
    }

    @Test
    public void unConfiguredTestLevelInjectionDoesNotFail() throws Throwable {
        Class<MockTestRequiresUnConfiguredDependency> test = MockTestRequiresUnConfiguredDependency.class;

        NepticalJUnit4Runner runner = new NepticalJUnit4Runner(test);

        try {
            runTestMethod(runner, test, STRING_DEPENDENCY_TEST_METHOD);
        } catch (ConfigurationException e) {
            fail("Configuration exception should no longer be thrown from method level injection");
        }

        assertTrue(NepticalJUnitTestModules.ASSERTION_DEPENDENCY.wasCalled());
    }


    // Test helpers - will do a bit of research and find better libraries for
    // handling these
    // If I do decide to keep them I should probably externalize them to a
    // different service
    // ----------------------------------------------------------------------------------------
    private static Method getJavaMethod(Class<?> testClass, String methodName) {

        if (testClass == null) {
            return null;
        }

        if (testClass.getClass().equals(java.lang.Object.class)) {
            return null;
        }

        if (testClass.isInterface()) {
            return null;
        }

        Method[] methods = testClass.getDeclaredMethods();

        for (Method method : methods) {
            if (StringUtils.equals(method.getName(), methodName)) {
                Method methodToReturn = method;
                methodToReturn.setAccessible(true);
                return method;
            }
        }

        return getJavaMethod(testClass.getSuperclass(), methodName);
    }

    private static FrameworkMethod getFrameworkMethod(Class<?> testClass, String methodName) {
        Method method = getJavaMethod(testClass, methodName);

        if (method == null) {
            return null;
        }

        return new FrameworkMethod(method);
    }

    @SuppressWarnings("deprecation")
    private static void runTestMethod(NepticalJUnit4Runner runnerInstance, Class<?> testClass, String testMethodName)
            throws Throwable {
        try {
            runnerInstance.run(new RunNotifier());
            FrameworkMethod method = getFrameworkMethod(testClass, testMethodName);

            runnerInstance.methodBlock(method).evaluate();

        } catch (Exception e) {
            fail(e.getMessage() + " " + e.getCause());
        }
    }
}
