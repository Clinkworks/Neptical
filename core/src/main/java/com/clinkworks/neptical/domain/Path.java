package com.clinkworks.neptical.domain;

import com.clinkworks.neptical.datatype.Vocabulary;

public class Path {

	private final Vocabulary vocabulary;
	
	public Path(Vocabulary vocabulary){
		this.vocabulary = vocabulary;
	}
	
	public Vocabulary getVocabulary(){
		return vocabulary;
	}
}
