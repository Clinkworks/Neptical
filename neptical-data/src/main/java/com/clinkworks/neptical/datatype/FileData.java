package com.clinkworks.neptical.datatype;

import java.io.File;

public interface FileData extends LoadableData, MutableData{
	public File getAsFile();
	public boolean isDirectory();
	public boolean isFile();
}
