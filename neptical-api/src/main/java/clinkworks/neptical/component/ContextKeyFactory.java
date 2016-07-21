package clinkworks.neptical.component;

import java.net.URI;

import clinkworks.neptical.datatype.Location;

public interface ContextKeyFactory {
	
	ContextKey createLocationKey(URI location);
	
	ContextKey createLocationKey(Location location);
	
	ContextKey createContextKey(String context, Class<?> contextType);
	
	ContextKey createContextPartitionKey(int partition, String context, Class<?> contextType);
	
}
