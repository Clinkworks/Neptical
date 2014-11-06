package com.clinkworks.neptical.module;

import com.clinkworks.neptical.spi.ApplicationModuleTemplate;
import com.clinkworks.neptical.spi.NepticalComponentFactory;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class NepticalComponentModule extends ApplicationModuleTemplate{
	
	@Override
	protected void configureApplication(){
	}
	
	@Override
	protected void configureFactoryModuleBuilder(FactoryModuleBuilder factoryModuleBuilder) {
	}

	@Override
	protected <NCF extends NepticalComponentFactory> Class<NCF> initalizeNepticalWithComponentFactorySubType() {
		return null;
	}

	
}
