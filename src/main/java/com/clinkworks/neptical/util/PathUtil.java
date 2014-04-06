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
    public static final Pattern SEGMENT_CONTAINS_ARRAY_SYNTAX_PATTERN = Pattern.compile("^\\w+(?:\\[(\\d+)\\])+$");
    public static final Pattern ARRAY_SYNTAX_PATTERN = Pattern.compile("\\[(\\d+)\\]");

    private PathUtil() {
        super();
    }

    public static String firstSegment(String segment) {
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
    
    public static String lastSegment(String segment) {
        segment = clean(segment);
        int index = segment.indexOf(DOT);
        return index > -1 ? segment.substring(index + 1, segment.length()) : "";
    }

    public static String chompLastSegment(String path){
    	String lastSegment = lastSegment(path);
    	path = path.substring(0, path.length() - lastSegment.length());
    	return clean(path);
    }
    
    public static String subtractSegment(String segment, String path) {
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

    public static boolean segmentContainsArraySyntax(String segment) {
        segment = clean(segment);
        
        if (segment.indexOf(DOT) != -1) {
            return false;
        }

        return SEGMENT_CONTAINS_ARRAY_SYNTAX_PATTERN.matcher(segment).find();
    }

    public static String getIndexAsString(String segment){
    	Matcher matcher = ARRAY_SYNTAX_PATTERN.matcher(segment);
    	matcher.find();
    	String match = matcher.group(0);
    	return matcher.group(0);
    }
    
    public static int getIndex(String segment) {
        if (segmentContainsArraySyntax(segment)) {
            return Integer.valueOf(ARRAY_SYNTAX_PATTERN.matcher(segment).group(1));
        }
        return -1;
    }
    
    private static final String stripDotsFromStartAndEndOfPath(String path){

    	if(path.charAt(0) == DOT){
    		path = path.substring(1);
    	}
    	if(path.charAt(path.length() - 1) == DOT){
    		path = path.substring(0, path.length() - 1);
    	}

		return path;
    }

    public static final String clean(String path) {
    	
    	if(StringUtils.isBlank(path)){
    		return EMPTY_STRING;
    	}
    	
    	path = stripDotsFromStartAndEndOfPath(path).trim();
        return path;
    }
}
