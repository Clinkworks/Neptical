package clinkworks.neptical.datatype;

public class LookupFailureException extends RuntimeException {

	private static final long serialVersionUID = -7424223503085954731L;

	public LookupFailureException(String message, Throwable e){
		super(message, e);
	}
	
}
