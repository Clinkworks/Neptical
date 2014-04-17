package com.clinkworks.neptical.modules;

import java.io.File;
import java.lang.reflect.Field;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.DataLoader.TestData;
import com.clinkworks.neptical.data.file.FileData;
import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class TestDataModule extends AbstractModule{
	
	public static final Data TEST_DATA;
	
	static{
		File dataDir = new File(Thread.currentThread().getContextClassLoader().getResource("data").getFile().replace("%20", " "));
		TEST_DATA = new FileData(null, null, null, null, dataDir);		
	}
	
	@Override
	protected void configure() {
		bindListener(Matchers.any(), new TestDataListener());
	}
	
    public static class TestDataInjector<T> implements MembersInjector<T>{

    	private final Field field;
    	private final TestData anno;
    	public TestDataInjector(Field field, TestData anno){
    		this.field = field;
    		this.anno = anno;
    	}
    	
		@Override
		public void injectMembers(T instance) {
			field.setAccessible(true);
			Data dataToInject = TEST_DATA.find(anno.value());
			try {
				field.set(instance, dataToInject);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
    	
    }
    
    public static class TestDataListener implements TypeListener{

		@Override
		public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
			for (Field field : type.getRawType().getDeclaredFields()) {
		        if (Data.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(TestData.class)) {
		          encounter.register(new TestDataInjector<I>(field, field.getAnnotation(TestData.class)));
		        }
		      }
			
		}
		
    }
}
