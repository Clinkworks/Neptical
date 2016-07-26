package clinkworks.neptical.component;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import clinkworks.neptical.api.CursorContext;
import clinkworks.neptical.api.DataModule;
import clinkworks.neptical.api.Location;
import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.Cursor.SystemCursor;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.NepticalData;
import clinkworks.neptical.domain.GenericSpace;
import clinkworks.neptical.util.PathUtil;

public final class Origin implements SystemCursor{

  private static final String MODULE_FRAGMENT_SELECTED = "-1";

  private static final String NEPTICAL_SCHEME = "neptical://";
  private static final String SCHEME_FRAGMENT_END = "/";

  private static volatile Cursor CURRENT_CURSOR;

  private final GenericSpace rootLocation;

  private volatile Location currentLocation;

  public static final Cursor getCursor(){

    if (CURRENT_CURSOR == null) {
      CURRENT_CURSOR = new Origin(GenericSpace.DEFAULT_NSPACE);
    }

    return CURRENT_CURSOR;
  }

  public static CursorProvider getCursorProvider(){
    return new CursorWrapper();
  }

  private Origin(GenericSpace definedSpace){
    rootLocation = definedSpace;
    currentLocation = rootLocation;
  }

  @Override
  public Location getLocation(){
    return currentLocation;
  }

  @Override
  public Cursor moveTo(String path) throws DataDefinitionException{
    currentLocation = find(path);
    return this;
  }

  @Override
  public Cursor moveTo(Location location){
    this.currentLocation = location;
    return this;
  }

  @Override
  public Location find(String query) throws DataDefinitionException{

    String path = query;
    GenericSpace currentSpace = rootLocation;
    DataModule parentModule = null;

    boolean changeNSpace = StringUtils.startsWith(path, NEPTICAL_SCHEME) && !StringUtils.startsWith(path, NEPTICAL_SCHEME + rootLocation.name());

    if (changeNSpace) {
      path = new String(path.substring(NEPTICAL_SCHEME.length()));
      String nspaceToSwitch = PathUtil.firstSegment(path);
      path = PathUtil.chompFirstSegment(path);

      currentSpace = NSpaceManager.getSpace(nspaceToSwitch);
      currentLocation = currentSpace;
    }

    String segment = PathUtil.firstSegment(path);

    if (currentSpace.containsModule(segment)) {
      parentModule = currentSpace.getDataModule(segment);
      path = PathUtil.chompFirstSegment(path);

      if (StringUtils.isEmpty(path) || StringUtils.endsWith(segment, MODULE_FRAGMENT_SELECTED)) {
        CursorLocation newLocation = new CursorLocation(new CursorWrapper(), currentSpace.getName(), segment, SCHEME_FRAGMENT_END);
        currentLocation = newLocation;
        return newLocation;
      }

      segment = PathUtil.firstSegment(path);
    }

    if (parentModule == null) {
      parentModule = currentSpace.getDataModuleContaining(segment);
    }

    if (parentModule == null) {
      currentSpace.addModule(segment);
      parentModule = currentSpace.getDataModule(segment);
    }

    List<NepticalData> dataList = parentModule.getDataAt(segment);

    // TODO: imbed the template concept into the locations
    String selectedTemplateId = String.valueOf(dataList.size() - 1);

    if (Integer.valueOf(MODULE_FRAGMENT_SELECTED).equals(selectedTemplateId)) {
      selectedTemplateId = SCHEME_FRAGMENT_END;
    }

    currentLocation = new CursorLocation(new CursorWrapper(), currentSpace.getName(), segment, selectedTemplateId);
    ;

    return currentLocation;
  }

  @Override
  public NepticalData getData(){
    Location location = getLocation();

    DataModule moduleContext = NSpaceManager.getSpace(location.context());

    List<NepticalData> data = moduleContext.getDataAt(location.fragment());

    if (data.isEmpty()) {
      return NepticalData.NULL_DATA;
    }

    if (location instanceof CursorLocation) {
      return data.get(Integer.valueOf(location.name()));
    }

    return null;
  }

  @Override
  public Location call() throws Exception{
    return null;
  }

  @Override
  public Cursor get(){
    return this;
  }

  @Override
  public GenericSpace currentNSpace(){
    return null;
  }

  @Override
  public List<DataModule> activeModules(){
    return null;
  }

  @Override
  public CursorContext activeCursorContext(){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toString(){
    return getLocation().toString();
  }

  public void clearLocations(){
    // this can only be used in origin by design
    currentLocation = rootLocation;
  }

  @Override
  public Cursor moveUp(){
    return null;
  }

  @Override
  public Cursor moveUp(int distance){
    return null;
  }

  @Override
  public Cursor moveDown(){
    Location location = getLocation();


    if ("/".equals(location.name())) {



      CursorLocation newLocation = new CursorLocation(new CursorWrapper(), location.context(), location.fragment() + location.name(), "1");
      currentLocation = newLocation;
      return this;
    }

    return null;
  }

  @Override
  public Cursor moveDown(int distance){
    return null;
  }

  @Override
  public Cursor moveRight(){
    Location location = getLocation();

    // need to move to the first segment available in the module identified
    // here.
    GenericSpace nspace = NSpaceManager.getSpace(location.context());

    DataModule dataModule = nspace.getDataModule(location.fragment());

    String[] segments = dataModule.segments();

    if (segments.length > 0) {
      currentLocation = new CursorLocation(new CursorWrapper(), location.context(), location.fragment(), segments[0]);
      return this;
    }

    return this;
  }

  @Override
  public Cursor moveRight(int distance){
    return null;
  }

  @Override
  public Cursor moveLeft(){
    Location location = getLocation();

    GenericSpace nspace = NSpaceManager.getSpace(location.context());

    DataModule dataModule = nspace.getDataModule(location.fragment());

    if (location.name().equals("/")) {
      currentLocation = nspace;
      return this;
    }

    String[] segments = dataModule.segments();

    String leftSegment = null;

    for (int i = segments.length; i >= 0; i--) {
      if (segments[i].equals(location.name())) {
        if (i == 0) {
          currentLocation = nspace;
          return this;
        } else {
          leftSegment = segments[i - 1];
          break;
        }
      }
    }

    if (leftSegment == null) {
      currentLocation = nspace;
      return this;
    }

    Location newLocation = new CursorLocation(new CursorWrapper(), location.context(), location.fragment(), leftSegment);
    this.currentLocation = newLocation;

    return this;
  }

  @Override
  public Cursor moveLeft(int distance){
    return null;
  }

}
