package com.clinkworks.neptical.modules;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;

public class NepticalPropertiesModule extends AbstractModule{

	public static final File DEFAULT_DATA_DIRECTORY = new File(System.getProperty("user.home") + "/neptical-data");
	
	@BindingAnnotation
	@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
	public static @interface DataDirectory{}
	

	
	@Override
	protected void configure() {
		bind(File.class).
			annotatedWith(DataDirectory.class).
				toInstance(DEFAULT_DATA_DIRECTORY);
	}
}
