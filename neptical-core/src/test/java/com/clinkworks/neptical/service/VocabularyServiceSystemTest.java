package com.clinkworks.neptical.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.clinkworks.neptical.domain.Path;
import com.clinkworks.neptical.module.VocabularyModule;
import com.clinkworks.neptical.util.GuiceInjectionUtil;
import com.google.inject.Injector;

public class VocabularyServiceSystemTest {
	
	@Test
	public void ensureVocabServiceCanParseDotNotation(){
		
		Injector injector = GuiceInjectionUtil.createInjector(VocabularyModule.class);
		Path path = injector.getInstance(VocabularyService.class).parse("path.to.data");
		assertEquals(3, path.length());
		
	}

}
