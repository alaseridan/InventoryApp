package com.example.database;

import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
/**
 * This class handles the tabs and their labels
 * @author Nathan Hallahan
 *
 */
public class customTabActivity extends TabActivity{
	
	private TabHost mTabHost;
	static customTabActivity _instance;//to use vars in a static method
	public TextView listCount;
	public boolean init;//an initialization boolean
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout);
		
		_instance = this;
		
		//create the tabs
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		setupTab(new TextView(this), "Inventory", "DatabaseActivity.class");
		setupTab(new TextView(this), "Shopping List", "shoppingListActivity.class");
		setupTab(new TextView(this), "Advanced","AdvancedActivity.class");
	}
	
	/**
	 * Setup the tabs, and set their activity classes
	 * @param view
	 * @param tag
	 * @param className
	 */
	private void setupTab(final View view, final String tag, final String className) {
		
		shoppingListBackend shopListManager = new shoppingListBackend();
		
		View tabview = createTabView(mTabHost.getContext(), tag, className, shopListManager.getCount(this));
		Intent intent;
		intent = new Intent().setClass(this, DatabaseActivity.class);
		
		if (className.equals("DatabaseActivity.class"))
		{
			intent = new Intent().setClass(this, DatabaseActivity.class);
		}
		if (className.equals("shoppingListActivity.class"))
		{
			intent = new Intent().setClass(this, shoppingListActivity.class);
		}
		if (className.equals("AdvancedActivity.class"))
		{
			intent = new Intent().setClass(this, AdvancedActivity.class);
		}
		
	        TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		mTabHost.addTab(setContent);
	}
	
	/**
	 * Create the tab view
	 * @param context
	 * @param text
	 * @param className
	 * @param shopListCount
	 * @return
	 */
	private static View createTabView(final Context context, final String text, final String className, int shopListCount) {
		//Put the shopping list count on the shopping tab
		if (className.equals("shoppingListActivity.class"))
		{
			View view = LayoutInflater.from(context).inflate(R.layout.tabs_shopping_bg, null);
			TextView tv = (TextView) view.findViewById(R.id.tabsShoppingListText);
			tv.setText(text);
			
			_instance.listCount = (TextView) view.findViewById(R.id.text_TabShoppingListCount);
			_instance.init = true; //now we have set up the shopping list tab			
			_instance._updateListCount(shopListCount);
			return view;
		}
		
		else
		{
			View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
			TextView tv = (TextView) view.findViewById(R.id.tabsText);
			tv.setText(text);
			return view;
		}
		
	}
	
	/**
	 * Calls updateListCount with the provided count to update the shopping list tab
	 * @param theCount
	 */
	public static void callUpdate(int theCount)
	{
		if (_instance != null)
		{
			_instance._updateListCount(theCount);
		}
		
	}
	
	/**
	 * Updates the number on the shopping list tab
	 * @param theCount
	 */
	public void _updateListCount(int theCount)
	{
		if (init)
		{
			if (listCount != null)
			{
				listCount.setText(String.valueOf(theCount));
			}
		}
		
	}

	
	
	
	
	

}
