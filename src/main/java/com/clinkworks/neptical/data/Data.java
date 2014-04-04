package com.clinkworks.neptical.data;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.clinkworks.neptical.data.file.FileData;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;

/**
 * DataPath
 *   This is a link between java and your test data.
 *   
 *   The behavior
 *   
 *      any file from resources/testData/ is cataloged.
 *      
 *      the directory tree is then scanned for any files recognized by
 *      Data.
 * 		
 * 		At this point, you are ready to grab all the data in the resources directory on demand.
 *      
 *  -Example
 *  --- /resources/data/account-api/accounts.json
 *  {
   "bank-accounts":{
      "badBalance":{
         "balance":-5
      }
   },
   "testAccount":{
      "accountNumber":"TEST_ACCOUNT_NUMBER",
      "firstName":"Bob",
      "lastName":"Smith",
      "otherAccounts" : 
      	['account1',['account2'],'account3'];
   }
}


public class Test{

   @DataPath("account-api.accounts.testAccount")
   private Data data;
   
   @DataPath("account-api.accounts.testAccount.lastName")
   @Test
   public void validateFirstName(String lastName){
      assertEquals(lastName, data.get("lastName");
      assertEquals("account2", testAccount.find("otherAccounts[0][0]").getAsString()); 
      assertEquals("account3", testAccount.find("otherAccounts[1]").getAsString());
   }
   
   public static class Account{
      private String accountNumber;
      private int currentBalance;
      private List<String> otherAccounts;
      
      //getters and setters
   }
   
   @DataPath("account-api.accounts", @DataAppend("testAccount.otherAccounts, "/bank-accounts.badBalance))
   @Test
   public void validateLinkedAccounts(@DataAs(Account.class) Account account){
   		assertEquals(-5, account.getBalance());
   }

//or more simply
 *  --- /resources/data/account-api/accounts.json
 *  {
 * "bob:{
 	  "accountNumber":"TEST_ACCOUNT_NUMBER",
      "firstName":"Bob",
      "lastName":"Smith",
 * },
   "bobWithAGoodBalance":{
      "{{accounts.bob}}" : {
      	"balance" : "{{/balances.good-balance}}"
      }
   },
   "bobWithABadBalance":{
      "{{accounts.bob}}" : {
      	"balance" : "/balances.bad-balance"
      }
   }
}

*  --- /resources/data/account-api/balances.json
*  {
*     {bad-balance : -5},
*     {good-balance : 5}
*  }

@DataPath("account-api")
public test{
  @DataPath("accounts.bobWithAGoodbalance", DataAs(Account.class))
  private Account badBob;
  
  @DataPath("accounts.bobWithABadbalance", DataAs(Account.class))
  private Account goodBob;
  
  @Test
  public void testBobsAccountData(){
     assertEquals(-5, badBob.getBalance());
     assertEquals(5, goodBob.getBalance());
     assertEquals(badBob.getAccountNumber(), goodbob.getAccountNumber());
  }
  
}

most of the time the data you need doesn't require this much work,
the test data is contained in the data class and can be used at will

public test{
  @Test
  public void doTest(){
     assertEquals(5, Data.find("account-api.accounts.bobWithAGoodBalance").getAsInt());
  }
  
  @DataAppend(path = "account-api.accounts", "{"inline-modification" : "is supported"}")
  @Test
  public void testInlineSupport(){
     Data data = Data.find("account-api.accounts.inline-modification");
     assertEquals("is supported", data.getAsString());
  }
}



 *
 */
public abstract class Data implements Iterable<Data>{

	public static final String DOT = ".";
	
	public @interface DataProperty {}

	public abstract Data find(String path);
	public abstract <T> T get(Class<T> type);
	public abstract <T> List<T> getList(Class<T> type);
		
	private static final Set<String> LOADED_PATHS;
	static{
		LOADED_PATHS = Sets.newConcurrentHashSet();
	}
	
	private final String path;
	private final String segment;
	private Data head;
	private Data parent;
	private Data next;
	private final Set<Data> children;
	private Object data;
	
	public Data(String segment, String path, Data head, Data parent, Object data){
		this.path = path;
		this.data = data;
		this.segment = segment;
		this.parent = parent;
		this.head = head;
		children = Sets.newConcurrentHashSet();
	}
	
	public Object get(){
		return data;
	}
	
	final protected Data getHead(){
		return head;
	}
	
	final protected Data getNext(){
		return next;
	}
	
	final protected String getSegment(){
		return segment;
	}
	
	final protected String getPath(){
		return path;
	}
	
	final protected void addChild(Data child){
		children.add(child);
	}
	
	final protected void removeChild(Data child){
		children.remove(child);
	}
	
	final protected Set<Data> getChildren(){
		return children;
	}
	

	public JsonElement getAsJsonElement(){
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}
	
	public boolean isFileData() {
		return this instanceof FileData;
	}

	public boolean isPrimitive() {
		return false;
	}
	
	public boolean getAsBoolean() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public Number getAsNumber() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public String getAsString() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public double getAsDouble() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public float getAsFloat() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public long getAsLong() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public int getAsInt() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public byte getAsByte() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public char getAsCharacter() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public BigDecimal getAsBigDecimal() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public BigInteger getAsBigInteger() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}

	public short getAsShort() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}
	
	public DataIterator dataIterator(){
		return new DataIterator();
	}
	
	public Iterator<Data> iterator(){
		return new DataIterator();
	}
	
	public class DataIterator implements ListIterator<Data> {

		@Override
		public boolean hasNext() {
			return getNext() != null;
		}
		
		@Override
		public Data next() {
			return next;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException("Will support soon.. I promise");
		}
		@Override
		public boolean hasPrevious() {
			return parent != null;
		}
		@Override
		public Data previous() {
			return parent;
		}
		@Override
		public int nextIndex() {
			throw new UnsupportedOperationException();
		}
		@Override
		public int previousIndex() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void set(Data replacementNode) {
			data = replacementNode.get();
			getChildren().clear();
			getChildren().addAll(replacementNode.getChildren());
		}
		
		@Override
		public void add(Data data) {
			getChildren().add(data);
		}
	}

}
