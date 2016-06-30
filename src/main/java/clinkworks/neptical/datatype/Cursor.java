package clinkworks.neptical.datatype;

import java.util.List;
import java.util.concurrent.Callable;

import clinkworks.neptical.component.CursorProvider;
import clinkworks.neptical.domain.NSpace;

public interface Cursor {
	
	Cursor moveToLocation(Location location);

	Location getLocation();
	
	Location find(String query) throws DataDefinitionException;

	NepticalData getData();

	String[] getColumns(CursorContext genericCursorContext);
	
	public static interface SystemCursor extends Callable<Location>, CursorProvider, Cursor{
		NSpace currentNSpace();
		List<DataModule> activeModules();
		CursorContext activeCursorContext();
	}
	
}
