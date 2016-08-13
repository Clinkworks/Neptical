package clinkworks.neptical;

import java.util.regex.Pattern;

public class Constants {

    public static final char DOT = '.';
    public static final String EMPTY_STRING = "";
    public static final char LEFT_BRACKET = '[';
    public static final char RIGHT_BRACKET = ']';

    /**
     * Represents the symbols used when parsing a context key
     * 
     * example:
     * <pre>neptical:/my.application.datatype.User@</pre>
     *
     */
    public static class ContextSymbols{
    	public static final char PATH_SEPARATOR = DOT;
    	public static final char IN_PARTITION = ':';
    	public static final char CONTEXT_SEPARATOR = '/';
    	public static final char ROOT_CONTEXT = CONTEXT_SEPARATOR;
    	
    	public static final char WITH_FRAGMENT = '#';
    	public static final char IN_CONTEXT = '@';

    	public static final String SYSTEM_CONTEXT = "neptical" + IN_PARTITION + ROOT_CONTEXT + CONTEXT_SEPARATOR;
    	public static final String DEFAULT_CONTEXT = SYSTEM_CONTEXT + "default" + CONTEXT_SEPARATOR;
    }
    
	public static class RegexPatterns{
	    public static final Pattern ARRAY_SYNTAX = Pattern.compile("(^\\w+)(?:(\\[(\\d+)\\])).*");
	    public static final Pattern ARRAY_SYNTAX_PATTERN = Pattern.compile("^(\\[(\\d+)\\]).*");
	    public static final Pattern ARRAY_AT_END = Pattern.compile(".*(\\[(\\d+)\\])+$");
	    public static final Pattern IS_FLOATING_NUMBER_PATTERN = Pattern.compile("[-+]?(\\d*[.])?\\d+");
	    public static final Pattern IS_INTEGER_PATTERN = Pattern.compile("[-+]?\\d+");
	}
	
}
