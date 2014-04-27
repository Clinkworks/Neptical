package com.clinkworks.neptical.data.api;

import com.clinkworks.neptical.data.datatypes.Data;
import com.clinkworks.neptical.data.datatypes.DataElementBase;

/**
 * This guy allows the internals to know about your classes, see DataElementBase for examples
 * 
 * much more documentation is nessesary to explain all this, but one day ill pull my notes together.
 * 
 *
 */
public class DataElement extends DataElementBase{

	public DataElement(Data backingData) {
		super(backingData);
	}

}
