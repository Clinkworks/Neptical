package com.clinkworks.neptical.data.loaders.json;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import com.clinkworks.neptical.data.Data;
import com.google.common.collect.Maps;
import com.google.inject.BindingAnnotation;

public interface DataLoader {
	
	public static final Map<String, DataLoader> REGISTERED_EXTENSIONS = Maps.newConcurrentMap();
	
	@BindingAnnotation 
	@Target({ FIELD, PARAMETER, METHOD }) 
	@Retention(RUNTIME)
	public @interface TestData{
		Class<?> dataManager() default JsonDataManager.class;
		String filePath() default "classpath:data/";
	}
	
	
}
