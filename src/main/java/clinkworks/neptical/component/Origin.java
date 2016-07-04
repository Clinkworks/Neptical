package clinkworks.neptical.component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Provider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import clinkworks.neptical.component.Origin.CursorLocation.CursorWrapper;
import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.Cursor.SystemCursor;
import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.LookupFailureException;
import clinkworks.neptical.datatype.NepticalData;
import clinkworks.neptical.domain.NSpace;
import clinkworks.neptical.util.PathUtil;

public final class Origin implements SystemCursor {

	private static volatile Cursor CURRENT_CURSOR;

	private final Cache<URI, Location> locationCache;
	private final NSpace rootLocation;

	private volatile Location currentLocation;
	private volatile ContextKey contextKey;

	public static final Cursor getCursor() {

		if (CURRENT_CURSOR == null) {
			CURRENT_CURSOR = new Origin(NSpace.DEFAULT_NSPACE);
		}

		return CURRENT_CURSOR;
	}

	public static Provider<Cursor> getCursorContext() {
		return new CursorWrapper();
	}

	private Origin(NSpace definedSpace) {
		rootLocation = definedSpace;
		currentLocation = rootLocation;
		locationCache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.SECONDS).build();
		locationCache.put(definedSpace.getResourceIdentity(), definedSpace);
	}

	@Override
	public Location getLocation() {
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
	public Location find(String query) throws DataDefinitionException {
		String path = query;
		String nepticalProtocol = "neptical://";
		NSpace currentSpace = rootLocation;
		DataModule parentModule = null;

		boolean changeNSpace = StringUtils.startsWith(path, nepticalProtocol)
				&& !StringUtils.startsWith(path, rootLocation.getResourceIdentity().toString());

		if (changeNSpace) {
			path = new String(path.substring(nepticalProtocol.length()));
			String nspaceToSwitch = PathUtil.firstSegment(path);
			path = PathUtil.chompFirstSegment(path);

			try {
				currentSpace = (NSpace) locationCache.get(new URI(nepticalProtocol + nspaceToSwitch),
						() -> NSpaceManager.getSpace(nspaceToSwitch));
				currentLocation = currentSpace;
			} catch (ExecutionException | URISyntaxException e) {
				throw new DataDefinitionException("Something went wrong when loading a new nspace from the query");
			}
		}

		String segment = PathUtil.firstSegment(path);

		if (currentSpace.containsModule(segment)) {
			parentModule = currentSpace.getDataModule(segment);
			path = PathUtil.chompFirstSegment(path);
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
		CursorLocation newLocation = new CursorLocation(currentSpace.getName(), segment,
				String.valueOf(dataList.size() - 1));
		locationCache.put(newLocation.getResourceIdentity(), newLocation);

		currentLocation = newLocation;

		return newLocation;
	}

	@Override
	public NepticalData getData() {
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
	public Location call() throws Exception {
		return null;
	}

	@Override
	public Cursor get() {
		return this;
	}

	@Override
	public NSpace currentNSpace() {
		return null;
	}

	@Override
	public List<DataModule> activeModules() {
		return null;
	}

	@Override
	public CursorContext activeCursorContext() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString(){
		return getLocation().getResourceIdentity().toString();
	}

	public void clearLocations() {
		// this can only be used in origin by design
		currentLocation = rootLocation;

		locationCache.invalidateAll();
	}

	public static class CursorLocation implements Location {

		private static final Logger LOGGER = LoggerFactory.getLogger(CursorLocation.class);

		private final String context;
		private final String fragment;
		private final String name;

		CursorLocation(String context, String fragment, String name) {
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
				return new URI(context + "/" + fragment + "." + name);
			} catch (URISyntaxException e) {
				LOGGER.debug("Something bad happened creating uri for Location " + this + "(" + e.getMessage() + ")",
						e);
				throw new RuntimeException(e);
			}
		}

		@Override
		public Cursor moveCursorHere() {
			return getCursor().moveTo(this);
		}

		@Override
		public NepticalData getData() {
			Location previousLocation = getCursor().getLocation();
			getCursor().moveTo(this);

			NepticalData nepticalData = getCursor().getData();

			getCursor().moveTo(previousLocation);

			return nepticalData;
		}

		@Override
		public CursorContext getCursorContext() {
			return NSpaceManager.getCursorContext(this);
		}

		@Override
		public String toString() {
			return getResourceIdentity().toString();
		}

		public static final class CursorWrapper implements CursorProvider, Cursor {

			private volatile ContextKey contextKey;

			CursorWrapper(ContextKey contextKey) {
				this.contextKey = contextKey;
			}

			CursorWrapper() {
			}

			@Override
			public Cursor moveTo(Location location) {
				LOGGER.warn("Could not move to " + location + " Cursor is not initalized.");
				return get() == null ? this : get().moveTo(location);
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
				return getCursor();
			}

			@Override
			public NepticalData getData() {
				return get() == null ? NepticalData.NULL_DATA : get().getData();
			}

			@Override
			public ContextKey getContextKey() {
				return contextKey;
			}

			@Override
			public void setContextKey(ContextKey contextKey) {
				this.contextKey = contextKey;
			}

			@Override
			public URI getIdentity() {

				ContextKey contextKey = getContextKey();

				if (contextKey == null && get() == null) {
					return NSpace.NEPTICAL_SYSTEM_SPACE.getIdentity();
				}

				if (getContextKey() == null) {
					return get().getLocation().getResourceIdentity();
				}
				try {
					return new URI(getContextKey().name());
				} catch (URISyntaxException e) {
					throw new LookupFailureException(e.getMessage(), e);
				}

			}

			@Override
			public String toString() {
				return get() == null ? "UNKOWN" : get().toString();
			}

			@Override
			public Cursor moveTo(String path) throws DataDefinitionException {
				return get() == null ? this : get().moveTo(path);
			}

		}

	}

	@Override
	public ContextKey getContextKey() {
		return contextKey;
	}

	@Override
	public void setContextKey(ContextKey contextKey) {
		this.contextKey = contextKey;
	}

	@Override
	public URI getIdentity() {
		return rootLocation.getIdentity();
	}

}
