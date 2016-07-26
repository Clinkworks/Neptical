package clinkworks.neptical.datatype;

public interface NepticalData{

  public static final NepticalData NULL_DATA = new NullData();

  public static final class NullData implements NepticalData{

    private NullData(){
    }

    @Override
    public Object get(){

      return null;
    }

    @Override
    public Class<?> getDataType(){

      return NullData.class;
    }
  }

  Object get();

  Class<?> getDataType();

}
