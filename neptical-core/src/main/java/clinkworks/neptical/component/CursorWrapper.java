package clinkworks.neptical.component;

import java.net.URI;
import java.net.URISyntaxException;

import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.LookupFailureException;
import clinkworks.neptical.domain.GenericSpace;

public class CursorWrapper extends CursorTemplate {
	
	CursorWrapper(ContextKey contextKey) {
		super(contextKey);
	}
	
	public CursorWrapper() {
		super();
	}
	
	@Override
	public Cursor get() {
		return Origin.getCursor();
	}

	@Override
	public URI getIdentity() {

		ContextKey contextKey = getContextKey();

		if (contextKey == null && get() == null) {
			return GenericSpace.NEPTICAL_SYSTEM_SPACE.getIdentity();
		}

		if (getContextKey() == null) {
			return get().getLocation().getResourceIdentity();
		}
		try {
			return new URI(getContextKey().name());
		} catch (URISyntaxException e) {
			throw new LookupFailureException(e.getMessage(), e);
		}

	}

	@Override
	public String toString() {
		return get().toString();
	}

}
