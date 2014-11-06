package com.clinkworks.neptical.module;

import com.google.inject.AbstractModule;

public class TestDataModule extends AbstractModule{

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		
	}
	
//	public static final NepticalData TEST_DATA;
//	
//	static{
//		File dataDir = new File(Thread.currentThread().getContextClassLoader().getResource("data").getFile().replace("%20", " "));
//		TEST_DATA = new JsonFileNode(null, null, null, null, dataDir);		
//	}
//	
//	@Override
//	protected void configure() {
//		bindListener(Matchers.any(), new TestDataListener());
//	}
//	
//    public static class TestDataInjector<T> implements MembersInjector<T>{
//
//    	private final Field field;
//    	private final TestData anno;
//    	public TestDataInjector(Field field, TestData anno){
//    		this.field = field;
//    		this.anno = anno;
//    	}
//    	
//		@Override
//		public void injectMembers(T instance) {
//			field.setAccessible(true);
//			NepticalData dataToInject = TEST_DATA.find(anno.value());
//			try {
//				field.set(instance, dataToInject);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			} 
//		}
//    	
//    }
//    
//    public static class TestDataListener implements TypeListener{
//
//		@Override
//		public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
//			for (Field field : type.getRawType().getDeclaredFields()) {
//		        if (NepticalData.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(TestData.class)) {
//		          encounter.register(new TestDataInjector<I>(field, field.getAnnotation(TestData.class)));
//		        }
//		      }
//			
//		}
//		
//    }
}
