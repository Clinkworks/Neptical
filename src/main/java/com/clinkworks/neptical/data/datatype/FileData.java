package com.clinkworks.neptical.data.datatype;

import java.io.File;

import com.clinkworks.neptical.data.api.NepticalData;

public interface FileData extends NepticalData, LoadableData, MutableData{
	public File getAsFile();
	public boolean isDirectory();
	public boolean isFile();
}
