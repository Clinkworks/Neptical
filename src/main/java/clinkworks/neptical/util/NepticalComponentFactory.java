package clinkworks.neptical.util;

import clinkworks.neptical.component.ContextKey;
import clinkworks.neptical.component.ContextKeyFactory;
import clinkworks.neptical.datatype.DataModule;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.domain.NSpace;

public abstract class NepticalComponentFactory implements ContextKeyFactory{
	
	public static interface ContextKeyComponentFactory{
		public ContextKey createContextKey(NSpace nspace) ;
		public ContextKey createContextKey(Location location);
		ContextKey createContextKey(DataModule dataModule);	
	}

	@Override
	public ContextKey createContextKey(String context, Class<?> contextType) {
		return null;
	}

	@Override
	public ContextKey createContextPartitionKey(int partition, String context, Class<?> contextType) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
