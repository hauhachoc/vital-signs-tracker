package com.vitalsigntracker.android;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import metadata.Constants;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

public class PatientDisplayTable extends Activity {

	private InputStream inpS;
    private OutputStream outS;
    
    TableLayout display;    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientpresenttable);
		
		display = (TableLayout) findViewById(R.id.ppresentPatientTable);			
		
		SharedPreferences mySharedPreferences = this.getSharedPreferences("MY_PREFS", MODE_PRIVATE);
		String patientEmail = mySharedPreferences.getString("patientEmail", "");
		
		String str = null;
		try {
			JSONObject object = new JSONObject();
			object.put("code", "patientDisplayTable");
			object.put("patientEmail", patientEmail);						
			str = object.toString();		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
            SocketAddress socketAddress = new InetSocketAddress(Constants.IP_ADD, Constants.PORT);
            Socket sock = new Socket();
            sock.connect(socketAddress, 10 * 1000);
            inpS = sock.getInputStream();
            outS = sock.getOutputStream();	           
           
            Scanner in = new Scanner(inpS);
            PrintWriter out = new PrintWriter(outS, true);
            	            
            out.println(str);            
            String response = in.nextLine();
                                    
            inpS.close();
            out.close();
            outS.close();
            sock.close();
            
            JSONObject obj = new JSONObject(response);
    		int numberRows = obj.getInt("numberRows");    		    	
    					
    		TableRow row;
    		TextView text;
    		
    		for (int i = 1; i <= numberRows; i++) {
    			row = new TableRow(this);
    			text = new TextView(this);
    			
    			text.setText(obj.getString(Integer.toString(i)));
    			row.setMinimumHeight(24);
    			
    			if (i % 2 == 1) {
    				row.setBackgroundColor(Color.GRAY);
    				text.setTextColor(Color.WHITE);
    			} else {
    				row.setBackgroundColor(Color.WHITE);
    				text.setTextColor(Color.BLACK);
    			}
    			row.addView(text);
    			display.addView(row, new TableLayout.LayoutParams(
    					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));    			
    		}
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}

