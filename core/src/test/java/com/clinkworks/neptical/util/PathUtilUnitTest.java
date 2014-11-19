package com.clinkworks.neptical.util;

import static com.clinkworks.neptical.util.PathUtil.addSegment;
import static com.clinkworks.neptical.util.PathUtil.containsArraySyntax;
import static com.clinkworks.neptical.util.PathUtil.firstSegment;
import static com.clinkworks.neptical.util.PathUtil.lastSegment;
import static com.clinkworks.neptical.util.PathUtil.subtractSegment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        assertEquals("", addSegment("", null));
        assertEquals("", addSegment(null, ""));
    }
    
    
    @Test
    public void reverseTest(){
        Integer[] ints = {1, 2, 3, 4};
        Integer[] reversedInts = reverse(ints);
        assertEquals(Integer.valueOf(1), reversedInts[3]);
        assertEquals(Integer.valueOf(4), reversedInts[0]);
        
        String[] strings = {"Hello", "World", "This", "Last", "is", "First"};
        
        strings = reverse(strings);
        
        assertEquals("First", strings[0]);
    }

    public static <T> T[] reverse(T[] arrayToReverse){
       List<T> listOfType = Arrays.asList(arrayToReverse);
       Collections.reverse(listOfType);
       return (T[])listOfType.toArray();
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
    	assertFalse(containsArraySyntax(segmentWithoutArray));
    	assertTrue(containsArraySyntax(segmentWithArray));
    	
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
    
    @Test
    public void canAppendPaths(){
    	String path = "account";
    	String segment = "transactions";
    	
    	String startingPath = "account.transactions[0][1]";
    	String expectedComplexPath = "account.transactions[0][1].value";
    	
    	assertEquals("account.transactions", addSegment(path, segment));
    	assertEquals("account[0]", addSegment(path, "[0]"));
    	assertEquals("account[0][1]", addSegment(addSegment(path, "[0]"), "[1]"));
    	assertEquals(expectedComplexPath, addSegment(startingPath, "value"));
    }
}
