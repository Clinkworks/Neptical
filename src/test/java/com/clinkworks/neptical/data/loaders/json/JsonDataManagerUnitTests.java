package com.clinkworks.neptical.data.loaders.json;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.clinkworks.neptical.data.loaders.json.JsonDataManager;
import com.google.gson.JsonElement;

@RunWith(BlockJUnit4ClassRunner.class)
public class JsonDataManagerUnitTests {
	
    private static final String ACCOUNTS_JSON = "{\"accounts\":{\"random-account\":{\"account\":{\"email\":\"{{random-email}}\",\"screenName\":\"MageSource\",\"firstName\":\"{{random-firstName}}\",\"lastName\":\"{{random-lastName}}\",\"middleName\":\"{{random-middleName}\"}}}}";
    private static final String ARRAY_JSON = "{\"object1\":{\"prop1\":{\"array1\":[[\"val1\"],\"val2\"]}}}";
	
	private JsonDataManager dataManager;
	
	@Test
	public void asef(){
		dataManager = new JsonDataManager();
	}
	
}
