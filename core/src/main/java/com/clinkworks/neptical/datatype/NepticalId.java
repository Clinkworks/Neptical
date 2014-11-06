package com.clinkworks.neptical.datatype;

import java.io.Serializable;

import org.apache.commons.lang3.ObjectUtils;


/**
 * Note:
 *   the id is  hashed with:
 *      - The fully qualfied class name represented by the implemented type
 *      - The hashCode of the stored id
 *      
 *  This allows for multiple key types with the same id.
 *  
 *  - NOTE: equals implementation works on the key's serializable value and not this hashcode
 *    this is explained in greater depth in the graph api.
 *  
 *  example:
 *     class MyId extends NepticalId<String>;
 *     class MyOtherId extends NepticalId<String>;
 *     
 *   int myIdHashCode = new MyId("String").hashCode();
 *   int myOtherIdHashCode = new MyOtherId("String").hashCode();
 *  assertNotEqual(myIdHashCode, myOtherIdHashCode);
 *  assertEqual(new MyId("String"), new MyOtherId("String"));
 *  
 *  reasoning:
 *  
 *  class DeletedId extends NepticalId<String>
 *  
 *  Map<? extends NepticalId, Object> idLookup;
 *  
 *  Object importantObject;
 *  Object someOtherObject;
 *  
 *  idLookup.put(myId, importantObject);
 *  idLookup.put(myOtherId, someOtherObject);
 *  
 *  //by implementing hashcode and equals this way, it allows flexibility to the developer
 *  
 *  now I can lookup the important object in a totally different context, allowing a bit of control
 *  
 *  Object importantObjectToBackup = idLookup.get(new MyId("String"));
 *  idLookup.remove(new MyId("String"));
 *  
 *  idLookup.put(new DeletedId("String"), importantObjectToBackup);
 *  assertEqual(new MyId("String"), new DeletedId("String"));
 *  
 *  
 *  
 *  idLookup.get(new 
 *  
 */
public abstract class NepticalId<T extends Serializable> {
	
	private final T id;
	
	public NepticalId(T id){
		this.id = id;
	}
	
	public T get(){
		return id;
	}
	

	@Override
	public int hashCode(){
		//Note: 
		//   the id is hashed with: 
		//   * the fully qualfied class name of the NepticalId subtype 
		//   * the hash value of the contained JavaType
		return ObjectUtils.hashCodeMulti(get(), getClass().getName());
	}
	
	@Override
	public boolean equals(Object object){
		
		if(object == null){
			return false;
		}
		
		if(object instanceof NepticalId<?>){
			return get().equals(((NepticalId<?>)object).get());
		}
		
		return get().equals(object);
	}
	
	@Override
	public String toString(){
		return ObjectUtils.toString(get(), super.toString());
	}
	
}
