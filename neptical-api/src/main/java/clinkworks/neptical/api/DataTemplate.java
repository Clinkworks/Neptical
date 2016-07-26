package clinkworks.neptical.api;

import java.util.List;

import clinkworks.neptical.component.ContextKey;
import clinkworks.neptical.component.TemplateContext;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.NepticalData;

public class DataTemplate implements DataModule{

  private final ContextKey contextKey;
  private final TemplateContext templateContext;
  private final DataModule internalModule;


  @Override
  public void addData(final String segment, final NepticalData data) throws DataDefinitionException{
    // TODO Auto-generated method stub

  }

  @Override
  public List<NepticalData> getAllData(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CursorContext getCursorContext(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<NepticalData> getDataAt(final String segment){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NepticalData getDataAt(final String segment, final int index){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getName(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] segments(){
    // TODO Auto-generated method stub
    return null;
  }


}
