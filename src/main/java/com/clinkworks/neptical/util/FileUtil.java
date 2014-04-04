package com.clinkworks.neptical.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FileUtil {
	
	public static class FlatFile{

		private final List<Record> records;
		private final RecordSettings recordSettings;
		
		public FlatFile(RecordSettings recordSettings){
			records = Lists.newArrayList();
			this.recordSettings = recordSettings;
		}
		
		public void readFile(File file){
			_readFile(file);
		}
		
		public Record get(int index){
			return records.get(index);
		}
		
		public Record remove(int index){
			return records.remove(index);
		}
		
		public boolean remove(Record record){
			return records.remove(record);
		}
		
		public Record add(String line){
			Record record = new Record(line, recordSettings);
			records.add(record);
			return record;
		}
		
		public void write(String path){
			throw new UnsupportedOperationException("Coming soon");
		}
		
		public static RecordBuilder createRecordBuilder(RecordSettings recordSettings){
			return new RecordBuilder(recordSettings);
		}
		
		public static Record readLine(RecordSettings recordSettings, String line){
			return createRecordBuilder(recordSettings).readLine(line);
		}
		
		public static class RecordBuilder{
			private final RecordSettings recordSettings;
			
			public RecordBuilder(RecordSettings recordSettings){
				this.recordSettings = recordSettings;
			}
			
			public Record readLine(StringBuffer line){
				return new Record(line, recordSettings);
			}
			
			public Record readLine(String line){
				return new Record(line, recordSettings);
			}
		}
		
		private static class MainframeField{
			private final int start;
			private final int end;
			
			public MainframeField(int startPos, int endPos){
				this.start = startPos;
				this.end = endPos;
			}
			
			public int getStart(){
				return start;
			}
			
			public int getEnd(){
				return end;
			}
			
			public int getLength(){
				return start + end;
			}
		}
		
		public static class RecordSettings{
			private final Map<String, MainframeField> fields;
			
			public RecordSettings(){
				fields = Maps.newHashMap();
			}
			
			public RecordSettings addField(String fieldName, int start, int end){
				fields.put(fieldName, new MainframeField(start, end));
				return this;
			}
			
			public MainframeField getField(String fieldName){
				return fields.get(fieldName);
			}
			
			public Map<String, MainframeField> getFields(){
				return fields;
			}
			
		}
		
	    public static class Record{
			private final StringBuffer line;
			private final RecordSettings recordSettings;
			
			protected Record(String line, RecordSettings recordSettings){
				this.line = new StringBuffer(line);
				this.recordSettings = recordSettings;
			}
			
			protected Record(StringBuffer line, RecordSettings recordSettings){
				this.line = line;
				this.recordSettings = recordSettings;
			}
			
			public String set(String fieldName, String data){
				MainframeField field = recordSettings.getField(fieldName);
				if(field == null){
					throw new IllegalStateException("Field " + fieldName + " is not defined.");
				}
				String newData = StringUtils.rightPad(data, field.getLength());
				line.replace(field.getStart(), field.getEnd(), newData);
				return data.trim();
			}
			
			public String get(String fieldName){
				MainframeField field = recordSettings.getField(fieldName);
				if(field == null){
					throw new IllegalStateException("Field " + fieldName + " is not defined.");
				}
				return line.substring(field.getStart(), field.getEnd()).trim();
			}
		}
	    
	    private void _readFile(File file){
	    	
	    	BufferedReader reader = null;
	    	
	    	try{	
	    		reader = new BufferedReader(new FileReader(file));
	    	    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
	    	    	records.add(new Record(line, recordSettings));
	    	    }
	    	}catch(Exception e){
	    	    throw new RuntimeException("Could not read file", e);
	    	}finally{
	    		try {
					reader.close();
				} catch (IOException e) {
					System.out.println("Problem closing file... eating exception");
					e.printStackTrace();
				}
	    	}
	    }
	}
}
