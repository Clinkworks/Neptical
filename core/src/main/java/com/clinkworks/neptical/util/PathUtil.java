package com.clinkworks.neptical.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class PathUtil {

    public static final char DOT = '.';
    public static final String EMPTY_STRING = "";
    public static final char LEFT_BRACKET = '[';
    public static final char RIGHT_BRACKET = ']';
    public static final Pattern SPLIT_BY_DOT_PATTERN = Pattern.compile("\\.");
    public static final Pattern SEGMENT_CONTAINS_ARRAY_SYNTAX = Pattern.compile("^\\w+(?:\\[(\\d+)\\])+$");
    public static final Pattern ARRAY_SYNTAX_PATTERN = Pattern.compile("\\[(\\d+)\\]");

    //muhahaha no construction
    private PathUtil() {}

    public static final String firstSegment(String segment) {
        segment = clean(segment);
        int index = segment.indexOf(DOT);
        return index > -1 ? firstArraySegment(segment.substring(0, index)) : segment;
    }

    private static String firstArraySegment(String segment){
    	if(segment.charAt(0) == LEFT_BRACKET){
    		return getIndexAsString(segment);
    	}else{
    		return segment;
    	}
    }
    
    public static final String lastSegment(String segment) {
        segment = clean(segment);
        int index = segment.indexOf(DOT);
        return index > -1 ? segment.substring(index + 1, segment.length()) : "";
    }

    public static final String chompFirstSegment(String path){
    	String firstSegment = firstSegment(path);
    	path = path.substring(firstSegment.length());
    	return clean(path);
    }
    
    public static final String chompLastSegment(String path){
    	String lastSegment = lastSegment(path);
    	path = path.substring(0, path.length() - lastSegment.length());
    	return clean(path);
    }
    
    public static final String subtractSegment(String segment, String path) {
    	segment = clean(segment);
    	path = clean(path);
    	
    	if(segment.indexOf(LEFT_BRACKET) == 0){
    		segment = segment.substring(0, segment.indexOf(RIGHT_BRACKET) + 1);
    		path = path.substring(segment.length());
    	}else{
    		path = path.replace(segment, "");
    	}
    	
        return clean(path);
    }

    public static final boolean segmentContainsArraySyntax(String segment) {
        segment = clean(segment);
        
        if (segment.indexOf(DOT) != -1) {
            return false;
        }

        return SEGMENT_CONTAINS_ARRAY_SYNTAX.matcher(segment).find();
    }

    public static final String getIndexAsString(String segment){
    	Matcher matcher = ARRAY_SYNTAX_PATTERN.matcher(segment);
    	matcher.find();
    	return matcher.group(0);
    }
    
    public static final int getIndex(String segment) {
        if (segmentContainsArraySyntax(segment)) {
            return Integer.valueOf(ARRAY_SYNTAX_PATTERN.matcher(segment).group(1));
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

		return path;
    }

	public static String addSegment(String path, String segment) {

		path = clean(path);
		segment = firstSegment(segment);
		if(segment.indexOf('[') == 0) {
			path = path + segment;
		}else{
			path = path + DOT + segment;
		}
		
		return path;
	}
}
