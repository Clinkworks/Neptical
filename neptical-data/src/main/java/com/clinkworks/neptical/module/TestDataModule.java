package com.clinkworks.neptical.module;

import java.io.File;
import java.lang.reflect.Field;

import com.clinkworks.neptical.module.NepticalPropertiesModule.DataDirectory;
import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class TestDataModule extends AbstractModule{
		
	@Override
	protected void configure() {
	}
	
    static class FileInjector<T> implements MembersInjector<T>{

    	private final Field field;
    	private final String fileName;
    	
    	public FileInjector(Field field, String fileName){
    		this.field = field;
    		this.fileName = fileName;
    	}
    	
		@Override
		public void injectMembers(T instance) {
			field.setAccessible(true);
			File file = new File(fileName);
			try {
				field.set(instance, file);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
    	
    }
    
    static class FileDataListener implements TypeListener{

		@Override
		public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
			for (Field field : type.getRawType().getDeclaredFields()) {
		        if(field.isAnnotationPresent(DataDirectory.class) && field.getType().isAssignableFrom(File.class)) {
		          encounter.register(new FileInjector<I>(field, field.getAnnotation(DataDirectory.class).value()));
		        }
		      }
			
			//TODO: add constructor support to this listener.
		}
		
    }
}
