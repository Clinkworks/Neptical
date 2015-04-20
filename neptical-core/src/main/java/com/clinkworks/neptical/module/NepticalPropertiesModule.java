package com.clinkworks.neptical.module;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.AbstractConfiguration;

import com.clinkworks.neptical.spi.GenericModuleTemplate;
import com.clinkworks.neptical.util.Common;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.netflix.config.ConfigurationManager;

public class NepticalPropertiesModule extends GenericModuleTemplate{
	
	private static final Pattern PROPERTY_FILE_PATTERN = Pattern.compile("(.*\\.properties$)");
	
	private final Properties nepticalProperties;
		
	public NepticalPropertiesModule() {
		nepticalProperties = loadProperties();
	}
	
	
	@Provides
	@Singleton
	public AbstractConfiguration archaiusConfiguration() throws IOException{
		System.setProperty("archaius.deployment.environment", "test");
		String project = System.getProperty("neptical.sprocket", "neptical");
		ConfigurationManager.loadCascadedPropertiesFromResources(project);
		return ConfigurationManager.getConfigInstance();
	}
	
	
	@Override
	protected void configure() {
		String defaultDataDirectory = nepticalProperties.getProperty("neptical-data");
		bind(File.class).annotatedWith(DataDirectory.class).toInstance(new File(defaultDataDirectory));
		Names.bindProperties(binder(), nepticalProperties);
	}
	
	private Properties loadProperties(){
		final Properties defaultProperties = new Properties();
		loadPropertyFiles(defaultProperties);
		defaultProperties.putAll(System.getProperties());
		defaultProperties.putAll(System.getenv());
		return new Properties(defaultProperties);
	}
	
	private void loadPropertyFiles(Properties properties){
		try {
			ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
			
			for(ResourceInfo resourceInfo : classPath.getResources()){
				Matcher matcher = PROPERTY_FILE_PATTERN.matcher(resourceInfo.getResourceName());
				if(matcher.matches()){
					URL url = resourceInfo.url();
					loadProperties(properties, url);
				}
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void loadProperties(Properties properties, URL propertiesFile) {
		InputStream inputStream = null;
		
		try {
			inputStream = propertiesFile.openStream();
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				Common.noOp("Eating exception");
			}
		}
		
	}
	
	@BindingAnnotation
	@Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
	public static @interface DataDirectory{
		String value() default "NEPTICAL-DATA";
	}
	
	
}