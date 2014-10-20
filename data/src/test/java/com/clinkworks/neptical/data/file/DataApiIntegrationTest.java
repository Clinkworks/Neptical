package com.clinkworks.neptical.data.file;

import org.junit.Test;

//@NepticalConfiguration({NepticalDataModule.class, NepticalPropertiesModule.class})
//@RunWith(NepticalJUnit4Runner.class)
public class DataApiIntegrationTest {
	
//	private Data data;
//	
//	@Inject
//	@DataDirectory
//	private File dataDirectory;
//	
//	@Before
//	public void setup(DataService dataService){
//		data = dataService.loadData();
//	}
//	
//    @Test
//    public void ensureCursorCanFindTheRootNode() {
//    	assertNull(data.find("abc"));
//    	assertEquals(dataDirectory, data.find("root").get());
//    	assertEquals(dataDirectory, data.find("//").get());
//    	assertEquals(dataDirectory, data.find("").get());
//    	assertNull(data.find("123"));
//    }
//    
//    @Test
//    public void ensureFileDataCanLoadNestedResources(){
//
//        FileData accounts = data.find("contacts").getAsFileData();
//        
//        assertTrue(accounts.isDirectory());
//    }
    
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
