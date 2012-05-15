/* DisplayGraph provides UI for the doctor to select
 * the options of the line graph: patient and period
 * of the line graph.
 * @author	Kelvin Komensen
 * @version	1.0.0 (CSC780 - SFSU Spring 2012) 
 */
package com.vitalsigntracker.android.Provider;
import metadata.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DisplayGraph extends Activity {

	String[] names = null;
	String[] period = { "Select Period", "1 week", "2 week", "1 month",
			"3 month", "1 year" };
	int[] periodInInt = { 0, 7, 14, 30, 90, 365 };

	String patientNameSelected = null;
	int periodSelected = 0;
	Spinner patientNames, periodOption;
	private SharedPreferences mySharedPreferences;
	private String MY_PREFS = "MY_PREFS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaygraph);
		// initGraph();

		mySharedPreferences = this.getSharedPreferences("MY_PREFS",
				MODE_PRIVATE);
		String allPatientNames = mySharedPreferences.getString(
				"allPatientName", "");

		JSONObject obj = null;
		try {
			obj = new JSONObject(allPatientNames);
			int size = obj.getInt("length");
			names = new String[size];
			for (int i = 0; i < size; i++) {
				if (i == 0) {
					names[i] = "Select Patient";
				} else {
					names[i] = obj.getString(Integer.toString(i));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Create spinner (drop down menu) of the patient's names.
		 */
		patientNames = (Spinner) findViewById(R.id.patientNameSpinner);
		ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, names);
		namesAdapter.
				setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		patientNames.setAdapter(namesAdapter);

		/*
		 * Create spinner (drop down menu) of the periods.
		 */
		periodOption = (Spinner) findViewById(R.id.tablePeriodSpinner);
		ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, period);
		periodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		periodOption.setAdapter(periodAdapter);

		//spinner listener for patient's names
		patientNames
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						patientNameSelected = names[position];
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		//spinner listener for the periods.
		periodOption
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						periodSelected = periodInInt[position];
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
	}

	//submit the request to server.
	//patient's name and period of the report.
	public void submitDisplayTableOnClick(View v) {
		// Check if the form is filled out completely.
		if (patientNameSelected.equals("Select Patient") || periodSelected == 0) {

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Uncomplete Information");
			alertDialog
					.setMessage("Please enter patient's name and period of time");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {

						}
					});
			alertDialog.show();

			// Form is filled out completely. Ready to submit to server.
		} else {

			String str = null;
			try {
				JSONObject object = new JSONObject();
				object.put("code", Constants.PROVIDER_DISPLAY_CHART);
				object.put("patientName", patientNameSelected);
				object.put("period", periodSelected);
				str = object.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}

			String response = ConnectionManager.connect(str);
			// put the "response" (json string) in SharedPreferences and call
			// the PresentTable class to display the table.
			mySharedPreferences = this.getSharedPreferences(MY_PREFS,
					MODE_PRIVATE);
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString("displayGraphJson", response);
			editor.putString("pName", patientNameSelected);
			editor.commit();

			//start a new activity to display the line graph.
			Intent i = new Intent(this, PresentLineChart.class);
			startActivity(i);
		}
	}
}
