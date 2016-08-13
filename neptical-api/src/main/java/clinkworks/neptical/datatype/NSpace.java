package clinkworks.neptical.datatype;

import javax.inject.Provider;

public interface NSpace extends DataModule, Location, Provider<Cursor>{
	
	boolean containsModule(String module);

	void addModule(DataModule dataModule);

	void addModule(String moduleName);
	
}
