package com.clinkworks.neptical.data.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.DataService;
import com.clinkworks.neptical.data.api.Cursor;
import com.clinkworks.neptical.data.api.NepticalProperty;
import com.clinkworks.neptical.data.domain.FileData;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner;
import com.clinkworks.neptical.junit.runners.NepticalJUnit4Runner.NepticalConfiguration;
import com.clinkworks.neptical.modules.NepticalDataModule;
import com.clinkworks.neptical.modules.NepticalPropertiesModule;
import com.clinkworks.neptical.modules.NepticalPropertiesModule.DataDirectory;
import com.google.inject.Inject;

@NepticalConfiguration({NepticalDataModule.class, NepticalPropertiesModule.class})
@RunWith(NepticalJUnit4Runner.class)
public class DataApiIntegrationTest {
	
	private Data data;
	
	@Inject
	@DataDirectory
	private File dataDirectory;
	
	@Before
	public void setup(DataService dataService){
		data = dataService.loadData();
	}
	
    @Test
    public void ensureCursorCanFindTheRootNode() {
    	assertNull(data.find("abc"));
    	assertEquals(dataDirectory, cursor.find("root").get());
    	assertEquals(dataDirectory, cursor.find("//").get());
    	assertEquals(dataDirectory, cursor.find("").get());
    	assertNull(cursor.find("123"));
    }
    
    @Test
    public void ensureFileDataCanLoadNestedResources(){

        FileData accounts = cursor.find("contacts").getAsFileData();
        
        assertTrue(accounts.isDirectory());
    }
    
    @Test
    public void ensureFileDataCanReferenceExternalPaths(){
//    	NepticalData email = root.find("users.random-account.account.email");
//    	assertEquals("{{random-email}}", email.getAsString());
    }
    
    @Test
    public void canCopy(){
//    	NepticalData account = root.find("users.random-account.account");
//    	NepticalData foundAgain = root.find("users.random-account.account");
//    	assertSame(account, foundAgain);
//    	NepticalData cloned = foundAgain.copyDeep();
//    	assertNotSame(cloned, foundAgain);
    }
    
    @Test
    public void canCloneFiles(){
//    	NepticalData file = root.find("users");
//    	assertSame(file, root.find("users"));
//    	assertNotSame(file, root.find("users").copyDeep());
    }
    
    @Test
    public void canTraverseNodes(){
//    	NepticalData account = root.find("users.random-account.account");
//    	NepticalData randomAccountInfo = root.find("users.random-account");
//    	assertEquals(root, account.root());
//    	assertEquals(randomAccountInfo, account.parent());
    }
    
    @Test
    public void canUtilizeFileExtensions(){
//    	NepticalData withExtension = root.find("users.json.random-account.account");
//    	NepticalData without = root.find("users.random-account.account");
//    	assertSame(withExtension, without);
    }
    
}
