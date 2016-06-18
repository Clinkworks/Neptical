package com.clinkworks.neptical.spi;

import com.google.inject.assistedinject.FactoryModuleBuilder;

public abstract class ApplicationModuleTemplate extends GenericModuleTemplate{

	/**
	 * if you choose to bind the factory to other implementations you can configure the binder using
	 * this method. Typically you can leave this method body empty
	 * @param factoryModuleBuilder
	 */
	protected abstract void configureFactoryModuleBuilder(FactoryModuleBuilder factoryModuleBuilder);
	
	/**
	 * @return null for the default factory, or a your own sub interface of the factory
	 */
	protected abstract <NCF extends NepticalComponentFactory> Class<NCF> initalizeNepticalWithComponentFactorySubType();
	
	protected abstract void configureApplication();
	
	@Override
	final protected void configure() {
		FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();
		configureFactoryModuleBuilder(factoryModuleBuilder);
		installNepticalComponentFactory(factoryModuleBuilder);
		configureApplication();
	}
	

	private final void installNepticalComponentFactory(FactoryModuleBuilder factoryModuleBuilder){
		configureFactoryModuleBuilder(factoryModuleBuilder);

		Class<? extends NepticalComponentFactory> componentFactoryType = initalizeNepticalWithComponentFactorySubType();
		
		if(componentFactoryType == null){
			componentFactoryType = NepticalComponentFactory.class;
		}
		
		install(factoryModuleBuilder.build(componentFactoryType));
		
	}

}
