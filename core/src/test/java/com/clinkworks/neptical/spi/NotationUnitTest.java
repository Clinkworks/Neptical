package com.clinkworks.neptical.spi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.clinkworks.neptical.domain.Path;
import com.clinkworks.neptical.spi.Notation.DotNotation;

public class NotationUnitTest {

	@Test
	public void dotNotationCanHandleSimplePaths(){
		String userInput = "path.to.my.data";
				
		Path parsedPath = new DotNotation().parse(userInput);
		assertEquals(4, parsedPath.length());
		assertEquals(userInput, parsedPath.getSegment(3).getPublicId().get());
		assertEquals("path.to", parsedPath.getSegment(1).getPublicId().get());
		assertEquals("my", parsedPath.getSegment(2).getName());
	}
	
}
