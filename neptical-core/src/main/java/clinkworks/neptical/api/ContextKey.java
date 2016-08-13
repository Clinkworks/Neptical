package clinkworks.neptical.api;

import java.net.URI;
import java.util.Objects;

import static clinkworks.neptical.Constants.ContextSymbols.IN_CONTEXT;
import static clinkworks.neptical.Constants.ContextSymbols.IN_PARTITION;
import clinkworks.neptical.datatype.CursorContext;

public class ContextKey {
	
	private static final int GENERIC_CONTEXT = -1;
	
	private final String contextName;
	private final Class<?> contextType;
	
	/** Simply a way to resolve specific context issues when using some inherited key types **/
	private final int partition;
	
	ContextKey(URI contextIdentity){
		this(contextIdentity.toString(), CursorContext.class);
	}
	
	ContextKey(String contextName, Class<?> contextType) {
		this(GENERIC_CONTEXT, contextName, contextType);
	}

	ContextKey(int partitionId, String context, Class<?> contextType){
		this.contextName = context;
		this.contextType = contextType;
		partition = partitionId == -1 ? getClass().hashCode() : partitionId;
	}
	
	@Override
	public boolean equals(Object object) {

		if (object == null || !(object instanceof ContextKey)) {
			return false;
		}

		if(object == this){
			return true;
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
		return new StringBuilder().append(contextType).append(IN_PARTITION).append(partition).append(IN_CONTEXT).append(contextName).toString();
	}

	public String name() {
		return contextName;
	}

	public Class<?> contextType() {
		return contextType;
	}
}