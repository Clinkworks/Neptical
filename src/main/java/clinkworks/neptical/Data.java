package clinkworks.neptical;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Provider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import clinkworks.neptical.component.NSpaceManager;
import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NepticalData;
import clinkworks.neptical.domain.GenericCursorContext;
import clinkworks.neptical.domain.NSpace;
import clinkworks.neptical.util.PathUtil;

public abstract class Data {

	private static final Logger LOGGER = LoggerFactory.getLogger(Data.class);
	
	public static final NepticalData BLANK = new BlankData();
	
	private static final CursorContext DEFAULT_CONTEXT = new GenericCursorContext(NSpace.DEFAULT_NSPACE, new CursorWrapper());

	public static final Location DEFAULT_SPACE = null
			;
	private static volatile Cursor CURRENT_CURSOR;

	public static final Cursor getCursor() {

		if (CURRENT_CURSOR == null) {
			CURRENT_CURSOR = new Origin(NSpace.DEFAULT_NSPACE);
		}

		return CURRENT_CURSOR;
	}

	public abstract Cursor moveToLocation(Location location);

	public abstract Location getLocation();
	
	public NepticalData getData(){
		Location location = getLocation();
		if(location == null){
			return null;
		}
		return location.getData();
	}
	
	public static Provider<Cursor> getCursorContext() {
		return DEFAULT_CONTEXT;
	}


	public static final class Origin implements Cursor {

		private final Cache<URI, Location> locationCache;
		private final NSpace rootLocation;
		private volatile Location currentLocation;
		
		private Origin(NSpace definedSpace) {
			rootLocation = definedSpace;
			currentLocation = rootLocation;
			locationCache = CacheBuilder.newBuilder().build();
			locationCache.put(definedSpace.getResourceIdentity(), definedSpace);
		}

		@Override
		public Cursor moveToLocation(Location location) {
			this.currentLocation = location;
			return this;
		}

		@Override
		public Location getLocation() {
			return currentLocation;
		}

		@Override
		public Location find(String query) throws DataDefinitionException {
			String path = query;
			String nepticalProtocol = "neptical://";
			NSpace currentSpace = rootLocation;
			DataModule parentModule = null;
			
			boolean changeNSpace = StringUtils.startsWith(path, nepticalProtocol) && !StringUtils.startsWith(path, rootLocation.getResourceIdentity().toString());
			
			
			if(changeNSpace){
				path = new String(path.substring(nepticalProtocol.length()));
				String nspaceToSwitch = PathUtil.firstSegment(path);
				path = PathUtil.chompFirstSegment(path);
				
				try {
					currentSpace = (NSpace)locationCache.get(new URI(nepticalProtocol + nspaceToSwitch), () -> new NSpace(nspaceToSwitch));
					currentLocation = currentSpace;
				} catch (ExecutionException | URISyntaxException e) {
					throw new DataDefinitionException("Something went wrong when loading a new nspace from the query");
				}
			}
			
			String segment = PathUtil.firstSegment(path);
			
			if(currentSpace.containsModule(segment)){
				parentModule = currentSpace.getDataModule(segment);
				path = PathUtil.chompFirstSegment(path);
				segment = PathUtil.firstSegment(path);
			}
			
			if(parentModule == null){
				parentModule = currentSpace.getDataModuleContaining(segment);
			}
			
			if(parentModule == null){
				currentSpace.addModule(segment);
				parentModule = currentSpace.getDataModule(segment);
			}
			
			List<NepticalData> dataList = parentModule.getDataAt(segment);
			CursorLocation newLocation = new CursorLocation(currentSpace.getName(), segment, String.valueOf(dataList.size() - 1));
			locationCache.put(newLocation.getResourceIdentity(), newLocation);
			
			currentLocation = newLocation;
			
			return newLocation;
		}

		@Override
		public NepticalData getData() {
			Location location = getLocation();
			
			DataModule moduleContext = NSpaceManager.getSpace(location.context());
			List<NepticalData> data = moduleContext.getDataAt(location.fragment());
			
			if(data.isEmpty()){
				return NepticalData.NULL_DATA;
			}
			
			if(location instanceof CursorLocation){
				return data.get(Integer.valueOf(location.name()));
			}
			
			return null;
		}

		

	}

	private static class CursorLocation implements Location{

		private final String context;
		private final String fragment;
		private final String name;
		
		private CursorLocation(String context, String fragment, String name) {
			this.context = context;
			this.fragment = fragment;
			this.name = name;
		}
		
		@Override
		public String context() {
			return context;
		}

		@Override
		public String fragment() {
			return fragment;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public URI getResourceIdentity() {
			try {
				return new URI(context + "/" + fragment + "." + "name");
			} catch (URISyntaxException e) {
				LOGGER.debug("Something bad happened creating uri for Location " + this + "(" + e.getMessage() + ")", e);
				throw new RuntimeException(e);
			}
		}

		@Override
		public Cursor moveCursorHere() {
			return getCursor().moveToLocation(this);
		}

		@Override
		public NepticalData getData() {
			Location previousLocation = getCursor().getLocation();
			getCursor().moveToLocation(this);
			
			NepticalData nepticalData = getCursor().getData();
			
			getCursor().moveToLocation(previousLocation);
			
			return nepticalData;
		}

	}
	
	static final class CursorWrapper implements Provider<Cursor>, Cursor{
		
		@Override
		public Cursor moveToLocation(Location location) {
			LOGGER.warn("Could not move to " + location + " Cursor is not initalized.");
			return get() == null ? this : get().moveToLocation(location);
		}

		@Override
		public Location getLocation() {
			return get() == null ? null : get().getLocation();
		}

		@Override
		public Location find(String query) throws DataDefinitionException {
			return get() == null ? null : get().find(query);
		}

		@Override
		public Cursor get() {
			return CURRENT_CURSOR;
		}

		@Override
		public NepticalData getData() {
			return get() == null ? NepticalData.NULL_DATA : get().getData();
		}

		
	}
	
	public static final class BlankData implements NepticalData{

		private BlankData(){}
		
		@Override
		public Object get() {
			return null;
		}

		@Override
		public Class<?> getDataType() {
			return BlankData.class;
		}
		
	}


	

}
