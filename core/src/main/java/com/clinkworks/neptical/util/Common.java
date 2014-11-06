package com.clinkworks.neptical.util;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Common {
	
	private Common(){
		Common.noOp("This class is designed to be used as a static utility");
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
			return "";
		}
		return PathUtil.lastSegment(file.getName()).toLowerCase();
	}
	
    public static final String readFile(File file){
    	
    	BufferedReader reader = null;
    	StringBuffer sb = new StringBuffer();
    	
    	try{	
    		reader = new BufferedReader(new FileReader(file));
    	    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
    	    	sb.append(line);
    	    }
    	}catch(Exception e){
    	    throw new RuntimeException("Could not read file: " + e.getMessage(), e);
    	}finally{
    		try {
    			if(reader != null){
    				reader.close();
    			}
			} catch (IOException e) {
				System.out.println("Problem closing file... eating exception");
				e.printStackTrace();
			}
    	}
    	
    	return sb.toString();
    }	
	
}
