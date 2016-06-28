package clinkworks.neptical.component;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.domain.NSpace;

public class NSpaceManager {

	private static final Cache<String, DataModule> REGISTERED_DATA_MODULES;

	static{
		REGISTERED_DATA_MODULES =  CacheBuilder.newBuilder().build();
		REGISTERED_DATA_MODULES.put("default", NSpace.DEFAULT_NSPACE);
	}
	
	NSpaceManager() {
	}

	public NSpace createNSpace(String name, String... dataModules) {
		return null;
	}

	public static DataModule getSpace(String context) {
		try {
			return REGISTERED_DATA_MODULES.get(context, () -> new NSpace(context));
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
