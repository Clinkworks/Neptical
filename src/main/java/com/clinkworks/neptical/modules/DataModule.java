package com.clinkworks.neptical.modules;

import java.io.File;

import com.clinkworks.neptical.Data;
import com.clinkworks.neptical.data.file.FileData;
import com.google.inject.AbstractModule;

public class DataModule extends AbstractModule{
	
	public static final Data SYSTEM_DATA;
	static{
		File dataDir = new File(Thread.currentThread().getContextClassLoader().getResource("data").getFile().replace("%20", " "));
		SYSTEM_DATA = new FileData(null, null, null, null, dataDir);		
	}
	
	private final Data testData;
	
	public DataModule(){
		testData = SYSTEM_DATA.copyDeep();
	}

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		
	}
}
