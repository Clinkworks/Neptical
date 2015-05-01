package com.clinkworks.neptical.service;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;

import com.clinkworks.neptical.domain.JsonData;
import com.clinkworks.neptical.module.NepticalPropertiesModule.NepticalProperties;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.inject.Singleton;

@Singleton
public class NepticalTemplateService {
	
	private final Handlebars handlebars;
	private final Properties properties;

	@Inject
	NepticalTemplateService(Handlebars handlebars, @NepticalProperties Properties properties){
		this.handlebars = handlebars;
		this.properties = properties;
	}

	//NOTE: this method will change drastically as the graph api is updated.
	@SuppressWarnings("unchecked")
	public <T> T resolve(Object data){
		JsonData jsonData = new JsonData(data);
		String input = resolve(jsonData.getAsString());
		return (T)new JsonData(input).getAs(data.getClass());
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
