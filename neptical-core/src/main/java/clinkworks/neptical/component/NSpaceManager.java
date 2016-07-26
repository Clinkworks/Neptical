package clinkworks.neptical.component;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import clinkworks.neptical.api.CursorContext;
import clinkworks.neptical.api.DataModule;
import clinkworks.neptical.api.Location;
import clinkworks.neptical.datatype.NepticalContext;
import clinkworks.neptical.domain.GenericCursorContext;
import clinkworks.neptical.domain.GenericDataModule;
import clinkworks.neptical.domain.GenericSpace;

public class NSpaceManager implements ContextKeyFactory{

  private static final String DEFAULT_MODULE_CONTEXT = "module";
  private static final String HOME_CONTEXT = "home";

  private static final NSpaceKey DEFAULT_KEY;
  private static final Cache<ContextKey, DataModule> CONTEXT_CACHE_MODULE;
  private static final Cache<ContextKey, CursorContext> CONTEXT_CACHE_CURSOR;
  private static final ContextKeyFactory CONTEXT_KEY_FACTORY;

  static final Logger LOGGER = null;

  static {
    CONTEXT_CACHE_MODULE = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
    CONTEXT_CACHE_CURSOR = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();
    DEFAULT_KEY = new NSpaceKey(GenericSpace.DEFAULT_NSPACE.name());
    CONTEXT_KEY_FACTORY = new NSpaceManager();

    clearCache();
  }

  NSpaceManager(){
  }

  /* TODO: have this clear only specific contexts within cache */
  public static final void clearCache(){
    CONTEXT_CACHE_CURSOR.invalidateAll();
    CONTEXT_CACHE_MODULE.invalidateAll();


    CONTEXT_CACHE_MODULE.put(DEFAULT_KEY, GenericSpace.DEFAULT_NSPACE);
    CONTEXT_CACHE_CURSOR.put(new ContextKey(DEFAULT_KEY.name(), Location.class), GenericSpace.DEFAULT_NSPACE.getCursorContext());

    ((Origin)Origin.getCursor()).clearLocations();
  }

  public GenericSpace createNSpace(String name, String... dataModules){
    ContextKey key = new NSpaceKey(name);
    GenericSpace newSpace = new GenericSpace(name, dataModules);
    CONTEXT_CACHE_MODULE.put(key, newSpace);
    manifestCursorContext(newSpace, Location.class);
    return newSpace;
  }

  public static final ContextKeyFactory getContextKeyFactory(){
    return CONTEXT_KEY_FACTORY;
  }

  public static GenericSpace getSpace(String context){
    try {
      return (GenericSpace)CONTEXT_CACHE_MODULE.get(new NSpaceKey(context), () -> new GenericSpace(context));
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  public static void addSpace(GenericSpace nspace){
    CONTEXT_CACHE_MODULE.put(new NSpaceKey(nspace.name()), nspace);
  }

  public static void addModule(DataModule dataModule){
    CONTEXT_CACHE_MODULE.put(new ContextKey(dataModule.getName(), DataModule.class), dataModule);
  }

  public static DataModule getDataModule(ContextKey contextKey){
    if (DataModule.class.isAssignableFrom(contextKey.contextType())) {

    }
    return null;
  }

  public static DataModule getDataModule(String context){

    try {
      return CONTEXT_CACHE_MODULE.get(new ContextKey(context, DataModule.class), () -> new GenericDataModule(context));
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  static CursorContext getNepticalContext(NepticalContext nepticalContext){
    if (nepticalContext.getContextKey() == null) {
      ContextKey contextKey = CONTEXT_KEY_FACTORY.createContextPartitionKey("UNSAFE".hashCode(), nepticalContext.getIdentity().toString(), Location.class);
      nepticalContext.setContextKey(contextKey);
      LOGGER.warn("Attempted to set a context that has never been inialized with a context key. Creating a cursor context unique to context: " + nepticalContext);
      return manifestCursorContext(contextKey);
    }
    return lookupContext(nepticalContext.getContextKey());
  }

  static CursorContext lookupContext(ContextKey contextKey){
    return CONTEXT_CACHE_CURSOR.getIfPresent(contextKey);
  }

  public static CursorContext getCursorContext(DataModule dataModule){
    return manifestCursorContext(getModuleHome(dataModule), DataModule.class);
  }

  public static class NSpaceKey extends ModuleKey{

    NSpaceKey(String contextName){
      super(contextName);
    }

  }

  public static class ModuleKey extends ContextKey{

    ModuleKey(String contextName){
      super(contextName, DataModule.class);
    }

  }

  public static final CursorContext getCursorContext(Location location){
    return manifestCursorContext(location, Location.class);
  }

  private static CursorContext manifestCursorContext(ContextKey contextKey){

    try {
      return CONTEXT_CACHE_CURSOR.get(contextKey, () -> new GenericCursorContext(getLocation(contextKey), new CursorWrapper(contextKey)));
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private static CursorContext manifestCursorContext(Location location, Class<?> contextType){
    ContextKey locationKey = getLocationKey(location);
    try {
      return CONTEXT_CACHE_CURSOR.get(locationKey, () -> new GenericCursorContext(location, new CursorWrapper(locationKey)));
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }

  }

  private static final Location getLocation(ContextKey contextKey){
    URI uri = URI.create(contextKey.name());
    return new CursorLocation(new CursorWrapper(), uri.getHost(), uri.getPath(), uri.getQuery());
  }

  private static final ContextKey getLocationKey(Location location){
    return new ContextKey(location.getResourceIdentity().toString(), Location.class);
  }

  private static final Location getModuleHome(DataModule dataModule){
    return new CursorLocation(new CursorWrapper(), DEFAULT_MODULE_CONTEXT, dataModule.getName(), HOME_CONTEXT);
  }

  @Override
  public ContextKey createContextKey(String context, Class<?> contextType){
    return new ContextKey(context, contextType);
  }

  @Override
  public ContextKey createContextPartitionKey(int partition, String context, Class<?> contextType){
    return new ContextKey(partition, context, contextType);
  }

  @Override
  public ContextKey createLocationKey(Location location){
    return getLocationKey(location);
  }

  @Override
  public ContextKey createLocationKey(URI location){
    return new ContextKey(location);
  }

}
