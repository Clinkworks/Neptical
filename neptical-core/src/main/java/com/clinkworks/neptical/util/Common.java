package com.clinkworks.neptical.util;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class Common {
	
	private Common(){
		noOp("This class is designed to be used as a static utility");
	}
	
	public static final void noOp(){
		assertTrue(true);
	}
	
	/**
	 * utilize this method if you wish to convey to the reader why you chose a no-op
	 * @param message
	 */
	public static final void noOp(String message){
		assertTrue(true);
	}
	
	public static final void noOp(String... message){
		assertTrue(true);
	}
	
	public static final boolean hasExtension(File file){
		return file.getName().lastIndexOf(PathUtil.DOT) > 0;
	}
	
	public static final String getExtension(File file){
		if(!hasExtension(file)){
			return PathUtil.EMPTY_STRING;
		}
		return PathUtil.lastSegment(file.getName()).toLowerCase();
	}
	
    public static final String readFile(File file){
    	try {
			return read(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
    }
    
	public static String readUrl(URL url) {
		try {
			return read(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
    private static final String read(Reader reader){
    	BufferedReader bufferedReader = new BufferedReader(reader);
    	StringBuffer stringBuffer = new StringBuffer();
    	
    	try{
		    for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
		    	stringBuffer.append(line);
		    }
    	}catch(IOException e){
    		throw new RuntimeException(e);
    	}finally{
    		try {
				bufferedReader.close();
			} catch (IOException e) {
				noOp("Eating the exception");
			}
    	}
    	return stringBuffer.toString();
    }


}
