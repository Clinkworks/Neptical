package clinkworks.neptical.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Provider;

import clinkworks.neptical.component.ContextKey;
import clinkworks.neptical.component.NSpaceManager;
import clinkworks.neptical.component.Origin;
import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NepticalContext;
import clinkworks.neptical.datatype.NepticalData;

public class NSpace implements DataModule, Location, Provider<Cursor>, NepticalContext{

	public static final NSpace NEPTICAL_SYSTEM_SPACE;
	public static final NSpace DEFAULT_NSPACE;
	
	static{
		 NEPTICAL_SYSTEM_SPACE = new NSpace("system");
		 DEFAULT_NSPACE = new NSpace("default");
	}
	
	private final Provider<Cursor> cursorProvider;
	private final String name;
	
	private List<String> fragments;
	private ContextKey contextKey;
	

	public NSpace(String name, String... dataModules){
		this(Origin.getCursorContext(), name, dataModules);
	}
	
	@Inject
	NSpace(Provider<Cursor> cursorProvider, String name, String... dataModules) {
		this(cursorProvider, name, Stream.of(dataModules).map((moduleName) -> NSpaceManager.getDataModule(moduleName))
				.toArray(DataModule[]::new));
	}

	NSpace(Provider<Cursor> cursorProvider, String name, DataModule... dataModules) {

		// hold the search order as Nspaces has different semantics than Modules
		fragments = new CopyOnWriteArrayList<>();

		for (int i = 0; i < dataModules.length; i++) {
			fragments.add(dataModules[i].getName());
			
		}
		this.cursorProvider = cursorProvider;
		this.name = name;
	}

	public boolean containsModule(String module){
		return fragments.contains(module);
	}
	
	public void defineModules(String... moduleNames) {

		fragments.clear();

		for (String moduleName : moduleNames) {
			fragments.add(moduleName);
		}

	}
	
	public void addModule(String moduleName){
		int currentFragmentIndex = fragments.indexOf(moduleName);
		
		if(currentFragmentIndex < 0){
			fragments.add(moduleName);
			return;
		}
		
		fragments.add(fragments.remove(currentFragmentIndex));
		
	}

	public void addData(String segment, Object data) throws DataDefinitionException {
		getCurrentModule().addData(segment, new GenericImmutableData(data));
	}
	
	public void addData(String dataModule, String segment, Object data) throws DataDefinitionException {
		getDataModule(dataModule).addData(segment, new GenericImmutableData(data));
	}

	
	public String[] segments() {
		return fragments.toArray(new String[fragments.size()]);
	}

	@Override
	public void addData(String segment, NepticalData data) throws DataDefinitionException {
		DataModule dataModule = getCurrentModule();
		if (dataModule == null) {
			throw new DataDefinitionException("NSpace " + getName()
					+ " does not have a registered data module, call addData(Module, Segment, Data) to register and add this data");
		}
		dataModule.addData(segment, data);
	}

	@Override
	public List<NepticalData> getDataAt(String segment) {
		DataModule dataModule = getCurrentModule();

		if (dataModule == null) {
			return new ArrayList<>();
		}

		List<NepticalData> moduleData = dataModule.getDataAt(segment);

		if (!moduleData.isEmpty()) {
			return moduleData;
		}
		
		for (int i = fragments.size() - 1; i > 0; i--) {
			moduleData = getDataModule(fragments.get(i - 1)).getDataAt(segment);
			if (moduleData.isEmpty()) {
				continue;
			}
			return moduleData;
		}
		
		return new ArrayList<>();

	}

	@Override
	public NepticalData getDataAt(String segment, int index) {
		DataModule dataModule = getDataModuleContaining(segment);
		if(dataModule == null){
			return NepticalData.NULL_DATA;
		}
		return dataModule.getDataAt(segment, index);
	}
	
	@Override
	public List<NepticalData> getAllData() {

		if (fragments.isEmpty()) {
			return new ArrayList<>();
		}

		return getDataModule(fragments.get(fragments.size() - 1)).getAllData();

	}
	
	public DataModule getDataModuleContaining(String segment){
				
		for(int i = fragments.size(); i > 0; i--){
			DataModule dataModule = getDataModule(fragments.get(i - 1));
			
			if(dataModule.getDataAt(segment).isEmpty()){
				continue;
			}
			
			return dataModule;
		}
		
		return null;
	}

	private DataModule getCurrentModule() {
		if (fragments.isEmpty()) {
			return null;
		}
		return getDataModule(fragments.get(fragments.size() - 1));
	}

	public DataModule getDataModule(String moduleName) {
		return NSpaceManager.getDataModule(moduleName);
	}

	@Override
	public String context() {
		return "neptical://";
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Cursor moveCursorHere() {
		return cursorProvider.get().moveTo(this);
	}


	@Override
	public NepticalData getData() {
		return NepticalData.NULL_DATA;
	}

	@Override
	public URI getResourceIdentity() {
			try {
				return new URI(context() + name());
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return null;
			}
	}

	@Override
	public String fragment() {
		return name();
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public Cursor get() {
		return cursorProvider.get();
	}

	@Override
	public CursorContext getCursorContext() {
		return NSpaceManager.getCursorContext((DataModule)this);
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
		return getResourceIdentity();
	}
	
	@Override
	public String toString(){
		return getIdentity().toString();
	}

}
