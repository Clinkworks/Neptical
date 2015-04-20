package com.clinkworks.neptical.module;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;

import org.apache.commons.configuration.AbstractConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.name.Names;
import com.netflix.config.ConfigurationManager;

public class NepticalPropertiesModule extends AbstractModule{
	
	private static final String DEFAULT_DATA_PROPERTY = "neptical.data.default";
	private static final String DEFAULT_DATA_DIRECTORY = "src/main/resources/neptical";
	
	@Override
	protected void configure() {
		
		AbstractConfiguration archaius = configureArchaius();
		String defaultDataDirectory = archaius.getString(DEFAULT_DATA_PROPERTY, DEFAULT_DATA_DIRECTORY);
		bind(File.class).annotatedWith(DataDirectory.class).toInstance(new File(defaultDataDirectory));
		
		final Properties properties = new Properties();
		
		archaius.getKeys().forEachRemaining(
			(key) -> {
				Object value = archaius.getProperty(key);
				if(value == null){
					System.out.println("null property: " + key);
				}else{
					properties.put(key, value.toString());
				}
			}
		);
		
		Names.bindProperties(binder(), properties);
	}
	
	private AbstractConfiguration configureArchaius() {
		
		System.setProperty("archaius.deployment.environment", "test");
		String project = System.getProperty("neptical.project", "neptical");
		
		try {
			ConfigurationManager.loadCascadedPropertiesFromResources(project);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return ConfigurationManager.getConfigInstance();
	}
		
	@BindingAnnotation
	@Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
	public static @interface DataDirectory{
		String value() default DEFAULT_DATA_DIRECTORY;
	}
	
	
}