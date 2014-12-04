package com.clinkworks.neptical.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.clinkworks.neptical.datatype.ListableTransformableData;
import com.clinkworks.neptical.datatype.TransformableData;
import com.clinkworks.neptical.util.JsonUtil;
import com.google.gson.JsonElement;

public class JsonDataUnitTest {
	
	private static final String JSON = "{\"hello\":\"Neptical\",\"list\":[\"ITEM1\",\"ITEM2\",2,{\"nestedObjectProperty\":\"OBJECT_PROP_VALUE\"}]}";
	private static final JsonElement JSON_ELEMENT = JsonUtil.parse(JSON);
	
	@Test
	public void showDifferencesBetweenNepticalDataAndGSONJson(){
		try{
			JSON_ELEMENT.getAsString();
			fail("GSON treats strings as primitives, so it is unacceptable for a json object to be a string " + 
			"Neptical attempts to treat all data in a generic way, so being a string is acceptable");
		}catch(UnsupportedOperationException e){
			JsonData jsonData = new JsonData("", JSON_ELEMENT);
			assertEquals(JSON, jsonData.getAsString());
		}
	}
	
	@Test
	public void ensureJsonDataProperlyConvertsValues(){
		
		JsonElement jsonElement = JSON_ELEMENT.getAsJsonObject().get("list");
		JsonData jsonData = new JsonData("", jsonElement);

		ListableTransformableData itemList = jsonData;
		TransformableData item1 = new JsonData("", jsonData.getAsJsonArray().get(0));
		
		List<String> strings = itemList.getListAs(String.class);
		
		assertEquals(4, strings.size());
		assertEquals("ITEM1", item1.getAs(String.class));
		assertSame(strings.get(0), item1.getAs(String.class));
	}
	
	@Test
	public void ensureJsonDataSupportsDotNotation(){
		JsonData jsonData = new JsonData(JSON_ELEMENT);
		assertEquals("ITEM1", jsonData.find("list[0]").getAsString());
		assertEquals(2, jsonData.find("list[2]").getAsInt());
		assertEquals("OBJECT_PROP_VALUE", jsonData.find("list[3].nestedObjectProperty").getAsString());
	}
}
