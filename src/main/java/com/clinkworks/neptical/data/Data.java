package com.clinkworks.neptical.data;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.clinkworks.neptical.data.file.FileData;
import com.google.gson.JsonElement;

/**
 * DataPath This is a link between java and your test data.
 * 
 * The behavior
 * 
 * any file from resources/testData/ is cataloged.
 * 
 * the directory tree is then scanned for any files recognized by Data.
 * 
 * At this point, you are ready to grab all the data in the resources directory
 * on demand.
 * 
 * -Example --- /resources/data/account-api/accounts.json { "bank-accounts":{
 * "badBalance":{ "balance":-5 } }, "testAccount":{
 * "accountNumber":"TEST_ACCOUNT_NUMBER", "firstName":"Bob", "lastName":"Smith",
 * "otherAccounts" : ['account1',['account2'],'account3']; } }
 * 
 * 
 * public class Test{
 * 
 * @DataPath("account-api.accounts.testAccount") private Data data;
 * @DataPath("account-api.accounts.testAccount.lastName")
 * @Test public void validateFirstName(String lastName){ assertEquals(lastName,
 *       data.get("lastName"); assertEquals("account2",
 *       testAccount.find("otherAccounts[0][0]").getAsString());
 *       assertEquals("account3",
 *       testAccount.find("otherAccounts[1]").getAsString()); }
 * 
 *       public static class Account{ private String accountNumber; private int
 *       currentBalance; private List<String> otherAccounts;
 * 
 *       //getters and setters }
 * @DataPath("account-api.accounts",
 * @DataAppend("testAccount.otherAccounts, "/bank -accounts.badBalance))
 * @Test public void validateLinkedAccounts(@DataAs(Account.class) Account
 *       account){ assertEquals(-5, account.getBalance()); }
 * 
 *       //or more simply --- /resources/data/account-api/accounts.json { "bob:{
 *       "accountNumber":"TEST_ACCOUNT_NUMBER", "firstName":"Bob",
 *       "lastName":"Smith", }, "bobWithAGoodBalance":{ "{{accounts.bob}}" : {
 *       "balance" : "{{/balances.good-balance}}" } }, "bobWithABadBalance":{
 *       "{{accounts.bob}}" : { "balance" : "/balances.bad-balance" } } }
 * 
 *       --- /resources/data/account-api/balances.json { {bad-balance : -5},
 *       {good-balance : 5} }
 * @DataPath("account-api") public test{
 * @DataPath("accounts.bobWithAGoodbalance", DataAs(Account.class)) private
 *                                           Account badBob;
 * @DataPath("accounts.bobWithABadbalance", DataAs(Account.class)) private
 *                                          Account goodBob;
 * @Test public void testBobsAccountData(){ assertEquals(-5,
 *       badBob.getBalance()); assertEquals(5, goodBob.getBalance());
 *       assertEquals(badBob.getAccountNumber(), goodbob.getAccountNumber()); }
 * 
 *       }
 * 
 *       most of the time the data you need doesn't require this much work, the
 *       test data is contained in the data class and can be used at will
 * 
 *       public test{
 * @Test public void doTest(){ assertEquals(5,
 *       Data.find("account-api.accounts.bobWithAGoodBalance").getAsInt()); }
 * @DataAppend(path = "account-api.accounts", "{"inline-modification" : "is
 *                  supported"}")
 * @Test public void testInlineSupport(){ Data data =
 *       Data.find("account-api.accounts.inline-modification");
 *       assertEquals("is supported", data.getAsString()); } }
 * 
 * 
 * 
 * 
 */
public abstract class Data{

    public static final String DOT = ".";

    public @interface DataProperty {}

    public abstract Data find(String path);

    private final String path;
    private final String segment;
    private final Data root;
    private final Data parent;
    private Data next;
    private Object data;

    protected Data(String segment, String path, Data root, Data parent, Object data) {
        this.path = path;
        this.data = data;
        this.segment = segment;
        this.parent = parent;
        this.root = root;
        if (root == null) {
            root = this;
        }
    }

    public final Object get() {
        return data;
    }

    public <T> T get(Class<T> type){
    	throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public <T> List<T> getList(Class<T> type){
    	throw new UnsupportedOperationException(getClass().getSimpleName());
    }
    
    public JsonElement getAsJsonElement() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public boolean isFileData() {
        return this instanceof FileData;
    }

    public File getAsFile(){
    	throw new UnsupportedOperationException(getClass().getSimpleName());
    }
    
    public boolean isPrimitive() {
        return false;
    }

    public String getExtension(){
    	throw new UnsupportedOperationException(getClass().getSimpleName());
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
    
    public final Data root() {
        return root;
    }

    public final Data next() {
        return next;
    }
    
    public final Data parent(){
    	return parent;
    }
    
    public final String getSegment() {
        return segment;
    }

    public final String getPath() {
        return path;
    }
    
    final protected void setNext(Data next){
    	this.next = next;
    }
    
    @Override
	public String toString(){
		return getPath();
	}
}
