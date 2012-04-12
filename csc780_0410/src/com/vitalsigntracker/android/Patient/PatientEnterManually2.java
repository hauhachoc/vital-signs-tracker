package com.vitalsigntracker.android.Patient;
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
import android.widget.EditText;
import android.widget.Toast;

public class PatientEnterManually2 extends Activity {

	String MY_PREFS = "MY_PREFS";
	SharedPreferences mySharedPreferences;
	EditText systole, diastole, heartRate, bodyTemp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patiententermanually2);
		
		systole = (EditText) findViewById(R.id.systole);
		diastole = (EditText) findViewById(R.id.diastole);
		heartRate = (EditText) findViewById(R.id.heartRate);
		bodyTemp = (EditText) findViewById(R.id.bodyTemp);		
	}

	public void submitButtonOnClick (View v) throws JSONException {
		
		if ((systole.getText()).equals(null) || 
				(diastole.getText()).equals(null) || 
				(heartRate.getText()).equals(null) ||
				(bodyTemp.getText()).equals(null)) {
			
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Error");
			alertDialog.setMessage("Please enter complete information.");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int i) {															
				}
			});
			alertDialog.show();
			
		} else {
			
		String json = prepareJSONString();
    	String response = ConnectionManager.connect(json);    	
    	boolean success = false;    	       
	
		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");
			if (success) {				
				Toast.makeText(this, "Data had been submitted.", Toast.LENGTH_LONG).show();
				Intent j = new Intent(this, PatientMainLobby.class);
				startActivity(j);
			}
		}		
	}
	
	public String prepareJSONString() {
		String str = null;
		mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);						
		
		try {
			JSONObject object = new JSONObject();
			object.put("code", "patientEnterManually");
			object.put("patientname", mySharedPreferences.getString("patientname", ""));
			object.put("arm", mySharedPreferences.getString("arm", ""));
			object.put("position", mySharedPreferences.getString("position", ""));
			object.put("systole", Integer.parseInt(systole.getText().toString()));
			object.put("diastole", Integer.parseInt(diastole.getText().toString()));
			object.put("heartRate", Integer.parseInt(heartRate.getText().toString()));
			object.put("bodyTemp", Integer.parseInt(bodyTemp.getText().toString()));
			str = object.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
}
