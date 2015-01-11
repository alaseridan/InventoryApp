package com.example.database;

/**
 * Product object used for record keeping
 * @author nate
 *
 */
public class productHistory {

	private int _id;
	private String _productname;
	private String _UOM;
	private float _quantity;
	private float _lowLevel;
	private float _preferredLevel;
	private int _manualShopList;
	private String _dateTime;
	private String _changeType;
	
	public productHistory()
	{
		
	}
	
	public productHistory(int id, String productName, float quantity, String changeType,String dateTime)
	{
		this._id = id;
		this._productname = productName;
		this._quantity = quantity;
		this._changeType = changeType;
		this._dateTime = dateTime;
	}
	
	public void setID(int id)
	{
		this._id = id;
	}
	
	public int getID()
	{
		return this._id;
	}
	
	public void setProductName(String productName)
	{
		this._productname = productName;
	}
	
	public String getProductName()
	{
		return this._productname;
	}
	
	public void setQauntity(float quantity)
	{
		this._quantity = quantity;
	}
	
	public float getQuantity()
	{
		return this._quantity;
	}
	
	public void setDateTime(String dateTime)
	{
		this._dateTime = dateTime;
	}
	
	public String getDateTime()
	{
		return this._dateTime;
	}
	
	public void setChangeType(String changeType)
	{
		this._changeType = changeType;
	}
	
	public String getChangeType()
	{
		return this._changeType;
	}

}
