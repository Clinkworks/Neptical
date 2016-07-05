package clinkworks.neptical.component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Provider;

import org.apache.commons.lang3.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.Cursor.SystemCursor;
import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NepticalData;
import clinkworks.neptical.domain.NSpace;
import clinkworks.neptical.util.PathUtil;

public final class Origin implements SystemCursor {

	private static final String MODULE_FRAGMENT_SELECTED = "-1";

	private static final String NEPTICAL_SCHEME = "neptical://";
	private static final String SCHEME_FRAGMENT_END = "/";

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
		locationCache = CacheBuilder.newBuilder().build();
		locationCache.put(definedSpace.getResourceIdentity(), definedSpace);
	}

	@Override
	public Location getLocation() {
		return currentLocation;
	}

	@Override
	public Cursor moveTo(String path) throws DataDefinitionException {
		currentLocation = find(path);
		return this;
	}

	@Override
	public Cursor moveTo(Location location) {
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

			if (StringUtils.isEmpty(path) || StringUtils.endsWith(segment, MODULE_FRAGMENT_SELECTED)) {
				CursorLocation newLocation = new CursorLocation(currentSpace.getName(), segment, SCHEME_FRAGMENT_END);
				locationCache.put(newLocation.getResourceIdentity(), newLocation);
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

		CursorLocation newLocation = new CursorLocation(currentSpace.getName(), segment, selectedTemplateId);

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
	public String toString() {
		return getLocation().toString();
	}

	public void clearLocations() {
		// this can only be used in origin by design
		currentLocation = rootLocation;

		locationCache.invalidateAll();
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

	@Override
	public Cursor moveUp() {
		return null;
	}

	@Override
	public Cursor moveUp(int distance) {
		return null;
	}

	@Override
	public Cursor moveDown() {
		return null;
	}

	@Override
	public Cursor moveDown(int distance) {
		return null;
	}

	@Override
	public Cursor moveRight() {
		Location location = getLocation();

		// need to move to the first segment available in the module identified
		// here.
		NSpace nspace = NSpaceManager.getSpace(location.context());

		DataModule dataModule = nspace.getDataModule(location.fragment());

		String[] segments = dataModule.segments();

		if (segments.length > 0) {
			CursorLocation newLocation = new CursorLocation(location.context(), location.fragment(), segments[0]);
			currentLocation = newLocation;
			locationCache.put(newLocation.getResourceIdentity(), newLocation);
			return this;
		}

		return this;
	}

	@Override
	public Cursor moveRight(int distance) {
		return null;
	}

	@Override
	public Cursor moveLeft() {
		return null;
	}

	@Override
	public Cursor moveLeft(int distance) {
		return null;
	}

}
