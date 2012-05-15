/*
 * PatientEnterMannually2 provides the UI for the
 * patient to enter his/her vital signs info. manually.
 * Once the information are complete, the information
 * from PatientEnterManually1 class and this class
 * will be sent to the server.
 * @author	Kelvin Komensen
 * @version	1.0.0 (CSC780 - SFSU Spring 2012) 
 */
package com.vitalsigntracker.android.Patient;
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

	/*
	 * The patient clicks the 'Submit' button to submit all 
	 * of his/her vital signs information.
	 * Information will be stored in JSON String object and
	 * sent to server.
	 */
	public void submitButtonOnClick(View v) throws JSONException {

		//information is not complete. pop up alert message.
		if ((systole.getText()).equals(null)
				|| (diastole.getText()).equals(null)
				|| (heartRate.getText()).equals(null)
				|| (bodyTemp.getText()).equals(null)) {

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Error");
			alertDialog.setMessage("Please enter complete information.");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int i) {
				}
			});
			alertDialog.show();

		} else {
			//Information is complete. Data send to the server.
			String json = prepareJSONString();
			String response = ConnectionManager.connect(json);
			boolean success = false;

			JSONObject obj = new JSONObject(response);
			success = obj.getBoolean("status");
			if (success) {
				Toast.makeText(this, "Data had been submitted.",
						Toast.LENGTH_LONG).show();
				Intent j = new Intent(this, PatientMainLobby.class);
				startActivity(j);
			}
		}
	}

	/*
	 * Creates a JSON String object.
	 * @param	None
	 * @return	JSON String object
	 */
	public String prepareJSONString() {
		String str = null;
		mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);

		try {
			JSONObject object = new JSONObject();
			object.put("code", Constants.PATIENT_ENTER_MANUALLY);
			object.put("patientname",
					mySharedPreferences.getString("patientname", ""));
			object.put("arm", mySharedPreferences.getString("arm", ""));
			object.put("position",
					mySharedPreferences.getString("position", ""));
			object.put("systole",
					Integer.parseInt(systole.getText().toString()));
			object.put("diastole",
					Integer.parseInt(diastole.getText().toString()));
			object.put("heartRate",
					Integer.parseInt(heartRate.getText().toString()));
			object.put("bodyTemp",
					Integer.parseInt(bodyTemp.getText().toString()));
			str = object.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
