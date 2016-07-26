package clinkworks.neptical.domain;

import clinkworks.neptical.datatype.NepticalData;

public class GenericImmutableData implements NepticalData{

  private final Object data;

  public GenericImmutableData(Object data){
    this.data = data;
  }

  @Override
  public Object get(){
    return data;
  }

  @Override
  public Class<?> getDataType(){
    return data == null ? NepticalData.class : data.getClass();
  }

}
