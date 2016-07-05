package clinkworks.neptical.component;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.LookupFailureException;
import clinkworks.neptical.datatype.NepticalContext;
import clinkworks.neptical.datatype.NepticalData;
import clinkworks.neptical.domain.NSpace;

public interface CursorProvider extends Provider<Cursor>, NepticalContext{
	
	static final class CursorWrapper implements CursorProvider, Cursor {

		private static final Logger LOGGER = LoggerFactory.getLogger(CursorWrapper.class);
		
		private volatile ContextKey contextKey;

		CursorWrapper(ContextKey contextKey) {
			this.contextKey = contextKey;
		}

		CursorWrapper() {
		}

		@Override
		public Cursor moveTo(Location location) {
			LOGGER.warn("Could not move to " + location + " Cursor is not initalized.");
			get().moveTo(location);
			return this;
		}

		@Override
		public Location getLocation() {
			return get() == null ? null : get().getLocation();
		}

		@Override
		public Location find(String query) throws DataDefinitionException {
			return get().find(query);
		}

		@Override
		public Cursor get() {
			return Origin.getCursor();
		}

		@Override
		public NepticalData getData() {
			return get().getData();
		}

		@Override
		public ContextKey getContextKey() {
			return contextKey;
		}

		@Override
		public void setContextKey(ContextKey contextKey) {
			this.contextKey = contextKey;
		}

		@Override
		public URI getIdentity() {

			ContextKey contextKey = getContextKey();

			if (contextKey == null && get() == null) {
				return NSpace.NEPTICAL_SYSTEM_SPACE.getIdentity();
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

		@Override
		public Cursor moveTo(String path) throws DataDefinitionException {
			get().moveTo(path);
			return this;
		}

		@Override
		public Cursor moveUp() {
			get().moveUp();
			return this;
		}

		@Override
		public Cursor moveUp(int distance) {
			get().moveUp(distance);
			return this;
		}

		@Override
		public Cursor moveDown() {
			get().moveDown();
			return this;
		}

		@Override
		public Cursor moveDown(int distance) {
			get().moveDown(distance);
			return this;
		}

		@Override
		public Cursor moveRight() {
			get().moveRight();
			return this;
		}

		@Override
		public Cursor moveRight(int distance) {
			get().moveRight(distance);
			return this;
		}

		@Override
		public Cursor moveLeft() {
			get().moveLeft();
			return this;
		}

		@Override
		public Cursor moveLeft(int distance) {
			get().moveLeft(distance);
			return this;
		}

	}
	
}
