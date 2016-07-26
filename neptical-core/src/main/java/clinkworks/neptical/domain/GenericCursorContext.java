package clinkworks.neptical.domain;

import java.net.URI;

import javax.inject.Provider;

import clinkworks.neptical.api.CursorContext;
import clinkworks.neptical.api.DataModule;
import clinkworks.neptical.api.Location;
import clinkworks.neptical.component.CursorProvider;
import clinkworks.neptical.component.NSpaceManager;
import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.NepticalData;

public class GenericCursorContext implements Provider<Cursor>, Location, CursorContext{

  private final Location rootLocation;
  private final CursorProvider cursorProvider;

  public GenericCursorContext(Location rootLocation, CursorProvider cursorProvider){
    this.cursorProvider = cursorProvider;
    this.rootLocation = rootLocation;
  }

  @Override
  public String context(){
    return rootLocation.context();
  }

  @Override
  public String fragment(){
    return rootLocation.fragment();
  }

  @Override
  public String name(){
    return rootLocation.name();
  }

  @Override
  public URI getResourceIdentity(){
    return rootLocation.getResourceIdentity();
  }

  @Override
  public Cursor moveCursorHere(){
    return get().moveTo(rootLocation);
  }

  @Override
  public NepticalData getData(){
    return rootLocation.getData();
  }

  @Override
  public Cursor get(){
    return cursorProvider.get();
  }

  @Override
  public Location getLocation(){
    return this;
  }

  @Override
  public String[] segments(){
    DataModule dataModule = NSpaceManager.getDataModule(rootLocation.fragment());

    return dataModule.segments();
  }

  @Override
  public String[] templates(){
    DataModule dataModule = NSpaceManager.getDataModule(rootLocation.fragment());
    //we have not created Template Ids yet. we are going to simply use the 1 based index of the largest row
    int biggestSegment = 0;

    for (String segment : segments()) {
      int lengthOfSegment = dataModule.getDataAt(segment).size();
      if (lengthOfSegment >= biggestSegment) {
        biggestSegment = lengthOfSegment;
      }
    }

    if (biggestSegment == 0) {
      return new String[0];
    }

    String[] templateIds = new String[biggestSegment];

    for (int i = 0; i < biggestSegment; i++) {
      templateIds[i] = String.valueOf(i + 1);
    }

    return templateIds;
  }

  @Override
  public Provider<Cursor> getContextCursorProvider(){
    return cursorProvider;
  }


}
