package com.example.database;

import java.net.ContentHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class handles the inventory database
 * @author Nathan Hallahan
 *
 */
public class MyDBHandler extends SQLiteOpenHelper {

	//strings for the table
	private static final int DATABASE_VERSION = 1;
	private static final String  DATABASE_NAME ="productDB.db";
	public static final String TABLE_PRODUCTS = "products";
	
	public static final String  COLUMN_ID = "_id";
	public static final String COLUMN_PRODUCTNAME = "productname";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_UOM = "uom";
	public static final String COLUMN_LOWLEVEL = "lowlevel";
	public static final String COLUMN_PREFERREDLEVEL = "preferredlevel";
	
	public MyDBHandler(Context context, String name,
			CursorFactory factory, int version)
	{
		super (context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
				TABLE_PRODUCTS + "(" 
				+ COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
				+ " TEXT," + COLUMN_QUANTITY + " REAL," + COLUMN_UOM + " REAL," + COLUMN_LOWLEVEL + " REAL," + COLUMN_PREFERREDLEVEL + " REAL" + ");";
		db.execSQL(CREATE_PRODUCTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST " + TABLE_PRODUCTS);
		onCreate(db);

	}
	
	/**
	 * Insert a new product to the table
	 * @param product
	 */
	public void addProduct(Product product)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTNAME, product.getProductName());
		values.put(COLUMN_QUANTITY, product.getQuantity());
		values.put(COLUMN_UOM, product.getUOM());
		values.put(COLUMN_LOWLEVEL, product.getLowLevel());
		values.put(COLUMN_PREFERREDLEVEL, product.getPreferredLevel());
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}
	
	/**
	 * Update the quantity for this item
	 * @param productname
	 * @param qty
	 */
	public void updateQty(String productname, float qty)
	{
		Product theProduct = findProduct(productname);
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_QUANTITY, qty);
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TABLE_PRODUCTS, values, COLUMN_ID +"="+theProduct.getID(), null);
		db.close();
		
	}
	
	/**
	 * Update all the values for this item
	 * @param oldName
	 * @param newName
	 * @param qty
	 * @param uom
	 * @param lowLevel
	 * @param prefLevel
	 */
	public void updateProduct(String oldName, String newName,float qty ,String uom, float lowLevel, float prefLevel)
	{
		Product theProduct = findProduct(oldName);
		ContentValues values = new ContentValues();
		values.put(COLUMN_PRODUCTNAME, newName);
		values.put(COLUMN_QUANTITY, qty);
		values.put(COLUMN_UOM, uom);
		values.put(COLUMN_LOWLEVEL, lowLevel);
		values.put(COLUMN_PREFERREDLEVEL, prefLevel);
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TABLE_PRODUCTS, values, COLUMN_ID +"="+theProduct.getID(), null);
				
		db.close();
	}
	
	/**
	 * Look for an item with this name in the table
	 * @param productname
	 * @return
	 */
	public Product findProduct(String productname)
	{
		String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		Product product = new Product();
		
		if (cursor.moveToFirst())
		{
			cursor.moveToFirst();
			product.setID(Integer.parseInt(cursor.getString(0)));
			product.setProductName(cursor.getString(1));
			product.setQuantity(Float.parseFloat(cursor.getString(2)));
			cursor.close();
		}
		else 
		{
			product = null;
		}
		db.close();
		return product;
	}
	
	/**
	 * Get the item with this id
	 * @param id
	 * @return
	 */
	public Product getProduct(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { COLUMN_ID,
	            COLUMN_PRODUCTNAME, COLUMN_QUANTITY, COLUMN_UOM, COLUMN_LOWLEVEL, COLUMN_PREFERREDLEVEL }, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	    Product product = new Product();
	    product.setID(Integer.parseInt(cursor.getString(0)));
	    product.setProductName(cursor.getString(1));
		product.setQuantity(Float.parseFloat(cursor.getString(2)));
	    return product;
	}
	
	/**
	 * Delete this item from the table
	 * @param productname
	 * @return
	 */
	public boolean deleteProduct(String productname) {
		
		boolean result = false;
		
		String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";

		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		Product product = new Product();
		
		if (cursor.moveToFirst()) {
			product.setID(Integer.parseInt(cursor.getString(0)));
			db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
		            new String[] { String.valueOf(product.getID()) });
			cursor.close();
			result = true;
		}
	        db.close();
		return result;
	}
	
	/**
	 * Get the count of items in this table
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
	 * Get the product details at this position in the table
	 * @param pos
	 * @return
	 */
	public Product getProducts(int pos)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
		
		if (cursor != null)
	        cursor.moveToPosition(pos);
		
		
		Product product = new Product();
	    product.setID(Integer.parseInt(cursor.getString(0)));
	    product.setProductName(cursor.getString(1));
		product.setQuantity(Float.parseFloat(cursor.getString(2)));
		product.setUOM(cursor.getString(3));
		product.setLowLevel(Float.parseFloat(cursor.getString(4)));
		product.setPreferredLevel(Float.parseFloat(cursor.getString(5)));
		
		
	   
	    
	    return product;
		
	}

}
