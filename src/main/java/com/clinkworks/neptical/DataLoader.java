package com.clinkworks.neptical;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.io.File;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.clinkworks.neptical.data.Data;
import com.google.inject.BindingAnnotation;

public interface DataLoader {
	
	@BindingAnnotation 
	@Target({ FIELD, METHOD }) 
	@Retention(RUNTIME)
	public @interface TestData{
		String value();
	}
	
	public Data loadData(String segment, String path, Data root, Data parent, File file);
	
}
