package clinkworks.neptical.component;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import clinkworks.neptical.api.CursorContext;
import clinkworks.neptical.api.DataModule;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.domain.GenericDataModule;

public class CursorContextUnitTest{

  private DataModule dataModule;

  @Before
  public void setup() throws DataDefinitionException{

    dataModule = new GenericDataModule("module1");
    dataModule.addData("column1", null);
    dataModule.addData("column2", null);
    dataModule.addData("column3", null);
    dataModule.addData("column4", null);
    dataModule.addData("column5", null);

    NSpaceManager.addModule(dataModule);
  }

  @Test
  public void whenAContextIsCreatedForADataModuleItsFragmentsAndTemplatesAreCorrectlyReflected(){

    CursorContext cursorContext = NSpaceManager.getCursorContext(dataModule);

    String[] segments = cursorContext.segments();
    String[] templates = cursorContext.templates();

    assertEquals(5, segments.length);
    assertEquals(1, templates.length);


  }


}
