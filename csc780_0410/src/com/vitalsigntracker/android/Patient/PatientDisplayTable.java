package com.vitalsigntracker.android.Patient;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

public class PatientDisplayTable extends Activity {

    TableLayout display;    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientpresenttable);
		
		display = (TableLayout) findViewById(R.id.ppresentPatientTable);			
		
		SharedPreferences mySharedPreferences = this.getSharedPreferences("MY_PREFS", MODE_PRIVATE);
		String patientEmail = mySharedPreferences.getString("patientEmail", "");
		
		String json = null;
		try {
			JSONObject object = new JSONObject();
			object.put("code", "patientDisplayTable");
			object.put("patientEmail", patientEmail);						
			json = object.toString();		
		} catch (Exception e) {
			e.printStackTrace();
		}
			
    	String response = ConnectionManager.connect(json);    	    	    	      
    	int numberRows = 0;
    	
        JSONObject obj = null;
		try {
			obj = new JSONObject(response);
			numberRows = obj.getInt("numberRows");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		    		    	
					
		TableRow row;
		TextView text;
		
		for (int i = 1; i <= numberRows; i++) {
			row = new TableRow(this);
			text = new TextView(this);
			
			try {
				text.setText(obj.getString(Integer.toString(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	}
}

