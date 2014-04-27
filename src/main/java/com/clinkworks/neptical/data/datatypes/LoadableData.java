package com.clinkworks.neptical.data.datatypes;

import java.io.Serializable;

/**
 * The loadable node interface used for lazy loading purposes.
 * 
 * If this node is reached and isLoaded() returns false, load will act as a callback on your behalf.
 * 
 * @author ClinkWorks
 *
 */
public interface LoadableData extends Data {
	/**
	 * Returned from this method is the type to load. Any identifier listed in the NepticalDataModule
	 * will suffice
	 * 
	 * An example, if you want to lazy load a file:
	 * 
	 * public FileNode implements LoadableNode{
	 *      public Serializable dataToLoad(){
	 *          return File.class;
	 *      }
	 * }
	 * 
	 * @param typeToLoad
	 */
	public abstract Serializable getLoaderCriterian();
	
	public abstract boolean isLoaded();
	
	public abstract void toggleLoadedTrue();
	
	public abstract void toggleLoadedFalse();
}
