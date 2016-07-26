package clinkworks.neptical.datatype;

public class DataDefinitionException extends Exception{

  private static final long serialVersionUID = -9025349701047641000L;

  public DataDefinitionException(String message){
    super(message);
  }

  public DataDefinitionException(String message, Throwable e){
    super(message, e);
  }


}
