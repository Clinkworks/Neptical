package clinkworks.neptical.datatype;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Provider;

import clinkworks.neptical.api.CursorContext;
import clinkworks.neptical.api.DataModule;
import clinkworks.neptical.api.Location;


public interface Cursor{

  Cursor moveTo(String path) throws DataDefinitionException;

  Cursor moveTo(Location location);

  Cursor moveUp();

  Cursor moveUp(int distance);

  Cursor moveDown();

  Cursor moveDown(int distance);

  Cursor moveRight();

  Cursor moveRight(int distance);

  Cursor moveLeft();

  Cursor moveLeft(int distance);


  Location getLocation();

  Location find(String query) throws DataDefinitionException;

  NepticalData getData();

  public static interface SystemCursor extends Callable<Location>, Provider<Cursor>, Cursor{

    NSpace currentNSpace();

    List<DataModule> activeModules();

    CursorContext activeCursorContext();
  }

}
