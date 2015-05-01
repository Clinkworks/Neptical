package com.clinkworks.neptical.test.modules;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.api.NepticalContext;
import com.clinkworks.neptical.util.Common;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class NepticalJUnitTestModules {

	public static final String DEFAULT_STRING = "I_AM_DEFAULT";
	public static final String STRING_ONE = "I_AM_STRING_ONE";
	public static final String STRING_TWO = "I_AM_STRING_TWO";
	public static final Integer DEFAULT_INTEGER = new Integer(1092);
	public static final Integer LEET_INTEGER = new Integer(1337);
	public static transient AssertionDependency ASSERTION_DEPENDENCY; 
	

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
	
    // Guice Test classes for test senarios
    // ----------------------------------------------------------------------------------------

    public static class MockTestThatNeedsSupportForNamedAnnotation {

        @Test
        @NepticalContext({ NamedModule.class, AssertionModule.class })
        public void testNamedConfiguration(@Named("String1") String string1, @Named("String2") String string2,
                AssertionDependency assertionDependency) {
            assertTrue(StringUtils.isNotBlank(string1));
            assertTrue(StringUtils.isNotBlank(string2));
            assertionDependency.assertEquals(NepticalJUnitTestModules.STRING_ONE, string1);
            assertionDependency.assertEquals(NepticalJUnitTestModules.STRING_TWO, string2);
        }
    }

    public static class MockTestThatEnsuresInstancesArePassedToTestMethods {

        private AssertionDependency assertionDependency;

        @Test
        @NepticalContext(AssertionModule.class)
        public void testMethodToTestInjectedDependencies(AssertionDependency assertionDependency) {
            this.assertionDependency = assertionDependency;
            assertionDependency.assertEquals("", "");
        }

        public AssertionDependency getAssertionDependency() {
            return assertionDependency;
        }
    }

    @NepticalContext({ AssertionModule.class, StringModule.class })
    public static class MockTestFullIntegrationMethodLevelClassLevelConfigurations {
        @Test
        @NepticalContext(IntegerModule.class)
        public void testMethodWithStringAndIntegerDependencys(String stringToInject, Integer integerToInject,
                AssertionDependency assertionDependency) {
            assertNotNull(integerToInject);
            assertTrue(StringUtils.isNotBlank(stringToInject));
            assertionDependency.assertEquals(NepticalJUnitTestModules.DEFAULT_INTEGER, integerToInject);
            assertionDependency.assertEquals(NepticalJUnitTestModules.DEFAULT_STRING, stringToInject);
        }
    }

    @NepticalContext(StringModule.class)
    public static class MockTestWithStringDependency {
        @Test
        public void testMethodWithStringDependency(String stringToInject) {
        }
    }

    public static class MockTestWithMethodParams {
        @Test
        public void testMethodWithDependency(String param) {
        }
    }

    @NepticalContext({ StringModule.class, AssertionModule.class })
    public static class MockTestWithBeforeMethod {
        @Before
        public void setup(String defaultString, AssertionDependency assertionDependency) {
            assertionDependency.assertEquals(NepticalJUnitTestModules.DEFAULT_STRING, defaultString);
        }

        @Test
        public void testMethodToEnsureBeforeMethodInjection() {
        }
    }

    @NepticalContext(AssertionModule.class)
    public static class MockTestRequiresUnConfiguredDependency {
        @Test
        public void testMethodWithStringDependency(String stringToInject, AssertionDependency assertionDependency) {
            assertionDependency.assertEquals("", stringToInject);
            assertTrue(StringUtils.isBlank(stringToInject));
        }
    }

    @NepticalContext({ AssertionModule.class, BoundIntegerModule.class })
    public static class MockTestWithBoundAnnotationDependency {

        @Test
        public void testMethodWithUnConfiguredIntegerAndBoundInteger(
                @BindingAnnotationForIntegerConfig Integer leetInteger, Integer nullInteger,
                AssertionDependency assertionDependency) {
            assertionDependency.assertEquals(NepticalJUnitTestModules.LEET_INTEGER, leetInteger);
            assertNull(nullInteger);
        }
    }
    
    @NepticalContext(MockTestModuleOne.class)
    public static class MockTestWithClassLevelGuiceConfig {
        @Test
        public void emptyTestMethod() {
        	Common.noOp();
        };
    }

    public static class MockTestWithMethodLevelGuiceConfig {
        @NepticalContext(MockTestModuleTwo.class)
        @Test
        public void annotatedTestMethod() {
        	Common.noOp();
        };
    }
}
