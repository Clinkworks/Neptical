package clinkworks.neptical.api;

import java.net.URI;

import javax.inject.Provider;

import org.apache.commons.lang.StringUtils;

import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NepticalData;

public class CursorLocation implements Location {

	private final String context;
	private final String fragment;
	private final String name;
	
	private final Provider<Cursor> cursorProvider;

	CursorLocation(Provider<Cursor> cursorProvider, String context, String fragment, String name) {
		this.context = context;
		this.fragment = fragment;
		this.name = name;
		this.cursorProvider = cursorProvider;
	}

	@Override
	public String context() {
		return context;
	}

	@Override
	public String fragment() {
		return fragment;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public URI getResourceIdentity() {

		String actualName = name();

		if(!StringUtils.isBlank(actualName) && !actualName.equals("/") && !fragment().endsWith("/")){
			actualName = "." + actualName; 
		}
		
		String uri = context() + "/" + fragment() + actualName;
		return URI.create(uri.replace(" ", "%20"));
	}

	@Override
	public Cursor moveCursorHere() {
		return cursor().moveTo(this);
	}

	@Override
	public NepticalData getData() {
		Location previousLocation = cursor().getLocation();
		cursor().moveTo(this);

		NepticalData nepticalData = cursor().getData();

		cursor().moveTo(previousLocation);

		return nepticalData;
	}


	@Override
	public String toString() {
		return getResourceIdentity().toString().replace("%20", " ");
	}

	private Cursor cursor(){
		return cursorProvider.get();
	}
}
