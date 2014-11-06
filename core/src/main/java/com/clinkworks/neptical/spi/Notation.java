package com.clinkworks.neptical.spi;

import static com.clinkworks.neptical.util.PathUtil.addSegment;
import static com.clinkworks.neptical.util.PathUtil.chompFirstSegment;
import static com.clinkworks.neptical.util.PathUtil.firstSegment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;

import com.clinkworks.neptical.datatype.Vocabulary;
import com.clinkworks.neptical.domain.GenericId;
import com.clinkworks.neptical.domain.Path;
import com.clinkworks.neptical.domain.PublicId;
import com.clinkworks.neptical.domain.Segment;

public abstract class Notation implements Vocabulary{

	public static class DotNotation extends Notation{

		@Override
		public Path parse(Serializable notation) {
			String path = notation.toString();
			return createPath(path);
		}

		private Path createPath(String path) {
			Path createdPath = createPath(createThreadsafePath(), null, path);
			
			if(createdPath.length() == 0){
				return null;
			}
			
			return createdPath;
		}
		
		private Path createPath(final Path createdPath, String processedPath, String remainingPath){
			
			if(StringUtils.isBlank(remainingPath)){
				return createdPath;
			}
			
			String currentSegment = firstSegment(remainingPath);
			remainingPath = chompFirstSegment(remainingPath);
	        processedPath = addSegment(processedPath, currentSegment);
	        		
			PublicId publicId = new PublicId(processedPath);
			GenericId<Serializable> genericId = new GenericId<Serializable>(processedPath);
			Segment newSegment = new Segment(publicId, genericId);
			
			createdPath.appendSegment(newSegment);
			
			return createPath(createdPath, processedPath, remainingPath);
		}

	}
	
	protected Path createThreadsafePath(){
		return new Path(Collections.synchronizedList(new ArrayList<Segment>()));
	}

}
