package clinkworks.neptical.component;

import java.net.URI;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang.StringUtils;

import clinkworks.neptical.api.CursorContext;
import clinkworks.neptical.api.Location;
import clinkworks.neptical.api.Segment;
import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.NepticalContext;
import clinkworks.neptical.datatype.NepticalData;
import clinkworks.neptical.util.NepticalComponentFactory;

public class CursorLocation implements Location, CursorContext{

  private final Location location;
  private final CursorContext self;
  private final NepticalComponentFactory nepticalComponentFactory;
  private final Provider<Cursor> cursorProvider;

  @Inject
  CursorLocation(final NepticalComponentFactory nepticalComponentFactory, final Provider<Cursor> cursorProvider, final String context, final String fragment, final String name){
    self = nepticalComponentFactory.createContextKey(context, CursorLocation.class);
    segment = fragment;
    this.name = name;
    this.cursorProvider = cursorProvider;
    this.nepticalComponentFactory = nepticalComponentFactory;
  }

  @Override
  public String context(){
    return context;
  }

  @Override
  public NepticalData getData(){
    final Location previousLocation = cursor().getLocation();
    cursor().moveTo(this);

    final NepticalData nepticalData = cursor().getData();

    cursor().moveTo(previousLocation);

    return nepticalData;
  }

  @Override
  public Location getLocation(){
    // TODO Auto-generated method stub
    return null;
  }

  public URI getResourceIdentity(){

    String actualName = name();

    if (!StringUtils.isBlank(actualName) && !actualName.equals("/") && !fragment().endsWith("/")) {
      actualName = "." + actualName;
    }

    final String uri = context() + "/" + fragment() + actualName;
    return URI.create(uri.replace(" ", "%20"));
  }

  @Override
  public Cursor moveCursorHere(){
    return cursor().moveTo(this);
  }

  @Override
  public String name(){
    return name;
  }


  @Override
  public String segment(){
    return segment;
  }

  @Override
  public Segment[] segments(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String template(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NepticalContext[] templates(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toString(){
    return getResourceIdentity().toString().replace("%20", " ");
  }

  private Cursor cursor(){
    return cursorProvider.get();
  }
}
