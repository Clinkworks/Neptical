package clinkworks.neptical.datatype;

import java.net.URI;

import clinkworks.neptical.api.Segment;
import clinkworks.neptical.component.ContextKey;

public interface NepticalContext{

  ContextKey getContextKey();

  URI getIdentity();

  Segment[] segments();

  DataContext[] templates();


}
