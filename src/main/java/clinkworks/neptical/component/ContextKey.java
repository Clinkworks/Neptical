package clinkworks.neptical.component;

import java.net.URI;
import java.util.Objects;

import clinkworks.neptical.datatype.CursorContext;
import clinkworks.neptical.util.Constants;

public class ContextKey {
	
	private final String contextName;
	private final Class<?> contextType;
	
	/** Simply a way to resolve specific context issues when using some inherited key types **/
	private final int partition;
	
	ContextKey(URI contextIdentity){
		this(CursorContext.class.hashCode(), contextIdentity.toString(), CursorContext.class);
	}
	
	ContextKey(String contextName, Class<?> contextType) {
		this(-1, contextName, contextType);
	}

	ContextKey(int partitionId, String contextName, Class<?> contextType){
		this.contextName = contextName;
		this.contextType = contextType;
		partition = partitionId == -1 ? getClass().hashCode() : partitionId;
	}
	
	@Override
	public boolean equals(Object object) {

		if (object == null || !(object instanceof ContextKey)) {
			return false;
		}

		ContextKey that = (ContextKey) object;

		return that == this || this.name().equals(that.name()) && this.contextType().equals(that.contextType());
	}

	@Override
	public int hashCode() {
		return Objects.hash(partition, name(), contextType());
	}
	
	@Override
	public String toString(){
		return contextName + Constants.DOT + partition + "@" + contextType.getSimpleName();
	}

	public String name() {
		return contextName;
	}

	public Class<?> contextType() {
		return contextType;
	}
}