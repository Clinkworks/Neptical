package clinkworks.neptical;

import java.util.regex.Pattern;

public class Constants {

    public static final char DOT = '.';
    public static final String EMPTY_STRING = "";
    public static final char LEFT_BRACKET = '[';
    public static final char RIGHT_BRACKET = ']';
	
	public static class RegexPatterns{
	    public static final Pattern ARRAY_SYNTAX = Pattern.compile("(^\\w+)(?:(\\[(\\d+)\\])).*");
	    public static final Pattern ARRAY_SYNTAX_PATTERN = Pattern.compile("^(\\[(\\d+)\\]).*");
	    public static final Pattern ARRAY_AT_END = Pattern.compile(".*(\\[(\\d+)\\])+$");
	    public static final Pattern IS_FLOATING_NUMBER_PATTERN = Pattern.compile("[-+]?(\\d*[.])?\\d+");
	    public static final Pattern IS_INTEGER_PATTERN = Pattern.compile("[-+]?\\d+");
	}
	
}
