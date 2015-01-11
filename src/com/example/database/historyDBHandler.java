package com.example.database;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Handles the history database
 * @author Nathan Hallahan
 *
 */
public class historyDBHandler extends SQLiteOpenHelper{

	//the strings we need for the database
	private static final int DATABASE_VERSION = 1;
	private static final String  DATABASE_NAME ="productHistoryDB.db";
	public static final String TABLE_PRODUCTS = "productHistory";
	
	public static final String  COLUMN_ID = "_id";
	public static final String COLUMN_PRODUCTNAME = "productname";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_DATETIME = "dateTime";
	public static final String COLUMN_CHANGETYPE = "changeType";
	
	public historyDBHandler(Context context, String name,
			CursorFactory factory, int version)
	{
		super (context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	//Create the table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
				TABLE_PRODUCTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
				+ COLUMN_PRODUCTNAME
				+ " TEXT," + COLUMN_QUANTITY + " REAL," + COLUMN_CHANGETYPE + " TEXT," + COLUMN_DATETIME + " TEXT" + ");";
		db.execSQL(CREATE_PRODUCTS_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		onCreate(db);
	}
	
	/**
	 * deletes the history table
	 */
	public void deleteTable()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCTS);
		onCreate(db);
	}
	
	/**
	 * Make a new entry for a newly created item
	 * @param productName
	 * @param firstCount
	 */
	public void changeHistoryCreated(String productName, float firstCount)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTNAME, productName);
		values.put(COLUMN_QUANTITY, firstCount);
		values.put(COLUMN_CHANGETYPE, "created");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		
		values.put(COLUMN_DATETIME, dateFormat.format(date));
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}
	
	/**
	 * Record the value that was added to the item
	 * @param productName
	 * @param addition
	 */
	public void changeHistoryMore(String productName, float addition)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTNAME, productName);
		values.put(COLUMN_QUANTITY, addition);
		values.put(COLUMN_CHANGETYPE, "increased");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		
		values.put(COLUMN_DATETIME, dateFormat.format(date));
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}
	
	/**
	 * Record the value that was subtracted from the item
	 * @param productName
	 * @param subtraction
	 */
	public void changeHistoryLess(String productName, float subtraction)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTNAME, productName);
		values.put(COLUMN_QUANTITY, subtraction);
		values.put(COLUMN_CHANGETYPE, "decreased");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		
		values.put(COLUMN_DATETIME, dateFormat.format(date));
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}
	
	/**
	 * Record the quantity added to the shopping list for this item
	 * @param productName
	 * @param qntyToList
	 */
	public void changeHistoryToList(String productName, float qntyToList)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTNAME, productName);
		values.put(COLUMN_QUANTITY, qntyToList);
		values.put(COLUMN_CHANGETYPE, "added to shopping list");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		
		values.put(COLUMN_DATETIME, dateFormat.format(date));
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}
	
	/**
	 * Record the quantity removed from the shopping list for this item
	 * @param productName
	 */
	public void changeHistoryRemoveFromList(String productName)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTNAME, productName);
		values.put(COLUMN_QUANTITY, 0);
		values.put(COLUMN_CHANGETYPE, "removed from shopping list");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		
		values.put(COLUMN_DATETIME, dateFormat.format(date));
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}
	
	/**
	 * Record the quantity bought off the shopping list for this item
	 * @param productName
	 * @param bought
	 */
	public void changeHistoryBought(String productName, float bought)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTNAME, productName);
		values.put(COLUMN_QUANTITY, bought);
		values.put(COLUMN_CHANGETYPE, "bought");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		
		values.put(COLUMN_DATETIME, dateFormat.format(date));
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}
	
	/**
	 * Record that this item was deleted from the database
	 * @param productName
	 */
	public void changeHistoryDeleted(String productName)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTNAME, productName);
		values.put(COLUMN_QUANTITY, 0);
		values.put(COLUMN_CHANGETYPE, "deleted");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		
		values.put(COLUMN_DATETIME, dateFormat.format(date));
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}
	
	/* un-used?
	public boolean deleteProduct(String productname) {
			
			boolean result = false;
			
			String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";
	
			SQLiteDatabase db = this.getWritableDatabase();
			
			Cursor cursor = db.rawQuery(query, null);
			
			productHistory product = new productHistory();
			
			if (cursor.moveToFirst()) {
				product.setID(Integer.parseInt(cursor.getString(0)));
				db.delete(TABLE_PRODUCTS, COLUMN_PRODUCTNAME + " = ?",
			            new String[] { String.valueOf(product.getID()) });
				cursor.close();
				result = true;
			}
		        db.close();
			return result;
		}*/
	
	/**
	 * Return a count of the entries in the history
	 * @return
	 */
	public int getTableCount()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
		
		if (cursor != null)
	        cursor.moveToFirst();
		int numRows = cursor.getCount();
		
		return numRows;
	}
	
	/**
	 * Get details for the product at this position in the list
	 * @param pos
	 * @return
	 */
	public productHistory getProducts(int pos)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
		
		if (cursor != null)
	        cursor.moveToPosition(pos);
		
		
		productHistory product = new productHistory();
		product.setID(Integer.parseInt(cursor.getString(0)));
		product.setProductName(cursor.getString(1));
		product.setQauntity(Float.parseFloat(cursor.getString(2)));
		product.setDateTime(cursor.getString(4));
		product.setChangeType(cursor.getString(3));
		
		
		
	   
	    
	    return product;
		
	}

}
