package com.example.database;

import static com.example.database.Constant.FIRST_COLUMN;
import static com.example.database.Constant.FOURTH_COLUMN;
import static com.example.database.Constant.SECOND_COLUMN;
import static com.example.database.Constant.THIRD_COLUMN;
import static com.example.database.Constant.FIFTH_COLUMN;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.util.Log;
//import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import android.support.v4.app.FragmentActivity;
/**
 * This class displays and edits the inventory.
 * @author Nathan Hallahan
 *
 */
public class DatabaseActivity extends ActivityGroup  implements OnItemClickListener{

	final Context context = this;
	private float newQnty;//newQnty will hold a value that we can operate on
	static DatabaseActivity _instance;
	private ArrayList<HashMap> list;//will hold the details of each Product so it can be displayed
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//show the main screen
		setContentView(R.layout.activity_database);
		//and assign this class as the static instance so we can access its vars in a static method later
		_instance = this;

		//updateList() is what will make set the adapter and make a call to populateList()
		//it is a separate method so it can be used by other methods
		updateList(); 

		getCount();
       
	}
	
	/**
	 * Sets the TextView's text to display the table count
	 */
	public void getCount()
	{
		MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        //dbHandler.getTableCount();
        
        TextView _tableCount = (TextView) findViewById(R.id.countText);
        _tableCount.setText(String.valueOf(dbHandler.getTableCount()));
	}

	@Override
	protected void onStart()
	{
		super.onStart();    	
	}
	
	
    
    /* keep around a bit before deleting, don't think its needed
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.database, 
                                              menu);
		return true;
	}
*/
	/**
	 * Called by the add button, opens the add dialog
	 * @param view
	 */
    public void newProduct (View view) 
    {	
    	   dialogAdd();
    }
        
      /**
       * Adds a new item to the database and updates the list  
       * @param itemName
       * @param UOM
       * @param QOH
       * @param lowLevel
       * @param preferredLevel
       */
      public void addNewItem(String itemName, String UOM, float QOH, float lowLevel, float preferredLevel)
      {
    	  MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    	  
    	  Product newProduct = new Product(itemName,UOM,QOH,lowLevel,preferredLevel);
    	  dbHandler.addProduct(newProduct);
    	//begin history block
	    	historyDBHandler history = new historyDBHandler(this, null, null, 1);
	    	history.changeHistoryCreated(itemName, QOH);
			//end history block
    	  getCount();
   	   	updateList();
      }
    
      /**
       * Creates and displays the add dialog
       */
      public void dialogAdd()
      {
    	  //create a new dialog
    	  final Dialog dialog = new Dialog(context);
    	  dialog.setContentView(R.layout.dialog_add_new);
    	  dialog.setTitle("Add a new item");
    	  
    	  //get its buttons so we can use them
    	  ImageButton cancle = (ImageButton) dialog.findViewById(R.id.CancleButton);
    	  ImageButton addNew = (ImageButton) dialog.findViewById(R.id.AddNewButton);
    	  ImageButton delete = (ImageButton) dialog.findViewById(R.id.button_AddNewPrompt_delete);
    	  //hide the delete button, we don't need it for new items
    	  delete.setVisibility(View.GONE);
    	  
    	  //close the dialog if we press the cancel button, look I spelled it wrong eh..
    	  cancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
    	  
    	  //get the text fields, we need their data!
    	  final EditText newProductBox = (EditText) dialog.findViewById(R.id.newItemName);
    	  final EditText newQohBox = (EditText) dialog.findViewById(R.id.newQOH);
    	  final EditText newUomBox = (EditText) dialog.findViewById(R.id.newUnitOfMeasure);
    	  final EditText newLowLevelBox = (EditText) dialog.findViewById(R.id.newLowLevel);
    	  final EditText newPrefLevelBox = (EditText) dialog.findViewById(R.id.newPreferredStock);
    	  
    	  //if we press the add new button...
    	  addNew.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					//make sure all fields have text
					if (!newProductBox.getText().toString().isEmpty()&&
							!newUomBox.getText().toString().isEmpty()&&
							!newQohBox.getText().toString().isEmpty() &&
							!newLowLevelBox.getText().toString().isEmpty()&&
							!newPrefLevelBox.getText().toString().isEmpty())
					{
						//gather the input...
						String itemName = newProductBox.getText().toString();
						String uom =  newUomBox.getText().toString();
						float qoh = Float.parseFloat(newQohBox.getText().toString());
						float lowLevel = Float.parseFloat(newLowLevelBox.getText().toString());
						float preferredLevel = Float.parseFloat(newPrefLevelBox.getText().toString());
						//...pass it onto addNewItem and close the dialog
						addNewItem(itemName, uom, qoh, lowLevel, preferredLevel);
						dialog.dismiss();
					}
					
					
				}
			});
    	  //need to show the dialog to use it...
    	  dialog.show();
      }
      
      /**
       * called by the change quantity button, opens the dialog
       * @param view
       */
      public void changeQty(View view)
      {
    	  LinearLayout rl = (LinearLayout)view.getParent();
          TextView nameText = (TextView)rl.findViewById(R.id.FirstText);
          String name = nameText.getText().toString();
          
          TextView uomText = (TextView)rl.findViewById(R.id.SecondText);
          String uom = uomText.getText().toString();
          
          TextView qntyText = (TextView)rl.findViewById(R.id.ThirdText);
          String qnty = qntyText.getText().toString();
          
          TextView lowLevelText = (TextView)rl.findViewById(R.id.FourthText);
          String lowLevel = lowLevelText.getText().toString();
          
          //System.out.println("gonna change the qnty of " + name);
          //open the dialog
          dialogChangeQnty(name, uom, qnty, lowLevel);
      }
      
      /**
       * Opens the change quantity dialog for an item
       * @param name
       * @param uom
       * @param qnty
       * @param lowLevel
       */
      public void dialogChangeQnty(String name, String uom, String qnty, String lowLevel)
      {
    	  //create the dialog
    	  final Dialog dialog = new Dialog(context);
    	  dialog.setContentView(R.layout.dialog_change_qnty);
    	  dialog.setTitle("Change the quantity of " + name);
    	  
    	  //get the buttons
    	  ImageButton cancel = (ImageButton) dialog.findViewById(R.id.button_promptChangeQnty_cancel);
    	  ImageButton ok = (ImageButton) dialog.findViewById(R.id.button_promptChangeQnty_ok);
    	  
    	  ImageButton plus = (ImageButton) dialog.findViewById(R.id.button_PromptchangeQntyMore);
    	  ImageButton minus = (ImageButton) dialog.findViewById(R.id.button_PromptchangeQntyLess);
    	  
    	  //we are gonna need to change the displayed value
    	  final EditText newQohBox = (EditText) dialog.findViewById(R.id.editText_promptChangeQnty_newQnty);
    	  
    	  //store the current qnty value so we can modify it...
    	  newQnty = Float.parseFloat(qnty);
    	  //...and assign it
    	  newQohBox.setText(dataHelper.floatAsString(String.valueOf(newQnty)));
    	  
    	  
    	  final float oldQnty = Float.parseFloat(qnty);
    	  final String theUOM = uom;
    	  
    	  //when we press the plus button add one to the quantity
    	  plus.setOnClickListener(new OnClickListener() {
    		  @Override
				public void onClick(View v) {
					newQnty += 1;
					newQohBox.setText(dataHelper.floatAsString(String.valueOf(newQnty)));
				}
    	  });
    	  
    	  //subtract
    	  minus.setOnClickListener(new OnClickListener() {
    		  @Override
				public void onClick(View v) {
    			  	if (newQnty > 0)
    			  	{
						newQnty -= 1;
						newQohBox.setText(dataHelper.floatAsString(String.valueOf(newQnty)));
    			  	}
				}
    	  });
    	  
    	  //close the dialog
    	  cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
    	  
    	  
    	  
    	  final String theName = name;
    	  final Context thisContext = this; //the differnt db handlers will want a Context, give em this.
    	  
    	  //we pressed ok, it is time to save the changes!
    	  ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//convert the quantity to a float
					float qoh = Float.parseFloat(newQohBox.getText().toString());
					MyDBHandler dbHandler = new MyDBHandler(thisContext, null, null, 1);
					//update this item with the new quantity
			    	dbHandler.updateQty(theName, qoh);
			    	
			    	
			    	//begin history block
			    	//save the changes to the history so it can be used for review or reports
			    	historyDBHandler history = new historyDBHandler(thisContext, null, null, 1);
			    	if (qoh > oldQnty)
	    			{
	    				history.changeHistoryMore(theName, qoh - oldQnty);
	    			}
	    			else if (qoh < oldQnty)
	    			{
	    				history.changeHistoryLess(theName, qoh - oldQnty);
	    			}
					//end history block
			    	
			    	//special handling for items in the shopping list
			    	shoppingListBackend shopListManager = new shoppingListBackend();
			    	
			    	//if this item is in the shopping list....
			    	if (shopListManager.isInShoppinglist(thisContext, theName))
			    	{
			    		//if someone added it intentionally, because they needed more than normal or something
			    		if (shopListManager.getProductInShoppingList(thisContext, theName).getAddedManualy() == 1)
			    		{
			    			//show the correct alert
			    			if (qoh > oldQnty)
			    			{
			    				alertAddManualQnty(theName, theUOM, oldQnty, qoh);
			    				dialog.dismiss();
			    			}
			    			else if (qoh < oldQnty)
			    			{
			    				alertSubManualQnty(theName, theUOM, oldQnty, qoh);
			    				dialog.dismiss();
			    			}
			    			//if there was no change who cares? but update the list anyway
			    			else 
			    			{
			    				dialog.dismiss();
								updateList();
			    			}
			    		}
			    		else 
		    			{
		    				dialog.dismiss();
							updateList();
		    			}
			    	}
			    	
			    	else
			    	{
						dialog.dismiss();
						updateList();
			    	}
				}
			});
    	  
    	  dialog.show();
    	  
      }
      
     
      /*
       * the button to edit an entry was pressed
       */
      public void invEdit(View view)
      {
    	  LinearLayout rl = (LinearLayout)view.getParent();
          TextView nameText = (TextView)rl.findViewById(R.id.FirstText);
          String name = nameText.getText().toString();
          
          TextView qntyText = (TextView)rl.findViewById(R.id.ThirdText);
          String qnty = qntyText.getText().toString();
          
          TextView uomText = (TextView)rl.findViewById(R.id.SecondText);
          String uom = uomText.getText().toString();
          
          TextView lowLevelText = (TextView)rl.findViewById(R.id.FourthText);
          String lowLevel = lowLevelText.getText().toString();
          
          TextView prefText = (TextView)rl.findViewById(R.id.FifthText);
          String preferredStock = prefText.getText().toString();
          
          dialogEdit(name, qnty, uom, lowLevel, preferredStock);
      }
      
      
      /**
       * open the edit dialog, alternate version of the add new item dialog
       * @param name
       * @param qoh
       * @param uom
       * @param lowLevel
       * @param preferredStock
       */
      public void dialogEdit(String name, String qoh, String uom, String lowLevel, String preferredStock)
      {
    	  //this is basically the same as dialogNew() except we are updating the Product in the database
    	  final Dialog dialog = new Dialog(context);
    	  dialog.setContentView(R.layout.dialog_add_new);
    	  dialog.setTitle("Edit an item");
    	  
    	  ImageButton cancle = (ImageButton) dialog.findViewById(R.id.CancleButton);
    	  ImageButton addNew = (ImageButton) dialog.findViewById(R.id.AddNewButton);
    	  ImageButton delete = (ImageButton) dialog.findViewById(R.id.button_AddNewPrompt_delete);
    	  
    	  
    	  cancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
    	  
    	  final EditText newProductBox = (EditText) dialog.findViewById(R.id.newItemName);
    	  final EditText newQohBox = (EditText) dialog.findViewById(R.id.newQOH);
    	  final EditText newUomBox = (EditText) dialog.findViewById(R.id.newUnitOfMeasure);
    	  final EditText newLowLevelBox = (EditText) dialog.findViewById(R.id.newLowLevel);
    	  final EditText newPrefLevelBox = (EditText) dialog.findViewById(R.id.newPreferredStock);
    	  
    	  newProductBox.setText(name);
    	  newQohBox.setText(qoh);
    	  newUomBox.setText(uom);
    	  newLowLevelBox.setText(lowLevel);
    	  newPrefLevelBox.setText(preferredStock);
    	  
    	  final String theName = name;
    	  final Context thisContext = this;
    	  final float oldQnty = Float.parseFloat(qoh);
    	  
    	  addNew.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyDBHandler dbHandler = new MyDBHandler(thisContext, null, null, 1);
					historyDBHandler history = new historyDBHandler(thisContext, null, null, 1);
					shoppingListBackend shopListManager = new shoppingListBackend();
					
					String itemName = newProductBox.getText().toString();
					String uom =  newUomBox.getText().toString();
					float qoh = Float.parseFloat(newQohBox.getText().toString());
					float lowLevel = Float.parseFloat(newLowLevelBox.getText().toString());
					float preferredLevel = Float.parseFloat(newPrefLevelBox.getText().toString());
					
					
					
					dbHandler.updateProduct(theName, itemName, qoh, uom, lowLevel, preferredLevel);
					if (qoh > oldQnty)
	    			{
						history.changeHistoryMore(theName, qoh - oldQnty);
	    			}
					else if (qoh < oldQnty)
	    			{
						history.changeHistoryLess(theName, qoh - oldQnty);
	    			}
					
					
					if (!theName.toString().equals(itemName.toString()))
					{
						removeProduct(theName);
					}
					
					if (shopListManager.isInShoppinglist(thisContext, itemName))
			    	{
			    		if (shopListManager.getProductInShoppingList(thisContext, itemName).getAddedManualy() == 1)
			    		{
			    			if (qoh > oldQnty)
			    			{
			    				alertAddManualQnty(theName, uom, oldQnty, qoh);
			    				dialog.dismiss();
			    			}
			    			else if (qoh < oldQnty)
			    			{
			    				alertSubManualQnty(theName, uom, oldQnty, qoh);
			    				dialog.dismiss();
			    			}
			    		}
			    	}
					
					dialog.dismiss();
					updateList();
				}
			});
    	  
    	  delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
					builder1.setMessage("Are you sure you want to delete this item?");
			        builder1.setCancelable(true);
			        builder1.setPositiveButton("Yes",
			                new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface alertDialog, int id) {
			            	removeProduct(theName);
			            	alertDialog.cancel();
			                dialog.dismiss();
			            }
			        });
			        builder1.setNegativeButton("No",
			                new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			            }
			        });

			        AlertDialog alert11 = builder1.create();
			        alert11.show();
					
				}
    	  });
    	  
    	  dialog.show();
      }
      
      /**
       * A very basic search function
       * @param view
       */
      public void lookupProduct (View view) {
    	  //get the data we need
    	  ListView lview = (ListView) findViewById(R.id.listview);
    	  
    	  LinearLayout parentView = (LinearLayout)view.getParent();
    	  EditText findField = (EditText) parentView.findViewById(R.id.editText_findField);
    	  String lookingFor = findField.getText().toString();
    	  //System.out.println("looking for " + lookingFor + " list " + list.size());
    	  
    	  //go through the list and select the item in the list view
    	  for (int i = 0; i < list.size(); i++)
    	  {
    		  HashMap theMap = list.get(i);
    		  String theName = theMap.get(FIRST_COLUMN).toString();
    		  System.out.println("line " + i + " is called " + theName);
    		  if (lookingFor.equals(theName))
    		  {
    			  lview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    			 
    			  lview.setItemChecked(i, true);
    			  lview.setSelection(i);
    			  return;
    		  }
    		      	  }     	
       }
   
      /**
       * Deletes an entry
       * @param name
       */
       public void removeProduct (String name) 
       {
    	   MyDBHandler dbHandler = new MyDBHandler(this, null,null, 1);
    	   //also remove it from the shopping list
    	   shoppingListBackend shopListManager = new shoppingListBackend();
    	   shopListManager.removeFromList(this, name);
    	    	
    	   dbHandler.deleteProduct(name);
    	   
    	   	//begin history block
    	   //keep a record in the history
	    	historyDBHandler history = new historyDBHandler(this, null, null, 1);
	    	history.changeHistoryDeleted(name);
			//end history block
	    	
      	   getCount();
      	   updateList(); 
       }
       
       /**
        * calls the updateList method, it should be done if changes are made
        */
       static public void updatedInvList()
	   	{
	   		if (_instance != null)
	   		{
	   			_instance.updateList();
	   		}
	   	}
       
       /**
        * Sets the ListView adapter and makes the call to populate the list
        */
       public void updateList()
       {
    	   	ListView lview = (ListView) findViewById(R.id.listview);
  	     	populateList();
  	     	listviewAdapter adapter = new listviewAdapter(this, list);
  	     	lview.setAdapter(adapter);
  	     	lview.setOnItemClickListener(this); 
       }
       
       /**
        * Gets the items from the database and populates the ListView
        */
       public void populateList() {
    	   //make a new list
           list = new ArrayList<HashMap>();
           
           MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
           
           shoppingListBackend shopListManager = new shoppingListBackend();
                     
           int _productCount = dbHandler.getTableCount();
           
           for (int i = 0; i < _productCount; i++)
           {
        	   Product theProduct = dbHandler.getProducts(i);//get the details for this item
        	   HashMap temp = new HashMap();//add the details to a HashMap, all as strings
               temp.put(FIRST_COLUMN, theProduct.getProductName());
               temp.put(SECOND_COLUMN, theProduct.getUOM());
               temp.put(THIRD_COLUMN, dataHelper.floatAsString(theProduct.getQuantity()));
               temp.put(FOURTH_COLUMN, dataHelper.floatAsString(theProduct.getLowLevel()));
               temp.put(FIFTH_COLUMN, dataHelper.floatAsString(theProduct.getPreferredLevel()));
               list.add(temp);//add the HashMap to the list
           
               //check to see if we need to add things to the shopping list now
           		if (theProduct.getQuantity() <= theProduct.getLowLevel())//if its low we need more!
           		{
           			//if the item is already in the shopping list we need to edit it
           			if (shopListManager.isInShoppinglist(this, theProduct.getProductName()))
           			{
           				if (shopListManager.getProductInShoppingList(this, theProduct.getProductName()).getAddedManualy() == 0)
           				{
           					//only do this if it wasn't added to the shopping list manually by the user
		           			float requestedLvl = theProduct.getPreferredLevel() - theProduct.getQuantity();
		           			shopListManager.editToList(this, theProduct.getProductName(), theProduct.getUOM(), requestedLvl);
           				}
           			}
           			
           			else //if it is not already in the shopping list
           			{
           				float requestedLvl = theProduct.getPreferredLevel() - theProduct.getQuantity();
	           			
	           			shopListManager.editToList(this, theProduct.getProductName(), theProduct.getUOM(), requestedLvl);
           			}
           			
           		}
           		//we have enough, remove the item from the shopping list if it is already there
           		else if (theProduct.getQuantity() > theProduct.getLowLevel() && shopListManager.isInShoppinglist(this, theProduct.getProductName()))
           		{
           			if (shopListManager.getProductInShoppingList(this, theProduct.getProductName()).getAddedManualy() == 0)
           			{
           				shopListManager.removeFromList(this, theProduct.getProductName());
           			}
           		}	
           }
           
       }
       
       /**
        * The button to add something to the shopping list manually was pressed
        * opens dialogSendToCart
        * @param view
        */
       public void addToShoppingList(View view)
       {
    	   //get the data and open the dialog
     	  LinearLayout rl = (LinearLayout)view.getParent();
           TextView nameText = (TextView)rl.findViewById(R.id.FirstText);
           String name = nameText.getText().toString();
           
           TextView qntyText = (TextView)rl.findViewById(R.id.ThirdText);
           String qnty = qntyText.getText().toString();
           
           TextView uomText = (TextView)rl.findViewById(R.id.SecondText);
           String uom = uomText.getText().toString();
           
           TextView lowLevelText = (TextView)rl.findViewById(R.id.FourthText);
           String lowLevel = lowLevelText.getText().toString();
           
           TextView prefText = (TextView)rl.findViewById(R.id.FifthText);
           String preferredStock = prefText.getText().toString();
           
           dialogSendToCart(name, uom, qnty, preferredStock);
       }
       
       /**
        * Opens the sent to shopping list dialog
        * @param name
        * @param uom
        * @param qnty
        * @param pref
        */
       public void dialogSendToCart(String name, String uom, String qnty, String pref)
       {
    	   //create the dialog, we are reusing the change quantity layout, so this is similar to dialogChangeQnty
     	  final Dialog dialog = new Dialog(context);
     	  dialog.setContentView(R.layout.dialog_change_qnty);
     	  dialog.setTitle("Add " + name + " to the shopping list");
     	  
     	  //get the buttons
     	 ImageButton cancel = (ImageButton) dialog.findViewById(R.id.button_promptChangeQnty_cancel);
     	ImageButton ok = (ImageButton) dialog.findViewById(R.id.button_promptChangeQnty_ok);
     	  
     	ImageButton plus = (ImageButton) dialog.findViewById(R.id.button_PromptchangeQntyMore);
     	ImageButton minus = (ImageButton) dialog.findViewById(R.id.button_PromptchangeQntyLess);
     	  
     	  
     	  final EditText newQohBox = (EditText) dialog.findViewById(R.id.editText_promptChangeQnty_newQnty);
     	  
     	  float theQnty = Float.parseFloat(qnty);
     	  float newPref = Float.parseFloat(pref);
     	  
     	  
     	  //Hold it! We can't add negative amounts to the shopping list!
     	  if (newPref - theQnty < 0)
     	  {
     		  newQnty = 0;
     	  }
     	  else
     	  {
     		  newQnty = newPref - theQnty;
     	  }
     	  
     	  newQohBox.setText(dataHelper.floatAsString(String.valueOf(newQnty)));
     	  
     	  plus.setOnClickListener(new OnClickListener() {
     		  @Override
 				public void onClick(View v) {
 					newQnty += 1;
 					newQohBox.setText(dataHelper.floatAsString(String.valueOf(newQnty)));
 				}
     	  });
     	  
     	  minus.setOnClickListener(new OnClickListener() {
     		  @Override
 				public void onClick(View v) {
     			  	if (newQnty > 0)
     			  	{
 						newQnty -= 1;
 						newQohBox.setText(dataHelper.floatAsString(String.valueOf(newQnty)));
     			  	}
 				}
     	  });
     	  
     	  cancel.setOnClickListener(new OnClickListener() {
 				@Override
 				public void onClick(View v) {
 					dialog.dismiss();
 				}
 			});
     	  
     	  
     	  
     	  final String theName = name;
     	  final Context thisContext = this;
     	  final String theUom = uom;
     	  
     	  
     	 
     	  //the order came through, let's do this!
     	  ok.setOnClickListener(new OnClickListener() {
 				@Override
 				public void onClick(View v) {
 					
 					float qoh = Float.parseFloat(newQohBox.getText().toString());
 					shoppingListBackend shopListManager = new shoppingListBackend();
 					//if this item is already in the shopping list, we need to add to it
 					if (shopListManager.isInShoppinglist(thisContext, theName))
 					{
 						Product theProduct = shopListManager.getProductInShoppingList(thisContext, theName);
 						qoh = qoh + theProduct.getPreferredLevel();
 					}
 			    	//just like adding to the list in populateList but this time we tell it that it was added manually
 					shopListManager.editToList(thisContext, theName, theUom, qoh, 1);
 					dialog.dismiss();
 					updateList();
 				}
 			});
     	  
     	  dialog.show();
     	  
       }
       
       /**
        * A simple alert for manually added items, lets user choose to replace or change the value
        * @param itemName
        * @param uom
        * @param oldValue
        * @param newValue
        */
      public void alertAddManualQnty(String itemName, String uom, float oldValue, float newValue)
      {
    	     	  
    	  final String theName = itemName;
    	  final String theUOM = uom;
    	  final Context theContext = this;
    	  final float theValue = newValue - oldValue;
    	  final float theNewValue = newValue;
    	  
    	  AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
          builder1.setMessage(itemName + " was added to the shopping list manualy before, what do you want to do?");
          builder1.setCancelable(true);
          builder1.setPositiveButton("Remove from shopping list.",
                  new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  
            	  shoppingListBackend shopListManager = new shoppingListBackend();
            	  shopListManager.removeFromList(theContext, theName);//.editToList(theContext, theName, theUOM, theNewValue, 0);            	  
                  dialog.cancel();
                  updateList();//if an item is low it will still get added to the shopping list
              }
          });
          builder1.setNegativeButton("Subtract the changed value.",
                  new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  shoppingListBackend shopListManager = new shoppingListBackend();
            	  
            	  float newAmount = shopListManager.getProductInShoppingList(theContext, theName).getPreferredLevel() - theValue;
            	  shopListManager.editToList(theContext, theName, theUOM, newAmount, 1);
                  dialog.cancel();
                  updateList();
              }
          });
          AlertDialog alert11 = builder1.create();
          alert11.show();
      }
      
      /**
       * A simple alert for manually added items, lets user choose to replace or change the value
       * @param itemName
       * @param uom
       * @param oldValue
       * @param newValue
       */
      public void alertSubManualQnty(String itemName, String uom, float oldValue, float newValue)
      {
    	  final String theName = itemName;
    	  final String theUOM = uom;
    	  final Context theContext = this;
    	  final float theValue = oldValue - newValue;
    	  final float theNewValue = newValue;
    	  
    	  AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
          builder1.setMessage(itemName + " was added to the shopping list manualy before, what do you want to do?");
          builder1.setCancelable(true);
          builder1.setPositiveButton("Replace quantity in shopping list.",
                  new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  shoppingListBackend shopListManager = new shoppingListBackend();
            	  shopListManager.removeFromList(theContext, theName);//editToList(theContext, theName, theUOM, theNewValue, 0);
                  dialog.cancel();
                  updateList();
              }
          });
          builder1.setNegativeButton("Add the changed value.",
                  new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  shoppingListBackend shopListManager = new shoppingListBackend();
            	  
            	  float newAmount = theValue + shopListManager.getProductInShoppingList(theContext, theName).getPreferredLevel();
            	  shopListManager.editToList(theContext, theName, theUOM, newAmount, 1);
                  dialog.cancel();
                  updateList();
              }
          });
          AlertDialog alert11 = builder1.create();
          alert11.show();
      }
      
    //pretty much does nothing now
  	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
          //Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
          TextView tv = (TextView) v.findViewById(R.id.FirstText);
          String name= tv.getText().toString();
          lookupProduct(v);
          
      }

}