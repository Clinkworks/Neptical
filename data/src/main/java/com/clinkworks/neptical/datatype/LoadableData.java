package com.clinkworks.neptical.datatype;

import java.io.Serializable;

import com.clinkworks.neptical.api.NepticalData;

/**
 * The loadable node interface used for lazy loading purposes.
 * 
 * If this node is reached and isLoaded() returns false, load will act as a callback on your behalf.
 * 
 * @author ClinkWorks
 *
 */
public interface LoadableData extends NepticalData {
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
	public Serializable getLoaderCriterian();
	public void setLoaderCriterian(Serializable loaderCriterian);
	
	public boolean isLoaded();
	
	public void toggleLoadedTrue();
	
	public void toggleLoadedFalse();
	
}
