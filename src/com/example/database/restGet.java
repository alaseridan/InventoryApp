package com.example.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.StrictMode;
/**
 * just experimenting with REST
 * @author Nathan Hallahan
 *
 */
public class restGet {

	private void getdata() {
	    try {
	    	
	        StrictMode.ThreadPolicy policy = new StrictMode.
	          ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy); 
	        
	        URL url = new URL("https://www.google.com/?gws_rd=ssl#safe=off&q=stuff");
	        HttpURLConnection con = (HttpURLConnection) url
	          .openConnection();
	        readStream(con.getInputStream());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}     

	private void readStream(InputStream in) {
		System.out.println("getting web data");
	  BufferedReader reader = null;
	  try {
	    reader = new BufferedReader(new InputStreamReader(in));
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	      System.out.println(line);
	    }
	  } catch (IOException e) {
	    e.printStackTrace();
	  } finally {
	    if (reader != null) {
	      try {
	        reader.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	  }
	} 

}
