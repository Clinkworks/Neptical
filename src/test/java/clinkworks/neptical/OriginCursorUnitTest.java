package clinkworks.neptical;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import clinkworks.neptical.component.Origin;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.domain.NSpace;
import mockit.Deencapsulation;

public class OriginCursorUnitTest {

	private NSpace originSpace;
	private Origin originCursor;
	
	private Object objectInModule1;
	private Object objectInModule2;
	private Object objectInModule3;
	
	@Before
	public void setup() throws DataDefinitionException{
		originCursor = (Origin)Data.getCursor();
		originSpace = Deencapsulation.getField(originCursor, "rootLocation");
		originSpace.defineModules("module1", "module2", "module3");
		
		objectInModule1 = new Object();
		objectInModule2 = new Object();
		objectInModule3 = new Object();
		
		
		originSpace.addData("module1", "data", objectInModule1);
		originSpace.addData("module2", "data", objectInModule2);
		originSpace.addData("module3", "data", objectInModule3);
		
	}
	
	@Test
	public void cursorCanMoveToALocationWithinSpace() throws DataDefinitionException{
		Object object2InModule3 = new Object();
		originSpace.addData("module3", "data", object2InModule3);
		
		Location module3RowTwoLocation = originCursor.find("data");
		
		assertEquals(object2InModule3, module3RowTwoLocation.getData().get());
		
	}
	
	@Test
	public void cursorCanSelectColumnsWithinAModule() throws DataDefinitionException{
		
		
		
		String module = "Column Select Test";
		String[] columns = new String[]{"Column1", "Column2", "Column3"};
		
		originSpace.addModule(module);
		originSpace.addData(columns[0], objectInModule1);
		originSpace.addData(columns[1], objectInModule2);
		originSpace.addData(columns[1], objectInModule3);
		
		DataModule datatestLocation = originSpace.getDataModule(module);
		
		originCursor.moveToLocation(originSpace);
		
		
		
		
	}
	
	
}
