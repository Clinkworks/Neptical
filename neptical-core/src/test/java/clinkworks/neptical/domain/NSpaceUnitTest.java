package clinkworks.neptical.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clinkworks.neptical.Data;
import clinkworks.neptical.component.NSpaceManager;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.NepticalData;

public class NSpaceUnitTest {

	private GenericSpace nspace;
	private Object object1InModule1;
	private Object objectInModule2;
	private Object objectInModule3;
	
	@Before
	public void setup() throws DataDefinitionException{
		
		nspace = new GenericSpace("default", "module1", "module2", "module3");
		
		object1InModule1 = new Object();
		objectInModule2 = new Object();
		objectInModule3 = new Object();
		
		nspace.addData("module1", "data", object1InModule1);
		nspace.addData("module2", "data", objectInModule2);
		nspace.addData("module3", "data", objectInModule3);
		
	}
	
	@After
	public void clearCache(){
		NSpaceManager.clearCache();
	}
	
	@Test
	public void nSpacesReportThierIdentityCorrectly() throws URISyntaxException{
		URI uri = new URI("neptical://default");
		
		assertEquals(uri, nspace.getResourceIdentity());
	}
	
	@Test
	public void whenAFragmentDoesNotExistANullDataIsReturned(){
		assertSame(NepticalData.NULL_DATA, nspace.getDataAt("non existant fragment", 0));
	}
	
	@Test
	public void nSpacesCascadesModulesInReverseOrder() throws DataDefinitionException{
		
		assertSame(objectInModule3, nspace.getDataAt("data", 0).get());
		
		nspace.addModule("module1");
		assertSame(object1InModule1, nspace.getDataAt("data", 0).get());
		
		nspace.defineModules("module3", "module1", "module2");
		
		assertSame(objectInModule2, nspace.getDataAt("data", 0).get());
		
	}

	@Test
	public void whenAModuleIsMissingASegmentTheNextModuleIsAttempted() throws DataDefinitionException{
	
		nspace.addModule("module4");
		Object objectInModule4 = new Object();
		
		assertSame(objectInModule3, nspace.getDataAt("data", 0).get());
		
		nspace.addData("data", objectInModule4);
		assertSame(objectInModule4, nspace.getDataAt("data", 0).get());
		
		nspace.addModule("module5");
		assertSame(objectInModule4, nspace.getDataAt("data", 0).get());
		
	}
	
	@Test
	public void ifAFragmentIsIntentionallyBlankTheNSpaceShouldTreatItAsANull() throws DataDefinitionException{
		nspace.addModule("BlankTestModule");
		nspace.addData("data", Data.BLANK);
		assertNull(nspace.getDataAt("data", 0).get());
		
		nspace.addModule("module2");
		assertSame(objectInModule2, nspace.getDataAt("data", 0).get());
	}
	
}
