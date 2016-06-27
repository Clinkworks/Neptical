package clinkworks.neptical;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Provider;

import org.apache.commons.lang3.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NepticalData;
import clinkworks.neptical.domain.CursorContext;
import clinkworks.neptical.domain.NSpace;
import clinkworks.neptical.util.PathUtil;

public abstract class Data {

	public static final NepticalData BLANK = new BlankData();
	
	private static final CursorContext DEFAULT_CONTEXT = new CursorContext(new CursorWrapper());
	private static volatile Cursor CURRENT_CURSOR;

	public static final Cursor getCursor() {

		if (CURRENT_CURSOR == null) {
			CURRENT_CURSOR = new Origin(new NSpace("default"));
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
		return location.get();
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
			
			List<NepticalData> dataList = parentModule.getData(segment);
			NepticalData data = dataList.isEmpty() ? NepticalData.NULL_DATA : dataList.get(dataList.size() - 1);
			CursorLocation newLocation = new CursorLocation(currentSpace.getName(), parentModule, segment, dataList.size() - 1);
			locationCache.put(newLocation.getResourceIdentity(), newLocation);
			newLocation.setNepticalData(data);
			
			currentLocation = newLocation;
			
			return newLocation;
		}

		public Location moveUp() {
			List<NepticalData> data = currentLocation.parentModule().getData(currentLocation.name());
			int currentIndex = currentLocation.rowId();

			if(currentIndex < 0){
				//move to next module and find last index of segment
			}
			
			CursorLocation cursorLocation = new CursorLocation(rootLocation.getName(), currentLocation.parentModule(), currentLocation.name(), currentLocation.rowId() - 1);
			cursorLocation.setNepticalData(data.get(cursorLocation.rowId()));
			currentLocation = cursorLocation;
			return currentLocation;
		}

		

	}

	private static class CursorLocation implements Location{

		private final DataModule parent;
		private final String nspace;
		private final String column;
		private final int row;
		private volatile NepticalData nepticalData;
		
		private CursorLocation(String nspace, DataModule module, String fragment, int row) {
			parent = module;
			column = fragment;
			this.nspace = nspace;
			this.row = row;
		}
		
		@Override
		public Cursor moveCursorHere() {
			return getCursor().moveToLocation(this);
		}

		@Override
		public NepticalData get() {
			return nepticalData == null ? NepticalData.NULL_DATA : nepticalData;
		}

		@Override
		public DataModule parentModule() {
			return parent;
		}

		@Override
		public String name() {
			return column;
		}
		
		

		@Override
		public URI getResourceIdentity() {
			try {
				return new URI("neptical://" + nspace + "." + parent.getName() + "." + column + "?row=" + row);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		private void setNepticalData(NepticalData nepticalData){
			this.nepticalData = nepticalData;
		}

		@Override
		public int rowId() {
			return row;
		}
	}
	
	static final class CursorWrapper implements Provider<Cursor>, Cursor{
		
		@Override
		public Cursor moveToLocation(Location location) {
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
