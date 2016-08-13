package clinkworks.neptical.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.DataDefinitionException;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NepticalData;

abstract class CursorTemplate implements CursorProvider, Cursor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CursorTemplate.class);
	
	private volatile ContextKey contextKey;

	CursorTemplate(ContextKey contextKey) {
		this.contextKey = contextKey;
	}

	CursorTemplate() {
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