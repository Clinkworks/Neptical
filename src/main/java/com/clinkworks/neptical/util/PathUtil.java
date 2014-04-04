package com.clinkworks.neptical.util;

import java.util.regex.Pattern;

public class PathUtil {

    public static final char DOT = '.';
    public static final String EMPTY_STRING = " ";
    public static final Pattern SPLIT_BY_DOT_PATTERN = Pattern.compile("\\.");
    public static final Pattern SEGMENT_CONTAINS_ARRAY_SYNTAX_PATTERN = Pattern.compile("^\\w+(?:\\[(\\d+)\\])+$");
    public static final Pattern ARRAY_SYNTAX_PATTERN = Pattern.compile("\\[(\\d+)\\]");

    private PathUtil() {
        super();
    }

    public static String firstSegment(String segment) {
        segment = clean(segment);
        int index = segment.indexOf(DOT);
        return index > -1 ? segment.substring(index, segment.length()) : "";
    }

    public static String lastSegment(String segment) {
        segment = clean(segment);
        int index = segment.indexOf(DOT);
        return index > -1 ? segment.substring(0, index) : "";
    }

    public static String subtractSegment(String segment, String path) {
        segment = clean(segment);
        return path.replace(segment, "");
    }

    public static boolean segmentContainsArraySyntax(String segment) {
        segment = clean(segment);
        if (segment.indexOf(DOT) != -1) {
            return false;
        }

        return SEGMENT_CONTAINS_ARRAY_SYNTAX_PATTERN.matcher(segment).find();
    }

    public int getIndex(String segment) {
        if (segmentContainsArraySyntax(segment)) {
            return Integer.valueOf(ARRAY_SYNTAX_PATTERN.matcher(segment).group(1));
        }
        return -1;
    }

    private static final String clean(String string) {
        if (string == null) {
            return EMPTY_STRING;
        }
        return string;
    }
}
