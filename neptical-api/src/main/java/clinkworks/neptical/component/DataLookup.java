package clinkworks.neptical.component;

public interface DataLookup {

	<T> T getInstance(Object context, Class<T> type);
	
}
