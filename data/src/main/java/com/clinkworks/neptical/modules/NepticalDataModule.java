package com.clinkworks.neptical.modules;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.clinkworks.neptical.api.ApplicationModuleBase;
import com.clinkworks.neptical.api.DataLoader;
import com.clinkworks.neptical.api.NepticalId;
import com.clinkworks.neptical.graph.Edge;
import com.clinkworks.neptical.graph.Node;
import com.clinkworks.neptical.graph.NodeId;
import com.clinkworks.neptical.loader.FileDataLoader;
import com.clinkworks.neptical.loader.JsonDataLoader;
import com.clinkworks.neptical.util.DataComponentFactory;
import com.clinkworks.neptical.util.NepticalComponentFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Provides;

public class NepticalDataModule extends ApplicationModuleBase{

	private final FileDataLoader fileDataLoader;
	private final JsonDataLoader jsonDataLoader;
	
	public NepticalDataModule(){
		fileDataLoader = new FileDataLoader();
		jsonDataLoader = new JsonDataLoader();
	}
	
	@Provides
	public Map<Serializable, DataLoader> dataLoaderRegistry(){
		Map<Serializable, DataLoader> dataLoaders = Maps.newHashMap();
		
		dataLoaders.put(File.class, fileDataLoader);
		dataLoaders.put("txt", fileDataLoader);
		dataLoaders.put("json", jsonDataLoader);
		
		return dataLoaders;
	}
	
	@Provides
	public Map<NepticalId<?>, Edge> edgeIdentificationMap(){
		return Maps.newConcurrentMap();
	}
	
	@Provides
	public List<Edge> edgeList(){
		return Lists.newArrayList();
	}
	
	@Provides
	public Map<NepticalId<?>, Node> nodeIdentificationMap(){
		return Maps.newConcurrentMap();
	}
	
	@Provides
	public NodeId genericNodeId(){
		return new NodeId();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected <NCF extends NepticalComponentFactory> Class<NCF> initalizeNepticalWithNewComponentFactoryType(){
		return (Class<NCF>) DataComponentFactory.class;
	}
	
}
