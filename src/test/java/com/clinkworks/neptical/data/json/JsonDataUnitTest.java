package com.clinkworks.neptical.data.json;

import static org.junit.Assert.*;

import org.junit.Test;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.data.loaders.json.JsonData;
import com.clinkworks.neptical.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonDataUnitTest {
    private static final String ACCOUNTS_JSON = "{\"accounts\":{\"random-account\":{\"email\":\"{{random-email}}\",\"screenName\":\"MageSource\",\"firstName\":\"{{random-firstName}}\",\"lastName\":\"{{random-lastName}}\",\"middleName\":\"{{random-middleName}\"}}}";
    private static final String ARRAY_JSON = "{\"object1\":{\"prop1\":{\"array1\":[[\"val1\"],\"val2\"]}}}";
	
	private JsonData jsonData;
	
	@Test
	public void canPropertlySearchJsonData(){
		JsonElement jsonElement = JsonUtil.parse(ACCOUNTS_JSON);
		jsonData = new JsonData("", "", null, null, jsonElement);
		
		Data emailElement = jsonData.find("accounts.random-account.email");
		assertEquals("{{random-email}}", emailElement.getAsString());
	}
	
	@Test
	public void canCloneJsonNodes(){
		
		JsonElement jsonElement = JsonUtil.parse(ACCOUNTS_JSON);
		jsonData = new JsonData("", "", null, null, jsonElement);
		
		Data randomAccount = jsonData.find("accounts.random-account");
		Data searchedAgain = jsonData.find("accounts.random-account");
		
		assertSame(randomAccount, searchedAgain);
		
		searchedAgain = searchedAgain.copyDeep();
		
		assertNotSame(randomAccount, searchedAgain);
		
	}
	
	
	@Test
	public void canProperlySearchThroughArrays(){
		JsonElement jsonElement = JsonUtil.parse(ARRAY_JSON);
		jsonData = new JsonData("", "", null, null, jsonElement);
		
		JsonArray jsonArray = jsonData.find("object1.prop1.array1").getAsJsonElement().getAsJsonArray();
		
		Data val1 = jsonData.find("object1.prop1.array1[0][0]");
		
		assertEquals("val1", val1.getAsString());
		assertSame(jsonArray, val1.root().root().getAsJsonElement());
		assertEquals("val2", jsonData.find("object1.prop1.array1[1]").getAsString());
		
		assertTrue(val1.isPrimitive());
	}
}
