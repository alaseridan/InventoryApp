package com.example.database;


import static com.example.database.Constant.FIRST_COLUMN;
//import static com.example.database.Constant.FOURTH_COLUMN;
import static com.example.database.Constant.SECOND_COLUMN;
import static com.example.database.Constant.THIRD_COLUMN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * This class controls the shopping list
 * @author Nathan Hallahan
 *
 */
public class shoppingListActivity extends Activity implements OnItemClickListener{
	
	final Context context = this;
	private float newQnty; //this will hold a value to modify
	
	List<String> groupList;
    List<String> childList;
    Map<String, List<String>> parentList;
	private ArrayList<HashMap> list;
	
	static shoppingListActivity _instance;
	
	@Override
	protected
	void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppinglist_menu_layout);
		
		ListView lview = (ListView) findViewById(R.id.listView_shoppingList);
		populateList();
		shoppingListViewAdapter adapter = new shoppingListViewAdapter(this, list);
		lview.setAdapter(adapter);
        
       getCount();
       
       lview.setOnItemClickListener(this);
       
       _instance = this;
       //tabActivity.updateTabCount();
	}	
	
	/**
	 * Get a count of items in the shopping list
	 */
	public void getCount()
	{
		shoppingListBackend shopListManager = new shoppingListBackend();
		int theCount = shopListManager.getCount(this);
        
        TextView _tableCount = (TextView) findViewById(R.id.text_shoppingListCount);
        _tableCount.setText(String.valueOf(theCount));
        
	}
	
	
	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * calls updateList()
	 */
	static public void updatedShoppingView()
	{
		if (_instance != null)
		{
			_instance.updateList();
		}
	}
	
	/**
	 * Updates the shopping list
	 */
	public void updateList()
    {
		ListView lview = (ListView) findViewById(R.id.listView_shoppingList);
		populateList();
		shoppingListViewAdapter adapter = new shoppingListViewAdapter(this, list);
		lview.setAdapter(adapter);
		getCount();
		
		//tabActivity.updateTabCount();
    }
	
	
	/**
	 * Populates the shopping list
	 */
	public void populateList() {
 	   
        list = new ArrayList<HashMap>();
        
        shoppingDBHandler dbHandler = new shoppingDBHandler(this, null, null, 1);
        
        int _productCount = dbHandler.getTableCount();
        
        for (int i = 0; i < _productCount; i++)
        {
     	   Product theProduct = dbHandler.getProducts(i);
     	   HashMap temp = new HashMap();
            temp.put("First", theProduct.getProductName());
            temp.put("Second", theProduct.getUOM());
            temp.put("Third",dataHelper.floatAsString(theProduct.getPreferredLevel()));
            
        list.add(temp);   
        }
        
    }
	
	/**
	 * Button opens the dialog
	 * @param view
	 */
	public void buyButton(View view)
	{
		LinearLayout rl = (LinearLayout)view.getParent();
        TextView nameText = (TextView)rl.findViewById(R.id.text_tobuyName);
        String name = nameText.getText().toString();
        //System.out.println("clicked on " + name);
        
        TextView uomText = (TextView)rl.findViewById(R.id.text_tobuyUOM);
        String UOM = uomText.getText().toString();
        
        TextView qntyText = (TextView)rl.findViewById(R.id.text_tobuyQnty);
        String QNTY = qntyText.getText().toString();
        
        buyDialog(name, UOM, QNTY);
	}
	
	/**
	 * Opens the buy/pickup dialog and saves changes to the shopping list and inventory
	 * @param name
	 * @param UOM
	 * @param QNTY
	 */
	public void buyDialog(String name, String UOM, String QNTY)
	{
		//create the dialog
		final Dialog dialog = new Dialog(context);
  	  dialog.setContentView(R.layout.dialog_buy_qnty);
  	  
  	  //remind the user what it is that they are getting, the dialog may cover their list
  	  dialog.setTitle("Picking up " + name + "? We need " + QNTY + " " + UOM);
  	  
  	  //get the buttons
  	  ImageButton cancel = (ImageButton) dialog.findViewById(R.id.button_CancelBuy);
  	  ImageButton buy = (ImageButton) dialog.findViewById(R.id.button_confirmBuy);
  	  ImageButton plus = (ImageButton) dialog.findViewById(R.id.button_buyPromptchangeQntyMore);
  	  ImageButton minus = (ImageButton) dialog.findViewById(R.id.button_buyPromptchangeQntyLess);
  	  
  	  
  	  cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
  	  
  	  
  	  final String theUOM = UOM;
  	  final String theName = name;
  	  final Context thisContext = this;
  	  
  	  final EditText buyQnty = (EditText) dialog.findViewById(R.id.editText_BuyQnty);
  	  
  	  newQnty = Float.parseFloat(QNTY);
  	  final float shouldBuy = Float.parseFloat(QNTY);
  	  buyQnty.setText(dataHelper.floatAsString(String.valueOf(newQnty)));
  	  
  	  //present a useful prompt to the user
  	  TextView textUOM = (TextView) dialog.findViewById(R.id.text_buyPromptUOM);
  	  textUOM.setText("How many " + UOM + "(s) of " + name + " did you get?");
  	  
  	plus.setOnClickListener(new OnClickListener() {
		  @Override
			public void onClick(View v) {
				newQnty += 1;
				buyQnty.setText(dataHelper.floatAsString(String.valueOf(newQnty)));
			}
	  });
	  
	  minus.setOnClickListener(new OnClickListener() {
		  @Override
			public void onClick(View v) {
			  	if (newQnty > 0)
			  	{
					newQnty -= 1;
					buyQnty.setText(dataHelper.floatAsString(String.valueOf(newQnty)));
			  	}
			}
	  });
  	  
	  //time to make some changes
  	  buy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					float addQnty = Float.parseFloat(buyQnty.getText().toString());
					
					//add it to inventory
					MyDBHandler dbHandler = new MyDBHandler(thisContext, null, null, 1);
					float updatedQnty = dbHandler.findProduct(theName).getQuantity() + addQnty;
			    	dbHandler.updateQty(theName, updatedQnty);
					
			    	//subtract from shopping list
			    	shoppingListBackend shopListManager = new shoppingListBackend();
			    	float subtractQnty = shopListManager.getProductInShoppingList(thisContext, theName).getPreferredLevel() - addQnty;
			    	if (subtractQnty > 0)
			    	{
			    		shopListManager.changeLevel(thisContext, theName, subtractQnty,1);
			    	}
			    	else
			    	{
			    		shopListManager.changeLevel(thisContext, theName, subtractQnty);
			    	}
			    	
			    	
			    	//begin history block
			    	historyDBHandler history = new historyDBHandler(thisContext, null, null, 1);
	    			history.changeHistoryBought(theName, addQnty);
					//end history block
			    	
			    	//update both list
			    	DatabaseActivity.updatedInvList();//the main inventory list
			    	updateList();//the shopping list
					dialog.dismiss();
				}
			});
  	  
  	  dialog.show();
	}

}
