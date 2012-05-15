/*
 * PatientActivateAccount will be called when the patient
 * wants to activate his/her account for the first time 
 * after the doctor subscribe him/her to the system.
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
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PatientActivateAccount extends Activity {

	EditText drname, email, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activate);

		drname = (EditText) findViewById(R.id.doctorNameField);
		email = (EditText) findViewById(R.id.yourEmailField);
		password = (EditText) findViewById(R.id.yourPasswordField);
	}
	
	/*
	 * okLoginClick methods will be called when the patient clicks
	 * the submit button after he/she fills out all of the required
	 * information for activating the account.
	 */
	public void okLoginClick(View v) throws JSONException {

		String json = prepareJSONString();
		String response = ConnectionManager.connect(json);
		boolean success = false;

		//Extract the response from server
		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");
		
		//activation account success. user gave the correct info.
		if (success) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Activate Account Success");
			alertDialog.setMessage("Account Activated.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							finish();
						}
					});
			alertDialog.show();

		} else {
			//activation account failed. user gave the wrong info.
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Activate Account Failed");
			alertDialog.setMessage("Information Incorrect.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							drname.setText("");
							email.setText("");
							password.setText("");
						}
					});
			alertDialog.show();
		}
	}

	/*
	 * prepareJSONString method creates a JSON String object.
	 * The JSON String object will be sent to server.
	 * @param	None
	 * @return	str (JSON String)
	 */
	public String prepareJSONString() {
		String str = null;

		try {
			JSONObject object = new JSONObject();
			object.put("code", Constants.PATIENT_ACTIVATE_ACCOUNT);
			object.put("drname", drname.getText());
			object.put("email", email.getText());
			object.put("password", password.getText());
			str = object.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
