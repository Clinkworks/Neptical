package com.clinkworks.neptical.data.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.clinkworks.neptical.data.api.NepticalData;
import com.clinkworks.neptical.data.domain.JsonData;
import com.clinkworks.neptical.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonDataUnitTest {
    private static final String ACCOUNTS_JSON = "{\"accounts\":{\"random-account\":{\"email\":\"{{random-email}}\",\"screenName\":\"MageSource\",\"firstName\":\"{{random-firstName}}\",\"lastName\":\"{{random-lastName}}\",\"middleName\":\"{{random-middleName}\"}}}";
    private static final String ARRAY_JSON = "{\"object1\":{\"prop1\":{\"array1\":[[\"val1\"],\"val2\"]}}}";
	
	private JsonData jsonData;
	
	@Test
	public void canPropertlySearchJsonData(){
		jsonData = new JsonData(ACCOUNTS_JSON);
		
//		NepticalData emailElement = jsonData.find("accounts.random-account.email");
//		assertEquals("{{random-email}}", emailElement.getAsString());
	}
	
	@Test
	public void canCloneJsonNodes(){
		
		JsonElement jsonElement = JsonUtil.parse(ACCOUNTS_JSON);
		jsonData = new JsonData("", "", null, null, jsonElement);
		
		NepticalData randomAccount = jsonData.find("accounts.random-account");
		NepticalData searchedAgain = jsonData.find("accounts.random-account");
		
		assertSame(randomAccount, searchedAgain);
		
		searchedAgain = searchedAgain.copyDeep();
		
		assertNotSame(randomAccount, searchedAgain);
		
	}
	
	
	@Test
	public void canProperlySearchThroughArrays(){
		JsonElement jsonElement = JsonUtil.parse(ARRAY_JSON);
		jsonData = new JsonData("", "", null, null, jsonElement);
		
		JsonArray jsonArray = jsonData.find("object1.prop1.array1").getAsJsonElement().getAsJsonArray();
		
		NepticalData val1 = jsonData.find("object1.prop1.array1[0][0]");
		
		assertEquals("val1", val1.getAsString());
		assertSame(jsonArray, val1.parent().parent().getAsJsonElement());
		assertEquals("val2", jsonData.find("object1.prop1.array1[1]").getAsString());
		
		assertTrue(val1.isPrimitive());
	}
}
