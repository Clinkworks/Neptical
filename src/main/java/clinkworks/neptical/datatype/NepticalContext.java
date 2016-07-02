package clinkworks.neptical.datatype;

import java.net.URI;

import clinkworks.neptical.component.ContextKey;

public interface NepticalContext {
	URI getIdentity();
	ContextKey getContextKey();
	void setContextKey(ContextKey contextKey);
}
