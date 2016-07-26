package clinkworks.neptical.datatype;

import javax.inject.Provider;

import clinkworks.neptical.api.DataModule;
import clinkworks.neptical.api.Location;

public interface NSpace extends NepticalContext, DataModule, Location, Provider<Cursor>{

}
