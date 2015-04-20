package com.clinkworks.neptical.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.domain.Path;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner.NepticalConfiguration;
import com.clinkworks.neptical.module.VocabularyModule;

@RunWith(NepticalJUnit4Runner.class)
@NepticalConfiguration(VocabularyModule.class)
public class VocabularyServiceSystemTest {
	
	@Test
	public void ensureVocabServiceCanParseDotNotation(VocabularyService vocabularyService){
		Path path = vocabularyService.parse("path.to.data");
		assertEquals(3, path.length());
	}

}
