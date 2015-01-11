package com.example.database;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * This class communicates with the shopping list
 * @author Nathan Hallahan
 *
 */
public class shoppingListBackend extends Activity {
	
	/**
	 * Determines if an item is in the shopping list already	
	 * @param context
	 * @param productName
	 * @return
	 */
	public boolean isInShoppinglist(Context context,String productName)
	{
		shoppingDBHandler dbHandler = new shoppingDBHandler(context, null, null, 1);
		
		Product product = 
	               dbHandler.findProduct(productName);
		if (product == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	/**
	 * Returns a Product in the shopping list
	 * @param context
	 * @param itemName
	 * @return
	 */
	public Product getProductInShoppingList(Context context, String itemName)
	{
		shoppingDBHandler dbHandler = new shoppingDBHandler(context, null, null, 1);
		
		Product product = 
	               dbHandler.findProduct(itemName);
		return product;
	}
	
	/**
	 * Adds an item to the shopping list
	 * @param context
	 * @param itemName
	 * @param UOM
	 * @param preferredLevel
	 */
	public void addToShoppingList(Context context, String itemName, String UOM, float preferredLevel)
    {
  	  shoppingDBHandler dbHandler = new shoppingDBHandler(context, null, null, 1);
  	  
  	  Product newProduct = new Product(itemName,UOM,0,0,preferredLevel);
  	  dbHandler.addProduct(newProduct);
	  	//begin history block
	  	historyDBHandler history = new historyDBHandler(context, null, null, 1);
	  	history.changeHistoryToList(itemName, preferredLevel);
		//end history block
    }
	
	/**
	 * Adds an item to the shopping list
	 * @param context
	 * @param itemName
	 * @param UOM
	 * @param preferredLevel
	 * @param addedManually 0 not added manually, 1 added manually
	 */
	public void addToShoppingList(Context context, String itemName, String UOM, float preferredLevel, int addedManually)
    {
  	  shoppingDBHandler dbHandler = new shoppingDBHandler(context, null, null, 1);
  	  
  	  Product newProduct = new Product(itemName,UOM,0,0,preferredLevel, addedManually);
  	  dbHandler.addProduct(newProduct);
  	//begin history block
	  	historyDBHandler history = new historyDBHandler(context, null, null, 1);
	  	history.changeHistoryToList(itemName, preferredLevel);
			//end history block
  	
    }
	
	
	/**
	 * Update the quantity
	 * @param context
	 * @param itemName
	 * @param requestedLevel
	 */
	public void changeLevel(Context context, String itemName, float requestedLevel)
	{
		shoppingDBHandler dbHandler = new shoppingDBHandler(context, null, null, 1);
		if ( requestedLevel <= 0)
		{
			removeFromList(context, itemName);
		}
		else
		{
			dbHandler.updateQty(itemName, requestedLevel);
		}
	}
	
	/**
	 * Update the quantity
	 * @param context
	 * @param itemName
	 * @param requestedLevel
	 * @param addedManually 0 not added manually, 1 added manually
	 */
	public void changeLevel(Context context, String itemName, float requestedLevel, int addedManually)
	{
		shoppingDBHandler dbHandler = new shoppingDBHandler(context, null, null, 1);
		if ( requestedLevel <= 0)
		{
			removeFromList(context, itemName);
		}
		else
		{
			dbHandler.updateQty(itemName, requestedLevel, addedManually);
		}
	}
	
	/**
	 * Get the shopping list count, updates the tab display
	 * @param context
	 * @return
	 */
	public int getCount(Context context)
	{
		shoppingDBHandler dbHandler = new shoppingDBHandler(context, null, null, 1);
        int theCount = dbHandler.getTableCount();
        
        customTabActivity.callUpdate(theCount);
        return theCount;
	}
	
	/**
	 * Decides whether to make a new entry or edit an existing one
	 * @param context
	 * @param itemName
	 * @param UOM
	 * @param requestedLevel
	 */
	public void editToList(Context context, String itemName, String UOM, float requestedLevel)
	{
		
		if (!isInShoppinglist(context, itemName)) //if it doesn't exist yet
			{
				addToShoppingList(context,itemName, UOM, requestedLevel);
			}
		else //edit the entry
		{
			if (getProductInShoppingList(context, itemName).getPreferredLevel() != requestedLevel)
			{
				historyDBHandler history = new historyDBHandler(context, null, null, 1);
			  	history.changeHistoryToList(itemName, requestedLevel - getProductInShoppingList(context, itemName).getPreferredLevel());
			}
			
			changeLevel(context, itemName, requestedLevel);
			
		}
		
		shoppingListActivity.updatedShoppingView();
		
		customTabActivity.callUpdate(getCount(context));
	}
	
	/**
	 * Decides whether to make a new entry or edit an existing one
	 * @param context
	 * @param itemName
	 * @param UOM
	 * @param requestedLevel
	 * @param addedManually 0 not added manually, 1 added manually
	 */
	public void editToList(Context context, String itemName, String UOM, float requestedLevel, int addedManually)
	{
		
		if (!isInShoppinglist(context, itemName)) //if it doesn't exist yet
			{
				addToShoppingList(context,itemName, UOM, requestedLevel, 1);
			}
		else //edit the entry
		{
			
			if (getProductInShoppingList(context, itemName).getPreferredLevel() != requestedLevel)
			{
				historyDBHandler history = new historyDBHandler(context, null, null, 1);
			  	history.changeHistoryToList(itemName, requestedLevel - getProductInShoppingList(context, itemName).getPreferredLevel());
			}
			
			changeLevel(context, itemName, requestedLevel, 1);
		}
		//tabActivity.updateTabCount();
		
		shoppingListActivity.updatedShoppingView();
		
		customTabActivity.callUpdate(getCount(context));
	}
	
	/**
	 * Remove item from the shipping list
	 * @param context
	 * @param itemName
	 */
	public void removeFromList(Context context, String itemName)
	{
		shoppingDBHandler dbHandler = new shoppingDBHandler(context, null, null, 1);
		if (isInShoppinglist(context, itemName))
		{
			dbHandler.deleteProduct(itemName);
			historyDBHandler history = new historyDBHandler(context, null, null, 1);
			history.changeHistoryRemoveFromList(itemName);
			shoppingListActivity.updatedShoppingView();
			customTabActivity.callUpdate(getCount(context));
		}
	}
	
	
	
	

}
