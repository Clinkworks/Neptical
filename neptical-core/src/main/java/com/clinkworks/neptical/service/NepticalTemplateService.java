package com.clinkworks.neptical.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.AbstractConfiguration;

import com.clinkworks.neptical.module.NepticalPropertiesModule.NepticalProperties;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NepticalTemplateService {
	
	private final Handlebars handlebars;
	private final Properties properties;
	private final VocabularyService vocabularyService;
	
	@Inject
	NepticalTemplateService(Handlebars handlebars, @NepticalProperties Properties properties, VocabularyService vocabularyService){
		this.handlebars = handlebars;
		this.properties = properties;
		this.vocabularyService = vocabularyService;
	}

	public String resolve(String input) {
		
		try {
			Template template = handlebars.compileInline(input);
			String resolved = template.apply(properties);
			return resolved;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	
}
