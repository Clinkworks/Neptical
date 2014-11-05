package com.clinkworks.neptical.api;

import com.clinkworks.neptical.util.NepticalComponentFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ApplicationModuleBase extends AbstractModule{

	@Override
	protected void configure() {
		FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();
		configureFactoryModuleBuilder(factoryModuleBuilder);
		installNepticalComponentFactory(factoryModuleBuilder);
	}
	
	public void configureFactoryModuleBuilder(FactoryModuleBuilder factoryModuleBuilder){
		
	}
	
	protected <NCF extends NepticalComponentFactory> Class<NCF> initalizeNepticalWithNewComponentFactoryType(){
		return null;
	}
	
	private void installNepticalComponentFactory(FactoryModuleBuilder factoryModuleBuilder){
		configureFactoryModuleBuilder(factoryModuleBuilder);
		

		Class<? extends NepticalComponentFactory> componentFactoryType = initalizeNepticalWithNewComponentFactoryType();
		
		if(componentFactoryType == null){
			componentFactoryType = NepticalComponentFactory.class;
		}
		
		install(factoryModuleBuilder.build(componentFactoryType));
		
	}

}
