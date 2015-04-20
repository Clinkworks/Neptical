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
	
	@Override
	protected void configure() {
		
		AbstractConfiguration archaius = configureArchaius();
		String defaultDataDirectory = archaius.getString("neptical.data.default", "src/main/resources/neptical");
		System.out.println(new File(".").getAbsolutePath());
		System.out.println(new File(defaultDataDirectory).exists());
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
		String value() default "src/test/resources";
	}
	
	
}