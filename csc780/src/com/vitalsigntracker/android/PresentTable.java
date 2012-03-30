package com.vitalsigntracker.android;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

public class PresentTable extends Activity {
	
	TableLayout display;
	TextView patientNameField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presenttable);
		
		display = (TableLayout) findViewById(R.id.presentPatientTable);
		
		SharedPreferences mySharedPreferences = this.getSharedPreferences("MY_PREFS", MODE_PRIVATE);
		String queryResult = mySharedPreferences.getString("displayTableJson", "");
		String patientName = mySharedPreferences.getString("pName", "");		
		
		patientNameField = (TextView) findViewById(R.id.patientNameText);
		patientNameField.setText(patientName);
		
		JSONObject obj = (JSONObject) JSONValue.parse(queryResult);
		int numberRows = Integer.parseInt(obj.get("numberRows").toString());
					
		TableRow row;
		TextView text;
		
		for (int i = 0; i < numberRows; i++) {
			row = new TableRow(this);
			text = new TextView(this);
			
			text.setText(obj.get(Integer.toString(i+1)).toString());
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(this, ProviderMainLobby.class);
		startActivity(i);
	}
	
	
	
	

}
