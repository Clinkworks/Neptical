package com.clinkworks.neptical.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.clinkworks.neptical.api.NepticalContext;
import com.google.common.collect.Lists;
import com.google.inject.BindingAnnotation;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.ProvisionException;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class GuiceInjectionUtil {
	
	private static final Logger LOGGER = Logger.getLogger(GuiceInjectionUtil.class);
	
	private static final String CREATING_GUICE_INJECTOR = " ---- Creating Guice Injector ---- ";
	private static final String CREATING_CLASS_DEBUG_MESSAGE = " ---- Creating Guice Module: %s ---- ";
	private static final String CONSTRUCTING_INJECTOR_DEBUG_MESSAGE = " ---- Instancating Guice Injector with modules: %s ---- ";
	private static final String FINISHED_CONSTRUCTING_INJECTOR = " ---- Guice injector created, enjoy! ---- ";

	private GuiceInjectionUtil(){
		Common.noOp("This class is designed to be used as a utility");
	}
	
    static List<Object> getParameterValuesToInject(Method method, Injector injector){
    	
		List<Object> params = new ArrayList<Object>();
    	
    	if(method == null){
    		return params;
    	}
    	
		Class<?>[] parameterTypes = method.getParameterTypes();
		
		for(int i = 0; i < parameterTypes.length; i++){
			try{
				Object dependency = createInstance(injector, method, i);
				params.add(dependency);
			}catch(ProvisionException e){
				params.add(null);
			}
		}
		
		return params;
    }    
    
    private static Object createInstance(Injector injector, Method method, int parameterIndex){
    	Annotation boundAnnotation = getBoundAnnotation(method.getParameterAnnotations()[parameterIndex]);
    	Class<?> parameterType = method.getParameterTypes()[parameterIndex];
    	
    	if(boundAnnotation == null){
    		try{
    			return injector.getInstance(parameterType);
    		}catch(ConfigurationException e){
    			LOGGER.warn(String.format("The parameter in method %s at index %s is set to null, please configure Neptical for this dependency (%s)",
    					method.getName(), parameterIndex, parameterType));
    			return null;
    		}
    	}
    	
    	if(boundAnnotation.annotationType().isAssignableFrom(Named.class)){
    		String namedValue = ((Named)boundAnnotation).value();
    		return injector.getInstance(Key.get(parameterType, Names.named(namedValue)));
    	}
    	
    	return injector.getInstance(Key.get(parameterType, boundAnnotation));
    }
    
    static Injector createInjectorWithConfiguredModules(Class<?> junitTestClass, Method method){
    	
    	LOGGER.debug(" ---- Gathering test modules ---- ");
    	Class<? extends Module>[] classModules = getConfiguredContextModules(junitTestClass);
    	Class<? extends Module>[] methodModules = getConfiguredTestModules(method);
        
    	if(methodModules.length == 0 && classModules.length > 0){
    		return createInjector(classModules);
    	}
    	
    	if(methodModules.length > 0 && classModules.length == 0){
    		return createInjector(methodModules);
    	}
    	
    	Class<? extends Module>[] testConfig = mergeConfigs(classModules, methodModules);
    	
    	return createInjector(testConfig);
    	
    }
 
    public static Class<? extends Module>[] getConfiguredContextModules(Class<?> context){
    	LOGGER.debug(" ---- Discovering context level guice configuration ---- ");
    	NepticalContext classConfig = context.getAnnotation(NepticalContext.class);
    	return classConfig == null ? getNewEmptyModuleArray() : classConfig.value();
    }
    
    public static Class<? extends Module>[] getConfiguredTestModules(Method frameworkMethod){
    	
    	LOGGER.debug(" ---- Discovering test level guice configuration ---- ");
    	
    	NepticalContext annotation = null;
    	
    	if(frameworkMethod != null){
    		annotation = frameworkMethod.getAnnotation(NepticalContext.class);
    	}
        
        return annotation == null ? getNewEmptyModuleArray() : annotation.value();
    }
    
	@SuppressWarnings("unchecked")
	public static Injector createInjector(Class<? extends Module>... modules){
		
		LOGGER.debug(CREATING_GUICE_INJECTOR);
		
		Module[] moduleList = createModules(modules);
		
		logLoadedModules(modules);
		
		Injector injector = Guice.createInjector(moduleList);
		
		LOGGER.debug(FINISHED_CONSTRUCTING_INJECTOR);
		
		return injector;
	}
	
	public static Module[] createModules(Class<? extends Module>[] modules){
		List<Module> moduleList = Lists.newArrayList();
		
		for(Class<? extends Module> module : modules){
			moduleList.add(createModule(module));
		}
				
		Module[] retval = new Module[moduleList.size()];
		
		for(int i = 0; i < moduleList.size(); i++){
			retval[i] = moduleList.get(i);
		}
		
		return retval;
	}
	
	public static Module createModule(Class<? extends Module> module){
		
		if(module == null){
			throw new NullPointerException("InjectionHelper.createModule() was given a null module");
		}
		
		if(!hasAccessableEmptyConstructor(module)){
			throw new IllegalArgumentException("Module " + module.getName() + " does not have an accessable constructor");
		}
		
		Module retval = instancateModule(module);
		
		return retval;
	}
	
	protected static boolean hasAccessableEmptyConstructor(Class<?> module){;
		
		int constructorCount = 0;
		
		for(Constructor<?> constructor : module.getConstructors()){
			if(constructor.getParameterTypes().length == 0){
				if(constructorIsAccessable(constructor)){
					constructorCount++;
					if(constructorCount > 1){
						return false;
					}
				}
			}
		}
		
		if(constructorCount == 1){
			return true;
		}
		
		return false;
		
	}
	
	protected static boolean constructorIsAccessable(Constructor<?> constructor){
		
		int constructorModifiers = constructor.getModifiers();
		
		if(Modifier.isPublic(constructorModifiers)){
			return true;
		}
		
		if(Modifier.isProtected(constructorModifiers)){
			Package thisPackage = GuiceInjectionUtil.class.getPackage();
			Package modulePackage = constructor.getDeclaringClass().getPackage();
			return thisPackage.equals(modulePackage);
		}
	
		return false;
	}
	
	private static Module instancateModule(Class<? extends Module> module){
		
		Module retval = null;
		
		try {
			LOGGER.trace(format(CREATING_CLASS_DEBUG_MESSAGE, module.toString()));
			retval = module.newInstance();
		} catch (Exception e){
			String message = format("Module %s failed in construction.", module.getName());
			LOGGER.debug(message);
			throw new RuntimeException(message);
		}
		
		return retval;
	}
	
	private static void logLoadedModules(Class<? extends Module>[] modules) {
		
		if(modules.length == 0){
			return;
		}
		
		LOGGER.debug(format(CONSTRUCTING_INJECTOR_DEBUG_MESSAGE, Arrays.toString(modules)));
	}	    
    
    private static final Class<? extends Module>[] mergeConfigs(Class<? extends Module>[] classLevelConfigs, Class<? extends Module>[] methodLevelConfigs){
    	int classLevelSize = getArraySize(classLevelConfigs);
    	int methodLevelSize = getArraySize(methodLevelConfigs);
    	
    	int arrayLength = classLevelSize + methodLevelSize;
    	
    	Class<? extends Module>[] retval = getNewClassArrayWithLength(arrayLength);
    	
    	int i = 0;
    	
    	if(classLevelSize > 0){
    		for(Class<? extends Module> config : classLevelConfigs){
    			retval[i] = config;
    			i++;
    		}
    	}

    	if(methodLevelSize > 0){
    		for(Class<? extends Module> config : methodLevelConfigs){
    			retval[i] = config;
    			i++;
    		}
    	}
    
    	return retval;
    }

    @SuppressWarnings("unchecked")
    private static final Class<? extends Module>[] getNewClassArrayWithLength(int length){
    	return new Class[length];
    }
    
    @SuppressWarnings("unchecked")
	private static final Class<? extends Module>[] getNewEmptyModuleArray(){
		return new Class[0];
    }
    
    private static Annotation getBoundAnnotation(Annotation[] annotationsForParameter){
    	for (Annotation annotation : annotationsForParameter) {
    		if(annotation.annotationType().getAnnotation(BindingAnnotation.class) != null){
    			return annotation;
    		}
    	}
    	return null;
    }
    
    private static final int getArraySize(Object[] array){
    	return array == null ? 0 : array.length;
    }
    
	private static String format(String message, String... messageParams){
		return String.format(message, (Object[])messageParams);
	}
}
