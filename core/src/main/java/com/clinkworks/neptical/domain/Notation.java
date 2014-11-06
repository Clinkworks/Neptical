package com.clinkworks.neptical.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.clinkworks.neptical.datatype.Vocabulary;
import com.clinkworks.neptical.util.PathUtil;

public abstract class Notation implements Vocabulary{

	public static class DotNotation extends Notation{

		@Override
		public Segment[] parse(Serializable notation) {
			String path = notation.toString();
			return createSegments(path);
		}

		private Segment[] createSegments(String path) {
			return createSegments(new ArrayList<Segment>(), null, path).toArray(new Segment[0]);
		}
		
		private List<Segment> createSegments(List<Segment> createdSegments, String processedPath, String remainingPath){
			
			if(StringUtils.isBlank(remainingPath)){
				return createdSegments;
			}
			
			String currentSegment = PathUtil.firstSegment(remainingPath);
			remainingPath = PathUtil.chompFirstSegment(remainingPath);
	        processedPath = PathUtil.addSegment(processedPath, currentSegment);
	        		
			PublicId publicId = new PublicId(processedPath);
			GenericId<Serializable> genericId = new GenericId<Serializable>(processedPath);
			Segment newSegment = new Segment(publicId, genericId);
			
			createdSegments.add(newSegment);
			
			return createSegments(createdSegments, processedPath, remainingPath);
		}

	}

}
