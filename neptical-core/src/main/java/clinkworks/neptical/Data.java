package clinkworks.neptical;

import clinkworks.neptical.component.Origin;
import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.NepticalData;

public abstract class Data {

	
	/**
	 * Represents an empty space where an empty space is desired. This will stop slices and modules from accidently overriding the location.
	 *   - see NSpaceUnitTest
	 *   
	 */
	public static final NepticalData BLANK = new BlankData();
	
	public static Cursor getCursor(){
		return Origin.getCursor();
	}
	
	public NepticalData getData(){
		return BLANK;
	}

	public static final class BlankData implements NepticalData{

		private BlankData(){}
		
		@Override
		public Object get() {
			return null;
		}

		@Override
		public Class<?> getDataType() {
			return BlankData.class;
		}
		
	}


}
