package clinkworks.neptical.component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import clinkworks.neptical.datatype.NepticalData;
import clinkworks.neptical.domain.NSpace;

public class NSpaceManager {
	
	private final Map<Class<? extends NepticalData>, List<? extends NepticalData>> typeMap;
	NSpaceManager(){
		typeMap = new ConcurrentHashMap<>();
	}

	public NSpace createNSpace(String... dataModules) {
		return null;
	}
	
	
}
