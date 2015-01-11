package com.example.database;

/**
 * Product object. Get or set values
 * @author Nathan Hallahan
 *
 */
public class Product {
	
	private int _id;
	private String _productname;
	private String _UOM;
	private float _quantity;
	private float _lowLevel;
	private float _preferredLevel;
	private int _manualShopList;
	
	public Product()
	{
		
	}
	
	
	public Product(int id, String productname, float quantity)
	{
		this._id = id;
		this._productname = productname;
		this._quantity = quantity;
	}
	
	public Product(String productname, float quantity)
	{
		this._productname = productname;
		this._quantity = quantity;
	}
	
	public Product(int id, String itemName, String UOM, float QOH, float lowLevel, float preferredLevel)
	{
		this._id = id;
		this._productname = itemName;
		this._UOM = UOM;
		this._quantity = QOH;
		this._lowLevel = lowLevel;
		this._preferredLevel = preferredLevel;
	}
	
	public Product(String itemName, String UOM, float QOH, float lowLevel, float preferredLevel)
	{
		this._productname = itemName;
		this._UOM = UOM;
		this._quantity = QOH;
		this._lowLevel = lowLevel;
		this._preferredLevel = preferredLevel;
	}
	
	public Product(String itemName, String UOM, float QOH, float lowLevel, float preferredLevel, int addedManualy)
	{
		this._productname = itemName;
		this._UOM = UOM;
		this._quantity = QOH;
		this._lowLevel = lowLevel;
		this._preferredLevel = preferredLevel;
		this._manualShopList = addedManualy;
	}
	
	public void setID(int id)
	{
		this._id = id;
	}
	
	public int getID()
	{
		return this._id;
	}
	
	public void setProductName(String productname)
	{
		this._productname = productname;
	}
	
	public String getProductName()
	{
		return this._productname;
	}
	
	public void setQuantity(float quantity)
	{
		this._quantity = quantity;
	}
	
	public float getQuantity()
	{
		return this._quantity;
	}
	
	/**
	 * set the Unit of Measure
	 * @param UOM
	 */
	public void setUOM(String UOM)
	{
		this._UOM = UOM;
	}
	
	/**
	 * get the Unit of Measure
	 * @return
	 */
	public String getUOM()
	{
		return this._UOM;
	}
	
	public void setLowLevel(float lowLevel)
	{
		this._lowLevel = lowLevel;
	}
	
	public float getLowLevel()
	{
		return this._lowLevel;
	}
	
	public void setPreferredLevel(float preferredLevel)
	{
		this._preferredLevel = preferredLevel;
	}
	
	public float getPreferredLevel()
	{
		return this._preferredLevel;
	}
	
	/**
	 * 0 not added manually, 1 added manually
	 * @param addedManualy
	 */
	public void setAddedManualy(int addedManualy)
	{
		this._manualShopList = addedManualy;
	}
	
	/**
	 * 0 not added manually, 1 added manually
	 * @return
	 */
	public int getAddedManualy()
	{
		return this._manualShopList;
	}

}
