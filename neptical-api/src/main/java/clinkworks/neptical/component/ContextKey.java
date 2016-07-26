package clinkworks.neptical.component;

import java.net.URI;
import java.util.Objects;

import clinkworks.neptical.Constants.TextConstants;
import clinkworks.neptical.api.CursorContext;
import clinkworks.neptical.api.Location;
import clinkworks.neptical.datatype.NepticalData;

public class ContextKey implements Location{

  private final String contextName;
  private final Class<?> contextType;

  /** Simply a way to resolve specific context issues when using some inherited key types **/
  private final int partition;

  ContextKey(final int partitionId, final String contextName, final Class<?> contextType){
    this.contextName = contextName;
    this.contextType = contextType;
    partition = partitionId == -1 ? getClass().hashCode() : partitionId;
  }

  ContextKey(final String contextName, final Class<?> contextType){
    this(-1, contextName, contextType);
  }

  ContextKey(final URI contextIdentity){
    this(CursorContext.class.hashCode(), contextIdentity.toString(), CursorContext.class);
  }

  @Override
  public String context(){
    return null;
  }

  public Class<?> contextType(){
    return contextType;
  }

  @Override
  public boolean equals(final Object object){

    if (object == null || !(object instanceof ContextKey)) {
      return false;
    }

    final ContextKey that = (ContextKey)object;

    return that == this || name().equals(that.name()) && contextType().equals(that.contextType());
  }

  @Override
  public NepticalData getData(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int hashCode(){
    return Objects.hash(partition, name(), contextType());
  }

  @Override
  public String name(){
    return contextName;
  }

  @Override
  public String segment(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String template(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toString(){
    return contextName + TextConstants.DOT + partition + TextConstants.AT + contextType.getSimpleName();
  }
}
