package com.clinkworks.neptical.service;

import java.io.Serializable;
import java.util.Map;

import com.clinkworks.neptical.datatype.Vocabulary;
import com.clinkworks.neptical.domain.Path;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class VocabularyService{

	private final Map<Class<? extends Serializable>, Vocabulary> pathTypeToVocabMap;
	
	@Inject
	VocabularyService(Map<Class<? extends Serializable>, Vocabulary> pathTypeToVocabMap){
		this.pathTypeToVocabMap = pathTypeToVocabMap;
	}
	
	public Vocabulary getVocabFor(Class<? extends Serializable> pathType){
		return pathTypeToVocabMap.get(pathType);
	}

	public Path parse(Serializable path) {
		Vocabulary vocabulary = getVocabFor(path.getClass());
		
		if(vocabulary == null){
			throw new UnsupportedOperationException("The vocabulary service is not configured to handle: " + path.getClass());
		}
		
		return vocabulary.parse(path);
	}
	
}
