/* PresentTable displays report table of a patient.
 * Doctor can select the patient and the period of 
 * the report.
 * Request will be sent to server to get the correct
 * data from database.
 * @author	Kelvin Komensen
 * @version	1.0.0 (CSC780 - SFSU Spring 2012)  
 */
package com.vitalsigntracker.android.Provider;
import org.json.JSONObject;

import com.vitalsigntracker.android.R;
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
		SharedPreferences mySharedPreferences = this.getSharedPreferences(
				"MY_PREFS", MODE_PRIVATE);
		//Retrieve information from the SharedPreferences object.
		String queryResult = mySharedPreferences.getString("displayTableJson", "");
		String patientName = mySharedPreferences.getString("pName", "");

		patientNameField = (TextView) findViewById(R.id.patientNameText);
		patientNameField.setText(patientName);

		JSONObject obj = null;
		int numberRows = 0;
		try {
			obj = new JSONObject(queryResult);
			numberRows = obj.getInt("numberRows");
		} catch (Exception e) {
			e.printStackTrace();
		}

		TableRow row;
		TextView text;

		for (int i = 0; i < numberRows; i++) {
			row = new TableRow(this);
			text = new TextView(this);
			try {
				text.setText(obj.getString(Integer.toString(i + 1)));
			} catch (Exception e) {
				e.printStackTrace();
			}

			row.setMinimumHeight(24);

			//Display the report table with white and black
			//background color.
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

	/*
	 * Override back button in order to direct to the correct class.
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(this, ProviderMainLobby.class);
		startActivity(i);
	}

}
