package com.example.database;

import static com.example.database.Constant.FIRST_COLUMN;
import static com.example.database.Constant.FOURTH_COLUMN;
import static com.example.database.Constant.SECOND_COLUMN;
import static com.example.database.Constant.THIRD_COLUMN;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * Displays and handles the "advanced" features not commonly used by the standard user
 * also changes content views to show other screens
 * @author Nathan Hallahan
 *
 */
public class AdvancedActivity extends ActivityGroup implements OnItemClickListener{
	
	final Context context = this;
	
	private List<Product> products;//a list to store the products for export
	
	private ArrayList<HashMap> list;
	private List<productHistory> historyProducts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//show the main "advanced" screen
		setContentView(R.layout.advanced_activity);
		
	}
	
	/**
	 * Changes the view to show the history screen
	 * @param view
	 */
	public void gotoHistory(View view)
	{
		setContentView(R.layout.history_menu_layout);
		
		ListView lview = (ListView) findViewById(R.id.listView_historyList);
		populateList();
        
        historyListviewAdapter adapter = new historyListviewAdapter(this, list);
        lview.setAdapter(adapter);
        
        lview.setOnItemClickListener(this); 
		
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Writes the history to an XML file
	 * @param fileName
	 * @throws IOException
	 */
	public void saveInvToXML(String fileName) throws IOException
	{		
		products = new ArrayList<Product>();//start a new list
		MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
		
		//go through the entries and add to the list "products"
		int _productCount = dbHandler.getTableCount();
		for (int i = 0; i < _productCount; i++)
        {
     	   Product theProduct = dbHandler.getProducts(i);
     	   products.add(theProduct);
        }
		
		//get file name and location to save to
		File root = Environment.getExternalStorageDirectory();
		System.out.println("sd card is here: " + root);
		FileOutputStream f = new FileOutputStream(new File(root, fileName +".xml"));
		System.out.println("wrote file " + fileName + ".xml");
		//FileWriter f = new FileWriter("/mnt/genydroidSD/historyExports/history.xml");
		
		//write it
		f.write(writeInvXml(products).getBytes()); 
		f.flush();
		f.close();
	}
	
	/**
	 * Save the shopping list to an XML file
	 * @param fileName
	 * @throws IOException
	 */
	public void saveShopToXML(String fileName) throws IOException
	{		
		products = new ArrayList<Product>();
		shoppingDBHandler dbHandler = new shoppingDBHandler(this, null, null, 1);
		int _productCount = dbHandler.getTableCount();
		for (int i = 0; i < _productCount; i++)
        {
     	   Product theProduct = dbHandler.getProducts(i);
     	   products.add(theProduct);
        }
		
		File root = Environment.getExternalStorageDirectory();
		System.out.println("sd card is here: " + root);
		FileOutputStream f = new FileOutputStream(new File(root, fileName +".xml"));
		System.out.println("wrote file " + fileName + ".xml");
		//FileWriter f = new FileWriter("/mnt/genydroidSD/historyExports/history.xml");
		
		f.write(writeShopXml(products).getBytes()); 
		f.flush();
		f.close();
	}
	
	/**
	 * Use the product list the create a string in XML format
	 * @param products - the list of products
	 * @return String - All the entries in XML format
	 */
	private String writeInvXml(List<Product> products){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.text("\n");
	        serializer.startTag("", "Inventory");
	        serializer.text("\n");
	        
	        for (Product msg: products){
	            serializer.startTag("", "Product");
	            serializer.attribute("", "name", msg.getProductName());
	            serializer.attribute("", "unitType", msg.getUOM());
	            serializer.attribute("", "quantity", String.valueOf(msg.getQuantity()));
	            serializer.attribute("", "lowLevel", String.valueOf(msg.getLowLevel()));
	            serializer.attribute("", "preferredStock", String.valueOf(msg.getPreferredLevel()));
	            serializer.endTag("", "Product");
	            serializer.text("\n");
	        }
	        serializer.endTag("", "Inventory");
	        serializer.endDocument();
	        return writer.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	/**
	 * Use the product list the create a string in XML format
	 * @param products
	 * @return
	 */
	private String writeShopXml(List<Product> products){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.text("\n");
	        serializer.startTag("", "Shopping List");
	        serializer.text("\n");
	        
	        for (Product msg: products){
	            serializer.startTag("", "Product");
	            serializer.attribute("", "name", msg.getProductName());
	            serializer.attribute("", "unitType", msg.getUOM());
	            serializer.attribute("", "low level", String.valueOf(msg.getLowLevel()));
	            serializer.attribute("", "needed", String.valueOf(msg.getPreferredLevel()));
	            serializer.attribute("", "added manualy?", String.valueOf(msg.getAddedManualy()));
	            serializer.endTag("", "Product");
	            serializer.text("\n");
	        }
	        serializer.endTag("", "Shopping List");
	        serializer.endDocument();
	        return writer.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	/**
	 * Open a dialog to name and export a file
	 * @param name
	 * @param invOrShop 0 Inventory, 1 shopping list, 2 history
	 */
	public void dialogExportFile(String name, int invOrShop)
	{
		//0 inv, 1 shopping list, 2 history
		final int _invOrShop = invOrShop;
		//create dialog
		final Dialog dialog = new Dialog(context);
  	  	dialog.setContentView(R.layout.dialog_export);
  	  	//choose the listType
  	  	String listType = "new file";
  	  	switch (invOrShop) {
		case 0:
			listType = " Inventory ";
			break;
		case 1:
			listType = " Shopping List ";
			break;
		case 2:
			listType = " History ";
			break;
		default:
			break;
		}
  	  	dialog.setTitle("Export" + listType +"as XML?");
  	  	 	  
	  	  ImageButton cancel = (ImageButton) dialog.findViewById(R.id.button_cancel);
		  ImageButton ok = (ImageButton) dialog.findViewById(R.id.button_ok);
		  
		  EditText fileName = (EditText) dialog.findViewById(R.id.editText_fileName);
		  fileName.setText(name);
		  
		  
		  final String theName = fileName.getText().toString();
		  
		  cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		  
		  //decide which method to use
		  ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if (_invOrShop == 0)
						{
							saveInvToXML(theName);
						}
						else if (_invOrShop == 1)
						{
							saveShopToXML(theName);
						}
						else if (_invOrShop == 2)
						{
							saveHistoryToXML(theName);
						}
						dialog.dismiss();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		  });
		  
		  dialog.show();
		
	}
	
	
	public void exportInv(View view)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = new Date();
		String theDate = dateFormat.format(date).toString();
		dialogExportFile("invList-"+theDate,0);
	}
	
	public void exportShop(View view)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = new Date();
		String theDate = dateFormat.format(date).toString();
		dialogExportFile("shoppingList-"+theDate,1);
	}
	
	
	///history stuff
	/**
	 * populates the history list
	 */
	public void populateList() {
	 	   
        list = new ArrayList<HashMap>();
        
        historyDBHandler dbHandler = new historyDBHandler(this, null, null, 1);
        
        int _productCount = dbHandler.getTableCount();
        
        for (int i = 0; i < _productCount; i++)
        {
     	   productHistory theProduct = dbHandler.getProducts(i);
     	   HashMap temp = new HashMap();
     	   
            temp.put(FIRST_COLUMN, theProduct.getProductName());
            temp.put(SECOND_COLUMN, theProduct.getQuantity());
            temp.put(THIRD_COLUMN, theProduct.getDateTime());
            temp.put(FOURTH_COLUMN, theProduct.getChangeType());
           
        list.add(temp);   
        }
	}
	
	/**
	 * Alert dialog. Deletes all entries in the history table
	 * @param view
	 */
	public void clearHistory(View view)
	{
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure you want to delete the history?");
        builder1.setCancelable(true);
        
        //if yes we delete the whole table
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	historyDBHandler dbHandler = new historyDBHandler(context, null, null, 1);
        		dbHandler.deleteTable();
        		updateList();
                dialog.cancel();
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
	
	/**
	 * button calls updateList()
	 * @param view
	 */
	public void refreshHistory(View view)
	{
		updateList();
	}
	
	/**
	 * Updates the history list
	 */
	public void updateList()
	{
		ListView lview = (ListView) findViewById(R.id.listView_historyList);
		populateList();
        historyListviewAdapter adapter = new historyListviewAdapter(this, list);
        lview.setAdapter(adapter);
	}

	/**
	 * Writes history to an XML file
	 * @param fileName
	 * @throws IOException
	 */
	public void saveHistoryToXML(String fileName) throws IOException
	{		
		historyProducts = new ArrayList<productHistory>();
		historyDBHandler dbHandler = new historyDBHandler(this, null, null, 1);
		int _productCount = dbHandler.getTableCount();
		for (int i = 0; i < _productCount; i++)
        {
     	   productHistory theProduct = dbHandler.getProducts(i);
     	  historyProducts.add(theProduct);
        }
		
		File root = Environment.getExternalStorageDirectory();
		System.out.println("sd card is here: " + root);
		FileOutputStream f = new FileOutputStream(new File(root, fileName +".xml"));
		System.out.println("wrote history file " + fileName + ".xml");
		//FileWriter f = new FileWriter("/mnt/genydroidSD/historyExports/history.xml");
		
		f.write(writeHistoryXml(historyProducts).getBytes()); 
		f.flush();
		f.close();
	}
	
	/**
	 * converts the products in the history list to an XML formated string
	 * @param _products
	 * @return
	 */
	private String writeHistoryXml(List<productHistory> _products){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.text("\n");
	        serializer.startTag("", "change history");
	        serializer.text("\n");
	        
	        for (productHistory msg: _products){
	            serializer.startTag("", "change");
	            serializer.attribute("", "date", msg.getDateTime());
	            serializer.attribute("", "name", msg.getProductName());
	            serializer.attribute("", "changedValue", String.valueOf(msg.getQuantity()));
	            serializer.attribute("", "changeType", msg.getChangeType());
	            serializer.endTag("", "change");
	            serializer.text("\n");
	        }
	        serializer.endTag("", "change history");
	        serializer.endDocument();
	        return writer.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	/**
	 * Change the view to the advanced activity
	 * @param view
	 */
	public void gotoAdvanced(View view)
	{
		setContentView(R.layout.advanced_activity);
	}
	
	/**
	 * button opens the dialog to export the history
	 * @param view
	 */
	public void exportHistory(View view)
	{
		System.out.println("open the export dialog");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = new Date();
		String theDate = dateFormat.format(date).toString();
		dialogExportFile("invHistory-"+theDate,2);
	}
	
	
	/**
	 * button opens an Android intent to send an email
	 * @param view
	 */
	public void sendEmail(View view)
	{
		Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
		email.setType("message/rfc822");
		
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@hammerwingstudios.com"});
		email.putExtra(Intent.EXTRA_SUBJECT, "inventory app support");
		
		try 
		{
			startActivity(Intent.createChooser(email, "choose an email client from..."));
		}
		catch (android.content.ActivityNotFoundException ex)
		{
			Toast.makeText(AdvancedActivity.this, "No email client installed.", Toast.LENGTH_LONG).show();
		}

	}
}
