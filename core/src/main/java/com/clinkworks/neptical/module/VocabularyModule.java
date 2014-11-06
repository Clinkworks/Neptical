package com.clinkworks.neptical.module;

import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.datatype.Vocabulary;
import com.clinkworks.neptical.spi.GenericModuleTemplate;
import com.clinkworks.neptical.spi.Notation.DotNotation;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Provides;

public class VocabularyModule extends GenericModuleTemplate {

	@Override
	protected void configure() {};
	
	@Provides
	public Map<Class<? extends Serializable>, Vocabulary> provideVocabMap(){
		Map<Class<? extends Serializable>, Vocabulary> vocabMap = Maps.newHashMap();
		vocabMap.put(String.class, new DotNotation());
		return ImmutableMap.copyOf(vocabMap);
	}
	
}
