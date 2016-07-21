package clinkworks.neptical.util;


import static clinkworks.neptical.Constants.DOT;
import static clinkworks.neptical.Constants.EMPTY_STRING;
import static clinkworks.neptical.Constants.RegexPatterns.ARRAY_AT_END;
import static clinkworks.neptical.Constants.RegexPatterns.ARRAY_SYNTAX;
import static clinkworks.neptical.Constants.RegexPatterns.ARRAY_SYNTAX_PATTERN;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

public class PathUtil {

	private static final int NOT_FOUND = -1;
   
	public static String addSegment(String path, String segment) {

		path = clean(path);
		segment = clean(segment);
		
		String theFirst = firstSegment(segment);
	
		if(isArraySegment(theFirst) || path == EMPTY_STRING) {
			path = path + segment;
		}else{
			path = path + DOT + segment;
		}
		
		return copy(path);
	}
    
    public static final String firstSegment(String segment) {
    	 	
    	segment = clean(segment);

    	Matcher segmentEndsInArrayMatcher = ARRAY_SYNTAX.matcher(segment);
        Matcher isArrayIndexMatcher = ARRAY_SYNTAX_PATTERN.matcher(segment);
    	
    	if(segmentEndsInArrayMatcher.matches()){
    		//my[0].dotted = returns my;
    		return segmentEndsInArrayMatcher.group(1);
    	}

    	if(isArrayIndexMatcher.matches()){
    		return isArrayIndexMatcher.group(1);
    	}
        
        int nextSegmentIndex = segment.indexOf(DOT);
        
        return nextSegmentIndex > NOT_FOUND ? clean(segment.substring(0, nextSegmentIndex)) : segment;
    }
    
    public static final String lastSegment(String segment) {
    	
        segment = clean(segment);
        Matcher arrayAtEndMatcher = ARRAY_AT_END.matcher(segment);
        
        if(arrayAtEndMatcher.matches()){
        	return arrayAtEndMatcher.group(1);
        }
        
        int index = segment.lastIndexOf(DOT);
        
        return index > NOT_FOUND ? segment.substring(index + 1, segment.length()) : segment;
       
    }

    public static final String chompFirstSegment(String path){

    	path = clean(path);
    	
    	if(path == EMPTY_STRING){
    		return path;
    	}
    	
    	String firstSegment = firstSegment(path);
    	path = path.substring(firstSegment.length());
    	return clean(path);
    }
    
    public static final String chompLastSegment(String path){
    	path = clean(path);
    	
    	if(path == EMPTY_STRING){
    		return path;
    	}
    	
    	String lastSegment = lastSegment(path);
    	path = path.substring(0, path.length() - lastSegment.length());
    	return clean(path);
    }
    
    public static final String subtractSegment(String segment, String path) {
    	
    	segment = clean(segment);
    	path = clean(path);
    	
    	if(path.startsWith(segment)){
    		if(segment.equals(path)){
    			return EMPTY_STRING;
    		}
    		path = path.substring(segment.length());
    	}
    	
        return copy(clean(path));
    }

    public static final boolean isArraySegment(String segment){
    	return ARRAY_SYNTAX_PATTERN.matcher(segment).matches();
    }
    
    public static final boolean containsArraySyntax(String segment) {
        return ARRAY_SYNTAX.matcher(segment).matches();
    }
    
    public static final String getIndexAsString(String segment){
    	Matcher matcher = ARRAY_SYNTAX_PATTERN.matcher(segment);
    	matcher.find();
    	return matcher.group(1);
    }
    
    public static final int getIndex(String segment) {
        if (isArraySegment(segment)) {
        	Matcher matcher = ARRAY_SYNTAX_PATTERN.matcher(segment);
        	matcher.find();
            return Integer.parseInt(matcher.group(2));
        }
        return -1;
    }
    
    public static final String clean(String path) {
    	
    	if(StringUtils.isBlank(path)){
    		return EMPTY_STRING;
    	}
    	
    	path = stripDots(path);
    	
        return path;
    }
    
    private static final String stripDots(String path){
    	
    	path = path.trim();
    	
    	if(path.charAt(0) == DOT){
    		path = path.substring(1);
    	}
    	
    	if(path.charAt(path.length() - 1) == DOT){
    		path = path.substring(0, path.length() - 1);
    	}

    	//jvm, neptical, and a few supporting frameworks use alot of the substrings from some core
    	//properties, this is required to ensure a new string is created this class is heavley used in creating indexes.
    	//copying the string ensures safe hashing in many of nepticals ids
		return path == null ? null : copy(path);
    }
	
    private static String copy(String value) {
		return String.copyValueOf(value.toCharArray());
	}
}
