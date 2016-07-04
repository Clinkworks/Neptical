package clinkworks.neptical.component;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.UrlEscapers;

import clinkworks.neptical.datatype.Cursor;
import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.datatype.Location;
import clinkworks.neptical.datatype.NepticalData;

public class CursorLocation implements Location {

	private static final Logger LOGGER = LoggerFactory.getLogger(CursorLocation.class);

	private final String context;
	private final String fragment;
	private final String name;

	CursorLocation(String context, String fragment, String name) {
		this.context = context;
		this.fragment = fragment;
		this.name = name;
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

		String name = StringUtils.isBlank(name()) ? "" : "." + name();

		String uri = context() + "/" + fragment() + name;
		return URI.create(uri.replace(" ", "%20"));
	}

	@Override
	public Cursor moveCursorHere() {
		return Origin.getCursor().moveTo(this);
	}

	@Override
	public NepticalData getData() {
		Location previousLocation = Origin.getCursor().getLocation();
		Origin.getCursor().moveTo(this);

		NepticalData nepticalData = Origin.getCursor().getData();

		Origin.getCursor().moveTo(previousLocation);

		return nepticalData;
	}

	@Override
	public CursorContext getCursorContext() {
		return NSpaceManager.getCursorContext(this);
	}

	@Override
	public String toString() {
		return getResourceIdentity().toString();
	}

}
