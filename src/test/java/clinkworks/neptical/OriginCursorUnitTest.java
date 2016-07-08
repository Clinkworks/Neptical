package clinkworks.neptical;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clinkworks.neptical.component.NSpaceManager;
import clinkworks.neptical.component.Origin;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.domain.NSpace;

public class OriginCursorUnitTest {

	private NSpace originSpace;
	private Origin originCursor;
	
	private String module = "Column Select Test";
	private String[] columns =  new String[]{"Column1", "Column2", "Column3", "Column4"};
	
	private Object objectInModule1;
	private Object objectInModule2;
	private Object objectInModule3;
	
	private Object objectInColumn1Row1;
	
	private Object objectInColumn2Row1;
	
	private Object objectInColumn3Row1;
	private Object objectInColumn3Row2;
	
	private Object objectInColumn4Row3;
	
	@BeforeClass
	public static void clearCacheFromPreviousContexts(){
		/** REMEMBER TO DELETE THIS CRAP! **/
		NSpaceManager.clearCache();
	}
	
	@Before
	public void setup() throws DataDefinitionException{
		originCursor = (Origin)Data.getCursor();
		originSpace = NSpace.DEFAULT_NSPACE;
				
		objectInModule1 = new Object();
		objectInModule2 = new Object();
		objectInModule3 = new Object();
		
		objectInColumn1Row1 = new Object();
		objectInColumn2Row1 = new Object();
		objectInColumn3Row1 = new Object();
		objectInColumn4Row3 = objectInModule3;
		
		originSpace.addData("module1", "data", objectInModule1);
		originSpace.addData("module2", "data", objectInModule2);
		originSpace.addData("module3", "data", objectInModule3);
		
		originSpace.addData(module, columns[0], objectInColumn1Row1);
		
		originSpace.addData(module, columns[1], objectInColumn2Row1);
		
		originSpace.addData(module, columns[2], objectInColumn3Row1);
		originSpace.addData(module, columns[2], objectInColumn3Row2);

		originSpace.addData(module, columns[3], null);
		originSpace.addData(module, columns[3], null);
		originSpace.addData(module, columns[3], "{{data}}");
		
	}
	
	@Test
	public void cursorCanMoveToALocationWithinSpace() throws DataDefinitionException{
		Object object2InModule3 = new Object();
		originSpace.addData("module3", "data", object2InModule3);
		
		Location module3RowTwoLocation = originCursor.find("data");
		
		assertEquals(object2InModule3, module3RowTwoLocation.getData().get());
		
	}
	
	@Test
	public void movingRightStartingAtASegmentInSpaceWillSelectTheFirstFragmentWithinIt() throws DataDefinitionException{
		
		originCursor.moveTo(module);
		
		assertEquals(module, originCursor.getLocation().fragment());
		assertEquals("/", originCursor.getLocation().name());
		
		Location column1Location = originCursor.moveRight().getLocation();
		
		assertEquals(module, column1Location.fragment());
		assertEquals(columns[0], column1Location.name());
		assertEquals("default/Column%20Select%20Test.Column1", column1Location.getResourceIdentity().toString());
		assertEquals("default/Column Select Test.Column1", column1Location.toString());
		assertEquals("default/Column Select Test.Column1", originCursor.toString());
		
	}
	
	@Test
	public void movingLeftFromAStartingColumnWillTakeYouToTheModule() throws DataDefinitionException{
		originCursor.moveTo(module);
		originCursor.moveLeft();
		assertEquals("neptical://default", originCursor.toString());
	}

	public Object getObjectInColumn4Row3() {
		return objectInColumn4Row3;
	}
	
}
