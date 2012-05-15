/*
 * PatientChangePassword will be called when the patient
 * wants to change his password.
 * New password info will be sent to server, and
 * database will be updated.
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PatientChangePassword extends Activity {

	EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientchangepassword);
		password = (EditText) findViewById(R.id.editText1);
	}
	
	/*
	 * submitPasswordOnClick will be called when the patient clicks
	 * on the submit button. New password will be sent
	 * to server. Database will be updated.
	 * @exception	JSONException
	 */
	public void submitPasswordOnClick(View v) throws JSONException {
		String json = prepareJSONString();
		String response = ConnectionManager.connect(json);
		boolean success = false;

		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");

		if (success) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Modify Password Success");
			alertDialog.setMessage("Password Change.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							finish();
						}
					});
			alertDialog.show();
		}
	}

	/*
	 * prepareJSONString creates a JSON String object.
	 * @param	None
	 * @return	str (JSON String)
	 */
	public String prepareJSONString() {
		String str = null;
		SharedPreferences mySharedPreferences = this.getSharedPreferences(
				"MY_PREFS", MODE_PRIVATE);
		String patientEmail = mySharedPreferences.getString("patientEmail", "");

		try {
			JSONObject object = new JSONObject();
			object.put("code", Constants.PATIENT_MODIFY_ACCOUNT);
			object.put("subcode", "password");
			object.put("newpassword", password.getText());
			object.put("oldemail", patientEmail);
			str = object.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
