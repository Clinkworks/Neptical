package com.clinkworks.neptical.data.file;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.clinkworks.neptical.Data;

public class DataApiIntegrationTest {

	private Data root;
	
	@Before
	public void setup(){
    	String resourceName = Thread.currentThread().getContextClassLoader().getResource("data").getFile().replace("%20", " ");
        File file = new File(resourceName);

        root = new FileData("", "", null, null, file);

	}
	
    @Test
    public void ensureFileDataCanLoadResourcesDirectory() {

        Data data = root.find("users");

        assertNotNull(data);
    }
    
    @Test
    public void ensureFileDataCanLoadNestedResources(){

        Data accounts = root.find("users.accounts");
        Data addresses = root.find("contacts.addresses");
        assertNotNull(accounts);
        assertNotNull(addresses);
    }
    
    @Test
    public void ensureFileDataCanReferenceExternalPaths(){
    	Data email = root.find("users.random-account.account.email");
    	assertEquals("{{random-email}}", email.getAsString());
    }
    
    @Test
    public void canCopy(){
    	Data account = root.find("users.random-account.account");
    	Data foundAgain = root.find("users.random-account.account");
    	assertSame(account, foundAgain);
    	Data cloned = foundAgain.copyDeep();
    	assertNotSame(cloned, foundAgain);
    }
    
    @Test
    public void canCloneFiles(){
    	Data file = root.find("users");
    	assertSame(file, root.find("users"));
    	assertNotSame(file, root.find("users").copyDeep());
    }
    
    @Test
    public void canTraverseNodes(){
    	Data account = root.find("users.random-account.account");
    	assertEquals(root, account.root().root().root().root());
    }
    
    @Test
    public void canUtilizeFileExtensions(){
    	Data withExtension = root.find("users.json.random-account.account");
    	Data without = root.find("users.random-account.account");
    	assertSame(withExtension, without);
    }
    
}
