package com.clinkworks.neptical.util;

import static com.clinkworks.neptical.util.PathUtil.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class PathUtilUnitTest {
    @Test
    public void allPathMethodsCanHandleAnEmptyAndNullString() {
        String path = "";

        assertEquals("", firstSegment(path));
        assertEquals("", firstSegment(null));
        assertEquals("", lastSegment(path));
        assertEquals("", lastSegment(null));
        assertEquals("", subtractSegment(null, ""));
        assertEquals("", subtractSegment(null, null));
        assertEquals("", subtractSegment("", null));
        assertEquals("", subtractSegment("", ""));
    }
    
    @Test
    public void dotsAtTheBeginingAndEndAreRemoved(){
    	String path = ".account.";
    	assertEquals("account", firstSegment(path));
    	path = ".account";
    	assertEquals("account", firstSegment(path));	
    	path = "account.";
    	assertEquals("account", firstSegment(path));
    }
    
    @Test
    public void canProperlyRetrieveTheFirstSegment(){
    	String path = ".account.firstName.";
    	assertEquals("account", firstSegment(path));
    }
    
    @Test
    public void canProperlyRetrieveTheLastSegment(){
    	String path = ".account.firstName.";
    	assertEquals("firstName", lastSegment(path));    
    }
    
    @Test
    public void canAssureArraySyntax(){
    	String segmentWithoutArray = "account.bills[0]";
    	String segmentWithArray = "bills[0]";
    	
    	//only segment that will be processed here is the first one.
    	assertFalse(segmentContainsArraySyntax(segmentWithoutArray));
    	assertTrue(segmentContainsArraySyntax(segmentWithArray));
    	
    }
    
    @Test
    public void canSubtractSegments(){
    	String segment1 = "account";
    	String segment2 = "account.firstName";
    	
    	assertEquals("firstName", subtractSegment(segment1, segment2));
    	assertEquals("", subtractSegment(segment1, segment1));
    }
    
    @Test
    public void canSubtractArrays(){
    	String segment1 = "[0]";
    	String segment2 = "[0][0]";
    	
    	assertEquals("[0]", subtractSegment(segment1, segment2));
    	assertEquals("", subtractSegment(segment1, segment1));
    }
}
