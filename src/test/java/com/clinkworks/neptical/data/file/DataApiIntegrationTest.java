package com.clinkworks.neptical.data.file;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.NepticalData;
import com.clinkworks.neptical.data.json.JsonFileNode;

public class DataApiIntegrationTest {

	private NepticalData root;
	
	@Before
	public void setup(){
    	String resourceName = Thread.currentThread().getContextClassLoader().getResource("data").getFile().replace("%20", " ");
        File file = new File(resourceName);

        root = new JsonFileNode("", "", null, null, file);

	}
	
    @Test
    public void ensureFileDataCanLoadResourcesDirectory() {

        NepticalData data = root.find("users");

        assertNotNull(data);
    }
    
    @Test
    public void ensureFileDataCanLoadNestedResources(){

        NepticalData accounts = root.find("users.accounts");
        NepticalData addresses = root.find("contacts.addresses");
        assertNotNull(accounts);
        assertNotNull(addresses);
    }
    
    @Test
    public void ensureFileDataCanReferenceExternalPaths(){
    	NepticalData email = root.find("users.random-account.account.email");
    	assertEquals("{{random-email}}", email.getAsString());
    }
    
    @Test
    public void canCopy(){
    	NepticalData account = root.find("users.random-account.account");
    	NepticalData foundAgain = root.find("users.random-account.account");
    	assertSame(account, foundAgain);
    	NepticalData cloned = foundAgain.copyDeep();
    	assertNotSame(cloned, foundAgain);
    }
    
    @Test
    public void canCloneFiles(){
    	NepticalData file = root.find("users");
    	assertSame(file, root.find("users"));
    	assertNotSame(file, root.find("users").copyDeep());
    }
    
    @Test
    public void canTraverseNodes(){
    	NepticalData account = root.find("users.random-account.account");
    	NepticalData randomAccountInfo = root.find("users.random-account");
    	assertEquals(root, account.root());
    	assertEquals(randomAccountInfo, account.parent());
    }
    
    @Test
    public void canUtilizeFileExtensions(){
    	NepticalData withExtension = root.find("users.json.random-account.account");
    	NepticalData without = root.find("users.random-account.account");
    	assertSame(withExtension, without);
    }
    
}
