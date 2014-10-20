package com.clinkworks.neptical.datatype;

import java.io.File;

import com.clinkworks.neptical.api.Cursor;
import com.clinkworks.neptical.api.NepticalData;

public interface FileData extends Cursor, LoadableData, MutableData{
	public File getAsFile();
	public boolean isDirectory();
	public boolean isFile();
}
