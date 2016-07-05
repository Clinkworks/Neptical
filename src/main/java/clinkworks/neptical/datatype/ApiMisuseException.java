package clinkworks.neptical.datatype;

public class ApiMisuseException extends RuntimeException{

	private static final long serialVersionUID = -8907007373273213339L;
	
	public ApiMisuseException(String message){
		super(message);
	}
	
	public ApiMisuseException(String message, Throwable e){
		super(message, e);
	}

}
