package com.clinkworks.neptical.module;

import com.clinkworks.neptical.service.NepticalTemplateService;
import com.github.jknack.handlebars.Handlebars;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class NepticalHandlebarsModule extends AbstractModule{

	@Override
	protected void configure() {
		install(new NepticalPropertiesModule());
		install(new VocabularyModule());
		bind(NepticalTemplateService.class);
	}
	
	@Provides
	public Handlebars handlebars(){
		return new Handlebars();
	}

}
