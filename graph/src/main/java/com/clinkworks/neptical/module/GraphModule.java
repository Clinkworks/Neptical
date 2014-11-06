package com.clinkworks.neptical.module;

import java.util.List;
import java.util.Map;

import com.clinkworks.neptical.datatype.NepticalId;
import com.clinkworks.neptical.graph.Edge;
import com.clinkworks.neptical.graph.Node;
import com.clinkworks.neptical.spi.ApplicationModuleTemplate;
import com.clinkworks.neptical.spi.GraphComponentFactory;
import com.clinkworks.neptical.spi.NepticalComponentFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class GraphModule extends ApplicationModuleTemplate{

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
	
	@Override
	protected void configureFactoryModuleBuilder(FactoryModuleBuilder factoryModuleBuilder) {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <NCF extends NepticalComponentFactory> Class<NCF> initalizeNepticalWithComponentFactorySubType() {
		return (Class<NCF>)GraphComponentFactory.class;
	}

	@Override
	protected void configureApplication() {
		
	}
	
}
